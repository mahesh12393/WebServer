import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool;

    public Server(int poolSize){
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket){
        try (var outputStream = clientSocket.getOutputStream();
             var writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(outputStream, java.nio.charset.StandardCharsets.UTF_8))
        ) {
            String jsonContent = new String(Files.readAllBytes(Paths.get("data.json")), java.nio.charset.StandardCharsets.UTF_8);
//                toSocket.println("Hello from Server with this content" + clientSocket.getInetAddress());
            writer.write(jsonContent);
            writer.flush();
        } catch (IOException ex){
                ex.printStackTrace();
            }
    }

    public static void main(String args[]){
        int port = 8001;
        int poolSize = 20;
        Server server = new Server(poolSize);
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("Server listening on port of " + port);
            while(true){
                Socket clientsocket = serverSocket.accept();
                //using threadpool to handling the client
                server.threadPool.execute(()-> server.handleClient(clientsocket));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            //shutdown thread
            server.threadPool.shutdown();
        }
    }

}
