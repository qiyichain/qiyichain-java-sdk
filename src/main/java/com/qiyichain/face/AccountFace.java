package com.qiyichain.face;

import com.alibaba.fastjson.JSONObject;
import com.qiyichain.env.EnvBase;
import com.qiyichain.env.EnvInstance;
import com.qiyichain.utils.EthConvert;
import com.qiyichain.utils.crypto.AddressUtil;
import com.qiyichain.utils.crypto.Crypto;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Bip44WalletUtils;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


/**
 * 账户类接口
 */
public class AccountFace {
    /**
     * 生成助记词、私钥、地址、公钥
     * @return
     */
    public static String createAccount(){
        String mnemonic= Crypto.generateMnemonic();
        String pri=Crypto.generatePrivateKeyFromMnemonic(mnemonic);
        String address=Crypto.generateAddressFromPriv(pri);
        String pub=Crypto.generatePubKeyHexFromPriv(pri);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mnemonic",mnemonic);
        jsonObject.put("pri",pri);
        jsonObject.put("address",addressToHex(address));
        jsonObject.put("pub",pub);
        return jsonObject.toJSONString();
    }


    /**
     * 导入助记词，生成私钥、地址、公钥
     * @param mnemonic
     * @return
     */
    public static String importByMnemonic(String mnemonic){
        String pri=Crypto.generatePrivateKeyFromMnemonic(mnemonic);
        String address=Crypto.generateAddressFromPriv(pri);
        String pub=Crypto.generatePubKeyHexFromPriv(pri);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mnemonic",mnemonic);
        jsonObject.put("pri",pri);
        jsonObject.put("address",addressToHex(address));
        jsonObject.put("pub",pub);
        return jsonObject.toJSONString();
    }

    /**
     * 导入私钥，生成地址、公钥
     * @param pri
     * @return
     */
    public static String importByPrivateKey(String pri){
        String address=Crypto.generateAddressFromPriv(pri);
        String pub=Crypto.generatePubKeyHexFromPriv(pri);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mnemonic","");
        jsonObject.put("pri",pri);
        jsonObject.put("address",addressToHex(address));
        jsonObject.put("pub",pub);
        return jsonObject.toJSONString();
    }

    /**
     * 地址转0x地址
     * @param address
     * @return
     */
    public static String addressToHex(String address){
        return AddressUtil.convertAddressFromBech32ToHex(address);
    }


    /**
     * 0x地址转Bech32地址
     * @param hexAddr
     * @return
     */
    public static String hexToAddress(String hexAddr){
        return AddressUtil.convertAddressFromHexToBech32(hexAddr);
    }


    /**
     * 获取主币余额
     * @param address
     * @return
     */
    public static BigDecimal getMainCoinBalance(String address){
        if (StringUtils.isEmpty(address)){
            System.out.println("address is null");
            return null;
        }
        if (!address.startsWith("0x")){
            address= AccountFace.addressToHex(address);
        }
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        EthGetBalance getBalance = null;
        try {
            getBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return Convert.fromWei(getBalance.getBalance().toString(), Convert.Unit.ETHER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取代币余额
     * @param address 地址
     * @param contractAddress 合约地址
     * @param len 单位长度
     * @return
     */
    public static BigDecimal getContractCoinBalance(String address,String contractAddress,Integer len){
        if (StringUtils.isEmpty(address)){
            System.out.println("address is null");
            return null;
        }
        if (!address.startsWith("0x")){
            address= AccountFace.addressToHex(address);
        }
        BigInteger balance = BigInteger.ZERO;
        List<Type> params= Arrays.asList(new Address(address));
        List<TypeReference<?>> outputParams=new ArrayList<>();
        outputParams.add(new TypeReference<Uint256>() {});
        List<Type>  types=TransactionFace.callContractViewMethod("0x3901952De2f16ad9B8646CF59C337d0b445A81Ca",contractAddress,"balanceOf",params,outputParams);
        if (types!=null&&types.size()==1){
            /**
             * 0: uint256: amount 50000000000000000000
             */

            Uint256 amount= (Uint256)types.get(0);
            balance=amount.getValue();
        }
        return EthConvert.fromWei(new BigDecimal(balance), EthConvert.Unit.fromLen(len));
    }





    public static void main(String[] args) {
     EnvInstance.setEnv(new EnvBase("119.23.237.46"));
        System.out.println(AccountFace.getMainCoinBalance("0x895FDd62dFD6A2b0ecd5294980781EA8F078AA83"));
        System.out.println(
                AccountFace.importByMnemonic("tag volcano eight thank tide danger coast health above argue embrace heavy"));
    }

}
