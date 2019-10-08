import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConsumerInput extends Thread{

    LinkedBlockingQueue<String> linkedBlockingQueue;
    Process process;

    public ConsumerInput(Process process, LinkedBlockingQueue<String> linkedBlockingQueue) {
        this.linkedBlockingQueue = linkedBlockingQueue;
        this.process = process;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            String stringToDisplay = "";
            while(! stringToDisplay.contentEquals("EndThread")) {
                if(! stringToDisplay.isEmpty())
                    System.out.println(stringToDisplay);
                stringToDisplay = linkedBlockingQueue.poll(15, TimeUnit.SECONDS);
                if(stringToDisplay == null){
                    process.destroy();
                    System.out.println("Process has been destroyed: took too much time");
                    stringToDisplay = "";
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
