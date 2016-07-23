package Cryptography;

import ExceptionList.PasswordTooShort;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;

public class AES {
  private String plaintext; /*Note null padding*/
  private String plaintextKey;
  private String[] encryptedKeys;
  
  
  /*AES constructor - Throws Exception is password is less than 8 characters
   *@param dataToEncrypt - This is where you place your data
   *@param passwordToUse - This is the password to use for encryption
   *@return
  */
  public AES(String dataToEncrypt, String passwordToUse) throws Exception{
      this.plaintext = dataToEncrypt;
      this.plaintextKey = passwordToUse;
      this.encryptedKeys = getSHA1(passwordToUse);
     
  }
  
  
  /*
   *encrypts the plainttext with the first encryptedKeys,
   *then encrypts the encrypted data with the second encryptedKeys
   */
  public String encrypt() throws Exception{
     return encrypt(encrypt(this.plaintext, encryptedKeys[0]), encryptedKeys[1]);
  }
  
  private String encrypt(String text, String key) throws Exception {
      byte[] crypted = null;
	  try{
	    SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.ENCRYPT_MODE, skey);
	      crypted = cipher.doFinal(text.getBytes());
	    }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){

	    }
          
	    return Base64.encode(crypted);
  }

  
  /*
   *decrypts the plainttext with the first encryptedKeys,
   *then decrypts the encrypted data with the second encryptedKeys
   */
  public String decrypt() throws Exception{
      return decrypt(decrypt(this.plaintext, encryptedKeys[1]), encryptedKeys[0]);
  }
  private  String decrypt(String text, String key) throws Exception{
   byte[] output = null;
	    try{
	      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.DECRYPT_MODE, skey);
	      output = cipher.doFinal(Base64.decode(text));
	    }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | Base64DecodingException | IllegalBlockSizeException | BadPaddingException e){
	    }
	    return new String(output);
  }
  
  
  /*
   *Computes a Hash from the password, then splits it into 2, 16-length arrays.
   *@param return returns an array of SHA-1 (0 - 16 characters), and SHA-1 (16 - 32 characters) 
  */
  private String[]  getSHA1(String plaintextKey) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] hash = md.digest(plaintextKey.getBytes());
                return new String[] { Hex.toHexString(hash).substring(0, 16) , Hex.toHexString(hash).substring(16, 32) };
}
}