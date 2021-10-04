import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxIndex = 0;
        for (String iterator : asciis) {
            maxIndex = Math.max(maxIndex, iterator.length());
        }
        ArrayList<String>[] bucket = new ArrayList[256];

        String[] s = asciis;
        for (int i = maxIndex; i > 0; i--) {
            for (int k = 0; k < 256; k++) {
                bucket[k] = new ArrayList<>();
            }
            for (String iterator : s) {
                if (iterator.length() < i) {
                    bucket[0].add(iterator);
                }
                else {
                    bucket[iterator.charAt(i - 1)].add(iterator);
                }
            }
            s = stringToQueuearray(bucket, asciis.length);
        }
        return s;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        return;
    }

    private static String[] stringToQueuearray(ArrayList<String>[] array, int length) {
        String[] result = new String[length];
        int index = 0;
        for (ArrayList<String> strings : array) {
            for (String iterator : strings) {
                result[index] = iterator;
                index++;
                if (index == length) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] a =  {"3561", "12", "904", "294", "209", "820", "394", "810","9845613"};
        System.out.println(Arrays.toString(sort(a)));
    }
}
