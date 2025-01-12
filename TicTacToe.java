import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 * 
 * @author Hugo Ngo.
 *      Student Number 101291485
 * @version April 8, 2024
 */

public class TicTacToe
{
   public static final String PLAYER_X = "X"; // player using "X"
   public static final String PLAYER_O = "O"; // player using "O"
   public static final String EMPTY = " ";  // empty cell
   public static final String TIE = "T"; // game ended in a tie
 
   private String player;   // current player (PLAYER_X or PLAYER_O)

   private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

   private int numFreeSquares; // number of squares still free
   
   private JButton[][] buttons; // 3x3 array representing the board
   
   private JFrame frame;
   
   private JLabel statusLabel;
   
   private JLabel scoreLabel;
   /* The reset menu item */
   private JMenuItem newItem;
   
   /* The quit menu item */
   private JMenuItem quitItem;
   
   private JMenuItem nightModeItem;
   
   private int nightmode = 0;
   
   private int x_win = 0;
   private int o_win = 0;
   private int tie = 0;
   AudioClip click;
   private JButton zomButton;
   private static ImageIcon icon = new ImageIcon("SCE.png");
   /** 
    * Constructs a new Tic-Tac-Toe board.
    */
   public TicTacToe()
   {    
       initializeGUI();
        clearBoard();
    }

