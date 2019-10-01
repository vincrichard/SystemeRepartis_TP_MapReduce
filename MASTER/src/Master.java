import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

public class Master {

    public static void  main(String[] args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/SLAVE/out/artifacts/SLAVE_jar/SLAVE.jar");
//        Map<String, String> env = pb.environment();
//        pb.directory(new File("/home/vincent"));
//        File log = new File("log");
//        pb.redirectErrorStream(true);
//        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process p = pb.start();
//        assert pb.redirectInput() == ProcessBuilder.Redirect.PIPE;
//        assert pb.redirectOutput().file() == log;
//        assert p.getInputStream().read() == -1;
        printInputReader(new InputStreamReader(p.getInputStream()));
        printInputReader(new InputStreamReader(p.getErrorStream()));
    }

    public static void printInputReader(Reader reader){
        BufferedReader readerOutput = new BufferedReader(reader);
//        readerOutput.lines().forEach( System.out::println );
        System.out.print(readerOutput.lines().collect(Collectors.joining(System.lineSeparator())));
    }
}
