import java.io.*;
import java.net.*;


public class ServerThread extends Thread {
    private Socket connectionSocket; //connection socket with client
    private String capitalizedSentence; // text to send to client after modification
    public ServerThread(Socket socket) { //constructor
        connectionSocket = socket;
    }

    public void run() {
        try {
            //create input stream attached to socket
            InputStream inFromClient = connectionSocket.getInputStream();
            int n = 0;
            String clientSentence = ""; // text from client
            byte[] bufferIn = new byte[100]; //reading a maximum of 100 bytes from the client

            //read max of 100 bytes and store them in bufferIn byte array
            n = inFromClient.read(bufferIn);

            //transform the bytes into characters to make up the string sent by client
            for (int i = 0; i < n; i++)
                clientSentence = clientSentence + (char) bufferIn[i];

            // create output stream attached to socket
            OutputStream outToClient = connectionSocket.getOutputStream();

            // change the received sentence to uppercase
            capitalizedSentence = clientSentence.toUpperCase();

            //transform the capitalized sentence(String) to bytes to send back to client
            byte[] bufferOut = capitalizedSentence.getBytes();

            //write out line to socket
            outToClient.write(bufferOut);

            System.out.println("Done with client");
        } catch (IOException ex) { //if the port number is wrong or not provided
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}