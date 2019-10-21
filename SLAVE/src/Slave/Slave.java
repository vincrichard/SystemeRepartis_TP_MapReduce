package Slave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static void shuffle(String mapNameAll) throws IOException {
        List<String> lines = fileToArrayList(mapNameAll);
//        List<String> words = separeWordsInLine(lines);
//        words.removeAll(Arrays.asList("1"));
        writeShuffleFile(lines);
    }

    private static void writeShuffleFile(List<String> lines) throws IOException {

        for(String line : lines) {
            int hashName = line.split(" ")[0].hashCode();
            String hostname = java.net.InetAddress.getLocalHost().getHostName();
            Path ouputshuffle = Paths.get("/tmp/vrichard/shuffle/"+hashName+ "-" + hostname +".txt");
            Files.write(ouputshuffle, Arrays.asList(line), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        }
    }

    /**
     * Process créé un dossier dans "tmp/vrichar/fileName
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
