package csci2020u.finalproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class FileServer{
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected FileServerThread[] threads = null;
    protected int numClients = 0;
    protected Vector messages = new Vector();

    public static int SERVER_PORT = 8081;
    public static int MAX_CLIENTS = 100;

    public FileServer() {
        // setting up new server socket
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("---------------------------");
            System.out.println("File Server Application is running");
            System.out.println("---------------------------");
            System.out.println("Listening to port: "+SERVER_PORT);
            threads = new FileServerThread[MAX_CLIENTS];
            while(true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client #"+(numClients+1)+" connected.");
                threads[numClients] = new FileServerThread(clientSocket, messages);
                threads[numClients].start();
                numClients++;
            }
        } catch (IOException e) {
            System.err.println("IOException while creating server connection");
        }
    }

    public static void main(String[] args) {
        FileServer app = new FileServer();
    }
}
