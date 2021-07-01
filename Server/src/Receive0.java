import java.io.*;
import java.net.Socket;


public class Receive0 implements Runnable{
    private Socket socket;
    private String role;
    Receive0(Socket socket, String role){
        this.socket = socket;
        this.role = role;
    }

    public void run(){

        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            while (true){
                String receive = input.readUTF();
                System.out.println(receive);

//                System.out.println(role+"/trs: "+receive);
//                receive = des.userDe(receive);
//                System.out.println(role+"/de: "+receive);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
