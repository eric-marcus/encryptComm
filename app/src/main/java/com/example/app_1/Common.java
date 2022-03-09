package com.example.app_1;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Common {
    private static Activity activity;
    private static View view;

    Common(Activity activity , View view){
        Common.activity = activity;
        Common.view = view;
    }

    public static View onClick(){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"ss",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}




class Receive implements Runnable{
    private Socket socket;
    private String role;
    Receive(Socket socket, String role){
        System.out.println("here");
        this.socket = socket;
        this.role = role;
    }

    public void run(){
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DES des = new DES("pass");
            while (true){
                String receive = input.readUTF();

                String [] strs = receive.split("@");
                String str = "";
                for(int i=1; i<strs.length; i++){
                    String temp = "";
                    System.out.println(role+"/trs: "+strs[i]);
                    temp = des.userDe(strs[i]);
                    System.out.println(role+"/de: "+temp);
                    str += temp;
                }
                System.out.println(str);

//                System.out.println(role+"/trs: "+receive);
//                receive = des.userDe(receive);
//                System.out.println(role+"/de: "+receive);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



class Send implements Runnable{
    private Socket socket;
    private String role;
    Send(Socket socket, String role) throws IOException {
        this.socket = socket;
        this.role = role;
    }

    public void run(){
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DES des = new DES("pass");
            while(true){
                String send = new BufferedReader(new InputStreamReader(System.in)).readLine();

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