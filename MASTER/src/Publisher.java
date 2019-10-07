import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Publisher extends Thread{
    InputStream inputStream;
    LinkedBlockingQueue<String> linkedBlockingQueue;

    public Publisher(InputStream inputStream, LinkedBlockingQueue linkedBlockingQueue) {
        this.inputStream = inputStream;
        this.linkedBlockingQueue = linkedBlockingQueue;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        readingInputStream(linkedBlockingQueue);
    }

    private void readingInputStream(LinkedBlockingQueue linkedBlockingQueue) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (bufferedReader.readLine() != null) {
                linkedBlockingQueue.put(bufferedReader.readLine());
            }
            linkedBlockingQueue.put("EndThread");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Process shut down took too much time");
            try {
                linkedBlockingQueue.put("EndThread");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
