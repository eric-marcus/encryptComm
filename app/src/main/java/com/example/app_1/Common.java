package com.example.app_1;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Common {
    public final static OkHttpClient client = new OkHttpClient();
    private final static String ip = "http://bigcat.tech:8080";
    public static Request requestLogin = new Request.Builder().url(ip+"/encrypt/login").build();
    public static Request requestRegister = new Request.Builder().url(ip+"/encrypt/register").build();
    public static Request requestChat = new Request.Builder().url(ip+"/encrypt/chat").build();
    public static Response response;


    public static String doPost(Request request, RequestBody fillBody) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    response = client.newCall(request.newBuilder().post(fillBody).build()).execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return response.body().string();
    }

    public static boolean isEmpty(TextInputEditText view){
        if(view.getText().length() == 0){
            view.setError("请完善信息");
            return true;
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context Activity的getAppliction()获取
     * @param className Activity名称 由类名.class.getName()获取
     *
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am =(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }



}




//class Receive implements Runnable{
//    private Socket socket;
//    private String role;
//    Receive(Socket socket, String role){
//        System.out.println("here");
//        this.socket = socket;
//        this.role = role;
//    }
//
//    public void run(){
//        try {
//            DataInputStream input = new DataInputStream(socket.getInputStream());
////            DES des = new DES("pass");
//            while (true){
//                String receive = input.readUTF();
//                Log.w("receive", receive );
////                String [] strs = receive.split("@");
////                String str = "";
////                for(int i=1; i<strs.length; i++){
////                    String temp = "";
////                    System.out.println(role+"/trs: "+strs[i]);
////                    temp = des.userDe(strs[i]);
////                    System.out.println(role+"/de: "+temp);
////                    str += temp;
////                }
////                System.out.println(str);
//
////                System.out.println(role+"/trs: "+receive);
////                receive = des.userDe(receive);
////                System.out.println(role+"/de: "+receive);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
//class Send implements Runnable{
//    private Socket socket;
//    private String role;
//    Send(Socket socket, String role) throws IOException {
//        this.socket = socket;
//        this.role = role;
//    }
//
//    public void run(){
//        try {
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
////            DES des = new DES("pass");
//            while(true){
//                String send = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                out.writeUTF(send);
////                String[] strs = toParts(send);
////                String str = "";
////                for(int i=0; i<strs.length; i++){
////                    //System.out.println("加密前："+strs[i]);
////                    str += "@"+des.userEn(strs[i]);
////                    //System.out.println("加密后："+str);
////                }
////                out.writeUTF(str);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //将字符串分割成字符串数组以用来循环加密
//    public String[] toParts(String str) throws UnsupportedEncodingException {
//        //字符转成编码对应byte数组
//        byte[] arrB = str.getBytes("GBK");
//        //分割的字符串
//        String[] strs;
//        //分割长度/加密次数
//        int times = 0;
//        //计算times
//        if(arrB.length%8 == 0)
//            times = arrB.length/8;
//        else
//            times = arrB.length/8+1;
//        strs = new String[times];
//
//        int leftCount = arrB.length;
//        int count = 0;
//        //分割字符串
//        for(int i=0; i<times; i++){
//            byte[] tempB = new byte[8];
//            //j<8 或者 余数; 填装待加密byte数组; count 是成功填装的下标;
//            for(int j = 0; j < Math.min(8, (leftCount - i*8)); j++){
//                if(arrB[count] < 0 && 8-j==1 && checkC(tempB)) {
//                    leftCount += 1;
//                    break;
//                }
//                tempB[j] = arrB[count];
//                count++;
//            }
//            String tempS = new String(tempB,"GBK");
//            strs[i] = tempS;
//        }
//        return strs;
//    }
//
//    //检查 byte数组中负数是否偶数个，即中文是否完整
//    boolean checkC(byte[] arrB){
//        int count = 0;
//        for (byte b : arrB) {
//            if (b < 0)
//                count++;
//        }
//        return (count % 2 == 0);
//    }
//}