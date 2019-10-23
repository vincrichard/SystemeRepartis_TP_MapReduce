package Slave;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Slave {
    public static void main (String[] args) throws InterruptedException, IOException {
        if(args.length == 0) {
            System.out.println("Usage: java -jar SLAVE.jar <option 0: map ou 1: shuffle> <option nom du Split>");
            System.exit(0);
        }
        if(args[0].contentEquals("0")){
            creationFolder("maps/");
            map(args[1]);
        }

        if(args[0].contentEquals("1")){
            creationFolder("shuffle/");
            shuffle(args[1]);
        }
    }

    private static void shuffle(String mapNameAll) throws IOException, InterruptedException {
        List<String> lines = fileToArrayList(mapNameAll);
        Map<String,Integer> fileNameHashDict = writeShuffleFile(lines);
        sendShuffleFile(fileNameHashDict);
    }

    private static void sendShuffleFile(Map<String, Integer> fileNameHashDict) throws InterruptedException, UnknownHostException {
        List<Thread> threadList = new ArrayList<>();
        List<String> ipMachineList = fileToArrayList("/tmp/vrichard/machine.txt");
        int nbMachine = ipMachineList.size();
        File shuffleDirectory = new File("/tmp/vrichard/shuffle");
        if(shuffleDirectory.listFiles() == null){
            System.out.println("Shuffle directory empty");
            return;
        }
        for (File shuffleFile : shuffleDirectory.listFiles()){
            int hash = fileNameHashDict.get(shuffleFile.getName());
            int numeroMachine = hash % nbMachine;
            String ipToSend = ipMachineList.get(numeroMachine);

            creationExternalFolder("shufflesreceived", ipToSend);

            ProcessBuilder sendShuffle = new ProcessBuilder("scp", shuffleFile.getAbsolutePath(),
                    "vrichard@"+ipToSend+":/tmp/vrichard/shufflesreceived/"+ shuffleFile.getName());
            ConnectingThread sendShuffleThread = new ConnectingThread(sendShuffle);
            threadList.add(sendShuffleThread);
        }
        //Attente de la fin des process
        for (Thread thread: threadList) {
            thread.join();
        }
    }

    private static void creationExternalFolder(String folderName, String ipToSend) throws InterruptedException {
        ProcessBuilder pbSshMkdir = new ProcessBuilder("ssh", "vrichard@"+ipToSend, "mkdir", "-p", "/tmp/vrichard/"+ folderName);
        ConnectingThread sshMkdirThread = new ConnectingThread(pbSshMkdir);
        while(sshMkdirThread.isAlive()){
            Thread.sleep(200);
        }
    }

    private static Map<String, Integer> writeShuffleFile(List<String> lines) throws IOException {
        Map<String, Integer> fileNameHashDict = new HashMap<>();
        for(String line : lines) {
            int hash = line.split(" ")[0].hashCode();
            String hostname = java.net.InetAddress.getLocalHost().getHostName();
            String fileName = hash+ "-" + hostname +".txt";
            Path ouputshuffle = Paths.get("/tmp/vrichard/shuffle/"+ fileName);
            Files.write(ouputshuffle, Arrays.asList(line), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            fileNameHashDict.put(fileName, hash);
        }
        return fileNameHashDict;
    }

    /**
     * Process créé un dossier dans "tmp/vrichard/fileName
     * @param folderName
     * @throws InterruptedException
     */
    private static void creationFolder(String folderName) throws InterruptedException {
        ProcessBuilder pbMkdir = new ProcessBuilder("mkdir", "-p", "/tmp/vrichard/"+ folderName);
        ConnectingThread mkdirThread = new ConnectingThread(pbMkdir);
        while(mkdirThread.isAlive()){
            Thread.sleep(200);
        }
    }

    /**
     * Main method Count word in a file and place the result in a HashMap
     * @param splitNameAll
     * @return
     */
    public static void map(String splitNameAll) throws IOException {
        String splitName = last(splitNameAll.split("/"));
        List<String> lines = fileToArrayList("/tmp/vrichard/splits/"+splitName);
        List<String> words = separeWordsInLine(lines);
        words.replaceAll(word -> word+" 1");
        writeSplitFile(splitName, words);
    }

    public static <T> T last(T[] array) {
        return array[array.length - 1];
    }

    /**
     * Convert Input file in an Array of line
     * @param fileName
     * @return
     */
    public static List<String> fileToArrayList(String fileName){
        List<String> lines = new ArrayList<>();
        try{
            lines = Files.readAllLines(Paths.get(fileName));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return lines;
    }

    /***
     * Convert Array of line in array of word contain in the lines
     * @param lineArray
     * @return
     */
    public static List<String> separeWordsInLine(List<String> lineArray){
        List<String> wordArray = new ArrayList<>();
        for (String line:lineArray) {
            String[] tempWordInLine = line.split(" ");
            for (String word : tempWordInLine) {
                if(!word.isEmpty())
                    wordArray.add(word);
            }
        }
        return wordArray;
    }

    /**
     * Write the output mapping file
     * @param splitFile
     * @param words
     * @throws IOException
     */
    public static void writeSplitFile(String splitFile, List<String> words) throws IOException {
        Path ouputMap = Paths.get("/tmp/vrichard/maps/UM"+splitFile.substring(1,2)+".txt");
        Files.write(ouputMap,words);
    }
}
