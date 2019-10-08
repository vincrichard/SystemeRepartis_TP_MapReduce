import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

public class Publisher extends Thread{
    Process process;
    InputStream inputStream;
    LinkedBlockingQueue<String> linkedBlockingQueue;

    public Publisher(Process process, LinkedBlockingQueue linkedBlockingQueue) {
        this.process = process;
        this.linkedBlockingQueue = linkedBlockingQueue;
        this.inputStream = process.getInputStream();
        this.start();
    }

    public Publisher(Process process, LinkedBlockingQueue linkedBlockingQueue, Boolean errorStream) {
        this.process = process;
        this.linkedBlockingQueue = linkedBlockingQueue;
        if(errorStream){
            this.inputStream = process.getErrorStream();
        }else{
            this.inputStream = process.getInputStream();
        }
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
            while(process.isAlive()) {
                String line = bufferedReader.readLine();
                if ( line != null) {
                    linkedBlockingQueue.put(line);
                }
                Thread.sleep(200);
            }
            linkedBlockingQueue.put("EndThread");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
