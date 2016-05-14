import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final String FILE_LOCATION = "C:/pdf/greekmyths2.pdf";
    private static final String EXIT_KEYWORD = "exit";
    public static final int FIRST_PAGE = 25;
    private static AtomicBoolean _isLoading = new AtomicBoolean(true);
    private static String[] _textBook;
    private static int[] _maxPageChars;
    public static void main(String args[]) {
        init();
    }
//
    private static void init() {
        println("Loading pdf into memory...");
        Thread thread = new Thread(new PdfLoader(new PdfLoader.Caller() {
            public void onComplete(String[] data1,int[] data2 ) {
                _isLoading.set(false);
                _textBook = data1;
                _maxPageChars = data2;
            }
        }, FILE_LOCATION));
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(boolean b = false; _isLoading.get(); b = !b) {
            if(b) {
                println("..");
            } else {
                println(".");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                findWords(words);

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findWords(String[] words) {

        WordTextLocation[] wordTextLocations = new WordTextLocation[words.length];
        for(int i = 0; i < words.length; i++) {
            String word = words[i];
            List<TextLocation> textLocations = new ArrayList<TextLocation>();
            for (int j = 0; j < _textBook.length; j++) {
                String page = _textBook[j];
                if (page.toLowerCase().contains(word.toLowerCase())) {
                    textLocations.add(new TextLocation(i,page.toLowerCase().indexOf(word.toLowerCase())));
                }
            }
            wordTextLocations[i] = new WordTextLocation(word, textLocations.toArray(new TextLocation[textLocations.size()]));
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

    private String[] removeFirst(String[] toRemove) {
        if(toRemove == null || toRemove.length == 1) {
            return null;
        }
        int newSize = toRemove.length-1;
        String[] temp = new String[newSize];
        for(int i = 0; i < newSize; i++ ) {
            temp[i] = toRemove[i + 1];
        }
        return temp;
    }

}
