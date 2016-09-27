package worker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by akhil on 25/9/16.
 */
public class SenderThread extends Thread {

    private ObjectOutputStream out = null;

    public SenderThread(ThreadGroup threadGroup, String sender, Socket socket) {
        super(threadGroup, sender);

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String msg) {
        try {
            //stream write the message
            out.writeObject(msg);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
