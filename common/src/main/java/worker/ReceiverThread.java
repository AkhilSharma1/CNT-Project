package worker;

import model.Message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by akhil on 25/9/16.
 */
public class ReceiverThread extends Thread {

    private final UserThreadGroup threadGroup;
    private BufferedInputStream bufferedInputStream = null;


    public ReceiverThread(UserThreadGroup threadGroup, String receiver, InputStream inputStream) {
        super(threadGroup, receiver);
        this.threadGroup = threadGroup;
        bufferedInputStream = new BufferedInputStream(inputStream);
    }


    void fileReceived(File file) {
        threadGroup.onFileReceived(file);
    }


    void MessageReceived(Message message) {
        threadGroup.onMessageReceived(message);
    }

}
