package worker;

import java.io.File;
import java.net.Socket;

/**
 * Created by akhil on 26/9/16.
 */
public interface WorkerContract {

    /* Implemented by the entity which manages threads*/


    void sendMessage(String toUserId, String messageJsonString);

    void sendFile(String toUserId, File file);

    void addUser(Socket socket, String userName);

    interface onDataReceiveCallback {
        void onFileReceived(String fromUserId, File file);

        void onMessageReceived(String fromUserId, String messageString);
    }

}
