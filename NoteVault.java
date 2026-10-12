import java.io.*;
import java.util.*;

public class NoteVault {
    private static final String FILE_NAME = "vault_notes.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== NoteVault CLI ===");
            System.out.println("1. Add Note");
            System.out.println("2. View All Notes");
            System.out.println("3. Search Note by Title");
            System.out.println("4. Clear All Notes");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-5): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addNote(scanner);
                    break;
                case 2:
                    viewNotes();
                    break;
                case 3:
                    searchNote(scanner);
                    break;
                case 4:
                    clearNotes();
                    break;
                case 5:
                    System.out.println("Exiting NoteVault. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void addNote(Scanner scanner) {
        System.out.print("Enter note title: ");
        String title = scanner.nextLine();
        System.out.print("Enter note content: ");
        String content = scanner.nextLine();

        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(title + "|" + content);
            System.out.println("Note saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving note: " + e.getMessage());
        }
    }

    private static void viewNotes() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No notes found yet. Create one!");
            return;
        }

        System.out.println("\n--- Your Stored Notes ---");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length >= 2) {
                    System.out.println(index++ + ". Title   : " + parts[0]);
                    System.out.println("   Content : " + parts[1]);
                    System.out.println("-------------------------");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading notes: " + e.getMessage());
        }
    }

    private static void searchNote(Scanner scanner) {
        System.out.print("Enter title keyword to search: ");
        String keyword = scanner.nextLine().toLowerCase();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No notes available to search.");
            return;
        }

        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length >= 2 && parts[0].toLowerCase().contains(keyword)) {
                    System.out.println("\n[Match Found]");
                    System.out.println("Title   : " + parts[0]);
                    System.out.println("Content : " + parts[1]);
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error searching notes: " + e.getMessage());
        }

        if (!found) {
            System.out.println("No matching notes found.");
        }
    }

    private static void clearNotes() {
        try (PrintWriter pw = new PrintWriter(FILE_NAME)) {
            pw.print("");
            System.out.println("All notes cleared successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No notes file exists to clear.");
        }
    }
}