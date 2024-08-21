// The given JUnit test is designed to test the private messaging feature of a server.

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStreamReader;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerPM {
    private Server server;
    private final int TEST_PORT = 9999;
    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream inContent;

    @BeforeEach
    public void setUp() {
        server = new Server(); // Create an instance of the Server class
        new Thread(server).start(); // Start the server in a separate thread

        // Redirect System.out to capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        server.shutdown(); // Shutdown the server after each test

        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    public void testIDAssignment() {
        // Simulate client connection and input/output
        inContent = new ByteArrayInputStream("TestName\n".getBytes());
        System.setIn(inContent);

        // Attempt to connect to the server
        try (Socket socket = new Socket("localhost", TEST_PORT)) {
            assertTrue(socket.isConnected()); // Verify that connection is successful
            
            // Read the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            
            // Verify that the response contains the correct ID assignment message
            assertTrue(response.startsWith("You are connected"));

            // Verify that the nickname is assigned correctly
            assertEquals("Current users online:\nID: 1, Nickname: TestName", outContent.toString().trim());
        } catch (IOException e) {
            fail("Failed to connect to the server");
        }
    }
}
