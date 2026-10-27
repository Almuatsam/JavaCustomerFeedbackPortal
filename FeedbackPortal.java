import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FeedbackPortal {

    static final int CAPACITY = 100;
    static final String FILE_NAME = "feedback.txt";

    // Parallel arrays: index i holds one customer's feedback across all three
    static String[] names = new String[CAPACITY];
    static int[] ratings = new int[CAPACITY];
    static String[] comments = new String[CAPACITY];

    // Number of feedback entries currently stored
    static int count = 0;

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        loadFeedback();
        int choice = 0;

        while (choice != 7) {
            showMenu();
            choice = readInt("Choose: ");

            switch (choice) {
                case 1:
                    addFeedback();
                    break;
                case 2:
                    showAll();
                    break;
                case 3:
                    showAverage();
                    break;
                case 4:
                    searchByRating();
                    break;
                case 5:
                    deleteFeedback();
                    break;
                case 6:
                    saveFeedback();
                    break;
                case 7:
                    saveFeedback();
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 7.");
            }
        }
    }

    static void showMenu() {
        System.out.println("===== CUSTOMER FEEDBACK PORTAL =====");
        System.out.println("1. Add feedback");
        System.out.println("2. Show all feedback");
        System.out.println("3. Show average rating");
        System.out.println("4. Search by rating");
        System.out.println("5. Delete feedback");
        System.out.println("6. Save to file");
        System.out.println("7. Save and exit");
    }

    // Reads one line from the keyboard; saves and exits if the input stream is closed
    static String readLine() {
        try {
            String line = reader.readLine();
            if (line != null) {
                return line;
            }
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
        saveFeedback();
        System.out.println("Input closed. Goodbye!");
        System.exit(0);
        return "";
    }

    // Asks until the user types a whole number, so text like "abc" never crashes the program
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number, not text.");
            }
        }
    }

    static void addFeedback() {
        if (count >= CAPACITY) {
            System.out.println("The feedback list is full.");
            return;
        }

        String name = "";
        while (name.isEmpty()) {
            System.out.print("Customer name: ");
            // "|" separates fields in the file, so it cannot appear inside a name
            name = readLine().trim().replace("|", "/");
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
            }
        }

        int rating = 0;
        while (rating < 1 || rating > 5) {
            rating = readInt("Rating (1-5): ");
            if (rating < 1 || rating > 5) {
                System.out.println("Rating must be between 1 and 5.");
            }
        }

        String comment = "";
        while (comment.isEmpty()) {
            System.out.print("Comment: ");
            comment = readLine().trim();
            if (comment.isEmpty()) {
                System.out.println("Comment cannot be empty.");
            }
        }

        names[count] = name;
        ratings[count] = rating;
        comments[count] = comment;
        count++;

        System.out.println("Feedback added.");
    }

    static void showAll() {
        if (count == 0) {
            System.out.println("No feedback yet.");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + names[i] + " | Rating: " + ratings[i]
                    + " | " + comments[i]);
        }
    }

    static void showAverage() {
        if (count == 0) {
            System.out.println("No feedback yet.");
            return;
        }

        int total = 0;
        for (int i = 0; i < count; i++) {
            total += ratings[i];
        }

        double average = (double) total / count;
        System.out.printf("Average rating: %.2f (from %d feedback)%n", average, count);
    }

    static void searchByRating() {
        int target = readInt("Rating to search for (1-5): ");

        if (target < 1 || target > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (ratings[i] == target) {
                System.out.println((i + 1) + ". " + names[i] + " | Rating: " + ratings[i]
                        + " | " + comments[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No feedback with rating " + target + ".");
        }
    }

    static void deleteFeedback() {
        if (count == 0) {
            System.out.println("No feedback to delete.");
            return;
        }

        showAll();
        int number = readInt("Feedback number to delete: ");

        if (number < 1 || number > count) {
            System.out.println("Invalid feedback number.");
            return;
        }

        int index = number - 1;
        for (int i = index; i < count - 1; i++) {
            names[i] = names[i + 1];
            ratings[i] = ratings[i + 1];
            comments[i] = comments[i + 1];
        }

        count--;
        names[count] = null;
        comments[count] = null;
        ratings[count] = 0;

        System.out.println("Feedback deleted.");
    }

    static void saveFeedback() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < count; i++) {
                writer.write(names[i] + "|" + ratings[i] + "|" + comments[i]);
                writer.newLine();
            }
            System.out.println("Saved " + count + " feedback to " + FILE_NAME + ".");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void loadFeedback() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = fileReader.readLine()) != null && count < CAPACITY) {
                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) {
                    continue;
                }
                try {
                    int rating = Integer.parseInt(parts[1]);
                    names[count] = parts[0];
                    ratings[count] = rating;
                    comments[count] = parts[2];
                    count++;
                } catch (NumberFormatException e) {
                    System.out.println("Skipped a damaged line in " + FILE_NAME + ".");
                }
            }
            System.out.println("Loaded " + count + " feedback from " + FILE_NAME + ".");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
