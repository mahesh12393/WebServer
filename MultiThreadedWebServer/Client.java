import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public Runnable getRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                int port = 8001;
                try{
                    InetAddress clientAddress = InetAddress.getByName("localhost");
                    Socket socket = new Socket(clientAddress,port);
                    PrintWriter toSocket = new PrintWriter(socket.getOutputStream());
                    BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toSocket.println("Hello from the client");
                    String line = fromSocket.readLine();
                    System.out.println("Response from the socket received is "+ line);
                    toSocket.close();
                    fromSocket.close();
                    socket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    public static void main(String args[]){
        Client client = new Client();
        try{
            for(int i=0;i<100;i++){
                Thread thread = new Thread(client.getRunnable());
                thread.start();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
