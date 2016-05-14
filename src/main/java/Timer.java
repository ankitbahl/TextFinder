public class Timer {
    private long _startTimeNanoSeconds;
    private boolean _wasStarted = false;
    private static final long TIMER_EXTRATIME = 3000;
    public void start() {
        _startTimeNanoSeconds = System.nanoTime();
        _wasStarted = true;
    }

    public synchronized void printTime() {
        assertStarted();
        long totalTime =  (System.nanoTime() -_startTimeNanoSeconds - TIMER_EXTRATIME);
        if(totalTime < 1000) {
            System.out.printf("Total time is " + Long.toString(totalTime) + " nanoseconds");
        } else if(totalTime < 1000000) {
            System.out.printf("Total time is " + Double.toString((double) totalTime/1000) + " microseconds");
        } else if (totalTime < 1000000000) {
            System.out.printf("Total time is " + Double.toString((double) totalTime/1000000) + " milliseconds");
        } else {
            System.out.printf("Total time is " + Double.toString((double) totalTime/1000000000) + " seconds");
        }
    }
    private void assertStarted() {
        if(!_wasStarted) {
            throw new RuntimeException("Timer has not been started");
        }
    }
}
