package com.qiyichain.face;

import com.qiyichain.msg.coin.BaseMsg;
import com.qiyichain.utils.HexUtil;
import com.qiyichain.utils.crypto.Crypto;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class NFTFace {
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
     * 标准销毁
     */
    public static BaseMsg burn(String priKey,String contract,BigInteger tokenId){
        Uint256 tokenId256=new Uint256(tokenId);
        List<Type> params= Arrays.asList(tokenId256);
        return BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,contract,params,"burn", BaseMsg.GAS_LIMIT.toBigInteger(),BaseMsg.GAS_PRICE.toBigInteger()));
    }

    /**
     * 创建+铸造
     * todo 方法大致是这样，待合约代码出来再更新
     */
    public static BaseMsg createAndMint(String priKey,String factoryContract,String name,String symbol,BigInteger amount){
        Uint256 quantity=new Uint256(amount);
        Utf8String nameWeb3=new Utf8String(name);
        Utf8String symbolWeb3=new Utf8String(symbol);
        List<Type> params= Arrays.asList(nameWeb3,symbolWeb3,quantity);
        return BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,factoryContract,params,"createNftContract", BaseMsg.GAS_LIMIT.toBigInteger(),BaseMsg.GAS_PRICE.toBigInteger()));
    }
}
