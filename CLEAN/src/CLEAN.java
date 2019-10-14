import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CLEAN {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> ipList = Files.readAllLines(Paths.get("/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/ipAdress"));
        List<Thread> threadList = new ArrayList<>();
        //Process de la suppression du dossier /tmp/vrichard/
        for (String ip: ipList) {
            ProcessBuilder pbMkdir = new ProcessBuilder("ssh", "vrichard@" + ip, "rm -rf /tmp/vrichard/");
            ConnectingThread mkdirThread = new ConnectingThread(pbMkdir);
            threadList.add(mkdirThread);
        }
        //Attente de la fin de tous les process de création des dossiers
        for (Thread thread: threadList) {
            thread.join();
        }
    }
}