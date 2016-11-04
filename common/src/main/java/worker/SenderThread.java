package worker;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by akhil on 25/9/16.
 */
public class SenderThread extends Thread {


    private final OutputStreamWriter osw;
    private final OutputStream out;
    private final ArrayBlockingQueue arrayBlockingQueue;

    SenderThread(ThreadGroup threadGroup, String sender, OutputStream out, ArrayBlockingQueue arrayBlockingQueue) {
        super(threadGroup, sender);
        osw = new OutputStreamWriter(out);
        this.out = out;
        this.arrayBlockingQueue = arrayBlockingQueue;
    }


    @Override
    public void run() {

        while (true) {
            System.out.println("sender loop");

            try {
                Object taken = arrayBlockingQueue.take();

                if (taken instanceof File)
                    sendFile((File) taken);
                else
                    sendMessage((String) taken);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }

    public void sendMessage(String jsonString) {
        try {
            osw.write(jsonString + "\n");
            osw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendFile(File file) {
        try {
            byte[] bytes = new byte[16 * 1024];
            InputStream in = new FileInputStream(file);
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            out.flush();
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
