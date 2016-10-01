import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by akhil on 26/9/16.
 */
public class Server extends Presenter {

    private static HashMap<String, String> users = new HashMap<>();
    volatile boolean keepRunning = true;
    volatile ServerSocket serverSocket;
    private int userCounter = 1;

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

    @Override
    public void processUserInput(String userInput) {

    }

    @Override
    public void onStart() {
        final Gson gson = new Gson();
        System.out.println("Welcome! Server is starting...");
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

                            String newUser = createNewUser();
                            Server.this.getWorker().addUser(newUser, socket);

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

    private String createNewUser() {
        return "user" + userCounter++;

    }


}
