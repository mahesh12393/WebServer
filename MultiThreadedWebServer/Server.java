import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public Consumer<Socket> getConsumer(){
        return new Consumer<Socket>() {
            @Override
            public void accept(Socket socket) {
                try{
                    PrintWriter toClient = new PrintWriter(socket.getOutputStream());
                    toClient.println("Hello from the Server");
                    toClient.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public void run() throws IOException {
        int port = 8001;
        Server server = new Server();
        ServerSocket socket = new ServerSocket(port);
        socket.setSoTimeout(10000); //10seconds timeout
        while(true){
            try {
                System.out.println("Server is listening on port " + port);
                Socket acceptedSocket = socket.accept();
                Thread thread = new Thread(()-> server.getConsumer().accept(acceptedSocket));
                thread.start();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }

    }
    public static void main(String args[]){
        Server server = new Server();
        try{
            server.run();
        }catch (Exception ex){
            System.out.println("Server starting failed with " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}