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
            String line = bufferedReader.readLine();
            while(process.isAlive()) {
                if ( line != null) {
                    linkedBlockingQueue.put(line);
                }
                Thread.sleep(200);
                if(process.isAlive())
                    line = bufferedReader.readLine();
            }
            //Gestion des messages restant dans le BufferedReader
            while(line != null) {
                linkedBlockingQueue.put(line);
                line = bufferedReader.readLine();
            }
            linkedBlockingQueue.put("EndThread");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
