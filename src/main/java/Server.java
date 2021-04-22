import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private int PORT;
    public Server(int PORT){
        this.PORT = PORT;
    }
    public Server listen() throws IOException {
        int MAX_POOL = 64;
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            var threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(MAX_POOL);
            threadPoolExecutor.execute(clientHandler);
        }
    }
}
