import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client1 {
    //127.0.0.1
    //47.114.48.8
    private static String ip="47.114.48.8";
    private static int port=7500;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(ip,port);
            socket.setSoTimeout(200 * 1000);



            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            String data = "000";

            int firs = 0;
            while (!data.equals("0")) {
                if(firs == 0){
                    Scanner scan = new Scanner(System.in);
                    out.writeUTF(scan.next());
                    firs++;
                }
                String send = new BufferedReader(new InputStreamReader(System.in)).readLine();
                data = send;

                out.writeUTF(send);
                String receive = input.readUTF();
                System.out.println("Server/trs: "+receive);

                System.out.println("Server/de: "+receive);
            }

            out.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
