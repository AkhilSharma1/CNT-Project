package worker;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by akhil on 25/9/16.
 */
public class SenderThread extends Thread {


    private final BufferedOutputStream out;
    private final ArrayBlockingQueue arrayBlockingQueue;

    SenderThread(ThreadGroup threadGroup, String sender, OutputStream out, ArrayBlockingQueue arrayBlockingQueue) {
        super(threadGroup, sender);
        this.out = new BufferedOutputStream(out);
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

    void sendMessage(String jsonString) {

        try {
           /* IOUtils.write(jsonString.getBytes(),out);
             out.flush();*/

            out.write((jsonString + "\n").getBytes());
            out.flush();
        } catch (IOException e) {
            System.out.println("1");
            e.printStackTrace();
        }
        System.out.println("send string is " + jsonString);

    }

    public void sendFile(File file) {
        try {

            byte[] bFile = new byte[(int) file.length()];

            FileInputStream in = new FileInputStream(file);

            in.read(bFile);
//            in.close();

           /* out.write(bFile);
            out.flush();
            System.out.println("file buf sent " + bFile.length);
            out.flush();
            out.flush();
            out.flush();*/


            IOUtils.copy(in, out);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
