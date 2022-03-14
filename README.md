## <center> 基于socket的DES加密通讯 </center>
<br></br>
### 更新至gitee https://gitee.com/eric-bigcat/encryptComm.git


- DES加密 / 解密
1. 字符根据GBK编码转为64位2进制进行加密
2. 用Base64再次加密为可打印字符作为密文进行通讯传输 
3. 解密部分对 2 , 1 进行逆向操作
<br></br>
- Socket通讯
1. 共有内容：对通讯内容每4个字符进行切割 / 连接
2. Server保持监听，等到Client上线后绑定，进行3次握手
3. 通讯连接由Server保持，且客户端不能主动关闭连接

- 点对点通讯
1. 借鉴思路
    1. croc 点对点/p2p 文件传输模式
    2. clubhouse
    3. qq/微信群聊
    4. 
    
2. 功能
    1. 点对点通讯，通过门牌号进行连接
    2. 
<br></br>
- 接口

    DES
    1. DES(String str);
        1. 参数: str为密钥
        2. 返回: void
    2. userEn(String text);
        1. 参数: text为明文
        2. 返回: Base64二次加密的密文(String)
    3. userDe(String text);
        1. 参数: text为Base64密文
        2. 返回: 明文GBK字符集格式(String)
        
    Socket
    1. 
    
    
- 问题
    1. 断点存续 - 存在如 “asd加中文” 其中“文”字要分给下一次加密，
    这样的话怎么能保证下次从 断点 继续读64位？<br><br>
    tempB[j] = arrB[i*8+j]
     -> temp[j] = arrB[count]<br>当需要的时候break内层循环
     并且进行加密，把第8位是中文字符的第一位放到下一次加密。
     2. sss

Q:为什么要用Base64再加密一次？

A:。因为网络传输只能传输可打印字符。什么是可打印字符？
在ASCII码中规定，0~31、127这33个字符属于控制字符，
32~126这95个字符属于可打印字符，也就是说网络传输只能传输这95个字符，
不在这个范围内的字符无法传输。
