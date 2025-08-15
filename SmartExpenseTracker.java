import java.io.*;
import java.util.*;

class Expense {
    int id;
    String category;
    double amount;
    String date;

    Expense(int id, String category, double amount, String date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return id + "," + category + "," + amount + "," + date;
    }
}

public class SmartExpenseTracker {
    static final String FILE_NAME = "expenses.txt";
    static List<Expense> expenses = new ArrayList<>();
    static int nextId = 1;

    public static void main(String[] args) {
        loadExpenses();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Smart Expense Tracker ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spent");
            System.out.println("4. Delete Expense");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addExpense(sc);
                case 2 -> viewExpenses();
                case 3 -> viewTotalSpent();
                case 4 -> deleteExpense(sc);
                case 5 -> {
                    saveExpenses();
                    System.out.println("Goodbye! Data saved to " + FILE_NAME);
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void addExpense(Scanner sc) {
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        expenses.add(new Expense(nextId++, category, amount, date));
        System.out.println("Expense added successfully!");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        System.out.println("\n--- All Expenses ---");
        for (Expense e : expenses) {
            System.out.println(
                    "ID: " + e.id + " | Category: " + e.category + " | Amount: Rs. " + e.amount + " | Date: " + e.date);
        }
    }

    private static void viewTotalSpent() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        System.out.println("Total Spent: Rs. " + total);
    }

    private static void deleteExpense(Scanner sc) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to delete.");
            return;
        }
        viewExpenses();
        System.out.print("Enter ID of expense to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean removed = expenses.removeIf(e -> e.id == id);
        if (removed) {
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Expense with ID " + id + " not found.");
        }
    }

    private static void saveExpenses() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                bw.write(e.toString());
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Error saving expenses: " + ex.getMessage());
        }
    }

    private static void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    expenses.add(new Expense(id, parts[1], Double.parseDouble(parts[2]), parts[3]));
                    if (id >= nextId)
                        nextId = id + 1;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading expenses: " + ex.getMessage());
        }
    }
}
