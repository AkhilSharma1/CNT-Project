package worker;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by akhil on 25/9/16.
 */
public class ReceiverThread extends Thread {

    private BufferedInputStream bufferedInputStream = null;


    public ReceiverThread(ThreadGroup threadGroup, String receiver, InputStream inputStream) {
        super(threadGroup, receiver);
        bufferedInputStream = new BufferedInputStream(inputStream);
    }


}
