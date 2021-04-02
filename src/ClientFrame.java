import javax.swing.JFrame;

public class ClientFrame {

    public static void main(String[] args) throws Exception{

        //throws error if port and hostname not provided
        if(args.length<2){
            System.out.println("Port number or host name not provided.");
            return;}

        //extract hostname and port number from main arguments
        String hostname=args[1];
        int port=Integer.parseInt(args[0]);

        String sentence; // data to send to server
        String modifiedSentence=""; // received from server

        System.out.println("Client is running, enter some text:");

        JFrame frame = new JFrame("Client Side");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new ClientPanel(port,hostname));
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
    }

}