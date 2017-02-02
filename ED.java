import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.xml.bind.DatatypeConverter;

import org.kohsuke.args4j.*;

class Encryptor {
  public static String encrypt(String input, String key, String algo, String cipherString) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException
  {
    byte[] crypted = null;
    SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algo);
    Cipher cipher = Cipher.getInstance(cipherString);
    cipher.init(Cipher.ENCRYPT_MODE, skey);
    crypted = cipher.doFinal(input.getBytes());
    return DatatypeConverter.printBase64Binary(crypted);
  }
}

class Decryptor {
  public static String decrypt(String input, String key, String algo, String cipherString) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException
  {
    byte[] crypted = null;
    SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algo);
    Cipher cipher = Cipher.getInstance(cipherString);
    cipher.init(Cipher.DECRYPT_MODE, skey);
    crypted = cipher.doFinal(DatatypeConverter.parseBase64Binary(input));
    return new String(crypted);
  }
}

public class ED {
   @Option(name="-algo", usage="the algorithm, default is AES")
   private String algo = "AES";

   @Option(name="-cipher", usage="the cipher, default is AES/ECB/PKCS5Padding")
   private String cipher = "AES/ECB/PKCS5Padding";

   @Option(name="-key", usage="the key, default is ytxQz6HJQgHv7Eei")
   private String key = "ytxQz6HJQgHv7Eei";

   @Option(name="-data", usage="the data to encrypt")
   private String data = "Hello World !!!!";

   @Option(name="-decrypt", usage="decrypt, instead of encrypt and decrypt")
   private boolean decrypt = false;

   public static void main(String[] argv) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
      new ED().doMain(argv);
   }

   public void doMain(String[] argv) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
      CmdLineParser parser = new CmdLineParser(this);

      try {
          // parse the arguments.
          parser.parseArgument(argv);

          System.out.println("Algo :" + algo);
          System.out.println("Cipher :" + cipher);
          System.out.println("Data :" + data);
          System.out.println("Key :" + key);

      
          if ( !decrypt ) {
             String encrypted = Encryptor.encrypt(data, key, algo, cipher);
             System.out.println("Encrypted :" + encrypted);
             System.out.println("Encrypted (Encoded):" + encode(encrypted));
             System.out.println("Encrypted (Decoded):" + decode(encrypted));
             System.out.println("Decrypted :" + Decryptor.decrypt(encrypted, key, algo, cipher));
          } else {
             System.out.println("Decrypted :" + Decryptor.decrypt(data, key, algo, cipher));
          }

      } catch( CmdLineException e ) {
          parser.printUsage(System.err);
          System.err.println();
          return;
      }

   }

   private String encode(String data) {
      return data.replaceAll("\\+","-").replaceAll("/","_").replaceAll("=",",");
   }
  
   private String decode(String data) {
      return data.replaceAll("-","\\+").replaceAll("_","/").replaceAll(",","=");
   }
}

