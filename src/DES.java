import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

public class DES {
    private String plaintext, str, ciphertext;
    private char[] plain = new char[64], plainL = new char[32], plainR = new char[32];
    private char[] key56 = new char[56], keyL = new char[28], keyR = new char[28];
    private char[][] keys = new char[16][];

    //修改 49 <-> 4 , 17 <-> 47;
    protected static char[] ipTable = {
            58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 49,
            62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
            57, 4, 41, 33, 25, 47,  9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 17, 39, 31, 23, 15, 7
    };
    protected static char[] keyInit = {
            57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4
    };
    protected static char[] keyCompress = {
            14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    };
    protected static char[] sbox1 = {
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
    };
    protected static char[] sbox2 = {
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
    };
    protected static char[] sbox3 = {
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
    };
    protected static char[] sbox4 = {
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 19,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
    };
    protected static char[] sbox5 = {
            2, 12, 4, 1, 7, 10, 11, 6, 5, 8, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 13, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
    };
    protected static char[] sbox6 = {
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
    };
    protected static char[] sbox7 = {
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
    };
    protected static char[] sbox8 = {
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
    };
    protected static char[] pbox = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25
    };







    //数据设置
    public void setPlaintext(String str){ this.plaintext = str; }
    public void setStr(String str){ this.str = str; }
    public void setCiphertext(String str){ this.ciphertext = str; }

    DES(){ }
    DES(String str){
        setStr(str);
    }

    //socket加密通讯
    public String userEn(String text) throws UnsupportedEncodingException {
        return encrypt(text, this.str);
    }
    public String userDe(String text) throws UnsupportedEncodingException {
        return decipherin(text, this.str);
    }

    public String encrypt(char[] s0, char[] k0) throws UnsupportedEncodingException {
        //将明文和密钥进行转码,并进行IP置换和奇偶校验

        this.plain = ipReplace(s0);
        this.key56 = getFirstKey(k0);
        char[] key, R = new char[32];
        char[] temp;
        //得到各轮的密钥
        for(int i=1; i<=16; i++){
            key = getKey(i);

//            System.out.print(i+"  key:");
//            for(int n=0; n<12; n++){
//                for(int m=0; m<4; m++){
//                    System.out.print(key[n*4+m]);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();

            //保存初始的R R=R(i)

            System.arraycopy(this.plainR, 0, R, 0, 32);
            temp = getR(key);
            //this.plainR = R(i+1)
            System.arraycopy(getR(key), 0, this.plainR, 0, 32);

            System.out.print(i+"  R:");
            for(int n=0; n<8; n++){
                for(int m=0; m<4; m++){
                    System.out.print(this.plainR[n*4+m]);
                }
                System.out.print(" ");
            }
            System.out.println();

            //this.plainL = R(i) L(i+1) = R(i)
            System.arraycopy(R, 0, this.plainL, 0, 32);

            System.out.print(i+"  L:");
            for(int n=0; n<8; n++){
                for(int m=0; m<4; m++){
                    System.out.print(this.plainL[n*4+m]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }

        char[] ss = toCombine();
        for(int i=0; i<ss.length; i++){
            System.out.print(ss[i]);
        }
        System.out.println();
        this.plain = inIp(toCombine());
        System.out.println("加密后的密文binary");
        for(int i=0; i<plain.length; i++){
            System.out.print(plain[i]);
        }
        return toStrGBK(plain);
    }
    public String decipherin(char[] s0, char[] k0) throws UnsupportedEncodingException {
        //将明文和密钥进行转码,并进行IP置换和奇偶校验

        this.plain = ipReplace(s0);
        this.key56 = getFirstKey(k0);
        char[] key, R = new char[32];
        char[] temp;

//        System.arraycopy(this.plainL, 0, R, 0, 32);
//        System.arraycopy(this.plainR, 0, this.plainL, 0, 32);
//        System.arraycopy(R, 0, this.plainR, 0, 32);
        //System.arraycopy(Integer.toString(s,2).toCharArray(), 0, arr32, 2, 4);
        //得到各轮的密钥
        for(int i=1; i<=16; i++){
            keys[i-1] = getKey(i);
        }

        for(int i=1; i<=16; i++){




            //保存初始的R R=R(i)

            System.arraycopy(this.plainR, 0, R, 0, 32);
            temp = getR(keys[16-i]);
            //this.plainR = R(i+1)
            System.arraycopy(getR(keys[16-i]), 0, this.plainR, 0, 32);

            System.out.print(i+"  R:");
            for(int n=0; n<8; n++){
                for(int m=0; m<4; m++){
                    System.out.print(this.plainR[n*4+m]);
                }
                System.out.print(" ");
            }
            System.out.println();

            //this.plainL = R(i) L(i+1) = R(i)
            System.arraycopy(R, 0, this.plainL, 0, 32);

            System.out.print(i+"  L:");
            for(int n=0; n<8; n++){
                for(int m=0; m<4; m++){
                    System.out.print(this.plainL[n*4+m]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }

        char[] ss = toCombine();
        for(int i=0; i<ss.length; i++){
            System.out.print(ss[i]);
        }
        System.out.println();
        this.plain = inIp(toCombine());

        System.out.println("解密后的明文binary");
        for(int i=0; i<plain.length; i++){
            System.out.print(plain[i]);
        }

        return toStrGBK(plain);
    }
    //返回加密后的密文
    public String encrypt(String plaintext, String str) throws UnsupportedEncodingException {
        //将明文和密钥进行转码,并进行IP置换和奇偶校验
//        System.out.println("明文字符串转2进制:");
        this.plain = ipReplace(toArrb(plaintext));
//        System.out.println("密钥字符串转2进制:");
        this.key56 = getFirstKey(toArrb(str));
//        this.plain = ipReplace(s0);
//        this.key56 = getFirstKey(k0);
        char[] key, R = new char[32];
        char[] temp;
        //System.arraycopy(Integer.toString(s,2).toCharArray(), 0, arr32, 2, 4);
        //得到各轮的密钥
        for(int i=1; i<=16; i++){
            key = getKey(i);

//            System.out.print(i+"  key:");
//            for(int n=0; n<12; n++){
//                for(int m=0; m<4; m++){
//                    System.out.print(key[n*4+m]);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();

            //保存初始的R R=R(i)

            System.arraycopy(this.plainR, 0, R, 0, 32);
            temp = getR(key);
            //this.plainR = R(i+1)
            System.arraycopy(getR(key), 0, this.plainR, 0, 32);

//            System.out.print(i+"  R:");
//            for(int n=0; n<8; n++){
//                for(int m=0; m<4; m++){
//                    System.out.print(this.plainR[n*4+m]);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();

            //this.plainL = R(i) L(i+1) = R(i)
            System.arraycopy(R, 0, this.plainL, 0, 32);

//            System.out.print(i+"  L:");
//            for(int n=0; n<8; n++){
//                for(int m=0; m<4; m++){
//                    System.out.print(this.plainL[n*4+m]);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();
        }

//        System.out.println();
        this.plain = inIp(toCombine());
//        System.out.println("加密后的密文binary");
//        for(int i=0; i<plain.length; i++){
//            System.out.print(plain[i]);
//        }
//        System.out.println("密文2进制转Base64:");
        return toBase64(plain);
    }
    //返回解密后的明文
    public String decipherin(String plaintext, String str) throws UnsupportedEncodingException {
        //将明文和密钥进行转码,并进行IP置换和奇偶校验
//        System.out.println("密文Base64转2进制:");
        this.plain = ipReplace(base64ToArrb(plaintext));
//        System.out.println("密钥字符串转2进制:");
        this.key56 = getFirstKey(toArrb(str));
//        this.plain = ipReplace(s0);
//
//        this.key56 = getFirstKey(k0);
        char[] key, R = new char[32];
        char[] temp;


        for(int i=1; i<=16; i++){
            keys[i-1] = getKey(i);
        }

        for(int i=1; i<=16; i++){
            System.arraycopy(this.plainR, 0, R, 0, 32);
            temp = getR(keys[16-i]);
            //this.plainR = R(i+1)
            System.arraycopy(getR(keys[16-i]), 0, this.plainR, 0, 32);

//            System.out.print(i+"  R:");
//            for(int n=0; n<8; n++){
//                for(int m=0; m<4; m++){
//                    System.out.print(this.plainR[n*4+m]);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();

            //this.plainL = R(i) L(i+1) = R(i)
            System.arraycopy(R, 0, this.plainL, 0, 32);

//            System.out.print(i+"  L:");
//            for(int n=0; n<8; n++){
//                for(int m=0; m<4; m++){
//                    System.out.print(this.plainL[n*4+m]);
//                }
//                System.out.print(" ");
//            }
//            System.out.println();
        }

        char[] ss = toCombine();
//        for(int i=0; i<ss.length; i++){
//            System.out.print(ss[i]);
//        }
//        System.out.println();
        this.plain = inIp(toCombine());

//        System.out.println("解密后的明文binary");
//        for(int i=0; i<plain.length; i++){
//            System.out.print(plain[i]);
//        }

//        System.out.println("明文字符串转2进制:");
        return toStrGBK(plain);
    }



    //通用
    //字符按照UTF-8转成2进制
    public char[] toArrb(String strs) throws UnsupportedEncodingException {
        char[] temp;
        byte[] arrD = strs.getBytes("GBK");
        String strB = new BigInteger(1,arrD).toString(2);
        //防止
        if(strB.length()%8 != 0){
            strB = '0'+strB;
        }
        int num = strB.length();
        if(num<64){
            for(int i=0; i<64-num; i++){
                strB = '0'+strB;
            }
        }
        temp = strB.toCharArray();
//        System.out.println("字符转bin"+strs);
//        for(int i=0; i<temp.length; i++){
//            System.out.print(temp[i]);
//        }
//        System.out.println();
        return temp;
    }
    //2进制按照UTF-8转回字符
    public String toStrGBK(char[] charB) throws UnsupportedEncodingException {
        //charArrB 2进制字符数组
        String strB="";
        for(int i=0; i<charB.length; i++){
            strB +=charB[i];
        }
        //strB 是2进制字符串
        String [] arrB = new String [strB.length()/8];
        //arrB 将2进制字符串按照8位分割
        for (int i=0; i<strB.length(); i+=8) {
            arrB[i/8] = strB.substring(i, i+8);
        }
        byte [] arrD = new byte[arrB.length];
        //arrD 将8位2进制转成10进制
        for(int i=0; i< arrB.length; i++) {
            arrD[i] = (byte)Integer.parseInt(arrB[i].toString(), 2);
        }
//        System.out.println("bin转字符");

        //将10进制按照UTF-8编码规则转成相应字符
        return new String(arrD, "GBK");
    }

    //base64解码并转成2进制
    public char[] base64ToArrb(String strs) throws UnsupportedEncodingException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] arrD = decoder.decode(strs);

//        System.out.println();
//        System.out.println("解密后的byte[]: ");
//        for(int i=0; i<arrD.length; i++){
//            System.out.print(arrD[i]);
//        }
//        System.out.println();
        String strB = new BigInteger(1,arrD).toString(2);
        char[] temp = strB.toCharArray();
        //防止
        if(strB.length()%8 != 0){
            strB = '0'+strB;
        }
        int num = strB.length();
        if(num<64){
            for(int i=0; i<64-num; i++){
                strB = '0'+strB;
            }
        }
        temp = strB.toCharArray();
//        System.out.println("字符转bin");
//        for(int i=0; i<temp.length; i++){
//            System.out.print(temp[i]);
//        }
//        System.out.println();
        return temp;
    }
    //base64加密编码
    public String toBase64(char[] charB) throws UnsupportedEncodingException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        //charArrB 2进制字符数组
        String strB="";
        for(int i=0; i<charB.length; i++){
            strB +=charB[i];
        }
        //strB 是2进制字符串
        String [] arrB = new String [strB.length()/8];
        //arrB 将2进制字符串按照8位分割
        for (int i=0; i<strB.length(); i+=8) {
            arrB[i/8] = strB.substring(i, i+8);
        }
        byte [] arrD = new byte[arrB.length];
        //arrD 将8位2进制转成10进制
//        System.out.println();
//        System.out.println("加密后的byte[]: ");
        for(int i=0; i< arrD.length; i++) {
            arrD[i] = (byte)Integer.parseInt(arrB[i].toString(), 2);
//            System.out.print(arrD[i]);
        }
//        System.out.println();
        //将10进制按照base64编码规则加密字符
        return encoder.encodeToString(arrD);
    }


    //密钥部分
    //返回56位密钥,初始化L0,R0 - 选择置换PC1
    private char[] getFirstKey(char[] key){
        char[] key56 = new char[56];
        int k=0;
        for(int i=0; i<56; i++){
            key56[i] = key[keyInit[i]-1];
        }
        for(int i=0; i<28; i++){
            this.keyL[i] = key56[i];
            this.keyR[i] = key56[i+28];
        }
//        this.keyL = toLf(this.keyL,1);
//        this.keyR = toLf(this.keyR,1);
        return key56;
    }
    //左移操作
    private char[] toLf(char[] key, int digit){
        char[] temp = new char[key.length];
        for(int i=0; i<key.length; i++){
            temp[i] = key[i+1];
            if(key.length-i == 2) {
                temp[i+1] = key[0];
                break;
            }
        }
        if(digit != 1){
            return toLf(temp,digit-1);
        }
        return temp;
    }
    //左移后合成为56位密钥
    private char[] keyCombine(){
        char[] key56 = new char[56];
        for(int i=0; i<28; i++){
            key56[i] = keyL[i];
            key56[i+28] = keyR[i];
        }
        return key56;
    }
    //压缩置换PC2 checked
    private char[] compressReplace(char[] arr56){
        char[] arr48 = new char[48];
        for(int i=0; i<48; i++){
            arr48[i] = arr56[keyCompress[i]-1];
        }
        return arr48;
    }
    //返回各轮密钥
    private char[] getKey(int round){
        if(round == 1 || round == 2 || round == 9 || round == 16){
            keyL = toLf(keyL,1);
            keyR = toLf(keyR,1);
        }else {
            keyL = toLf(keyL,2);
            keyR = toLf(keyR,2);
        }
        char[] key56 = keyCombine();
        char[] key48 = compressReplace(key56);
        return key48;
    }


    //明文部分
    //IP置换
    private char[] ipReplace(char[] arr){
        char[] temp = new char[64];
        for(int i=0; i<64; i++)
        {
            temp[i] = arr[ipTable[i]-1];
        }
        for(int i=0; i<32; i++){
            this.plainL[i] = temp[i];
            this.plainR[i] = temp[i+32];
        }
        return temp;
    }
    //逆IP置换
    private char[] inIp(char[] bit64) {
        char[] arr = new char[64];
        for(int i=0; i<64; i++) {
            arr[ipTable[i]-1] = bit64[i];
        }
        return arr;
    }
    //拓展置换 E
    private char[] expandReplace(char[] r32){
        char[] r48 = new char[48];
        char[][] temp = new char[8][6];
        for(int i=0; i<8; i++){
            for(int j=0; j<6; j++){
                if(i==7 && j==5){
                    temp[7][5] = r32[0];
                    break;
                }
                if(i==0 && j==0){
                    temp[0][0] = r32[31];
                    continue;
                }
                temp[i][j] = r32[i*4+j-1];
            }
            System.arraycopy(temp[i], 0, r48, i*6, 6);
        }

        return r48;
    }
    //异或运算 XOR
    private char[] xor(char[] r, char[] k){
        char[] temp = new char[r.length];
        for(int i=0; i<r.length; i++){
            int num = r[i]^k[i];
            temp[i] = Integer.toString(num).toCharArray()[0];
        }
        return temp;
    }
    //压缩置换 S
    private char[] sboxReplace(char[] arr48){
        char[][] arr = new char[8][6];
        char[] arr32 = new char[32];
        for(int i=0; i<8; i++){
            System.arraycopy(arr48, i * 6, arr[i], 0, 6);
            int line = Integer.parseInt(""+arr[i][0]+arr[i][5],2);
            int column = Integer.parseInt(""+arr[i][1]+arr[i][2]+arr[i][3]+arr[i][4],2);
            String temp;
            int num;
            switch (i){
                case 0:
                    temp = Integer.toString(sbox1[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 1:
                    temp = Integer.toString(sbox2[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 2:
                    temp = Integer.toString(sbox3[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 3:
                    temp = Integer.toString(sbox4[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 4:
                    temp = Integer.toString(sbox5[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 5:
                    temp = Integer.toString(sbox6[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 6:
                    temp = Integer.toString(sbox7[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
                case 7:
                    temp = Integer.toString(sbox8[line*16+column],2);
                    num = temp.length();
                    if(temp.length()<4) {
                        for(int j=0; j<4-num; j++)
                            temp = "0" + temp;
                    }
                    System.arraycopy(temp.toCharArray(), 0, arr32, i*4, 4);
                    break;
            }
        }
        return arr32;
    }
    //选择置换 P
    private char[] pboxReplace(char[] arr32){
        char[] temp = new char[32];
        for(int i=0; i<32; i++)
        {
            temp[i] = arr32[pbox[i]-1];
        }
        return temp;
    }
    //将L和R合起来
    private char[] toCombine(){
        char[] arr64 = new char[64];
        for(int i=0; i<32; i++){
            arr64[i] = plainR[i];
            arr64[i+32] = plainL[i];
        }
        return arr64;
    }
    //返回每轮R
    public char[] getR(char[] key){
        char[] arrR = new char[32], temp = new char[48];
        System.arraycopy(this.plainR, 0, arrR, 0, 32);
        temp = expandReplace(arrR);
        temp = xor(temp,key);
        arrR = sboxReplace(temp);
        arrR = pboxReplace(arrR);
        arrR = xor(arrR,this.plainL);
        return arrR;
    }
}