    /**
     * Initializes the GUI components.
     */
    private void initializeGUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setPreferredSize(new Dimension(470,450));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 3));
        
        buttons = new JButton[3][3];
        ButtonClick bClick = new ButtonClick();
        

        // Create buttons and add them to the frame
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 80));
                buttons[i][j].addActionListener(bClick); 
                buttons[i][j].setBackground(Color.YELLOW);
                frame.add(buttons[i][j]);
                
            }
        }
        
      JMenuBar menubar = new JMenuBar();
      frame.setJMenuBar(menubar); // add menu bar to our frame

      JMenu fileMenu = new JMenu("Options"); // create a menu
      menubar.add(fileMenu); // and add to our menu bar

      newItem = new JMenuItem("New Game"); // create a menu item called "Reset"
      fileMenu.add(newItem); // and add to our menu
      
      nightModeItem = new JMenuItem("Night Mode"); // create a menu item called "Reset"
      fileMenu.add(nightModeItem);      
    
      quitItem = new JMenuItem("Quit"); // create a menu item called "Quit"
      fileMenu.add(quitItem); // and add to our menu
      
      // this allows us to use shortcuts (e.g. Ctrl-R and Ctrl-Q)
      final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(); // to save typing
      newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
      quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
      nightModeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, SHORTCUT_MASK));
      // listen for menu selections
      
      newItem.addActionListener(new ActionListener() // create an anonymous inner class
        { 
            public void actionPerformed(ActionEvent event)
            {
                clearBoard();
            }
        } // end of anonymous subclass
      );  // create an anonymous inner class
      quitItem.addActionListener(new ActionListener() // create an anonymous inner class
        { 
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0); // quit
            }
        } // end of anonymous subclass
      ); 
      nightModeItem.addActionListener(new ActionListener() // create an anonymous inner class
        { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
                if (nightmode == 0){
                    frame.getContentPane().setBackground(Color.BLACK);
                    statusLabel.setForeground(Color.WHITE);
                    scoreLabel.setForeground(Color.WHITE);
                    nightmode = 1;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setBackground(Color.GRAY);
                            buttons[i][j].setForeground(Color.MAGENTA); // Set text color
                        }
                    }
                }
                else{
                    frame.getContentPane().setBackground(Color.WHITE);
                    statusLabel.setForeground(Color.BLACK);
                    scoreLabel.setForeground(Color.BLACK);
                    nightmode = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setBackground(Color.YELLOW);
                            buttons[i][j].setForeground(Color.BLACK); // Set text color
                        }
                    }
                }
            }
            
        } // end of anonymous subclass
      );    
        statusLabel = new JLabel("   Game start!");
        statusLabel.setVerticalAlignment(JLabel.CENTER);
        frame.add(statusLabel);
        
        scoreLabel = new JLabel("X: "+x_win+"   |   O: "+ o_win+"   |   Tie: "+tie);
        scoreLabel.setVerticalAlignment(JLabel.TOP);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(scoreLabel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));
        zomButton = new JButton();
        zomButton.setIcon(icon);
        zomButton.addActionListener(new ActionListener() // create an anonymous inner class
        { 
            public void actionPerformed(ActionEvent event)
            {
                statusLabel.setText("Zombie Apocolypse!!!");
                scoreLabel.setText("BRAINNNN!");
                URL urlClick = TicTacToe.class.getResource("zom.wav"); // beep
                click = Applet.newAudioClip(urlClick);
                click.play(); // just plays clip once
            }
        }); // end of anonymous subclass
        frame.add(zomButton);
        
        frame.pack();
        frame.setVisible(true);
    }

   /**
    * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
    * and indicates no winner yet, 9 free squares and the current player is player X.
    */
   private void clearBoard()
   {
      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            buttons[i][j].setText(EMPTY);
         }
      }
      winner = EMPTY;
      numFreeSquares = 9;
      player = PLAYER_X;     // Player X always has the first turn.
   }

   /**
    * Add Action to Click 
    */
   private class ButtonClick implements ActionListener
    {@Override
        
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        
        if (!button.getText().equals(EMPTY) || !winner.equals(EMPTY))
            return; // Ignore if button already marked or game over

        button.setText(player);
        numFreeSquares--;
        scoreLabel.setText("X: "+x_win+"   |   O: "+ o_win+"   |   Tie: "+tie);
        // Check if game is over
        if (haveWinner() || numFreeSquares == 0) {
            endGame();
        } else {
            // Change player
            player = (player.equals(PLAYER_X)) ? PLAYER_O : PLAYER_X;
            statusLabel.setText("Player "+ player +"'s turn.");
        }
        
        URL urlClick = TicTacToe.class.getResource("beep-08b.wav"); // beep
        click = Applet.newAudioClip(urlClick);
        click.play(); // just plays clip once
    
    }
                   
    
   /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise.
    *
    *  
    * 
    * @return true if we have a winner, false otherwise
    */
   private boolean haveWinner() 
   {
       // Check rows
        for (int i = 0; i < 3; i++) {
            if (!buttons[i][0].getText().equals(EMPTY) && buttons[i][0].getText().equals(buttons[i][1].getText())
                    && buttons[i][0].getText().equals(buttons[i][2].getText())) {
                winner = buttons[i][0].getText();
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!buttons[0][j].getText().equals(EMPTY) && buttons[0][j].getText().equals(buttons[1][j].getText())
                    && buttons[0][j].getText().equals(buttons[2][j].getText())) {
                winner = buttons[0][j].getText();
                return true;
            }
        }

        // Check diagonals
        if (!buttons[0][0].getText().equals(EMPTY) && buttons[0][0].getText().equals(buttons[1][1].getText())
                && buttons[0][0].getText().equals(buttons[2][2].getText())) {
            winner = buttons[0][0].getText();
            return true;
        }
        
        if (!buttons[0][2].getText().equals(EMPTY) && buttons[0][2].getText().equals(buttons[1][1].getText())
                && buttons[0][2].getText().equals(buttons[2][0].getText())) {
            winner = buttons[0][2].getText();
            return true;
        }
        return false;
   }
  
    
   /**
    * Returns a string representing the current state of the game.  This should look like
    * a regular tic tac toe board, and be followed by a message if the game is over that says
    * who won (or indicates a tie).
    *
    * @return String representing the tic tac toe game state
    */
    public void endGame() 
    {
    // Append the game outcome message if the game is over
        if (winner == (EMPTY)) {
            winner = TIE;
        }
            
        if (winner == (TIE)) {
            statusLabel.setText("It's a tie!");
            tie+=1;
        } else {
            statusLabel.setText("Player " + winner + " wins!");
            if (winner == "X"){
                x_win += 1;
            }
            else{
                o_win += 1;
            }
        }
        scoreLabel.setText("X: "+x_win+"   |   O: "+ o_win+"   |   Tie: "+tie);
    }
    
}
}

