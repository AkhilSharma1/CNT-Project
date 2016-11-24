package worker;

import model.Message;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by akhil on 25/9/16.
 */
public class SenderThread extends Thread {


    private final ArrayBlockingQueue arrayBlockingQueue;
    private ObjectOutputStream out = null;

    SenderThread(ThreadGroup threadGroup, String sender, OutputStream out, ArrayBlockingQueue arrayBlockingQueue) {
        super(threadGroup, sender);
        try {
            this.out = new ObjectOutputStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    @Override
    public void run() {

        while (true) {

            try {
                Object taken = arrayBlockingQueue.take();

                sendMessage((Message) taken);

            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }

    void sendMessage(Message message) {


        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


}
