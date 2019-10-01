import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class Main {

    public static void  main(String[] args){
        long startTime = System.currentTimeMillis();
        Map<String, Integer> mapCount = count("deontologie_police_national.txt");
//        System.out.println(mapCount);
        long endTime = System.currentTimeMillis();
        long totalTimeCount = endTime - startTime;
        startTime = System.currentTimeMillis();
        displayByFrequency(mapCount);
        endTime = System.currentTimeMillis();
        long totalTimeSort = endTime - startTime;
        System.out.println("Counting time is:" + totalTimeCount);
        System.out.println("Sorting time is:" + totalTimeSort);
    }

    /**
     * Main method Count word in a file and place the result in a HashMap
     * @param fileName
     * @return
     */
    public static Map<String, Integer> count(String fileName){
        List<String> lines = fileToArrayList(fileName);
        List<String> words = separeWordsInLine(lines);

        Map<String, Integer> mapWordCount = new HashMap<>();
        for (String word :words) {
            if(mapWordCount.containsKey(word.toLowerCase())){
                mapWordCount.put(word.toLowerCase(), mapWordCount.get(word.toLowerCase()) + 1);
            }else{
                mapWordCount.put(word.toLowerCase(), 1);
            }
        }
        return mapWordCount;
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
            line = line.replaceAll("[^\\p{L}\\s ]", "");
            String[] tempWordInLine = line.split(" ");
            for (String word : tempWordInLine) {
                if(!word.isEmpty())
                    wordArray.add(word);
            }
        }
        return wordArray;
    }


    /**
     * Display the mapHarray in order of frequency
     * @param mapToSort
     */
    public static void displayByFrequency(Map<String, Integer> mapToSort){
        List<Map.Entry<String, Integer>> keySorted = new ArrayList<>(mapToSort.entrySet());
        keySorted.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> s, Map.Entry<String, Integer> t1) {
                if(t1.getValue() == s.getValue()){
                    return s.getKey().compareTo(t1.getKey());
                }
                return t1.getValue().compareTo(s.getValue());
            }
        });
        System.out.println("Affichage par Quantite");
        int i =0;
        for (Map.Entry entry :keySorted) {
            if(i>50)
                break;
            System.out.println(entry.getKey()+ " : " +  entry.getValue());
            i++;
        }
    }

}
