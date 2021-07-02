import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int port = 8089;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();
            socket.setSoTimeout(200 * 1000);
            InetAddress ip = socket.getInetAddress();


            Runnable runnable = new Receive(socket,"Server");
            Runnable runnable1 = new Send(socket,"Server");
            Thread thread = new Thread(runnable);
            Thread thread1 = new Thread(runnable1);
            thread.start();
            thread1.start();

//轮流对话
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//            DataInputStream input = new DataInputStream(socket.getInputStream());
//            DES des = new DES("pass");
//            String data = "000";
//
//            while (!data.equals("0")) {
//                String receive = input.readUTF();
//                System.out.println("Client/trs: "+receive);
//                receive = des.userDe(receive);
//                System.out.println("Client/de: "+receive);
//                String send = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                data = send;
//                send = des.userEn(send);
//                out.writeUTF(send);
//            }
//
//            out.close();
//            input.close();
//            socket.close();
//            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
