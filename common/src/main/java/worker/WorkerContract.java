package worker;

import java.net.Socket;

/**
 * Created by akhil on 26/9/16.
 */
public interface WorkerContract {

    /* Implemented by the entity which manages threads*/

    void addUser(String userName, Socket socket);

    void removeUser(String userName);

    void sendMessage(String userName, String message);



    /* Implemented by the entity which wants to receive messages from Threads*/

    interface MessageReceiveCallback {
        void messageReceived(String userName, String message);
    }

}
