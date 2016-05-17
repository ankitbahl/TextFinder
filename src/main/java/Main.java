import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * to run from console: java -cp TextFinder.jar Main
 */
public class Main {
    private static final String FILE_LOCATION = "C:/pdf/greekmyths2.pdf";
    private static final String EXIT_KEYWORD = "exit";
    public static final int FIRST_PAGE = 25;
    private static final int SEARCH_RADIUS = 1;
    private static AtomicBoolean _isLoading = new AtomicBoolean(true);
    private static AtomicInteger _loadProgressPercent = new AtomicInteger();
    private static String[] _textBook;
    private static int[] _maxPageChars;
    public static void main(String args[]) {
        //init();
        print("" + standardDev(4,2,5,8,6));
    }

    private static void init() {
        println("Loading pdf into memory...");
        Thread thread = new Thread(new PdfLoader(new PdfLoader.Caller() {
            @Override
            public void sendProgress(int percentProgress) {
                if(_loadProgressPercent.get() != percentProgress) {
                    _loadProgressPercent.set(percentProgress);
                }
            }

            @Override
            public void onComplete(String[] data1, int[] data2) {
                _isLoading.set(false);
                _textBook = data1;
                _maxPageChars = data2;
            }
        },FILE_LOCATION));
        thread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            print("0%%");
        for(;_isLoading.get();) {
            deleteCharsFromConsole(4);
            print(_loadProgressPercent.get() + "%%");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        deleteCharsFromConsole(4);
        println("100%%");
        println("PDF Loaded");
        onLoad();
    }

    private static void onLoad() {
        while (true) {
            try {
                println("Enter number of terms: ");
                int numberOfTerms;
                try {
                    numberOfTerms = consoleReadInt();
                    if(numberOfTerms == 0) {
                        return;
                    }
                } catch (NumberFormatException e) {
                    println("Not a valid number");
                    continue;
                }
                println("Enter in each term and press enter");
                String[] words = new String[numberOfTerms];
                for(int i = 0; i < numberOfTerms; i++) {
                    String input = consoleRead();
                    if(input.equals(EXIT_KEYWORD)) {
                        return;
                    }
                    words[i] = input;
                }

                //TODO allocate a thread for each word for efficiency
                List<WordTextLocation> wordTextLocations = findWords(words);
                search(wordTextLocations);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }


    //TODO check for case that a word/phrase starts in one page and finishes in the next
    private static List<WordTextLocation> findWords(String[] words) {
        List<WordTextLocation> wordTextLocations = new ArrayList<>(words.length);
        for(int i = 0; i < words.length; i++) {
            String word = words[i];
            WordTextLocation wordTextLocation = new WordTextLocation(word);
            for (int j = 0; j < _textBook.length; j++) {
                String page = _textBook[j];

                if (page.toLowerCase().contains(word.toLowerCase())) {
                    List<Integer> occurrencesArray = getAllIndexOccurrencesOnPage(word.toLowerCase(),page.toLowerCase());
                    for(int occurrenceIndex : occurrencesArray) {
                        wordTextLocation.addEntry(new TextLocation(j,occurrenceIndex));
                    }
                }

                if(page.toLowerCase().contains(spacesInBetweenEachCharacter(word.toLowerCase()))) {
                    List<Integer> occurrencesArray = getAllIndexOccurrencesOnPage(spacesInBetweenEachCharacter(word.toLowerCase()),page.toLowerCase());
                    for(int occurrenceIndex : occurrencesArray) {
                        wordTextLocation.addEntry(new TextLocation(j,occurrenceIndex));
                    }
                }
            }
            wordTextLocations.add(wordTextLocation);
        }
        return wordTextLocations;
    }

    public static void print(String... s) {
        String base = "";
        for(String sk : s) {
            base += sk;
        }
        System.out.printf(base);
    }

    public static void println(String... s) {
        String base = "";
        for(String sk : s) {
            base += sk;
        }
        System.out.printf(base + "\n");
    }

    public static void println(int i) {
        System.out.print(i);
        System.out.printf("\n");
    }

    public static void clearConsole() {
        for(int i = 0; i < 25; i ++) {
            System.out.printf("\n");
        }
    }

    public static void deleteCharsFromConsole(int numChars) {
        for(int i = 0; i < numChars; i++) {
            System.out.printf("\b \b");
        }
    }

    private static String consoleRead() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private static int consoleReadInt() throws NumberFormatException{
        String stringInput = consoleRead();
        return Integer.parseInt(stringInput);
    }

    public static int[] getMaxPageChars() {
        return _maxPageChars;
    }

    /**
     * assumes that it is page contains words
     * @param word
     * @param page
     * @return
     */
    private static List<Integer> getAllIndexOccurrencesOnPage(String word, String page) {
        return getAllIndexOccurrencesOnPageHelper(word,page,0);
    }

    private static List<Integer> getAllIndexOccurrencesOnPageHelper(String word, String page, int toAdd) {
        List<Integer> allIndexes = new ArrayList<Integer>();
        int index = page.indexOf(word);
        allIndexes.add(index+toAdd);
        String rest = page.substring(index + word.length());
        if(rest.contains(word)) {
            List<Integer> indexList = getAllIndexOccurrencesOnPageHelper(word,rest,toAdd + index + word.length());
            allIndexes.addAll(indexList);
        }
        return allIndexes;
    }

    private static String getPage(int pageNum) {
        try {
            PDDocument document = PDDocument.load(new File(FILE_LOCATION));
            PDFTextStripper reader = new PDFTextStripper();
            reader.setStartPage(pageNum);
            reader.setEndPage(pageNum);
            return reader.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String spacesInBetweenEachCharacter(String original) {
        return original.replaceAll(".(?=.)", "$0 ");
    }

    private static List<WordTextLocation> removeFirst(List<WordTextLocation> original) {
        return original.subList(1, original.size());
    }

    private static void search(List<WordTextLocation> wordTextLocations) {
        WordTextLocation firstWord = wordTextLocations.get(0);
        List<Integer> pageList = firstWord.getPageList();
        List<WordTextLocation> otherWords = removeFirst(wordTextLocations);
        List<Results> results = new ArrayList<>();
        for(int pageNum : pageList) {

            List<List<WordTextLocation>> allPossibleResults = new ArrayList<>(otherWords.size());
            for(WordTextLocation otherWord : otherWords) {
                List<WordTextLocation> possibleResultsForWord = new ArrayList<>(3);
                for(int pageCheck = pageNum - SEARCH_RADIUS; pageCheck < pageNum + SEARCH_RADIUS + 1; pageCheck++ ) {
                    if(otherWord.hasResultsOnPage(pageCheck)) {
                        possibleResultsForWord.add(otherWord);
                    }
                }
                allPossibleResults.add(possibleResultsForWord);
            }
            print("");

        }
    }
    private static List<Integer> getTotalCharDistance(WordTextLocation firstWord, List<WordTextLocation> otherWords) {
        return null;
    }

    public static double standardDev(double... nums) {
        double totalSum = 0;
        for(Double num : nums) {
            totalSum += num;
        }
        int numTerms = nums.length;
        double average = totalSum/numTerms;
        double sumSquares = 0;
        for(Double num : nums) {
            sumSquares += Math.pow(num - average,2);
        }
        double aveSquares = sumSquares / numTerms;
        return Math.sqrt(aveSquares);
    }

}
