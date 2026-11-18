
import java.util.Scanner;

public class PlayfairCipher {

    static char[][] matrix = new char[5][5];

    // Generate the 5x5 key matrix
    static void generateMatrix(String key) {

        boolean[] used = new boolean[26];
        StringBuilder keyString = new StringBuilder();

        key = key.toUpperCase().replace('J', 'I');

        // Add unique characters from keyword
        for (char c : key.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && !used[c - 'A']) {
                keyString.append(c);
                used[c - 'A'] = true;
            }
        }

        // Add remaining alphabets
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') {
                continue;
            }

            if (!used[c - 'A']) {
                keyString.append(c);
                used[c - 'A'] = true;
            }
        }

        // Fill matrix
        int index = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = keyString.charAt(index++);
            }
        }
    }

    // Display key matrix
    static void displayMatrix() {
        System.out.println("\nGenerated Key Matrix:");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Find position of a character
    static int[] findPosition(char c) {

        if (c == 'J') {
            c = 'I';
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }

    // Prepare plaintext into pairs
    static String prepareText(String text) {

        text = text.toUpperCase().replaceAll("[^A-Z]", "")
                .replace('J', 'I');

        StringBuilder result = new StringBuilder();

        int i = 0;

        while (i < text.length()) {

            char first = text.charAt(i);
            char second;

            if (i + 1 >= text.length()) {
                second = 'X';
                i++;
            }
            else if (first == text.charAt(i + 1)) {
                second = 'X';
                i++;
            }
            else {
                second = text.charAt(i + 1);
                i += 2;
            }

            result.append(first).append(second);
        }

        return result.toString();
    }

    // Encrypt or decrypt
    static String processText(String text, boolean encrypt) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int[] pos1 = findPosition(a);
            int[] pos2 = findPosition(b);

            int row1 = pos1[0];
            int col1 = pos1[1];

            int row2 = pos2[0];
            int col2 = pos2[1];

            if (row1 == row2) {

                // Same row
                int shift = encrypt ? 1 : 4;

                col1 = (col1 + shift) % 5;
                col2 = (col2 + shift) % 5;
            }

            else if (col1 == col2) {

                // Same column
                int shift = encrypt ? 1 : 4;

                row1 = (row1 + shift) % 5;
                row2 = (row2 + shift) % 5;
            }

            else {

                // Rectangle rule
                int temp = col1;
                col1 = col2;
                col2 = temp;
            }

            result.append(matrix[row1][col1]);
            result.append(matrix[row2][col2]);
        }

        return result.toString();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== PLAYFAIR CIPHER =====");

        System.out.print("Enter keyword: ");
        String key = sc.nextLine();

        generateMatrix(key);
        displayMatrix();

        System.out.print("\nEnter plaintext: ");
        String plaintext = sc.nextLine();

        String preparedText = prepareText(plaintext);

        System.out.println("\nPrepared Plaintext: " + preparedText);

        String encrypted = processText(preparedText, true);

        System.out.println("Encrypted Text: " + encrypted);

        String decrypted = processText(encrypted, false);

        System.out.println("Decrypted Text: " + decrypted);

        sc.close();
    }
}
