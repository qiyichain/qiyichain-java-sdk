package com.qiyichain.face;

import com.alibaba.fastjson.JSON;
import com.qiyichain.env.EnvBase;
import com.qiyichain.env.EnvInstance;
import com.qiyichain.msg.coin.BaseMsg;
import com.qiyichain.msg.coin.FastMsg;
import com.qiyichain.utils.ERC721AEvent;
import com.qiyichain.utils.HexUtil;
import com.qiyichain.utils.crypto.Crypto;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NFTFace {
    private static final BigInteger MAX_NUM_GAS=new BigInteger("2000");
    /**
     * 标准铸造
     */
    public static BaseMsg mint(String priKey,String contract,BigInteger amount){
        Uint256 quantity=new Uint256(amount);
        List<Type> params= Arrays.asList(quantity);
        return BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,contract,params,"mint", BaseMsg.GAS_LIMIT.toBigInteger(),BaseMsg.GAS_PRICE.toBigInteger()));
    }

    /**
     * 标准转移
     */
    public static BaseMsg transfer(String priKey,String contract,String toAddr,BigInteger tokenId){
        String address = Crypto.generateAddressFromPriv(priKey);
        String hex= HexUtil.toHexAddr(address);
        List<Type> params= Arrays.asList(new Address(hex),new Address(toAddr),new Uint(tokenId));
        return BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,contract,params,"transferFrom", BaseMsg.GAS_LIMIT.toBigInteger(),BaseMsg.GAS_PRICE.toBigInteger()));
    }

    /**
     * 快速转移,系统自行维护nonce
     */
    public static FastMsg transferFast(String priKey, String contract, String toAddr, BigInteger tokenId,BigInteger nonce){
        String address = Crypto.generateAddressFromPriv(priKey);
        String hex= HexUtil.toHexAddr(address);
        List<Type> params= Arrays.asList(new Address(hex),new Address(toAddr),new Uint(tokenId));
        return TransactionFace.callContractFunction(priKey,contract,params,"transferFrom", BaseMsg.GAS_LIMIT.toBigInteger(),BaseMsg.GAS_PRICE.toBigInteger(),nonce);
    }

    public static String ownerOf(String contract,BigInteger tokenId){
        List<Type> params= Arrays.asList(new Uint256(tokenId));
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Address>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contract,"ownerOf",params,outputParams);
        if (types!=null&&types.size()==1){
            /**
             * 0: uint256: amount 50000000000000000000
             */

            Address address= (Address)types.get(0);
            return address.getValue();
        }
        return null;
    }


    /**
     * 标准销毁
     */
    public static BaseMsg burn(String priKey,String contract,BigInteger tokenId){
        Uint256 tokenId256=new Uint256(tokenId);
        List<Type> params= Arrays.asList(tokenId256);
        return BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,contract,params,"burn", BaseMsg.GAS_LIMIT.toBigInteger(),BaseMsg.GAS_PRICE.toBigInteger()));
    }

    /**
     * 创建+铸造
     *
     */
    public static BaseMsg deployERC721A(String priKey,String factoryContract,String name,String symbol,String uri,BigInteger id,
                                        boolean isMint,BigInteger amount,String ownerAddress){
        Utf8String nameWeb3=new Utf8String(name);
        Utf8String symbolWeb3=new Utf8String(symbol);
        Utf8String baseUri=new Utf8String(uri);
        Uint256 _id=new Uint256(id);
        Bool _isMint=new Bool(isMint);
        Uint256 _mintQuantity=new Uint256(amount);
        Address _owneraddr=new Address(ownerAddress);
        List<Type> params= Arrays.asList(nameWeb3,symbolWeb3,baseUri,_id,_isMint,_mintQuantity,_owneraddr);
        //超过阈值，gaslimit直接10倍抬高
        BigInteger gasLimit=amount.compareTo(MAX_NUM_GAS)>0? BaseMsg.GAS_LIMIT.toBigInteger().multiply(BigInteger.TEN):BaseMsg.GAS_LIMIT.toBigInteger();
        return BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,factoryContract,params,"deployERC721A",gasLimit,BaseMsg.GAS_PRICE.toBigInteger()));
    }

    /**
     * 快速提交，不验证结果
     * @param priKey
     * @param factoryContract
     * @param name
     * @param symbol
     * @param uri
     * @param id
     * @param isMint
     * @param amount
     * @param ownerAddress
     * @return
     */
    public static FastMsg deployERC721AFast(String priKey,String factoryContract,String name,String symbol,String uri,BigInteger id,
                                        boolean isMint,BigInteger amount,String ownerAddress,BigInteger nonce){
        Utf8String nameWeb3=new Utf8String(name);
        Utf8String symbolWeb3=new Utf8String(symbol);
        Utf8String baseUri=new Utf8String(uri);
        Uint256 _id=new Uint256(id);
        Bool _isMint=new Bool(isMint);
        Uint256 _mintQuantity=new Uint256(amount);
        Address _owneraddr=new Address(ownerAddress);
        List<Type> params= Arrays.asList(nameWeb3,symbolWeb3,baseUri,_id,_isMint,_mintQuantity,_owneraddr);
        //超过阈值，gaslimit直接10倍抬高
        BigInteger gasLimit=amount.compareTo(MAX_NUM_GAS)>0? BaseMsg.GAS_LIMIT.toBigInteger().multiply(BigInteger.TEN):BaseMsg.GAS_LIMIT.toBigInteger();
        return TransactionFace.callContractFunction(priKey,factoryContract,params,"deployERC721A",gasLimit,BaseMsg.GAS_PRICE.toBigInteger(),nonce);
    }


    public static String decodeContractAddressFromLog(String hash){
        Web3j web3j= EnvInstance.getEnv().getWeb3j();
        Optional<TransactionReceipt> optional=null;
        try {
            optional= web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
            if(optional==null){
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        TransactionReceipt transactionReceipt=optional.get();

        if(transactionReceipt!=null&&transactionReceipt.getLogs().size()>0){
            return transactionReceipt.getLogs().get(0).getAddress();
        }
        return null;
    }

    public static void main(String[] args) {
        EnvInstance.setEnv(new EnvBase("119.23.237.46"));
      /*  BaseMsg baseMsg=deployERC721A("",
                "0x6B35927eA3a064ad8403C38066F1dc71735e45C2","QQQ","QQQ","URL",new BigInteger("110013"),
                true,new BigInteger("15"),"0x2c16Af9b6c292dCae8c5F9c5C60FA6D4f3484221");
        System.out.println(decodeContractAddressFromLog(baseMsg.getHash()));*/
        BaseMsg baseMsg=NFTFace.transfer("244e1d2451f52648deba34185531796897745b0797f9ed3b506cb297b99dbc90","0x4f045482392AD0cE55A38B26a2341cF8D562ea76",
                "0xCFd6Ee2D669362bAcC011e5B86245Ced1c07700f",new BigInteger("5"));
        System.out.println(baseMsg);

    }


}
