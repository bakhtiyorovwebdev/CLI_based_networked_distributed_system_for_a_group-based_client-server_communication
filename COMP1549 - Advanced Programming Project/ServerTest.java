// The given JUnit test is designed to test the initialisation of the server
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerTest {
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
    public void testServerInitialization() {
        // Attempt to connect to the server
        try (Socket socket = new Socket("localhost", TEST_PORT)) {
            assertTrue(socket.isConnected()); // Verify that connection is successful
        } catch (IOException e) {
            fail("Server did not start listening on port " + TEST_PORT);
        }
    }
}
