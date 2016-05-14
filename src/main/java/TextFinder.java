/**
 * Created by Ankit on 2016-05-14.
 */
public class TextFinder implements Runnable{
    private final Caller _caller;

    public TextFinder(Caller caller) {
        _caller = caller;
    }
    public void run() {
        _caller.whileRunning();
        _caller.onComplete();
    }

    public interface Caller {
        void whileRunning();
        void onComplete();
    }
}
