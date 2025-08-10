import java.io.*;
import java.util.*;

public class KMPsearch {
    private static Map<Character, int[]> skipTable; // stores the skip table where each character maps to an array of
                                                    // skip values

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("1. To display the skip table:");
            System.out.println("   java KMPsearch \"pattern\"");
            System.out.println("2. To search for a pattern in a file:");
            System.out.println("   java KMPsearch \"pattern\" filename.txt");
            return;
        }

        String pattern = args[0];
        buildSkipTable(pattern);
        if (args.length == 1) {
            printSkipTable(pattern);
        } else {
            String filename = args[1];
            searchInFile(pattern, filename);
        }
    }

    public static void buildSkipTable(String pattern) { // Build the skip table for the pattern
        skipTable = new TreeMap<>(); // use treeMap to maintain alphabetical order
        int m = pattern.length();

        for (char c : pattern.toCharArray()) { // Add all unique characters from pattern to the skip table
            if (!skipTable.containsKey(c)) {
                skipTable.put(c, new int[m]); // Create array for each unique char
            }
        }

        for (char c : skipTable.keySet()) { // Calculate skip values for each character at each position
            for (int pos = 0; pos < m; pos++) {
                skipTable.get(c)[pos] = calculateSkip(pattern, pos, c);
            }
        }
    }

    private static int calculateSkip(String pattern, int pos, char c) { // Calculate skip value for a specific character
                                                                        // at a specific position
        if (pattern.charAt(pos) == c) {
            return 0; // No skip if characters match
        }

        String prefix = pattern.substring(0, pos) + c; // Find the largest prefix that matches a suffix ending with 'c'
        for (int k = prefix.length(); k > 0; k--) {
            if (k <= pattern.length() && prefix.endsWith(pattern.substring(0, k))) {
                return pos + 1 - k;
            }
        }
        return pos + 1; // If no prefix match, skip full pattern length
    }

    public static void printSkipTable(String pattern) { // Print the skip table
        int m = pattern.length();

        System.out.print("*"); // Print header row
        for (int i = 0; i < m; i++) {
            System.out.print("," + pattern.charAt(i));
        }
        System.out.println();

        for (char c : skipTable.keySet()) { // Print rows for each character in pattern (alphabetical order)
            System.out.print(c);
            for (int pos = 0; pos < m; pos++) {
                System.out.print("," + skipTable.get(c)[pos]);
            }
            System.out.println();
        }

        System.out.print("*"); // Print row for all other characters (*)
        for (int pos = 0; pos < m; pos++) {
            System.out.print("," + (pos + 1));
        }
        System.out.println();
    }

    public static int kmpSearch(String text, String pattern) { // Search using the skip table
        int m = pattern.length();
        int n = text.length();
        int pos = 0; // current position in pattern

        for (int i = 0; i < n;) {
            char c = text.charAt(i);
            int[] skips;

            if (skipTable.containsKey(c)) { // Get skip values for current character
                skips = skipTable.get(c);
            } else {
                skips = new int[m]; // For non-pattern chars, move full pattern length
                for (int j = 0; j < m; j++)
                    skips[j] = j + 1;
            }

            int skip = skips[pos];

            if (skip == 0) {
                // Match found at current position
                pos++;
                i++;
                if (pos == m) {
                    return i - m + 1; // Return 1-based index of match
                }
            } else {
                // Skip ahead according to skip table
                i += Math.max(1, skip - pos);
                pos = Math.max(0, pos - skip);
            }
        }
        return -1;
    }

    public static void searchInFile(String pattern, String filename) { // Read file and search line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int index = kmpSearch(line, pattern);
                if (index != -1) {
                    System.out.println(index + " " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
        }
    }
}