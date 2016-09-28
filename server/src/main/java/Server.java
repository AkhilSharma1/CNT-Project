import worker.ThreadManager;
import worker.WorkerContract;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by akhil on 26/9/16.
 */
public class Server extends Program {

    private static HashMap<String, String> users = new HashMap<>();
    volatile boolean keepRunning = true;
    private int userCounter = 1;


    public Server() {

    }


    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

    @Override
    public void processUserInput(String userInput) {

    }

    @Override
    public void onStart() {
        System.out.println("Welcome! Server is starting...");
        System.out.println("Type quit to close server");

//        startServerSocketListenerThread();

    }

    @Override
    public void onStop() {
        System.out.println("server shutting down...");

        //TODO signal all threads to stop
    }


    private void startServerSocketListenerThread() {

        //TODO use a dependency injector such as dagger2
        WorkerContract worker = new ThreadManager();
        try {
            new Thread() {
                //The server will be listening on this port number
                private static final int sPort = 8080;
                //TODO read port number from a config file
                //TODO learn to interrupt this thread


                ServerSocket listener = new ServerSocket(sPort);

                @Override
                public void run() {

                    try {
                        while (keepRunning) {
                            Socket socket = listener.accept();

                            String newUser = createNewUser();
                            worker.addUser(newUser, socket);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            listener.close();


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
