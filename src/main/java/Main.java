import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static final int CONSOLE_SIZE = 200;
    private static final int SEARCH_RADIUS = 1;
    private static final int CHARACTER_DISPLAY_RADIUS = 500;
    private static AtomicBoolean _isLoading = new AtomicBoolean(true);
    private static AtomicInteger _loadProgressPercent = new AtomicInteger();
    public static String[] _textBook;
    private static int[] _maxPageChars;
    public static void main(String args[]) {
        init();
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
            //deleteCharsFromConsole(4);
            print("\n");
            print(_loadProgressPercent.get() + "%%");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //deleteCharsFromConsole(4);
        print("\n");
        println("100%%");
        println("PDF Loaded");
        onLoad();
    }

    private static void onLoad() {
        while (true) {
            try {
                println("Enter terms separated by spaces: ");
                String input;
                try {
                    input = consoleRead();
                } catch (NumberFormatException e) {
                    println("Not a valid number");
                    continue;
                }
                if(input.equals(EXIT_KEYWORD)) {
                    return;
                }


                String[] words  = input.split(" ");

                if(words.length < 2) {
                    println("More than one word required!");
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
                        TextLocation resultIndex = result.getIndex();
                        print("\nResult " + (i + 1) + ": location is " + (resultIndex.getAbsoluteLocation() + 3));
                        println(" with std " + result.getStandardDeviation());
                        printNearbyText(resultIndex);
                        print("\n");
                    }
                    print("\n\n");
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printNearbyText(TextLocation index) {
        int charNumber = index.getCharNumber();
        int pageNumber = index.getPageNumber();
        int maxPageChar = _maxPageChars[pageNumber];
        String toFormat;
        String page = _textBook[pageNumber];
        if(charNumber > CHARACTER_DISPLAY_RADIUS && charNumber + CHARACTER_DISPLAY_RADIUS < maxPageChar) {
            toFormat = page.substring(charNumber - CHARACTER_DISPLAY_RADIUS,charNumber + CHARACTER_DISPLAY_RADIUS);
            charNumber = CHARACTER_DISPLAY_RADIUS;
        } else if(charNumber < CHARACTER_DISPLAY_RADIUS) {
            if(maxPageChar <= 2*CHARACTER_DISPLAY_RADIUS ) {
                toFormat = page;
            }
            else {
                toFormat = page.substring(0, 2 * CHARACTER_DISPLAY_RADIUS);
            }
        } else {
            if(maxPageChar <= 2*CHARACTER_DISPLAY_RADIUS) {
                toFormat = page;
            }
            else {
                toFormat = page.substring(maxPageChar - 2 * CHARACTER_DISPLAY_RADIUS);
            }
            charNumber = charNumber - maxPageChar + CHARACTER_DISPLAY_RADIUS*2;
        }
        println(insertCharactersEveryFewLines("\n",toFormat,CONSOLE_SIZE,charNumber));

    }

    public static String insertCharactersEveryFewLines(String character, String original, int numChars,int wordIndex) {
        StringBuilder stringBuilder = new StringBuilder(original);
        stringBuilder.insert(wordIndex,"--->");
        for(int i = numChars; i < original.length(); i += numChars) {
            stringBuilder.insert(i,character);
        }
        return stringBuilder.toString();
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
