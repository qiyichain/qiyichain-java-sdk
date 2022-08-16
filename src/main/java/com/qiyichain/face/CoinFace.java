package com.qiyichain.face;


import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;

import java.util.List;


public class CoinFace {

    /**
     * 获取合约精度
     */
    public static BigInteger getDecimal(String contract){
        List<Type> list=new ArrayList<>();
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Uint256>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contract,"decimals",list,outputParams);
        if (types!=null&&types.size()==1){
            /**
             * 0: uint256: amount 50000000000000000000
             */

            Uint256 amount= (Uint256)types.get(0);
            return amount.getValue();
        }
        return BigInteger.valueOf(18);
    }


    /**
     * 严格执行，没有返回长度0，用于ERC721与ERC20区分
     * @param contract
     * @return
     */
    public static BigInteger getDecimalMust(String contract){
        List<Type> list=new ArrayList<>();
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Uint256>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contract,"decimals",list,outputParams);
        if (types!=null&&types.size()==1){
            /**
             * 0: uint256: amount 50000000000000000000
             */

            Uint256 amount= (Uint256)types.get(0);
            return amount.getValue();
        }
        return BigInteger.valueOf(0);
    }


    /**
     * 获取合约单位符号
     * @param contract
     * @return
     */
    public static String getSymbol(String contract){
        List<Type> list=new ArrayList<>();
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Utf8String>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contract,"symbol",list,outputParams);
        if (types!=null&&types.size()==1){
            Utf8String amount= (Utf8String)types.get(0);
            return amount.getValue();
        }
        return null;
    }

    public static String getOwner(String contract){
        List<Type> list=new ArrayList<>();
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Address>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contract,"owner",list,outputParams);
        if (types!=null&&types.size()==1){
            Address address= (Address)types.get(0);
            return address.getValue();
        }
        return null;
    }


    public static String getName(String contract){
        List<Type> list=new ArrayList<>();
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Utf8String>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contract,"name",list,outputParams);
        if (types!=null&&types.size()==1){
            Utf8String amount= (Utf8String)types.get(0);
            return amount.getValue();
        }
        return null;
    }
}


