import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConsumerError extends Thread{

    LinkedBlockingQueue<String> linkedBlockingQueue;
    Process process;

    public ConsumerError(LinkedBlockingQueue<String> linkedBlockingQueue) {
        this.linkedBlockingQueue = linkedBlockingQueue;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        String stringToDisplay = "";
        while( ! stringToDisplay.contentEquals("EndThread")) {
            if(! stringToDisplay.isEmpty())
                System.out.println(stringToDisplay);
            try {
                stringToDisplay = linkedBlockingQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(stringToDisplay == null){
                stringToDisplay = "";
            }
        }
    }
}
