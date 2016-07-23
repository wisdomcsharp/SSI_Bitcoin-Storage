package Cryptography;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Wisdom on 22/07/2016.
 */
public class OutputJSON {
    String address;
    File output;
    boolean withPrK = false;

    public OutputJSON(String address, File output) {
        this.address = address;
        this.output = output;
    }
    public OutputJSON(String address, File output, boolean includePrivateKey) {
        this.address = address;
        this.output = output;
        this.withPrK = includePrivateKey;
    }

    public void export() throws IOException {

        String[] arrayAddress = this.address.toString().split("\\n");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        //JSON start & date
        String JSONPublicKeys = "{\n" ;
        JSONPublicKeys += "\"created_on\"" + ":" + "\"" + dateFormat.format(date) + "\""+","+"\n";

        int pos = 0 ;
        for(String line : arrayAddress){
            String comma = ",";
            if(pos+1 == arrayAddress.length){
                comma = "";
            }
            String[] privateKeyAndPublicKey = line.split("\\s+");
            if(withPrK == false)
            JSONPublicKeys += "\"" + pos + "\"" + ":" + "\"" + privateKeyAndPublicKey[1] + "\""+comma+"\n";

            if(withPrK == true)
                JSONPublicKeys += "\"" + privateKeyAndPublicKey[1] + "\"" + ":" + "\"" + privateKeyAndPublicKey[0] + "\""+comma+"\n";

            pos++;
        }

        //JSON end
        JSONPublicKeys += "}";


        //JSON write
        Files.write(this.output.toPath(), JSONPublicKeys.getBytes());


    }
}