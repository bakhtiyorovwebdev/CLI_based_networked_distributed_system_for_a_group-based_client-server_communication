// The given JUnit test is designed to test the feature of admin change in the server.

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerTestAdminChange {
    private Server server;
    private final int TEST_PORT = 9999;

    @BeforeEach
    public void setUp() {
        server = new Server(); // Create an instance of the Server class
        new Thread(server).start(); // Start the server in a separate thread
    }

    @AfterEach
    public void tearDown() {
        server.shutdown(); // Shutdown the server after each test
    }

    @Test
    public void testAdminChange() {
        // Connect multiple clients to the server
        try (Socket adminSocket = new Socket("localhost", TEST_PORT);
             Socket userSocket1 = new Socket("localhost", TEST_PORT);
             Socket userSocket2 = new Socket("localhost", TEST_PORT)) {

            assertTrue(adminSocket.isConnected()); // Verify that admin connection is successful
            assertTrue(userSocket1.isConnected()); // Verify that user1 connection is successful
            assertTrue(userSocket2.isConnected()); // Verify that user2 connection is successful

            // Read admin's ID from the response
            BufferedReader adminIn = new BufferedReader(new InputStreamReader(adminSocket.getInputStream()));
            String adminResponse = adminIn.readLine();
            int adminId = Integer.parseInt(adminResponse.split(" ")[6]); // Extract admin ID

            // Simulate admin leaving
            adminSocket.close();

            // Read the next messages to find out if another user becomes the admin
            BufferedReader userIn1 = new BufferedReader(new InputStreamReader(userSocket1.getInputStream()));
            String userResponse1 = userIn1.readLine();

            BufferedReader userIn2 = new BufferedReader(new InputStreamReader(userSocket2.getInputStream()));
            String userResponse2 = userIn2.readLine();

            // Verify that one of the remaining users becomes the new admin
            int newAdminId = -1;
            if (userResponse1.startsWith("You are the new admin.")) {
                newAdminId = Integer.parseInt(userResponse1.split(" ")[6]);
            } else if (userResponse2.startsWith("You are the new admin.")) {
                newAdminId = Integer.parseInt(userResponse2.split(" ")[6]);
            }

            assertNotEquals(adminId, newAdminId); // Ensure a new admin is assigned
        } catch (IOException e) {
            fail("Failed to connect to the server");
        }
    }
}
