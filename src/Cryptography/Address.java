package Cryptography;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Arrays; 

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.math.ec.ECPoint;




public class Address {
	private final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	 private final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
	 private final int BASE_58 = ALPHABET.length;
	 private final int BASE_256 = 256;
/*************
 * This method will create the ECDSA public and private key which is returned in a Byte array of Byte arrays, index 0 is the private key and
 * index 1 of the array will return the ECDSA public key
 * 
 * -Wisdom: This guy didn't check whether the returned Private Key would be more than 32.
 * @return
 
 */
private byte[][] ECDSAgeneratePublicAndPrivateKey(){
       int length = 0;
       byte[][] keys;
       
    do{
        
	ECKeyPairGenerator gen = new ECKeyPairGenerator();
	SecureRandom secureRandom = new SecureRandom();
        X9ECParameters secnamecurves = SECNamedCurves.getByName("secp256k1");
	ECDomainParameters ecParams = new ECDomainParameters(secnamecurves.getCurve(), secnamecurves.getG(), secnamecurves.getN(), secnamecurves.getH());
	ECKeyGenerationParameters keyGenParam = new ECKeyGenerationParameters(ecParams, secureRandom);
	gen.init(keyGenParam);
	AsymmetricCipherKeyPair kp = gen.generateKeyPair();
	ECPrivateKeyParameters privatekey = (ECPrivateKeyParameters)kp.getPrivate();
	ECPoint dd = secnamecurves.getG().multiply(privatekey.getD());
	byte[] publickey=new byte[65];
	System.arraycopy(dd.getY().toBigInteger().toByteArray(), 0, publickey, 64-dd.getY().toBigInteger().toByteArray().length+1, dd.getY().toBigInteger().toByteArray().length);
	System.arraycopy(dd.getX().toBigInteger().toByteArray(), 0, publickey, 32-dd.getX().toBigInteger().toByteArray().length+1, dd.getX().toBigInteger().toByteArray().length);
	publickey[0]=4;
        length = privatekey.getD().toByteArray().length;
        keys = new byte[][]{privatekey.getD().toByteArray(),publickey};
        
    }while(length != 32);
	return keys;
}
/************
 * This is the SHA-256 hashing function that is performed on the public key
 * @param tobeHashed - Byte Array that will be hashed
 * @return - The hashed Byte Array is returned in a Byte Array
 */
private byte[] SHA256hash(byte[] tobeHashed){
		SHA256Digest digester=new SHA256Digest(); 
		byte[] retValue=new byte[digester.getDigestSize()]; 
		digester.update(tobeHashed, 0, tobeHashed.length); 
		digester.doFinal(retValue, 0);
	    return retValue; 
}
/********
 * This method does all the RipeMD 160 hashing function, and after that hash has taken place, it prepends the version 
 * byte to the beginning of the Byte array
 * @param tobeHashed - Byte Array of the public key after a SHA-256 hash
 * @return - Byte Array after the RipeMD 160 hash function
 */
private byte[] RIPEMD160(byte[] tobeHashed){
	RIPEMD160Digest digester = new RIPEMD160Digest();
	byte[] retValue=new byte[digester.getDigestSize()]; 
	digester.update(tobeHashed, 0, tobeHashed.length); 
	digester.doFinal(retValue, 0);	   
	byte[] version = new byte[]{0x00};
	return concateByteArray(version,retValue);	
}
/*************
 * This method is a helper function to concate two byte arrays together
 * @param firstarray - this will be the first array place in the empty array
 * @param secondarray - this is the second array that will be placed in the empty array 
 * @return - returns one byte array which is the concate of the first and second byte array 
 */
private byte[] concateByteArray(byte[] firstarray, byte[] secondarray){
	byte[] output = new byte[firstarray.length + secondarray.length];
	int i=0;
	for(i=0;i<firstarray.length;i++){
	    output[i] = firstarray[i];
	}
	int index=i;
	for(i=0;i<secondarray.length;i++){
	  output[index] = secondarray[i];
	  index++;
	}
	return output;
}
/********
 * Another helper function that will convert a byte array to a Hex String with leading zeros in place
 * @param data - the byte array you want to conver to Hex
 * @return - returns a string of hex of the byte array
 */
private String toHex(byte[] data) {
    char[] chars = new char[data.length * 2];
    for (int i = 0; i < data.length; i++) {
        chars[i * 2] = HEX_DIGITS[(data[i] >> 4) & 0xf];
        chars[i * 2 + 1] = HEX_DIGITS[data[i] & 0xf];
    }
    return new String(chars);
}
/***************
 * This encodes a String input to a base 58 taken from the bitcoinj implmentation of bas58checking encoding.
 * @param input - A String that you want converted to a base of 58 
 * @return - returns a String of base58 encoded
 */
private String base58encode(byte[] input) {
        if (input.length == 0) {
            return "";
    }               
    input = copyOfRange(input, 0, input.length);
    // Count leading zeroes.
    int zeroCount = 0;
    while (zeroCount < input.length && input[zeroCount] == 0) {
            ++zeroCount;
    }
    // The actual encoding.
    byte[] temp = new byte[input.length * 2];
    int j = temp.length;

    int startAt = zeroCount;
    while (startAt < input.length) {
            byte mod = divmod58(input, startAt);
            if (input[startAt] == 0) {
                    ++startAt;
            }
            temp[--j] = (byte) ALPHABET[mod];
    }

    // Strip extra '1' if there are some after decoding.
    while (j < temp.length && temp[j] == ALPHABET[0]) {
            ++j;
    }
    // Add as many leading '1' as there were leading zeros.
    while (--zeroCount >= 0) {
            temp[--j] = (byte) ALPHABET[0];
    }

    byte[] output = copyOfRange(temp, j, temp.length);
    try {
    return new String(output, "US-ASCII");
    } catch (UnsupportedEncodingException e) {
    throw new RuntimeException(e);  // Cannot happen.
    }       
}
/**********
 * Base58 helper method
 * @param number
 * @param startAt
 * @return
 */
private byte divmod58(byte[] number, int startAt) {
    int remainder = 0;
    for (int i = startAt; i < number.length; i++) {
            int digit256 = (int) number[i] & 0xFF;
            int temp = remainder * BASE_256 + digit256;

            number[i] = (byte) (temp / BASE_58);

            remainder = temp % BASE_58;
    }

    return (byte) remainder;
}
/*************
 * Base58 helper method
 * @param source
 * @param from
 * @param to
 * @return
 */
private byte[] copyOfRange(byte[] source, int from, int to) {
    byte[] range = new byte[to - from];
    System.arraycopy(source, from, range, 0, range.length);

    return range;
}
/*********
 * This method takes a byte array of the private key and returns a String of a value Wallet Import Formatted Private key to 
 * be able to import it to a wallet
 * @param privatekey - the byte array of the private key you want to make wallet import format
 * @return - returns a string that is a wallet import format of the private key
 */
private String convertPrivateKeytoWIF(byte[] privatekey){
	byte[] step1=concateByteArray(new byte[]{(byte) 0x80},privatekey);
	byte[] step2to3=SHA256hash(SHA256hash(step1));
	byte[] checksum=getCheckSum(step2to3);
	byte[] step5=concateByteArray(step1,checksum);
	return base58encode(step5);
}

/*******
 * This returns the checksum of four bytes
 * @param checksum
 * @return
 */
private byte[] getCheckSum(byte[] checksum){
	return Arrays.copyOfRange(checksum, 0, 4);
}
/**********
 * This method will return a string array of the 3 keys. The first element is the private key, the second element is the private
 * key in Wallet Injection Format, the third element is the bitcoin address.
 * @return
 */
public String[] createNewAddress(){
    byte[][] pairs=ECDSAgeneratePublicAndPrivateKey();
    byte[] afterhashing=RIPEMD160(SHA256hash(pairs[1]));
    byte[] checksum=getCheckSum(SHA256hash(SHA256hash(afterhashing)));
    byte[] bitcoinaddress=concateByteArray(afterhashing,checksum);
    return new String[]{toHex(pairs[0]), convertPrivateKeytoWIF(pairs[0]), base58encode(bitcoinaddress)};
    }
}