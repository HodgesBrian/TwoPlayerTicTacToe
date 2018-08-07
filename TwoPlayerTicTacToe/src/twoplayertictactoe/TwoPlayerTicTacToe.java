/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twoplayertictactoe;

/**
 *
 * @author Brian
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener; 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class TwoPlayerTicTacToe {

    public JFrame frame;
    public JLabel messageLabel;
    private String currPlayer = "X";
    private String PlayerX = "X";
    private String PlayerO = "O";
    private boolean hasWinner;
    public JLabel setPlayer;
    public JLabel oponentPlayer;
    public JButton[] board = new JButton[9];
    public int playerMoveCount = 0;
    //public JButton pgb = new JButton();
    private JButton currJButton = new JButton();
    
    private static int PORT;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    //private JFrame frame = new JFrame("Capitalize Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);
    
    private boolean yourTurn = false;
    private boolean circle = true;
    private String[] spaces = new String[9];
    

    
    public TwoPlayerTicTacToe(String serverAddress) throws Exception  
    {
        socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);        
            for (int i = 0; i < 1; i++){
                messageArea.append(in.readLine() + "\n");
            }
            
        
      frame = new JFrame("Tic Tac Toe");
      messageLabel = new JLabel("Messages");
      messageLabel.setBackground(Color.lightGray);
      //frame.getContentPane().add(messageLabel, "South"); 
        messageArea.setEditable(false);
        messageArea.setBackground(Color.lightGray);
        //messageLabel = new JLabel("Messages");
        JPanel messageBox = new JPanel();
        //messageBox.setLayout(new GridLayout(2,1));
        messageBox.add(new JScrollPane(messageArea));
        //messageBox.add(dataField);
                dataField.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        out.println(dataField.getText()); 

                    }
                });
                frame.add(messageBox, "South");

      
        //frame.setLayout(new BorderLayout());
         
        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.ORANGE);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (int i = 0; i < board.length; i++)
        {
            final int j = i;
            board[i] = new JButton()
            {{
                setBackground(Color.black);
                setForeground(Color.RED);
            }};
            //board[i] = pgb;
            board[i].addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    
                    //board[j].setText(currPlayer);
                    currJButton = board[j];
                    currJButton.setText(currPlayer);
                    out.println("MOVE " + j);
                    //switchPlayer();
                }

            });
            boardPanel.add(board[i]);
        }  
        frame.getContentPane().add(boardPanel, "Center");
        //frame.revalidate();
        //frame.repaint(); 
    } 
    
         public void play() throws Exception, IOException{
         String response;
         try{

             response = in.readLine();

             if(response.startsWith ("WELCOME")) 
             {
               char mark = response.charAt(8);
               currJButton.setText(currPlayer);
               //board.setText(currPlayer);
               frame.setTitle("TicTacToe " + mark);
               //switchPlayer(); 
             } while (true)
             {
                 response = in.readLine();
                 messageArea.append(response + "\n");
                 dataField.selectAll();
                 
                 if (response.startsWith("VALID_MOVE"))
                 {
                     
                     messageLabel.setText("Valid move, please wait.");
                     currJButton.equals(currPlayer);
                     currJButton.repaint();
                     switchPlayer();
                 }
                 else if (response.startsWith("OPPONENT_MOVED"))
                 {
                     
                     int loc = Integer.parseInt (response.substring(15));
                     board[loc].setText(currPlayer);
                     board[loc].repaint();
                     messageLabel.setText("Opponent moved, your turn.");
                     switchPlayer();
                 }
                 else if (response.startsWith("VICTORY"))
                 {
                     messageLabel.setText("You Win!");
                     break;
                 }
                 else if (response.startsWith("DEFEAT"))
                 {
                     messageLabel.setText("You Lose!");
                     break;
                 }
                 else if (response.startsWith("TIE"))
                 {
                     messageLabel.setText("Tie Game!");
                     break;
                 }
                 else if (response.startsWith("VICTORY"))
                 {
                     messageLabel.setText("You Win!");
                     break;
                 }
                 else if (response.startsWith("MESSAGE"))
                 {
                     messageLabel.setText(response.substring(8));
                 }
             } 
             out.println("QUIT");
         } finally 
         {
             socket.close();
         }
     }
         
    //Switches the players
    private void switchPlayer()
    {
        if (currPlayer == "X")
            currPlayer = "O";
        else if (currPlayer == "O")
            currPlayer = "X";
    }
    
    private boolean wantToPlayAgain()
    {
        int response = JOptionPane.showConfirmDialog(frame,
                "What to play again?",
                "TicTacToe is fun fun",
                JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception 
    {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        TwoPlayerTicTacToe client = new TwoPlayerTicTacToe(serverAddress);
        //client.myButtons();
        //client.connectToServer();
        //client.initializeMessages();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(800, 600);
        client.frame.setVisible(true);
        client.frame.setResizable(false);
        client.play();
        if(!client.wantToPlayAgain()) 
        {
           
        } 
        //while (true){client.serverListener();}  
    }
    
}
