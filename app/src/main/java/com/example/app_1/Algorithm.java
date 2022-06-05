package  com.example.app_1;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Algorithm {
    protected Object noise = null;
    protected Object sound = null;
    protected String funName;


    public void setNoise(String str){noise = str;}
    public void setFunName(String str){funName = str;}


    public Object encrypt(Object param) throws Exception {return null;}
    public Object decrypt(Object param) throws Exception {return null;}
//    public void test(){}

    public static Object getAlgorithm(String Name) throws Exception {
        return Class.forName(Name).newInstance();
    }

    protected Key createKey(KeySpec keySpec, String algorithm) throws Exception{
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        Key key = secretKeyFactory.generateSecret(keySpec);
        key = new SecretKeySpec(key.getEncoded(),funName);
        return key;
    }

    protected byte[] getFormedNoise(int length){
        int lengthNoise = noise.toString().length();
        if(lengthNoise < length){
            int len = length - lengthNoise;
            String strPlus = "";
            while(len-- != 0)
                strPlus += '0';
            return ((String) noise + strPlus).getBytes(StandardCharsets.UTF_8);
        } else {
            byte[] bytes = new byte[length];
            System.arraycopy(noise.toString().getBytes(StandardCharsets.UTF_8),length,bytes,0,0);
            return bytes;
        }
    }




}

class DES extends Algorithm{

    public byte[] encrypt(Object info) throws Exception {
        if(info == null && noise == null){
            System.out.println("have null input");
            return null;
        }
        Key key = createKey(new DESKeySpec(getFormedNoise(8)),funName);
        Cipher cipher = Cipher.getInstance(funName);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] bytesInfo = info.toString().getBytes(StandardCharsets.UTF_8);
        byte[] bytes = cipher.doFinal(bytesInfo);
//        sound = bytes;
        return bytes;
    }

    public byte[] decrypt(Object sound) throws Exception {
        if(sound == null && noise == null && funName == null){
            System.out.println("have null input");
            return null;
        }
        Key key = createKey(new DESKeySpec(getFormedNoise(8)),funName);
        Cipher cipher = Cipher.getInstance(funName);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] bytesSound = (byte[]) sound;
        byte[] bytes = cipher.doFinal(bytesSound);
//        info = bytes;
        return bytes;
    }
}

class AES extends Algorithm{
    public byte[] encrypt(Object info) throws Exception{
        byte[] salt = getFormedNoise(8);
        Key key = createKey(new PBEKeySpec(noise.toString().toCharArray(),salt,66,256),"PBKDF2WithHmacSHA256");
        Cipher cipher = Cipher.getInstance(funName);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] bytesInfo = info.toString().getBytes(StandardCharsets.UTF_8);
        byte[] bytes = cipher.doFinal(bytesInfo);
//        sound = bytes;
        return bytes;
    }

    public byte[] decrypt(Object sound) throws Exception{
        byte[] salt = getFormedNoise(8);
        Key key = createKey(new PBEKeySpec(noise.toString().toCharArray(),salt,66,256),"PBKDF2WithHmacSHA256");
        Cipher cipher = Cipher.getInstance(funName);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] bytesSound = (byte[]) sound;
        byte[] bytes = cipher.doFinal(bytesSound);
//        info = bytes;
        return bytes;
    }
}