import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Master {

    public static void  main(String[] args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/SLAVE/out/artifacts/SLAVE_jar/SLAVE.jar");
        Process p = pb.start();
        LinkedBlockingQueue<String> errorQueue = new LinkedBlockingQueue();
        LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue();
        Thread readError = new Publisher(p.getErrorStream(), errorQueue);
        Thread readInput = new Publisher(p.getInputStream(), inputQueue);
        Thread displayError = new ConsumerError(errorQueue);
        Thread displayInput = new ConsumerInput(p, inputQueue);

        //Autre solution
//        try {
//            if(! p.waitFor(15, TimeUnit.SECONDS)){
//                System.exit(1);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        printInputReader(new InputStreamReader(p.getInputStream()));
//        printInputReader(new InputStreamReader(p.getErrorStream()));
    }

    public static void printInputReader(Reader reader){
        BufferedReader readerOutput = new BufferedReader(reader);
//        readerOutput.lines().forEach( System.out::println );
        System.out.print(readerOutput.lines().collect(Collectors.joining(System.lineSeparator())));
    }
}
