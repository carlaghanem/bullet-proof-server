import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class ClientPanel extends JPanel{
    private final int WIDTH = 1300, HEIGHT = 500;
    private JButton pshButton;
    private JTextField t;
    private JLabel outputLabel;
    private JLabel title;

    String sentence; // data to send to server
    String modifiedSentence=""; // received from server
    String hostname;
    int port;
    //panel constructor
    public ClientPanel(int port,String hostname) throws IOException {

        this.port=port;
        this.hostname=hostname;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3);

        title = new JLabel("Text to Uppercase Converter");
        title.setFont(new Font("Monospaced", Font.BOLD , 40));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setPreferredSize(new Dimension(200,40));
        gbc.gridwidth=3;
        gbc.gridx=0;
        gbc.gridy=0;
        add(title,gbc);


        t = new JTextField(" ",80);
        t.setToolTipText("Please enter some text");
        t.setHorizontalAlignment(JTextField.CENTER);
        t.setFont(new Font("Arial",Font.ITALIC|Font.BOLD,16));
        t.setBackground(Color.yellow);
        gbc.insets = new Insets(15,0,0,0);
        t.setPreferredSize(new Dimension(140,40));
        t.setDocument(new JTextFieldLimit(100));
        gbc.gridy=1;
        gbc.gridx=0;
        gbc.gridwidth=3;
        add(t,gbc);

        pshButton = new JButton("Send Text");
        pshButton.setFont(new Font("Arial", Font.BOLD, 40));
        pshButton.addActionListener(new ButtonPushHandler());
        pshButton.setHorizontalAlignment(JButton.CENTER);
        gbc.insets = new Insets(10,0,0,0);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=3;
        gbc.anchor=GridBagConstraints.CENTER;
        add(pshButton,gbc);


        outputLabel = new JLabel("Output will appear here ");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 16));
        outputLabel.setBorder(border);
        outputLabel.setHorizontalAlignment(JLabel.CENTER);
        outputLabel.setPreferredSize(new Dimension(200,40));
        gbc.gridwidth=3;
        gbc.gridx=0;
        gbc.gridy=3;
        add(outputLabel,gbc);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));


    }


    private class ButtonPushHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            // create client socket and connect to server; this initiates TCP cnx between client and server
            try(Socket clientSocket = new Socket(hostname, port)) {

                // create output stream attached to socket
                OutputStream outToServer = clientSocket.getOutputStream();

                // create input stream attached to socket
                InputStream inFromServer = clientSocket.getInputStream();

                //if user sends empty message, will notify that it is not possible, so rerunning the client is necessary
                if(t.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "No data entered, Please run the client again!");
                }

                //write the bytes to the socket (no need anymore for carriage)
                outToServer.write(t.getText().getBytes());

                // set the text of field to blank
                t.setText("");

                byte[] bufferIn = new byte[TCPServer.numOfBytes]; //byte array to be received
                int n = inFromServer.read(bufferIn); //length of  received modified, read received string bytes into bufferIn array
                for (int i = 0; i < n; i++)
                    modifiedSentence = modifiedSentence + (char) bufferIn[i]; //transforming bytes to string

                //print modified sentence to console
                System.out.println("FROM SERVER: " + modifiedSentence);

                //display the modified text in the output label
                outputLabel.setText(modifiedSentence);

                System.out.println("Client closed the socket and is done execution");

                modifiedSentence = "";

                }catch(UnknownHostException ex){ //if wrong hostname is provided
                    System.out.println("Server not found: " + ex.getMessage());

                } catch (IOException ex) { //wrong port is provided

                    System.out.println("I/O error: " + ex.getMessage());
                }
        }

    }
    class JTextFieldLimit extends PlainDocument {
        private int limit;
        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        JTextFieldLimit(int limit, boolean upper) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }



}
