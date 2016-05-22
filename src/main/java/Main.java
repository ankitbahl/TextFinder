import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
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
    private static AtomicBoolean _isLoading = new AtomicBoolean(true);
    private static AtomicInteger _loadProgressPercent = new AtomicInteger();
    public static String[] _textBook;
    private static int[] _maxPageChars;
    public static void main(String args[]) {

        //init();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.remove(2);
        for(int i = 0; i < list.size(); i++) {
            println("" + list.get(i));
        }
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
                List<WordTextLocation> wordTextLocations = Search.findWords(words);
                List<Result> results = Search.search(wordTextLocations);
                if(results.isEmpty()) {
                    println("No results");
                }
                else {
                    for (int i = 0; i < results.size() && i < 5; i++) {
                        Result result = results.get(i);
                        println("Result " + (i + 1) + ": location is " + (result.getIndex().getAbsoluteLocation() + 3));
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
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
}
