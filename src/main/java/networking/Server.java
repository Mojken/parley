package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//Name: Torsten
public class Server {

    boolean running = false;

    private ServerSocket serverSocket;

    private List<ServerClient> clients = new ArrayList<ServerClient>();
    private Queue<String> messageQueue = new ConcurrentLinkedQueue<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        running = true;
        Server server = this;

        new Thread ("Connection Listener") {
            public void run() {
                while (running) {
                    try {
                        ServerClient client = new ServerClient(serverSocket.accept());
                        clients.add(client);
                        client.receive(server);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    void queueAdd(String msg) {
        messageQueue.add(msg);
    }

    public void toAll(String message) {
        for (ServerClient client : clients) {
            client.send(message);
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
        running = false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(1337);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}