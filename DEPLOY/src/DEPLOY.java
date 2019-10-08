import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class DEPLOY {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> ipList = Files.readAllLines(Paths.get("ipAdress"));
//        ProcessBuilder pb = new ProcessBuilder("ssh", "vrichard@"+"c130-16", "hostname");
//        ConnectingProcess connectingProcess = new ConnectingProcess(pb);
        for (String ip: ipList) {
            ProcessBuilder pb = new ProcessBuilder("ssh", "vrichard@"+ip, "hostname");
            ConnectingProcess connectingProcess = new ConnectingProcess(pb);
        }

    }
}
