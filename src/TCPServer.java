import java.io.*;
import java.net.*;

public class TCPServer {
    protected static final int numOfBytes=100;//will be used to specify number of bytes to be read(at client side too)
    public static void main(String[] args) throws Exception {
        //if port is not provided
        if(args.length<1) {
            System.out.println("Please enter a port number when running.");
            return;}

        //extract port number from main argument
        int port=Integer.parseInt(args[0]);

        System.out.println("Server up and running, waiting for requests....");

        String capitalizedSentence; // text to send to client

        // create welcoming socket at port and include in try so it throws exception if port not provided
        try(ServerSocket welcomeSocket = new ServerSocket(port)) {

            int clientId = 0;

            while(true) {
                // wait on welcoming socket for contact by client, creates a new socket
                Socket connectionSocket = welcomeSocket.accept();
                clientId++;
                System.out.println("Processing client " + clientId);

                //create a new thread server for this client
                new ServerThread(connectionSocket).start();

            } // loop back and wait for another client connection
        }catch(IOException ex){
            System.out.println("Server exception "+ex.getMessage());
        }
    }


}