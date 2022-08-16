package com.qiyichain.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

public class RSAUtil {

    public static  String PUBLIC_KEY="30819f300d06092a864886f70d010101050003818d0030818902818100b8122867252f3098a364c08532dafe745f418aa1e6b7a91ac0eedf38c8cca1242c05967f9c9c7729e7e1655ff96f04d6a3f9b233df240f47ef38746aba1aedbfdb60af265e0625960e4aabad1be6ecad6cd92533222ddd1c864b4f5536b76eae109fb6dcdce606a831768c9829a5a1b0791fc478790d07750138f5cc7b6696d50203010001";
    public static  String PRI_KEY="30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100b8122867252f3098a364c08532dafe745f418aa1e6b7a91ac0eedf38c8cca1242c05967f9c9c7729e7e1655ff96f04d6a3f9b233df240f47ef38746aba1aedbfdb60af265e0625960e4aabad1be6ecad6cd92533222ddd1c864b4f5536b76eae109fb6dcdce606a831768c9829a5a1b0791fc478790d07750138f5cc7b6696d50203010001028180143a5a9a4b29ce6b57b960b6c289f260937f3e5ee05dda8588223801a37f694df25082e36f307a9fee4d84b8714ed3f74a79cc2d8b1e0957dc6015db0cac9ce7c284928a88ef55eab4011d3330b68af3cc5598be774489e07cc909599b686eee08138a0edcd27c8939289195feff8ea963e01aadd088ac3dd67442565e0d4bd9024100f4a6e034939986568dc36d529cac031d912f0b06b5a885545a11550483f9b30a8168f857bb0f19f35410c4a477ebe1aa58b48f3ae684c2a08b1e0e1a347406cb024100c09be9907c3a78edadbfb7e8aeccbb8dc02c720a4cb5135b06236fe0a19b500fa7543e0b2de08b76bde9583f508f3fe234d708ab4b5a3ec4de1bbae2f5fc84df024100d177cf9f3480ce346d4c55ddd5a6b1442f7a47c686e2b2c5761840e28cdcce1ec08e4f934de5b0225667c4f5cc7779cf9003885eb5e01583bf990c1a03af13e102403de71e26f0b63e3d6d552bbf5dc6b114bac9ce111f5c270f5f26ae834c8d452af2c5717758544d9e692500d6c08679aab2fed56b70395de919d561b82604f593024100be139706a78b05ac0e0e7df547c2247f0329e7e58877711b8ca2f45b4d516e91071915de94638a9522901661b4144a7648c3c9a18f34bca10b4b27a3a34de3cf";


    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    /**
     *生成私钥  公钥
     */
    private static void gen(){
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            PUBLIC_KEY=toHexString(publicKeyBytes);
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            PRI_KEY=toHexString(privateKeyBytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String pubkey) throws Exception {
        byte[] keyBytes = convertHexString(pubkey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * 获取私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String prikey)throws Exception {

        byte[] keyBytes =convertHexString(prikey);
        PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    /**
     * 加密数据
     * @param data
     * @param pubKey
     * @return
     */
    public static String enData(String data,String pubKey){
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            RSAPublicKey pub = (RSAPublicKey) getPublicKey(pubKey);
            cipher.init(Cipher.ENCRYPT_MODE, pub);
            return toHexString( cipher.doFinal(data.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密数据
     * @param data
     * @param priKey
     * @return
     */
    public static String deData(String data,String priKey){
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            RSAPrivateKey  pri = (RSAPrivateKey) getPrivateKey(priKey);
            cipher.init(Cipher.DECRYPT_MODE, pri);
            return new String( cipher.doFinal(convertHexString(data)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    public static void main(String[] args) {
        String data="464ce138ce94e427bbfa3095c30944637b7ca9e64b4068fbb7ce849534b48206";
        String en0= enData(data,PUBLIC_KEY);
        System.out.println(en0);
        System.out.println(deData(en0,PRI_KEY));
    }
}
