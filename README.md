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
<a name="XO2ko"></a>
## 变更记录
> 记录每次修订的内容，方便追溯。

| **版本** | **修订内容** | **作者** | **发布日期** |
| --- | --- | --- | --- |
| v1.0 | 完成文档 | 0xmoy | 2022-09-28 |

代码仓库：[https://github.com/qiyichain/qiyichain-java-sdk](https://github.com/qiyichain/qiyichain-java-sdk)
<a name="UZdKd"></a>
## 1. 背景介绍
<a name="tHUV5"></a>
### 1.1 业务背景
> 用于与奇异链客户端集成的轻量级 Java 和 Android 库。


<a name="sgk74"></a>
## 2. 功能说明
<a name="cMIEx"></a>
### 2.1 功能列表
> 注意：任何写操作均需消耗一定GAS

| 编号 | **功能** | **类型** | **内容** |
| --- | --- | --- | --- |
| 100 | [创建钱包](#KpHKf) | 离线 | 创建助记词、私钥、公钥以及奇异链地址 |
| 101 | [导入助记词](#A0qOc) | 离线 | 通过导入助记词生成私钥、公钥以及奇异链地址 |
| 102 | [导入私钥](#zsIzm) | 离线 | 通过导入私钥生成公钥以及奇异链地址(注：无法还原助记词) |
| 103 | [查询GAS余额](#MClk6) | 查询 | 查询主网GAS余额 |
| 104 | [查询ERC20标准余额](#auf5p) | 查询 | 查询ERC20标准合约余额 |
| 105 | [查询合约拥有人](#wCTjP) | 查询 | 查询合约拥有人(仅限提供了公共访问own字段的合约) |
| 106 | [查询合约符号](#dOo4j) | 查询 | 查询合约符号(仅限提供了公共访问symbol字段的合约) |
| 107 | [查询合约名称](#jnl9I) | 查询 | 查询合约符号(仅限提供了公共访问name字段的合约) |
| 108 | [部署数字藏品合约](#WvVCh) | 写合约 | 发布数字藏品智能合约,ERC721A标准(注：需白名单账户地址方可调用) |
| 109 | [铸造藏品](#pUCMM) | 写合约 | 铸造(锻造)数字藏品 |
| 110 | [获取藏品部署合约](#JO6Mj) | 查询 | 根据部署交易hash解析合约地址 |
| 111 | [查询藏品拥有人](#ORLay) | 查询 | 查询指定藏品合约的token持有人地址 |
| 112 | [转移藏品](#ABeiG) | 写合约 | 转移藏品到指定地址 |
| 113 | [销毁藏品](#OVa4s) | 写合约 | 销毁藏品合约的token |
| 114 | [查询交易](#weo9b) | 查询 | 查询GAS转移、藏品部署、铸造、销毁等跟写合约有关的任何交易结果(结果存在一定延迟) |
| 115 | [转移GAS](#vL1eV) | 写链数据 | 转移GAS到指定地址 |
| 116 | [通用写合约](#dcc05) | 写合约 | 通用写合约方法,自定义的输入参数以及方法名 |
| 117 | [通用读合约](#VXoHX) | 查询 | 通用查询合约方法，自定义的输入参数以及方法名 |
| 118 | [账户当前Nonce](#G5qAO) | 查询 | 查询账户地址当前的Nonce值 |


<a name="lCjyZ"></a>
## 3. 功能使用
注意：使用非离线功能时，例如查询和写功能时，请先初始化请求信息：<br />`EnvInstance._setEnv_(new EnvBase("接口地址","链ID"));`<br />关于链ID： 主网(2285)，测试网(12285)
<a name="KpHKf"></a>
### 3.1.1 创建钱包
> 功能编号：100
> 创建助记词、私钥、公钥以及奇异链地址
> SDK方法路径：com.qiyichain.face.AccountFace#createAccount
> 使用方法：
> `AccountFace.createAccount()`

<a name="A0qOc"></a>
### 3.1.2 导入助记词
> 功能编号：101
> 导入助记词、私钥、公钥以及奇异链地址
> SDK方法路径：com.qiyichain.face.AccountFace#importByMnemonic
> 使用方法：
> `AccountFace.importByMnemonic("助记词内容")`

<a name="zsIzm"></a>
### 3.1.3 导入私钥
> 功能编号：102
> 通过导入私钥生成公钥以及奇异链地址(注：无法还原助记词)
> SDK方法路径：com.qiyichain.face.AccountFace#importByPrivateKey
> 使用方法：
> `AccountFace.importByPrivateKey("私钥内容")`

<a name="MClk6"></a>
### 3.1.4 查询GAS余额
> 功能编号：103
> 查询主网GAS余额
> SDK方法路径：com.qiyichain.face.AccountFace#getMainCoinBalance
> 使用方法：
> `AccountFace.getMainCoinBalance("链地址")`

<a name="auf5p"></a>
### 3.1.5 查询ERC20标准余额
> 功能编号：104
> 查询ERC20标准合约余额
> SDK方法路径：com.qiyichain.face.AccountFace#getContractCoinBalance
> 使用方法：
> `AccountFace.getContractCoinBalance("链地址","合约地址",合约精度)`
> 注意点：合约精度请参照部署合约时的精度传入，通常是18位

<a name="wCTjP"></a>
### 3.1.6 查询合约拥有人
> 功能编号：105
> 查询合约拥有人(仅限提供了公共访问own字段的合约)
> SDK方法路径：com.qiyichain.face.CoinFace#getOwner
> 使用方法：
> `CoinFace.getOwner("合约地址")`

<a name="dOo4j"></a>
### 3.1.7 查询合约符号
> 功能编号：106
> 查询合约符号(仅限提供了公共访问symbol字段的合约)
> SDK方法路径：com.qiyichain.face.CoinFace#getSymbol
> 使用方法：
> `CoinFace.getSymbol("合约地址")`

<a name="jnl9I"></a>
### 3.1.8 查询合约名称
> 功能编号：107
> 查询合约符号(仅限提供了公共访问symbol字段的合约)
> SDK方法路径：com.qiyichain.face.CoinFace#getName
> 使用方法：
> `CoinFace.getName("合约地址")`

<a name="WvVCh"></a>
### 3.1.9 部署数字藏品合约
> 功能编号：108
> 发布数字藏品智能合约,ERC721A标准(注：需白名单账户地址方可调用)
> SDK方法路径：com.qiyichain.face.NFTFace#deployERC721AFast
> 使用方法：
> `NFTFace.deployERC721AFast(...)`
> 参数说明：

| 参数名 | 说明 |
| --- | --- |
| priKey | 私钥 |
| factoryContract | 工厂合约地址(调用方需先行注册成白名单) |
| name | 藏品名称 |
| symbol | 藏品符号 |
| uri | IPFS 或 OSS 目录地址 |
| id | 部署唯一ID(可传业务ID) |
| isMint | 是否同时铸造 |
| amount | 铸造数量(不需要铸造时传0) |
| ownerAddress | 合约持有人地址 |
| nonce | 调用顺序编号，取值参考 [这里](#G5qAO) |

<a name="pUCMM"></a>
### 3.2.0 铸造藏品
> 功能编号：109
> 铸造(锻造)数字藏品
> SDK方法路径：com.qiyichain.face.NFTFace#mintByNonce
> 使用方法：
> `NFTFace.mintByNonce(...)`
> 参数说明：

| 参数名 | 说明 |
| --- | --- |
| priKey | 私钥 |
| contract | 藏品合约地址 |
| amount | 铸造数量 |
| nonce | 调用顺序编号，取值参考 [这里](#G5qAO) |

<a name="JO6Mj"></a>
### 3.2.1 获取藏品部署合约
> 功能编号：110
> 根据部署交易hash解析合约地址
> SDK方法路径：com.qiyichain.face.NFTFace#decodeContractAddressFromLog
> 使用方法：
> `NFTFace.decodeContractAddressFromLog("交易Hash")`
> 参数说明：交易hash为 [部署藏品接口](#WvVCh)返回的hash字段

<a name="ORLay"></a>
### 3.2.2 查询藏品拥有人
> 功能编号：111
> 查询指定藏品合约的token持有人地址
> SDK方法路径：com.qiyichain.face.NFTFace#ownerOf
> 使用方法：
> `CoinFace.ownerOf("合约地址","藏品编号")`

<a name="ABeiG"></a>
### 3.2.3 转移藏品
> 功能编号：112
> 查询指定藏品合约的token持有人地址
> SDK方法路径：com.qiyichain.face.NFTFace#transferFast
> 使用方法：
> `CoinFace.transferFast(...)`

| 参数名 | 说明 |
| --- | --- |
| priKey | 私钥 |
| contract | 藏品合约地址 |
| toAddr | 接收人地址 |
| tokenId | 藏品编号 |
| nonce | 调用顺序编号，取值参考 [这里](#G5qAO) |

<a name="OVa4s"></a>
### 3.2.4 销毁藏品
> 功能编号：113
> 查询指定藏品合约的token持有人地址
> SDK方法路径：com.qiyichain.face.NFTFace#burnFast
> 使用方法：
> `CoinFace.burnFast(...)`

| 参数名 | 说明 |
| --- | --- |
| priKey | 私钥 |
| contract | 藏品合约地址 |
| tokenId | 藏品编号 |
| nonce | 调用顺序编号，取值参考 [这里](#G5qAO) |

<a name="weo9b"></a>
### 3.2.5 查询交易
> 功能编号：114
> 查询GAS转移、藏品部署、铸造、销毁等跟写合约有关的任何交易结果(结果存在一定延迟，若返回false并不代表交易失败，需间隔1秒重试，通常重试10次之后仍然false则表示失败)
> SDK方法路径：com.qiyichain.face.TransactionFace#getTransactionStatus
> 使用方法：
> `TransactionFace.getTransactionStatus("交易hash")`

<a name="vL1eV"></a>
### 3.2.6 转移GAS
> 功能编号：115
> 转移GAS到指定地址
> SDK方法路径：com.qiyichain.face.TransactionFace#sendCommonAndGet
> 使用方法：
> `TransactionFace.sendCommonAndGet("交易发起人私钥","数量","接收人地址")`


<a name="dcc05"></a>
### 3.2.7 通用写合约
> 功能编号：116
> 通用写合约方法,自定义的输入参数以及方法名，如需部署自定义合约，需开通白名单功能
> SDK方法路径：com.qiyichain.face.TransactionFace#callContractFunctionOpByNonce
> 使用方法：
> `CoinFace.callContractFunctionOpByNonce(...)`

| 参数名 | 说明 |
| --- | --- |
| priKey | 私钥 |
| contractAddress | 合约地址 |
| inputParams | 入参列表 |
| funcName | 合约方法名 |
| maxGas | 最大gas限制 |
| gasPrice | gas价格 |
| nonce | 调用顺序编号，取值参考 [这里](#G5qAO) |

<a name="VXoHX"></a>
### 3.2.7 通用读合约
> 功能编号：117
> 通用写合约方法,自定义的输入参数以及方法名，如需部署自定义合约，需开通白名单功能
> SDK方法路径：com.qiyichain.face.TransactionFace#callContractViewMethod
> 使用方法：
> `CoinFace.callContractViewMethod(...)`

| 参数名 | 说明 |
| --- | --- |
| contract | 合约地址 |
| method | 合约方法名 |
| inputParams | 入参列表 |
| outputParams | 出参列表(结果字段列表) |

<a name="G5qAO"></a>
### 3.2.8 账户当前Nonce
> 功能编号：118
> 查询账户地址当前的Nonce值，由于交易是节点排序打包，若发送nonce冲突，则按gas最高的优先被打包，交易存在被覆盖的可能，此方法可返回不同状态下的nonce值，值得注意的是，不管何种状态，交易提交前，请读取最新的nonce值。
> SDK方法路径：com.qiyichain.face.TransactionFace#getNonce
> 使用方法：
> `CoinFace.getNonce(...)`

| 参数名 | 说明 |
| --- | --- |
| fromAddress | 发起人地址 |
| type | 区块打包类型，LATEST(最新区块)，PENDING(打包中区块)。<br />使用LATEST时，同一个发起人地址请勿并发提交，否则容易返回相同的nonce<br />使用PENDING打包，则返回未上链，且在缓存中的交易顺序。（推荐使用）<br />不同发起人地址之间，nonce没有关联。<br />同一发起人，请勿并发调用，避免nonce覆盖的情况 |

