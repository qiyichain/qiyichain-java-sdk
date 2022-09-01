## Platform introduction
The qiyichain SDK provides the underlying signature and communication functions for accessing the qiyichain blockchain to interact with the qiyichain blockchain and smart contract. The qiyichain SDK is also suitable for Ethereum system access. The qiyichain SDK provides comprehensive decentralized interaction. For better user experience, it is recommended…

## Build
```
 mvn install
```
and
```
 <dependency>
    <groupId>com.qiyichain</groupId>
    <artifactId>qiyichain-java-sdk</artifactId>
    <version>0.18.0</version>
 </dependency>
```
If a dependency conflict occurs...
```
 <dependency>
  <groupId>com.google.guava</groupId>
  <artifactId>guava</artifactId>
  <version>30.1-jre</version>
 </dependency>
 <dependency>
    <groupId>com.qiyichain</groupId>
    <artifactId>qiyichain-java-sdk</artifactId>
    <version>0.18.0</version>
 </dependency>
```

## How to use
### Create account
```
com.qiyichain.face.AccountFace.createAccount
```
### Import account (Mnemonic)
```
com.qiyichain.face.AccountFace.importByMnemonic
```
### Import account (PriKey)
```
com.qiyichain.face.AccountFace.importByPrivateKey
```
### Get balance 
```
com.qiyichain.face.AccountFace.getMainCoinBalance
```
### Get token balance
```
com.qiyichain.face.AccountFace.getContractCoinBalance
```
### Send base transaction
```
com.qiyichain.face.TransactionFace.sendCommonTrans
```
### Send erc20 transaction
```
com.qiyichain.face.TransactionFace.sendContractTrans
```
### Call any contract method
```
com.qiyichain.face.TransactionFace.callContractFunctionOp
```
### Call deploy nft contract and mint
```
com.qiyichain.face.NFTFace.deployERC721A
```