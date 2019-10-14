import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectingThread extends Thread{
    private ProcessBuilder processBuilder;
    private Process process;

    public ConnectingThread(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
        this.process = null;
        this.start();
//        p.waitFor();
    }

    @Override
    public void run() {
        super.run();
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedBlockingQueue<String> errorQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
        Thread readError = new Publisher(process, errorQueue, Boolean.TRUE);
        Thread readInput = new Publisher(process, inputQueue);
        Thread displayError = new ConsumerError(errorQueue);
        Thread displayInput = new ConsumerInput(process, inputQueue);
    }

    public Process getProcess() {
        return process;
    }
}
