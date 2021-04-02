import java.io.*;
import java.net.*;

public class TCPClient{

    public static void main(String[]args)throws Exception{

        //throws error if port and hostname not provided
        if(args.length<2) {
            System.out.println("Port number or host name not provided.");
            return;}

        //extract hostname and port number from main arguments
        String hostname=args[1];
        int port=Integer.parseInt(args[0]);

        String sentence; // data to send to server
        String modifiedSentence=""; // received from server

        System.out.println("Client is running, enter some text:");

        // create input stream, client reads line from standard input
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // create client socket and connect to server; this initiates TCP cnx between client and server
        try(Socket clientSocket = new Socket(hostname, port)){

            // create output stream attached to socket
            OutputStream outToServer= clientSocket.getOutputStream();

            // create input stream attached to socket
            InputStream inFromServer= clientSocket.getInputStream();

            sentence = inFromUser.readLine(); // read line from user
            outToServer.write(sentence.getBytes()); //write the bytes to the socket (no need anymore for carriage)

            byte[] bufferIn = new byte[TCPServer.numOfBytes]; //byte array to be received
            int n = inFromServer.read(bufferIn); //length of  received modified, read received string bytes into bufferIn array
            for(int i=0;i<n;i++)
                modifiedSentence = modifiedSentence+(char)bufferIn[i]; //transforming bytes to string

            //print modified sentence
            System.out.println("FROM SERVER: " + modifiedSentence);

            clientSocket.close(); // close TCP cnx between client and server

            System.out.println("Client closed the socket and is done execution");}

        catch(UnknownHostException ex){ //if wrong hostname is provided
            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) { //wrong port is provided

            System.out.println("I/O error: " + ex.getMessage());
        }
    }

}
