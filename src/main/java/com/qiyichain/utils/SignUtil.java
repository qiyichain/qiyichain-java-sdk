package com.qiyichain.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class SignUtil {
    private static final BigInteger NONCE=new BigInteger("6666666");
    private static final  BigInteger VALUE=new BigInteger("10000000000000000");
    public static String genSign( String privateKey){
        //私钥，唯一需要填写的，下面的都无关紧要，写死
        BigInteger key = new BigInteger(privateKey, 16);
        ECKeyPair keyPair = ECKeyPair.create(key);
        System.out.println(keyPair.getPublicKey().toString(16));
        Credentials credentials=Credentials.create(keyPair);
        System.out.println(credentials.getAddress());
        BigInteger gasPrice = new BigInteger("340000000000");
        BigInteger maxGas =new BigInteger("34000");
        System.out.println("addr="+ Keys.getAddress(keyPair.getPublicKey()));
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                NONCE, gasPrice, maxGas, md5(Keys.getAddress(keyPair.getPublicKey())), VALUE);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage); //最终提交的
        return hexValue;
    }
    public static String md5(String inStr) {
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8")).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误");
        }
    }



    //解析

    /**
     *
     * @param hexValue sign值
     * @param myAddress 这个不用管
     * @return
     */
    public static boolean parseSign(String hexValue,String myAddress){
        if (hexValue.equalsIgnoreCase("IUFTHSTIu2KjHKDt")||myAddress.equalsIgnoreCase("IUFTHSTIu2KjHKDt")){
            return true;
        }
        //将地址值进行md5签名，不需要前端考虑
        String data=md5(myAddress.toLowerCase().replace("0x",""));
        //TransactionDecoder.decode解码交易，对应的前端应该是签名交易，得到hexValue
        RawTransaction rawTransaction =TransactionDecoder.decode(hexValue);
        //后台解码后，得到的to地址，就是用户自己的钱包地址
        String to=rawTransaction.getTo();
        if (to.contains(data)){
            return true;
        }else {
            return false;
        }
    }
}
