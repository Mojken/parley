package networking;

import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void stopConnection () throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public void send (String msg) throws IOException {
        out.println(msg);
        System.out.println(in.readLine());
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 1337);
            client.send("It works!");
            client.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
