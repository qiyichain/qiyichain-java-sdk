package com.qiyichain.utils;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liyong E-mail:liam.li@qiyihuo.com
 * @version 创建时间：2022/9/1 12:03
 * @describe:
 */
public class ERC721AEvent extends Contract {

    public static final Event TRANSFER_EVENT = new Event("DeployERC721",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {},
                    new TypeReference<Address>(true) {},
                    new TypeReference<Address>(true) {},
                    new TypeReference<Utf8String>(true) {},
                    new TypeReference<Address>(true) {},
                    new TypeReference<Address>(true) {}));
    ;

    protected ERC721AEvent(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    protected ERC721AEvent(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    protected ERC721AEvent(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ERC721AEvent(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ERC721AEvent(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public ERC721AEvent(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static class DeployEventResponse {
        public Log log;

        public String id;

        public String callAddress;

        public String contractAddress;

    }

    public List<DeployEventResponse> getDeployEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<DeployEventResponse> responses = new ArrayList<DeployEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DeployEventResponse typedResponse = new DeployEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.callAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.contractAddress = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }
}
