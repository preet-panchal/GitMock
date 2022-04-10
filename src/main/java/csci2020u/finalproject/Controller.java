package csci2020u.finalproject;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Controller {

    public MenuBar menuBar;
    @FXML
    private Button Pull;
    @FXML
    private Button Push;
    @FXML
    private Button Fetch;
    @FXML
    private ListView<String> LocalRepo = new ListView<>();
    @FXML
    private ListView<String> RemoteRepo = new ListView<>();
    @FXML
    private Label SystemMessage = new Label();
    @FXML
    private Label LocalRepoLabel = new Label();
    @FXML
    private Label RemoteRepoLabel = new Label();
    @FXML
    private Parent root;
    private Scene scene;
    private Stage stage;

    protected List<String> ClientFiles = new ArrayList<>();
    protected List<String> ServerFiles = new ArrayList<>();
    protected ListProperty<String> ClientFileList = new SimpleListProperty<>();
    protected ListProperty<String> ServerFileList = new SimpleListProperty<>();
    final File Client_folder = new File("ClientFiles");
    private PrintWriter Out = null;
    private BufferedReader In = null;

    public void initialize() {
        LocalRepoLabel.setFont(new Font("Verdana", 25));
        RemoteRepoLabel.setFont(new Font("Verdana", 25));
        String DataFromServer = "";
        String message1= "";
        String message2= "";

        /*
         * creates new socket to get all the files
         * from the server file to get all the filenames to show on the UI
         * */
        try{
            Socket clientSocket = new Socket("localhost", 8081);
            Out = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            In =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message1 = In.readLine();
            message2 = In.readLine();
            Out.println("Dir");
            Out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataFromServer = In.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] FromServer = DataFromServer.split(" ");
        Collections.addAll(ServerFiles, FromServer);


        // takes the file folder and makes it a list that we can go through
        File[] listOfFilesClient = Client_folder.listFiles();
        // goes through the folder if in the folder of the object is a file then adds it to the ClientFiles List
        assert listOfFilesClient != null;
        for (File file : listOfFilesClient) {
            if (file.isFile()) {
                ClientFiles.add(file.getName());
            }
        }
        SystemMessage.setText("""
                               GitMock HELP Menu
                
                Push:
                  Choose folder you would like to push
                  from local-repo then press push.
                  
                Pull:
                  Choose folder you would like to pull
                  from remote-repo then press pull.
                  
                Fetch:
                  To update repos click fetch.""");

        RemoteRepo.itemsProperty().bind(ServerFileList);
        LocalRepo.itemsProperty().bind(ClientFileList);
        ClientFileList.set(FXCollections.observableArrayList(ClientFiles));
        ServerFileList.set(FXCollections.observableArrayList(ServerFiles));

    }

    /*
     * onBackButtonClick is used to navigate from 'Main' screen to 'Help' screen.
     *
     *@pram ActionEvent actionEvent
     *
     * when button is pressed, a new scene is created, which is the 'Help' scene.
     * The stage is reset to the 'Help' screen
     *
     * */
    @FXML
    public void helpLabel(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("help.fxml")));
        stage = (Stage)menuBar.getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Help");
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("client.css")).toExternalForm());
        stage.show();
    }
    /*
     * onBackButtonClick is used to navigate from 'Help' screen to main screen.
     *
     *@pram ActionEvent actionEvent
     *
     * when button is pressed, a new scene is created, which is the main scene.
     * The stage is reset to the main screen
     *
     * */
    public void onBackButtonClick(ActionEvent event) throws IOException {
        /*
         * Hides The Label and Hide button once run
         */
        //initialize();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 600, 400);
        stage.setTitle("GitMock Desktop");
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("client.css")).toExternalForm());
        stage.show();
    }
    /*
     * btnOnPressPull is used to get the file from the server and download it to the ClientFolder
     *
     *@pram ActionEvent actionEvent
     * when button is pressed it creates a new client socket and new output stream
     * It sends a command to the server "Download" and the filename that we want to get
     * once the server gets the command it starts sending the contents of the file and the function receives
     * it and creates the file in The client file with the data gotten from the server.
     * */
    public void btnOnPressPull(ActionEvent actionEvent) throws IOException {
        Socket clientSocket = null;
        PrintWriter OUT = null;
        String DataFromServer;
        String message1= "";
        String message2= "";

        try {
            clientSocket = new Socket("localhost",8081);
            OUT = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            OUT.println("DownLoad"+" "+ RemoteRepo.getSelectionModel().getSelectedItems().get(0));
            In =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message1 = In.readLine();
            message2 = In.readLine();
            OUT.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            //data from server is read and the stored in DataFromServer
            DataFromServer = In.readLine();
            //path to where the new file is going to be created
            String path = "ClientFiles/" + RemoteRepo.getSelectionModel().getSelectedItems().get(0);
            File IncomingFile= new File(path);
            //looks to see if the file already exists
            if(!IncomingFile.exists()) {
                IncomingFile.createNewFile();
            }
            FileWriter fw=new FileWriter(path);
            //writes to new file
            String [] Sentences =DataFromServer.split(" ");
            for(String Sentence:Sentences){
                fw.write(Sentence+" ");
            }
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //closes the socket
        clientSocket.close();
        //updates the list
        ListViewUpdate();
    }

    /*
     * btnOnPressPush is used to upload Files from the ClientFiles to The ServerFiles
     *
     * It first creates a socket that connects to port 8081
     * and then sends a command to upload first the File name
     * and then after it sends the content of the file
     * */
    public void btnOnPressPush(ActionEvent actionEvent) {
        Socket clientSocket = null;
        PrintWriter out = null;
        try{
            //creates socket
            clientSocket = new Socket("localhost",8081);
            out = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            //sends upload command with the file name as second argument
            out.println("Upload"+" "+ LocalRepo.getSelectionModel().getSelectedItems().get(0));
            //takes client_folder and makes it a list of files;
            File[] listOfFilesClient = Client_folder.listFiles();
            //loops until the file selected is found
            assert listOfFilesClient != null;
            for (File file : listOfFilesClient) {
                if (file.getName().equals(LocalRepo.getSelectionModel().getSelectedItems().get(0)) ) {
                    //this is where the file content is sent through the socket
                    File FileReading = new File(String.valueOf(file.getAbsoluteFile()));
                    Scanner sc = new Scanner(file);
                    StringBuilder FileSending = new StringBuilder(" ");
                    while (sc.hasNextLine()) {
                        FileSending.append(sc.nextLine()).append(" ");
                    }
                    out.println(FileSending);
                }
            }
            out.flush();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //updates the listView
        ListViewUpdate();
    }


    public void exitProgram(ActionEvent event){
        /*
         * Function closes the system running
         */
        System.exit(0);
    }

    /*
     * ListViewUpdate is used to update what is seen on the ListView when new files are put in the client file
     *
     * It works by first clearing the ArrayList ServerFiles and ClientFiles
     * Then it grabs the new Data and enters them in their respective ArrayList
     * */
    public void ListViewUpdate() {
        ServerFiles.clear();
        ClientFiles.clear();

        String DataFromServer = "";
        String message1= "";
        String message2= "";

        try{
            Socket clientSocket = new Socket("localhost", 8081);
            Out = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            In =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message1 = In.readLine();
            message2 = In.readLine();
            Out.println("Dir");
            Out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataFromServer = In.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] FromServer = DataFromServer.split(" ");
        Collections.addAll(ServerFiles, FromServer);


        // takes the file folder and makes it a list that we can go through
        File[] listOfFilesClient = Client_folder.listFiles();
        assert listOfFilesClient != null;
        for (File file : listOfFilesClient) {
            if (file.isFile()) {
                ClientFiles.add(file.getName());
            }
        }

        RemoteRepo.itemsProperty().bind(ServerFileList);
        LocalRepo.itemsProperty().bind(ClientFileList);
        ClientFileList.set(FXCollections.observableArrayList(ClientFiles));
        ServerFileList.set(FXCollections.observableArrayList(ServerFiles));
    }

    /*function that when pressed calls the ListViewUpdate
     * to update the ListView on the UI to the most up-to-date version*/
    public void btnOnPressFetch(ActionEvent actionEvent) {
        ListViewUpdate();
    }
}
