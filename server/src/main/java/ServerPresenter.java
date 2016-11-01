import com.google.gson.Gson;
import model.Message;
import modelold.WelcomeMessage;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by akhil on 26/9/16.
 */
public class ServerPresenter extends Presenter {

    volatile boolean keepRunning = true;
    volatile ServerSocket serverSocket;
    private ArrayList<String> users = new ArrayList<>();
    private int userCounter = 1;

    public ServerPresenter(ViewContract view) {
        super(view);
    }

    @Override
    public void sendData(Message message) {

    }


    @Override
    public void onStart() {
        System.out.println("Welcome! ServerPresenter is starting...");
        System.out.println("Type quit to close server");

        //TODO read port number from a config file
        final int sPort = 8080;
        try {
            serverSocket = new ServerSocket(sPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startServerSocketListenerThread();

    }

    @Override
    public void onStop() {
        keepRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("server shutting down...");
    }


    private void startServerSocketListenerThread() {


        try {
            new Thread() {
                //The server will be listening on this port number

                //TODO learn to interrupt this thread

                @Override
                public void run() {

                    try {
                        while (keepRunning) {
                            Socket socket = serverSocket.accept();
                            ServerPresenter.this.newConnection(socket);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            serverSocket.close();
                            //workerthreadmanager


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }.start();


        } catch (Exception e) {
            //TODO HANDLE
            e.printStackTrace();
        }


    }

    private void newConnection(Socket socket) {
        String newUser = newUserName();
        createNewConnection(socket, newUser);

        sendWelcomeMessage(newUser);
        users.add(newUser);

    }

    private void sendWelcomeMessage(String newUser) {
        String welcomeMessage = createWelcomeMessage(newUser, users);
        WelcomeMessage welcomeData = new WelcomeMessage(newUser, welcomeMessage);

        //TODO singleton
        Gson gson = new Gson();
        String data = gson.toJson(welcomeData);
//        getWorker().sendData(newUser, data);

    }

    private String createWelcomeMessage(String newUser, ArrayList<String> users) {
        return "Welcome " + newUser + "! Online users are :" + users.toString();
    }


    private String newUserName() {
        return "user" + userCounter++;

    }


    @Override
    public void onFileReceived(String user, File file) {

    }

    @Override
    public void onMessageReceived(Message message) {

    }
}
