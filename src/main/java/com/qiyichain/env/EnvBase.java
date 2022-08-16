package com.qiyichain.env;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EnvBase {

    protected String ipAddr;
    protected String ipAddrServerUrl;
    protected String restServerUrl;
    protected String mainPrefix;
    protected String denom;
    protected String chainID;
    protected String hdPath;
    protected String validatorAddrPrefix;
    protected String pubPrefix;
    protected String restPathPrefix;
    protected String txUrlPath;
    protected String accountUrlPath;
    protected Web3j web3j;
    protected JsonRpcHttpClient jsonrpcClient;

    public JsonRpcHttpClient getJsonrpcClient() {
        return jsonrpcClient;
    }

    public EnvBase() {
        this.restServerUrl = "http://127.0.0.1:8545";
        this.ipAddr="127.0.0.1";
        this.ipAddrServerUrl="http://"+ipAddr+":1317";
        this.mainPrefix = "qyc";
        this.denom = "qyc";
        //todo
        this.chainID = "2285";
        this.hdPath = "M/44H/2285H/0H/0/1";


        this.accountUrlPath = "/accounts/";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30*1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(30*1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(30*1000, TimeUnit.MILLISECONDS);
        OkHttpClient httpClient = builder.build();
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545",httpClient,false));
        this.web3j=web3j;
        JsonRpcHttpClient jsonrpcClient = null;
        try {
            jsonrpcClient = new JsonRpcHttpClient(new URL("http://127.0.0.1:8545"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        jsonrpcClient.setHeaders(headers);
        this.jsonrpcClient=jsonrpcClient;

    }

    public EnvBase(String ip) {
        this.restServerUrl = "http://"+ip+":8545";
        this.mainPrefix = "de";
        this.ipAddr=ip;
        this.denom = "de";
        this.ipAddrServerUrl="http://"+ipAddr+":1317";
        this.chainID = "2285";
        this.hdPath = "M/44H/2285H/0H/1";
        this.pubPrefix = "depub";

        this.accountUrlPath = "/accounts/";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30*1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(30*1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(30*1000, TimeUnit.MILLISECONDS);
        OkHttpClient httpClient = builder.build();
        Web3j web3j = Web3j.build(new HttpService(this.restServerUrl,httpClient,false));
        this.web3j=web3j;

        JsonRpcHttpClient jsonrpcClient = null;
        try {
            jsonrpcClient = new JsonRpcHttpClient(new URL(this.restServerUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        jsonrpcClient.setHeaders(headers);
        this.jsonrpcClient=jsonrpcClient;
    }

    public String getIpAddrServerUrl() {
        return ipAddrServerUrl;
    }

    public String getRestServerUrl() {
        return restServerUrl;
    }

    public void setIpAddrServerUrl(String ipAddrServerUrl) {
        this.ipAddrServerUrl = ipAddrServerUrl;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    public String GetMainPrefix() {
        return this.mainPrefix;
    }

    public String GetDenom() {
        return this.denom;
    }

    public String GetChainid() {
        return this.chainID;
    }

    public String GetRestServerUrl() {
        return this.restServerUrl;
    }

    public String GetHDPath() {
        return this.hdPath;
    }

    public String GetValidatorAddrPrefix() {
        return this.validatorAddrPrefix;
    }

    public String GetPubPrefix() {
        return this.pubPrefix;
    }

    public String GetRestPathPrefix() {
        return this.restPathPrefix;
    }

    public String GetTxUrlPath() {
        return this.txUrlPath;
    }

    public String GetAccountUrlPath() {
        return this.accountUrlPath;
    }

    public void setRestServerUrl(String restServerUrl) {
        this.restServerUrl = restServerUrl;
    }

    public void setMainPrefix(String mainPrefix) {
        this.mainPrefix = mainPrefix;
    }

    public void setDenom(String denom) {
        this.denom = denom;
    }

    public void setChainID(String chainID) {
        this.chainID = chainID;
    }

    public void setHdPath(String hdPath) {
        this.hdPath = hdPath;
    }

    public void setValidatorAddrPrefix(String validatorAddrPrefix) {
        this.validatorAddrPrefix = validatorAddrPrefix;
    }

    public void setPubPrefix(String pubPrefix) {
        this.pubPrefix = pubPrefix;
    }

    public void setRestPathPrefix(String restPathPrefix) {
        this.restPathPrefix = restPathPrefix;
    }

    public void setTxUrlPath(String txUrlPath) {
        this.txUrlPath = txUrlPath;
    }

    public void setAccountUrlPath(String accountUrlPath) {
        this.accountUrlPath = accountUrlPath;
    }
}
