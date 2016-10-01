package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by akhil on 25/9/16.
 */
public class ReceiverThread extends Thread {

    private ObjectInputStream objectInputStream = null;


    public ReceiverThread(ThreadGroup threadGroup, String receiver, Socket socket) {
        super(threadGroup, receiver);
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
