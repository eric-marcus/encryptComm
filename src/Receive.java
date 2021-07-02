import java.io.*;
import java.net.Socket;


public class Receive implements Runnable{
    private Socket socket;
    private String role;
    Receive(Socket socket, String role){
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
