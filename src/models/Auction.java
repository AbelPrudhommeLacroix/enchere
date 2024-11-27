import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Auction {

    // Method to list a product for auction
    public static void listProduct(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Add your product ===");
            System.out.print("Name: ");
            String name = scanner.next();

            System.out.print("Price: ");
            float price = scanner.nextFloat();

            System.out.print("Stock: ");
            int stock = scanner.nextInt();

            System.out.print("Category: ");
            scanner.nextLine(); // consume newline
            String category = scanner.nextLine();

            // Simulate ID generation (in real application use auto-increment)
            int productId = (int) (Math.random() * 10000);

            String insertProductSql = "INSERT INTO Products (name, price, stock, category) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertProductSql)) {
                stmt.setString(1, name);
                stmt.setFloat(2, price);
                stmt.setInt(3, stock);
                stmt.setString(4, category);
                stmt.executeUpdate();
                System.out.println("Product added!");
            }

        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    // Method to handle bidding
    public static void bid(Connection conn, Scanner scanner) {
        // Add bidding logic (placeholder for now)
        System.out.println("Bidding functionality not implemented yet.");
    }

    // Method to view auction status
    public static void viewAuctionStatus(Connection conn, Scanner scanner) {
        // Add logic to view auction status (placeholder for now)
        System.out.println("View auction status functionality not implemented yet.");
    }
}
