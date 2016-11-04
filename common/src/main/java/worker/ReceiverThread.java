package worker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by akhil on 25/9/16.
 */
public class ReceiverThread extends Thread {

    private final UserThreadGroup threadGroup;
    private BufferedInputStream bufferedInputStream = null;
    private volatile boolean stopped = false;


    public ReceiverThread(UserThreadGroup threadGroup, String receiver, InputStream inputStream) {
        super(threadGroup, receiver);
        this.threadGroup = threadGroup;
        bufferedInputStream = new BufferedInputStream(inputStream);
    }

    @Override
    public void run() {
        while (!stopped) {
            System.out.println("receiver loop");

            java.util.Scanner s = new java.util.Scanner(bufferedInputStream).useDelimiter("\n");
            String receivedString = s.hasNext() ? s.next() : "";
            messageReceived(receivedString);
//            System.out.println(receivedString);


        }
    }

    void fileReceived(File file) {
        threadGroup.onFileReceived(file);
    }


    void messageReceived(String messageString) {
        threadGroup.onMessageReceived(messageString);
    }

}
