import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Master {

    public static void  main(String[] args) throws IOException, InterruptedException {
//        runSlave();
        copySlpits();
    }

    private static void runSlave() throws IOException, InterruptedException {
        List<String> ipList = Files.readAllLines(Paths.get("/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/ipAdress"));
        List<Thread> threadList = new ArrayList<>();
        //Process run le programme SLAVE sur les machines connecté en ssh
        for (String ip: ipList) {
            ProcessBuilder pbRunSlave = new ProcessBuilder("ssh", "vrichard@" + ip, "java -jar /tmp/vrichard/SLAVE.jar");
            ConnectingThread runSlaveThread = new ConnectingThread(pbRunSlave);
            threadList.add(runSlaveThread);
        }
        //Attente de la fin de tous les process
        for (Thread thread: threadList) {
            thread.join();
        }
    }

    private static void copySlpits() throws IOException, InterruptedException {
        List<String> ipList = Files.readAllLines(Paths.get("/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/ipAdress"));
        List<Thread> threadList = new ArrayList<>();
        //Process run le programme SLAVE sur les machines connecté en ssh
        for (String ip: ipList) {
            ProcessBuilder pbMkdir = new ProcessBuilder("ssh", "vrichard@" + ip, "mkdir -p /tmp/vrichard/splits/");
            ConnectingThread mkdirThread = new ConnectingThread(pbMkdir);
            threadList.add(mkdirThread);
        }
        //Attente de la fin de tous les process de création des dossiers
        for (Thread thread: threadList) {
            thread.join();
        }
        //Recupération des splits
        File splitsDirectory = new File("/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/splits/");
        String [] splitsList = splitsDirectory.list();
        //Process de copie les splits
        for (String ip: ipList){
            for (String split: splitsList) {
                ProcessBuilder pbScpSplit = new ProcessBuilder("scp",
                        "/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/splits/"+split,
                        "vrichard@" + ip + ":/tmp/vrichard/splits/"+split);
                ConnectingThread scpThread = new ConnectingThread(pbScpSplit);
                threadList.add(scpThread);
            }
        }
        //Attente de la fin de tous les process
        for (Thread thread: threadList) {
            thread.join();
        }
    }
}
