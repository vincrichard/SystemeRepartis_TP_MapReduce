import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Master {

    public static void  main(String[] args) throws IOException, InterruptedException {
        List<String> ipList = Files.readAllLines(Paths.get("/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/ipAdress"));
        copySplits(ipList);
//        runSlave(ipList);
    }

    private static void runSlave(List<String> ipList) throws IOException, InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        //Process run le programme SLAVE sur les machines connecté en ssh
        processBuilderOnAllIp(ipList, threadList, "java -jar /tmp/vrichard/SLAVE.jar");
    }

    private static void processBuilderOnAllIp(List<String> ipList, List<Thread> threadList, String s) throws InterruptedException {
        for (String ip : ipList) {
            ProcessBuilder pbMkdir = new ProcessBuilder("ssh", "vrichard@" + ip, s);
            ConnectingThread mkdirThread = new ConnectingThread(pbMkdir);
            threadList.add(mkdirThread);
        }
        //Attente de la fin de tous les process de création des dossiers
        for (Thread thread : threadList) {
            thread.join();
        }
    }

    private static void copySplits(List<String> ipList) throws IOException, InterruptedException {
        //List de tread assurant la synchronisation des étapes
        List<Thread> threadList = new ArrayList<>();
        //Dictionaire permettant de garder l'information des couple machine / splits
        Map<String,List<String>> ipDictNumberSplit = new HashMap<>();
        //Process qui créé le dossier splits
        processBuilderOnAllIp(ipList, threadList, "mkdir -p /tmp/vrichard/splits/");
        //Recupération des splits
        File splitsDirectory = new File("/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/splits/");
        String [] splitsList = splitsDirectory.list();
        //Existence de splitList
        if(splitsList == null){
            System.out.println("There is no file in the splits directory");
            System.exit(0);
        }
        //Process de copie les splits
        int numMachine = 0; //Variable permettant de répartir les splits sur nos machines
        for (String split: splitsList) {
            //Vérification pour boucler sur no machine
            if(numMachine == ipList.size()) {
                numMachine = 0;
            }
            //Assignation de l'ip
            String ip = ipList.get(numMachine);
            //Copie du split actuel sur la machine ip
            envoiSplit(threadList, split, ip);
            //Garder le lien pour savoir sur quelle machine est quel split
            keepLinkMachineSplit(ipDictNumberSplit, split, ip);
            numMachine++;
        }
        //Attente de la fin de tous les process
        for (Thread thread: threadList) {
            thread.join();
        }
    }

    private static void keepLinkMachineSplit(Map<String, List<String>> ipDictNumberSplit, String split, String ip) {
        if(ipDictNumberSplit.containsKey(ip)) {
            ipDictNumberSplit.get(ip).add(split);
        }else{
            ipDictNumberSplit.put(ip, new ArrayList<>());
            ipDictNumberSplit.get(ip).add(split);
        }
    }

    private static void envoiSplit(List<Thread> threadList, String split, String ip) {
        ProcessBuilder pbScpSplit = new ProcessBuilder("scp",
                "/media/vincent/C0FC3B20FC3B0FE0/MSBGD/SystemeRepartie/TPMapReduce/data/splits/"+split,
                "vrichard@" + ip + ":/tmp/vrichard/splits/"+split);
        ConnectingThread scpThread = new ConnectingThread(pbScpSplit);
        threadList.add(scpThread);
    }
}
