## C/S模式 加密通讯
- 简介
   - 关键技术/理念 socket , DES , p2p 
   - 用socket作为通讯机制，DES加密内容，进行点对点的通讯
   
- 使用说明
   - 打开客户端
   - 作为host输入房间号，开启房间<br>
     作为guest输入房间号，进入房间
   - 双方进入后，通讯建立
   - 关闭客户端即可断开通讯
   
- 文件说明
    - 
    - Server中，Server源版本，Server0 1对1通讯版本
    - Client , Client可用版本，Client1 本地测试

- 安全性
    1. 
   
- 新思路
    - 仍然点对点通讯，增加图片，音频甚至视频支持；但可能需要校验
    或者速度慢。
    - 房间可以公开，根据房号可进，所有用户发言对房间内都可见，群聊
- 开发日志
    - 21-7-1
        1. 解决<br>
            - 服务器转发,多客户端组网通讯成功<br>
            - 多线程(6个)迅速增加cpu负担问题，加sleep<br>
        2. 遗留<br>
            - 房间查重<br>
            - 连接成功提醒，双方上线前禁用输入<br>
            - 身份问题，1对1通讯和 群发<br>
            - 权限问题，有没有必要增加权限机制<br>
            - 画报(真不想搞)黑客简笔画形象，稀奇古怪的字符流<br>
        
    - 21-7-2
        1. 解决<br>
            - 房间遗留问题,输入特定字符进行退房，后期如果要用ui的话可以进行封装<br>
            - 连接成功提醒<br>
            - 身份-1对1版本 **Server0**  服务器小分支
        2. 遗留<br>
            - 禁止输入方法<br>
            - 房间查重<br>
            - 画报<br>
            - 权限问题<br>
        
    - 21-7-3
        1. 解决<br>
            - 