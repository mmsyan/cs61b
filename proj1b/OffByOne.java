public class OffByOne implements CharacterComparator{

    /** equalChars returns true for characters that are different by exactly one */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}
