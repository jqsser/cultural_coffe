package tn.esprit.tests;

import tn.esprit.entities.*;
import tn.esprit.services.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /** try {
            // First test UserService since Matching depends on User
            testUserService();

            // Then test MatchingService since Message depends on Matching
            testMatchingService();

            // Finally test MessageService
            testMessageService();

        } catch (Exception e) {
            System.err.println("Error in main: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testUserService() throws SQLException {
        System.out.println("\n=== TESTING USER SERVICE ===");
        UserService userService = new UserService();

        // Create test user
        User testUser = new User();
        testUser.setNomUser("Test");
        testUser.setPrenomUser("User");
        testUser.setEmailUser("test.user@example.com");
        testUser.setPassword("test123");
        testUser.setRoleUser("ROLE_USER");
        testUser.setAdresse("123 Test Street");
        testUser.setTelephoneUser(123456789);
        testUser.setDateNaissanceUser(LocalDate.of(1990, 1, 1));
        testUser.setPhotoUser("profile.jpg");

        // Test CREATE
        System.out.println("Creating user...");
        userService.ajouter(testUser);
        System.out.println("User created with ID: " + testUser.getId());

        // Test READ ALL
        System.out.println("\nAll users:");
        List<User> users = userService.recuperer();
        users.forEach(System.out::println);

        // Test GET BY ID
        System.out.println("\nGetting user by ID...");
        User fetchedUser = userService.getById(testUser.getId());
        System.out.println("Fetched user: " + fetchedUser);

        // Test UPDATE
        System.out.println("\nUpdating user...");
        fetchedUser.setNomUser("Updated");
        fetchedUser.setPrenomUser("Name");
        userService.modifier(fetchedUser);
        System.out.println("User updated: " + userService.getById(fetchedUser.getId()));

        // Test DELETE (commented out to keep user for other tests)
        // System.out.println("\nDeleting user...");
        // userService.supprimer(fetchedUser.getId());
        // System.out.println("User deleted. Current count: " + userService.recuperer().size());
    }

    public static void testMatchingService() throws SQLException {
        System.out.println("\n=== TESTING MATCHING SERVICE ===");
        MatchingService matchingService = new MatchingService();
        UserService userService = new UserService();

        // Get a user to be the host
        List<User> users = userService.recuperer();
        if (users.isEmpty()) {
            System.out.println("No users available - create a user first");
            return;
        }
        User hostUser = users.get(0);

        // Create test matching
        Matching testMatching = new Matching();
        testMatching.setName("Tech Discussion");
        testMatching.setSujetRencontre("Programming languages");
        testMatching.setNumTable(5);
        testMatching.setNbrPersonneMatchy(4);
        testMatching.setImage("tech.jpg");
        testMatching.setUser(hostUser);

        // Test CREATE
        System.out.println("Creating matching...");
        matchingService.ajouter(testMatching);
        System.out.println("Matching created with ID: " + testMatching.getId());

        // Test GET BY ID with null check
        System.out.println("\nGetting matching by ID...");
        Matching fetchedMatching = matchingService.getById(testMatching.getId());

        if (fetchedMatching != null) {
            System.out.println("Fetched matching: " + fetchedMatching.getName());

            // Test UPDATE
            System.out.println("\nUpdating matching...");
            fetchedMatching.setName("Updated Tech Discussion");
            matchingService.modifier(fetchedMatching);
            System.out.println("Matching updated: " + matchingService.getById(fetchedMatching.getId()).getName());
        } else {
            System.err.println("Error: Could not find matching with ID " + testMatching.getId());

            // Debugging: Print all matchings to see what's in DB
            System.out.println("\nAll matchings in database:");
            matchingService.recuperer().forEach(m ->
                    System.out.println("ID: " + m.getId() + ", Name: " + m.getName()));
        }
    }

    public static void testMessageService() throws SQLException {
        System.out.println("\n=== TESTING MESSAGE SERVICE ===");
        MessageService messageService = new MessageService();
        UserService userService = new UserService();
        MatchingService matchingService = new MatchingService();

        // Get required entities
        List<User> users = userService.recuperer();
        List<Matching> matchings = matchingService.recuperer();

        if (users.isEmpty() || matchings.isEmpty()) {
            System.out.println("Need at least one user and one matching to test messages");
            return;
        }

        User sender = users.get(0);
        Matching matching = matchings.get(0);

        // Create test message
        Message testMessage = new Message();
        testMessage.setContent("Hello, this is a test message!");
        testMessage.setUser(sender);
        testMessage.setMatching(matching);
        testMessage.setCreatedAt(LocalDateTime.now());
        testMessage.setUpdatedMessage(false);

        // Test CREATE
        System.out.println("Creating message...");
        messageService.ajouter(testMessage);
        System.out.println("Message created with ID: " + testMessage.getId());

        // Verify the message was actually created
        Message createdMessage = messageService.getById(testMessage.getId());
        if (createdMessage == null) {
            System.err.println("ERROR: Message creation failed - could not retrieve created message");
            System.out.println("Debug info - all messages in database:");
            messageService.recuperer().forEach(m -> System.out.println("ID: " + m.getId() + " - " + m.getContent()));
            return;
        }

        // Test READ ALL
        System.out.println("\nAll messages:");
        List<Message> messages = messageService.recuperer();
        messages.forEach(m -> System.out.println(m.getId() + ": " + m.getContent()));

        // Test GET BY ID
        System.out.println("\nGetting message by ID...");
        Message fetchedMessage = messageService.getById(createdMessage.getId());
        if (fetchedMessage != null) {
            System.out.println("Fetched message: " + fetchedMessage.getContent());

            // Test UPDATE
            System.out.println("\nUpdating message...");
            fetchedMessage.setContent("Updated message content");
            fetchedMessage.setUpdatedMessage(true);
            messageService.modifier(fetchedMessage);

            // Verify update
            Message updatedMessage = messageService.getById(fetchedMessage.getId());
            if (updatedMessage != null) {
                System.out.println("Message updated: " + updatedMessage.getContent() +
                        " (Updated: " + updatedMessage.isUpdatedMessage() + ")");
            } else {
                System.err.println("ERROR: Could not verify message update");
            }

            // Test DELETE
            System.out.println("\nDeleting message...");
            messageService.supprimer(updatedMessage.getId());
            System.out.println("Message deleted. Current count: " + messageService.recuperer().size());
        } else {
            System.err.println("ERROR: Could not fetch message by ID");
        }*/
    }
}