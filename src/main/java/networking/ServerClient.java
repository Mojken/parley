package networking;

import java.io.*;
import java.net.Socket;

public class ServerClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private boolean connected = false;
    private final String IP;


    ServerClient(Socket clientSocket) {
        this.clientSocket = clientSocket;

        this.IP = clientSocket.toString();
        System.out.println(IP);

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: evaluate necessity of a thread here
    public void send(String msg) {
        out.println(msg);
    }


    void receive(Server server) {
        new Thread(IP + " listener") {
            public void run() {
                while (connected) {
                    try {
                        server.queueAdd(in.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
        }.start();
    }

    void disconnect() {
        try {
            connected = false;
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
