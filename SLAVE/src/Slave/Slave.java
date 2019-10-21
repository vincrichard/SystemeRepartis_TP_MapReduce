package Slave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Slave {
    public static void main (String[] args) throws InterruptedException, IOException {
        creationFolder("maps/");
        if(args.length == 0)
            System.out.println("Usage: java -jar SLAVE.jar <option numero du Split>");
            System.exit(0);
        map(args[0]);
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
     * @param splitNumber
     * @return
     */
    public static void map(String splitNumber) throws IOException {
        String splitName = "S"+splitNumber+".txt";
        List<String> lines = fileToArrayList("/tmp/vrichard/splits/"+splitName);
        List<String> words = separeWordsInLine(lines);
        writeFile(splitNumber, words);
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
                    wordArray.add(word+ " 1");
            }
        }
        return wordArray;
    }

    /**
     * Write the output mapping file
     * @param splitNumber
     * @param words
     * @throws IOException
     */
    public static void writeFile(String splitNumber, List<String> words) throws IOException {
        Path ouputMap = Paths.get("/tmp/vrichard/maps/UM"+splitNumber+".txt");
        Files.write(ouputMap,words);
    }
}
