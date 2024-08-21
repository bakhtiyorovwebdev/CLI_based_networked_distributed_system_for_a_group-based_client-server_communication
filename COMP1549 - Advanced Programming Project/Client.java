import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;

    public Client(String serverIp, int serverPort, String randomIp, int randomPort) {
        try {
            client = new Socket(serverIp, serverPort);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Immediately send random IP and port for server verification
            out.println(randomIp);
            out.println(randomPort);
        } catch (IOException e) {
            System.err.println("Error connecting to the server. Exiting...");
            System.exit(-1); // Exit the program if we cannot connect
        }
    }

    @Override
    public void run() {
        try {
            // Process messages from the server
            String fromServer;
            while ((fromServer = in.readLine()) != null && !done) {
                System.out.println(fromServer);
            }
        } catch (IOException e) {
            if (!done) System.err.println("Connection to server lost.");
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        done = true;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) client.close();
        } catch (IOException e) {
            // Ignored during shutdown
        }
    }

    class InputHandler implements Runnable {
        @Override
        public void run() {
            try (BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) {
                String userInput;
                while (!done && (userInput = userIn.readLine()) != null) {
                    out.println(userInput);
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Chat Application!");

        System.out.println("Enter the IP address provided by the server:");
        String randomIp = scanner.nextLine();
        
        System.out.println("Enter the port provided by the server:");
        int randomPort = scanner.nextInt(); scanner.nextLine(); // Consume the newline character left after nextInt()

        // Initialize and start the client thread
        Client client = new Client("127.0.0.1", 9999, randomIp, randomPort);
        Thread clientThread = new Thread(client);
        clientThread.start();

        // Start a separate thread to handle user input
        Thread inputThread = new Thread(client.new InputHandler());
        inputThread.start();

        try {
            // Wait for the client processing thread to finish
            clientThread.join();
            // Ensure the input thread is stopped if still running
            inputThread.interrupt();
        } catch (InterruptedException e) {
            System.err.println("Client application interrupted.");
        } finally {
            scanner.close();
        }
    }
}