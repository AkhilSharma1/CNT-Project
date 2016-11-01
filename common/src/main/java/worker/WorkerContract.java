package worker;

import model.Message;

import java.io.File;
import java.net.Socket;

/**
 * Created by akhil on 26/9/16.
 */
public interface WorkerContract {

    /* Implemented by the entity which manages threads*/


    void sendMessage(String toUser, Message message);

    void sendFile(String toUser, File file);

    void addUser(Socket socket, String userName);

    /* Implemented by the entity which wants to receive messages from Threads*/

    interface onDataReceiveCallback {
        void onFileReceived(String user, File file);

        void onMessageReceived(Message message);
    }

}
