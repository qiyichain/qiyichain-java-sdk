package com.qiyichain.face;


import com.alibaba.fastjson.JSON;
import com.qiyichain.env.EnvBase;
import com.qiyichain.env.EnvInstance;

import com.qiyichain.msg.coin.BaseMsg;
import com.qiyichain.msg.coin.FastMsg;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 签名类接口
 */
public class TransactionFace {

    private static final BigInteger ETH_MAX_GAS=BigInteger.valueOf(21000);
    private static final BigInteger CONTRACT_MAX_GAS=BigInteger.valueOf(100000);

    public static BigInteger getCommonGasPrice() {
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        EthGasPrice gasPrice = null;
        try {
            gasPrice = web3j.ethGasPrice().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger baseGasPrice =  gasPrice.getGasPrice();
        return baseGasPrice;
    }
    public static BigInteger getNonce(String fromAddress) throws ExecutionException, InterruptedException {
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        return nonce;
    }



    //获取主链推荐手续费
    public static BigDecimal getEthFee(){
        BigInteger gasPrice= getCommonGasPrice();
        return new BigDecimal(gasPrice).multiply(new BigDecimal(ETH_MAX_GAS)).divide((BigDecimal.TEN.pow(18))).setScale(8,BigDecimal.ROUND_DOWN);
    }




    //获取合约推荐手续费
    public static BigDecimal getContractFee(){
        BigInteger gasPrice= getCommonGasPrice();
        return new BigDecimal(gasPrice).multiply(new BigDecimal(CONTRACT_MAX_GAS)).divide((BigDecimal.TEN.pow(18))).setScale(8,BigDecimal.ROUND_DOWN);
    }


    /**
     * 查询交易是否完成等信息
     */
    public static boolean getTransactionStatus(String hash){
        TransactionReceipt transactionReceipt=getTransactionDetail(hash);
        if (transactionReceipt!=null&&transactionReceipt.getStatus().equalsIgnoreCase("0x1")){
            return true;
        }
        return false;
    }


    /**
     * 获取交易详情
     * @param hash
     * @return
     *
     * String transactionH
     * String transactionI
     * String blockHash;
     * String blockNumber;
     * String cumulativeGa
     * String gasUsed;
     * String contractAddr
     * String root;
     * String status;  状态
     * String from;
     * String to;
     * List<Log> logs; 日志（重要！！！）
     * String logsBloom;
     */
    public static TransactionReceipt getTransactionDetail(String hash){
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        Optional<TransactionReceipt> optional=null;
        try {
            optional= web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (optional!=null&&optional.isPresent()){
            return optional.get();
        }
        return null;
    }






    /**
     * 发送普通交易
     * @param priKey
     * @param amount
     * @param toAddress
     * @return
     */
    public static String sendCommonTrans(String priKey,String amount,String toAddress){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        String hexValue=commonTransSign(priKey,amount,toAddress);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            return transactionHash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BaseMsg sendCommonAndGet(String priKey, String amount, String toAddress){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        String hexValue=commonTransSign(priKey,amount,toAddress);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            return BaseFace.dealMsg(transactionHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseMsg.buildError("send fail");
    }

    /**
     * 发送合约交易
     * @param priKey
     * @param amount
     * @param toAddress
     * @return
     */
    public static String sendContractTrans(String priKey,String contract,String amount,String toAddress){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        String hexValue=contract20Sign(priKey,contract,amount,toAddress);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            return transactionHash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送合约交易
     * @param priKey
     * @param amount
     * @param toAddress
     * @return
     */
    public static BaseMsg sendContractTransAndGet(String priKey,String contract,String amount,String toAddress){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        String hexValue=contract20Sign(priKey,contract,amount,toAddress);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            return BaseFace.dealMsg(transactionHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 普通交易签名
     */
    public static String commonTransSign(String priKey,String amount,String toAddress){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            BigInteger gasPrice = getCommonGasPrice();
            BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

            BigInteger maxGas = new BigInteger("21000");
            System.out.println("value="+value+",gasPrice="+gasPrice+",gasLimit="+maxGas+",nonce="+nonce+",address="+toAddress);
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, gasPrice, maxGas, toAddress, value);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,EnvInstance.getChainId(), credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            return hexValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String commonTransSignByNonce(String priKey,String amount,String toAddress,BigInteger nonce){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            BigInteger gasPrice = getCommonGasPrice();
            BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

            BigInteger maxGas = new BigInteger("21000");
            System.out.println("value="+value+",gasPrice="+gasPrice+",gasLimit="+maxGas+",nonce="+nonce+",address="+toAddress);
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, gasPrice, maxGas, toAddress, value);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,EnvInstance.getChainId(), credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            return hexValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 合约交易签名
     */
    public static String contract20Sign(String priKey,String contractAddress,String amount,String toAddress){
        if (StringUtils.isEmpty(toAddress)){
            System.out.println("to addr is null");
            return null;
        }
        if (!toAddress.startsWith("0x")){
            toAddress= AccountFace.addressToHex(toAddress);
        }
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            BigInteger gasPrice=BaseMsg.GAS_PRICE.toBigInteger();
            BigInteger value = new BigInteger(amount);
            Function fn = new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(value)), Collections.<TypeReference<?>>emptyList());
            String data = FunctionEncoder.encode(fn);
            System.out.println("value="+value+",gasPrice="+gasPrice+",gasLimit="+CONTRACT_MAX_GAS+",nonce="+nonce+",address="+toAddress+"contract="+contractAddress);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice, CONTRACT_MAX_GAS, contractAddress, data);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, EnvInstance.getChainId(),credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            System.out.println("hexRawValue ="+hexValue );
            return hexValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 调用指定合约函数,此方法是调用操作合约的方法，需要消耗gas
     * 函数名，参数列表
     *  外部自行维护nonce
     */
    public static String callContractFunctionOpByNonce(String priKey, String contractAddress,
                                                List<Type> inputParams,
                                                String funcName, BigInteger maxGas, BigInteger gasPrice, BigInteger nonce){
        List<TypeReference<?>> result=new ArrayList<>();
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            Function fn = new Function(funcName, inputParams,result);
            String data = FunctionEncoder.encode(fn);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice.multiply(BigInteger.TEN.pow(9)), maxGas, contractAddress, data);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,EnvInstance.getChainId(), credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            System.out.println("hexRawValue ="+hexValue );
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            return transactionHash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 调用指定合约函数,此方法是调用操作合约的方法，需要消耗gas
     * 函数名，参数列表
     */
    public static String callContractFunctionOp(String priKey, String contractAddress,
                                                      List<Type> inputParams,
                                                      String funcName, BigInteger maxGas, BigInteger gasPrice){
        List<TypeReference<?>> result=new ArrayList<>();
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            Function fn = new Function(funcName, inputParams,result);
            String data = FunctionEncoder.encode(fn);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice.multiply(BigInteger.TEN.pow(9)), maxGas, contractAddress, data);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,EnvInstance.getChainId(), credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            System.out.println("hexRawValue ="+hexValue );
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            return transactionHash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 调用指定合约函数,返回调用状态和失败消息
     * 函数名，参数列表
     */
    public static FastMsg callContractFunction(String priKey, String contractAddress,
                                               List<Type> inputParams,
                                               String funcName, BigInteger maxGas, BigInteger gasPrice,BigInteger nonce){
        FastMsg fastMsg=new FastMsg();
        List<TypeReference<?>> result=new ArrayList<>();
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            Function fn = new Function(funcName, inputParams,result);
            String data = FunctionEncoder.encode(fn);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice.multiply(BigInteger.TEN.pow(9)), maxGas, contractAddress, data);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,EnvInstance.getChainId(), credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            System.out.println("hexRawValue ="+hexValue );
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash );
            if(StringUtils.isEmpty(transactionHash)){
                fastMsg.setSuccess(false);
                if(ethSendTransaction.getError()==null){
                    fastMsg.setMsg("unknown:"+ethSendTransaction.getResult());
                }else {
                    fastMsg.setMsg(ethSendTransaction.getError().getMessage());
                }
            }else {
                fastMsg.setSuccess(true);
                fastMsg.setHash(transactionHash);
            }
            return fastMsg;
        } catch (Exception e) {
            e.printStackTrace();
            fastMsg.setSuccess(false);
            fastMsg.setMsg(e.getMessage());
        }
        return fastMsg;
    }


    /**
     * 调用指定合约函数,此方法是调用操作合约的方法，需要消耗gas
     * 函数名，参数列表
     * 附带value
     */
    public static String callContractFunctionOpValue(String priKey, String contractAddress,
                                                List<Type> inputParams,
                                                String funcName, BigInteger maxGas, BigInteger gasPrice,BigInteger value){
        List<TypeReference<?>> result=new ArrayList<>();
        Credentials credentials=Credentials.create(priKey);
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        try {
            EthGetTransactionCount ethGetTransactionCount =web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            Function fn = new Function(funcName, inputParams,result);
            String data = FunctionEncoder.encode(fn);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice, maxGas, contractAddress,value, data);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            System.out.println("hexRawValue ="+hexValue );
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("txid ="+transactionHash +",result="+ethSendTransaction.getResult());
            return transactionHash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 无需消耗gas的合约调用，仅限支持view的方法
     */
    public static List<Type> callContractViewMethod(String from,String contract,String method,List<Type> inputParams,List<TypeReference<?>> outputParams){
        Web3j web3j=EnvInstance.getEnv().getWeb3j();

        Function function = new Function(
                method,
                inputParams,
                outputParams);

        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = null;
        try {
            response = web3j.ethCall(
                    org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(from,contract, encodedFunction),
            DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response!=null){
            List<Type> someTypes = FunctionReturnDecoder.decode(
                    response.getValue(), function.getOutputParameters());
            return someTypes;
        }else {
            return null;
        }
    }




    public static Transaction getTransaction(String hash){
        Web3j web3j=EnvInstance.getEnv().getWeb3j();
        Optional<Transaction> optional=null;
        try {
            optional= web3j.ethGetTransactionByHash(hash).send().getTransaction();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (optional!=null&&optional.isPresent()){
            return optional.get();
        }
        return null;
    }
    /**
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EnvInstance.setEnv(new EnvBase("119.23.210.140"));
        Transaction transaction=TransactionFace.getTransaction("0x998a5433f1d4d1dfba57c3a0a1023ab51499df22516ede4e2bca35c996809e1d");
        TransactionFace.getTransactionStatus("0x998a5433f1d4d1dfba57c3a0a1023ab51499df22516ede4e2bca35c996809e1d");

    }



    private static void  testStatus(String hs){
        System.out.println(TransactionFace.getTransactionStatus(hs));
    }


    private static void testMethodSign(){
        System.out.println(FunctionEncoder.encode(new Function("redpackCreated",new ArrayList<>(),new ArrayList<>())));
    }


    private static void sendErc20(){
        System.out.println(sendContractTransAndGet("551ab8527bc625e607b5ca58aa9aeb3b617d2f297d512b322ecc042d21a3d5c6","0xaED255425d58ADbd7f0dFcE809943eA5F4DcE432",new BigInteger("500").multiply(BigInteger.TEN.pow(18))+"",AccountFace.addressToHex("de18wryxwsvh9yfpn4m087mze8n4s7dl0kjepxhm2")));
    }




    public static void decodeInput() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String inputData = "0xa9059cbb00000000000000000000000035f5992e40facfcad742fcfcc1d94ee0581e9cb100000000000000000000000000000000000000000000003635c9adc5dea00000";
        String method = inputData.substring(0, 10);
        System.out.println(method);
        String to = inputData.substring(10, 74);
        String value = inputData.substring(74);
        Method refMethod = TypeDecoder.class.getDeclaredMethod("decode", String.class, int.class, Class.class);
        refMethod.setAccessible(true);
        Address address = (Address) refMethod.invoke(null, to, 0, Address.class);
        System.out.println(address.toString());
        Uint256 amount = (Uint256) refMethod.invoke(null, value, 0, Uint256.class);
        System.out.println(amount.getValue());


    }




}
