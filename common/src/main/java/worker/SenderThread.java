package worker;

import model.Message;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by akhil on 25/9/16.
 */
public class SenderThread extends Thread {

    private BufferedOutputStream out = null;

    SenderThread(ThreadGroup threadGroup, String sender, OutputStream out) {
        super(threadGroup, sender);
        this.out = new BufferedOutputStream(out);
    }

    void send(String msg) {
        try {
            out.write(msg.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


    }

    public void sendMessage(Message messageObj) {

    }

    public void sendFile(File file) {

    }
}
