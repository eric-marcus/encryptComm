import java.io.*;
import java.net.Socket;
import java.net.SocketOption;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client1 {
    private static String ip="127.0.0.1";
    private static int port=7500;

    public static void main(String[] args) throws IOException {
        try {
            Socket socket = new Socket(ip,port);
            socket.setSoTimeout(200 * 1000);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner scan = new Scanner(System.in);
            out.writeUTF(scan.next());

            new Thread(new Receive(socket)).start();
            new Thread(new Send(socket)).start();
            System.out.println(socket.getKeepAlive());
            System.out.println(socket.isClosed());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Receive implements Runnable{
        private Socket socket;
        Receive(Socket socket){
            this.socket = socket;
        }

        public void run(){

            try {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DES des = new DES("pass");
                while (true){
                    String receive = input.readUTF();
                    if(receive.equals("++++0000") || receive.equals("////1111")){
                        System.out.println("both online");
                        continue;
                    }


                    String [] strs = receive.split("@");
                    String str = "";
                    for(int i=1; i<strs.length; i++){
                        String temp = "";
//                        System.out.println("/trs: "+strs[i]);
                        temp = des.userDe(strs[i]);
//                        System.out.println("/de: "+temp);
                        str += temp;
                    }
                    System.err.println("someone: "+str);

//                System.out.println(role+"/trs: "+receive);
//                receive = des.userDe(receive);
//                System.out.println(role+"/de: "+receive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static class Send implements Runnable{
        private Socket socket;
        Send(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DES des = new DES("pass");
                while(true){
                    String send = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    if(send.equals("quit")){
                        out.writeUTF("quit");
                        socket.close();

                    }
//                //多个64字节串起来
//                String str="";
//                String sends = new String(send);
//                int times = (send.getBytes("GBK").length-8)/8+1;
//                for(int i=0; i<times; i++){
//
////                    byte[] arrB = str.getBytes("GBK");
////                    byte[] nt = new byte[8];
////                    for(int i=0; i<8; i++){
////                        nt[i] = arrB[i];
////                    }
////                    String ss = new String(nt,"GBK");
//
//                    String temp = "";
//                    if(sends.length()<=8){
//                        temp = sends.substring(0,sends.length());
//                        sends  = new String(sends.substring(0,sends.length()));
//                    }else {
//                        temp = sends.substring(0,8);
//                        sends  = new String(sends.substring(8,sends.length()));
//                    }
//
//                    System.out.println("本次要转: "+temp);
//                    System.out.println("还剩下的: "+sends);
//                    str += "@"+des.userEn(temp);
//                    System.out.println("加密后:   "+str);
//                }

//                send = des.userEn(send);
//                out.writeUTF(send);

                    String[] strs = toParts(send);
                    String str = "";
                    for(int i=0; i<strs.length; i++){
                        //System.out.println("加密前："+strs[i]);
                        str += "@"+des.userEn(strs[i]);
                        //System.out.println("加密后："+str);
                    }

                    out.writeUTF(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将字符串分割成字符串数组以用来循环加密
        public String[] toParts(String str) throws UnsupportedEncodingException {
            //字符转成编码对应byte数组
            byte[] arrB = str.getBytes("GBK");
            //分割的字符串
            String[] strs;
            //分割长度/加密次数
            int times = 0;
            //计算times
            if(arrB.length%8 == 0)
                times = arrB.length/8;
            else
                times = arrB.length/8+1;
            strs = new String[times];

            int leftCount = arrB.length;
            int count = 0;
            //分割字符串
            for(int i=0; i<times; i++){
                byte[] tempB = new byte[8];
                //j<8 或者 余数; 填装待加密byte数组; count 是成功填装的下标;
                for(int j = 0; j < Math.min(8, (leftCount - i*8)); j++){
                    if(arrB[count] < 0 && 8-j==1 && checkC(tempB)) {
                        leftCount += 1;
                        break;
                    }
                    tempB[j] = arrB[count];
                    count++;
                }
                String tempS = new String(tempB,"GBK");
                strs[i] = tempS;
            }
            return strs;
        }
        //检查 byte数组中负数是否偶数个，即中文是否完整
        boolean checkC(byte[] arrB){
            int count = 0;
            for (byte b : arrB) {
                if (b < 0)
                    count++;
            }
            return (count % 2 == 0);
        }
    }

}
