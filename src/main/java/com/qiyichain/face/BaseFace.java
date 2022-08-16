package com.qiyichain.face;

import com.qiyichain.env.EnvBase;
import com.qiyichain.env.EnvInstance;
import com.qiyichain.msg.coin.BaseMsg;

import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class BaseFace {

    public static BaseMsg dealMsg(String hash){
        BaseMsg baseMsg=new BaseMsg();
        baseMsg.setHash(hash);
        try {
            CompletableFuture<String> futureSubmit = CompletableFuture.supplyAsync(()->{
                return hash;
            });
            CompletableFuture<Boolean> future2 = futureSubmit.thenApply((p)->{
                if (StringUtils.isEmpty(p)){
                    return false;
                }
                int tryCount=0;
                boolean status=false;
                while (tryCount<10&&!status){
                    tryCount++;
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("try count "+ tryCount);
                    status=TransactionFace.getTransactionStatus(p);
                }
                return status;
            });
            baseMsg.setHash(hash);
            if (future2.join()){
                baseMsg.setMsg("success");
                baseMsg.setSuccess(true);
            }else {
                baseMsg.setMsg("失败");
                baseMsg.setSuccess(false);
            }
            return baseMsg;
        }catch (Exception e){
            e.printStackTrace();
            return BaseMsg.buildError("执行错误");
        }
    }



    public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }

    public static void main(String[] args) throws Exception {
        EnvInstance.setEnv(new EnvBase("x"));
        String priKey="6e8e06bc5e83d82e90c63370fcf741b0a40e48f31a5789b698002e99a0b512e8";
        String name="xxx";
        test();
    }

    private static void test(){
        EnvInstance.setEnv(new EnvBase(""));
        String priKey="";
        String name="xxx";
        List<Type> params=Arrays.<Type>asList(new DynamicArray<Address>(new Address("0xe18c0c5336bd9f09423f9ad53070e6940e72f3f0")),new DynamicArray<Utf8String>(new Utf8String(name)));
        //List<Type> params= Arrays.asList(new DynamicArray(new Address("0xfcf2fa6b2abb863e73d421797944aa19ec719811")),new DynamicArray(new Utf8String(name)));
        BaseMsg baseMsg=BaseFace.dealMsg(TransactionFace.callContractFunctionOp(priKey,"0x2ea7b8661D7395bcD6d777C50B075BDd2E61b110",params,"updateTokenName",BaseMsg.GAS_LIMIT.toBigInteger().multiply(BigInteger.TEN),BaseMsg.GAS_PRICE.toBigInteger().multiply(BigInteger.TEN)));
    }
}
