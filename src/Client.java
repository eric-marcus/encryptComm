import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static String ip="127.0.0.1";
    private static int port=8089;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(ip,port);
            socket.setSoTimeout(200 * 1000);

            Runnable runnable = new Receive(socket,"Client");
            Runnable runnable1 = new Send(socket,"Client");
            Thread thread = new Thread(runnable);
            Thread thread1 = new Thread(runnable1);
            thread.start();
            thread1.start();

//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//            DataInputStream input = new DataInputStream(socket.getInputStream());
//            DES des = new DES("pass");
//            String data = "000";
//
//            while (!data.equals("0")) {
//                String send = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                data = send;
//                send = des.userEn(send);
//                out.writeUTF(send);
//                String receive = input.readUTF();
//                System.out.println("Server/trs: "+receive);
//                receive = des.userDe(receive);
//                System.out.println("Server/de: "+receive);
//            }
//
//            out.close();
//            input.close();
//            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
