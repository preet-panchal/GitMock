package csci2020u.finalproject;

import java.io.*;
import java.net.*;
import java.util.*;

public class FileServerThread extends Thread{
    protected Socket socket = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    protected Vector messages = null;

    public FileServerThread(Socket socket, Vector messages) {
        super();
        this.socket = socket;
        this.messages = messages;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    public void run() {
        // initialize interaction
        out.println("Connected to Mock Github Server");
        out.println("200 Ready For Chat");

        boolean endOfSession = false;
        while(!endOfSession) {
            endOfSession = processCommand();
        }
        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean processCommand() {
        String message = null;
        try {
            message = in.readLine();
            System.out.println(message);
        } catch (IOException e) {
            System.err.println("Error reading command from socket.");
            return true;
        }
        if (message == null) {
            return true;
        }
        StringTokenizer st = new StringTokenizer(message);
        String command = st.nextToken();
        String args = null;
        if (st.hasMoreTokens()) {
            args = message.substring(command.length()+1, message.length());
        }
        return processCommand(command, args);
    }

    protected boolean processCommand(String command, String arguments) {
        if(command.equalsIgnoreCase("DownLoad")){
            /*
             *The client sends a request to download a file with the filename of the file that the client wants to delete
             * Once The server gets the request it goes through the Server_folder directory for the file the client requested
             * Once it finds the file it reads the content and sends it to to the client
             * */
            final File Server_folder = new File("ServerFiles");
            File[] listOfFilesServer = Server_folder.listFiles();
            assert listOfFilesServer != null;
            for(File files:listOfFilesServer){
                if(files.getName().equals(arguments)){
                    File FileReading = new File(String.valueOf(files.getAbsoluteFile()));
                    Scanner sc = null;
                    try {
                        sc = new Scanner(files);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StringBuilder FileSending = new StringBuilder(" ");
                    while (true) {
                        assert sc != null;
                        if (!sc.hasNextLine()) break;
                        FileSending.append(sc.nextLine()).append(" ");
                    }
                    out.println(FileSending);
                }
            }


        }else if(command.equalsIgnoreCase("Upload")){

            /*
             * When the client sends the Upload command
             * the filename is used to create the new file
             * then the contents are sent from the client to t e server and
             *  then are written to the new file that was created
             * */
            String path = "";
            String Filename = "";
            File IncomingFile = null;

            try {
                if (arguments.contains(".txt")) {
                    Filename = arguments;
                    path = "ServerFiles/" + arguments;
                    System.out.println(path);

                    IncomingFile = new File(path);

                    if(!IncomingFile.exists()) {
                        IncomingFile.createNewFile();
                    }
                    String FileReceiving = in.readLine();
                    FileWriter fw=new FileWriter(path);
                    String [] Sentences =FileReceiving.split(" ");
                    for(String Sentence:Sentences){
                        fw.write(Sentence+" ");
                    }
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(command.equalsIgnoreCase("Dir")){
            /*
             * When this command is requested it gets the all the file names in the server
             * and concatenate them to a string and send it to the client.
             * */
            StringBuilder FilesSending = new StringBuilder();
            final File Server_folder = new File("ServerFiles");
            File[] listOfFilesServer = Server_folder.listFiles();
            System.out.println(Arrays.toString(listOfFilesServer));
            assert listOfFilesServer != null;
            for (File file :listOfFilesServer) {
                if (file.isFile()) {
                    FilesSending.append(file.getName()).append(" ");

                }
            }
            out.println(FilesSending);

        }

        return false;
    }
}