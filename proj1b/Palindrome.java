public class Palindrome {

    /** Return a Deque where the characters appear in the same order as in the String. */
    public Deque<Character> wordToDeque(String word) {
        ArrayDeque<Character> result = new ArrayDeque<>();
        for(int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    private boolean isPalindromeHelp(Deque<Character> deque) {
        if (deque.size() < 2) {
            return true;
        }
        if (deque.removeFirst().equals(deque.removeLast())) {
            return isPalindromeHelp(deque);
        }
        return false;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> ad = wordToDeque(word);
        return isPalindromeHelp(ad);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        char[] words = word.toCharArray();
        for (int i = 0; i < word.length() / 2; i++) {
            if (!cc.equalChars(words[i], words[words.length - i - 1])) {
                return false;
            }
        }
        return true;
    }
}
