package oosc.assignment01.contract;

/**
 * Design-by-Contract example.
 */
public class ContractDemo {
    public static void main(String[] args) {
        DateRange range = new DateRange(1, 10);
        range.shift(2);
        System.out.println("Range: " + range.getStart() + ".." + range.getEnd());
    }

    public static final class DateRange {
        private int start;
        private int end;

        public DateRange(int start, int end) {
            require(start <= end, "start must be <= end");
            this.start = start;
            this.end = end;
        }

        public void shift(int delta) {
            int oldStart = start;
            int oldEnd = end;
            start += delta;
            end += delta;
            ensure(end - start == oldEnd - oldStart, "range length must remain constant");
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException("Precondition failed: " + message);
        }
    }

    private static void ensure(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("Postcondition failed: " + message);
        }
    }
}