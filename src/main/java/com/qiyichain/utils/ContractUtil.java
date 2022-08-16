package com.qiyichain.utils;

import com.qiyichain.env.EnvBase;
import com.qiyichain.env.EnvInstance;
import com.qiyichain.msg.coin.BaseMsg;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

public class ContractUtil {

    /**
     * 创建合约
     * @param priKey
     * @param amount
     * @param len
     * @param symbol
     * @param tokenName
     * @return
     */
    public static PubTokenSol createContract(String priKey, BigInteger amount, Integer len, String symbol, String tokenName){
        Web3j web3j= EnvInstance.getEnv().getWeb3j();
        Credentials credentials=Credentials.create(priKey);
        try {
            PubTokenSol token=
            PubTokenSol.
                    deploy(web3j,credentials, BaseMsg.GAS_PRICE.toBigInteger(),
                            BaseMsg.GAS_LIMIT.toBigInteger(),amount,tokenName,BigInteger.valueOf(len),symbol).send();

            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建NFT合约
     * @param priKey
     * @return
     */
    public static NFTSol createContractNFT(String priKey, String tokenName, String symbol, String pic, String content){
        Web3j web3j= EnvInstance.getEnv().getWeb3j();
        Credentials credentials=Credentials.create(priKey);
        try {
            NFTSol token=
                    NFTSol.
                            deploy(web3j,credentials, BaseMsg.GAS_PRICE.toBigInteger(),
                                    BaseMsg.GAS_LIMIT.toBigInteger(),tokenName,symbol,pic,content).send();

            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {
        EnvInstance.setEnv(new EnvBase(""));
        String pri="";
        BigInteger amount=new BigInteger("10000000").multiply((BigInteger.TEN.pow(18)));
        Integer len=18;
        String symbol="";
        String tokenName="";
        System.out.println(ContractUtil.createContract(pri,amount,len,symbol,tokenName).getContractAddress());
    }
}
