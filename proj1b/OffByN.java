public class OffByN implements CharacterComparator {

    private int value;

    public OffByN(int N) {
        value = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == value;
    }
}
