import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectingProcess {

    public ConnectingProcess(ProcessBuilder pb) throws IOException, InterruptedException {
        Process p = pb.start();
        LinkedBlockingQueue<String> errorQueue = new LinkedBlockingQueue();
        LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue();
        Thread readError = new Publisher(p, errorQueue, Boolean.TRUE);
        Thread readInput = new Publisher(p, inputQueue);
        Thread displayError = new ConsumerError(errorQueue);
        Thread displayInput = new ConsumerInput(p, inputQueue);
        p.waitFor();
    }
}
