import javax.swing.JFrame;

public class GameFrame extends JFrame {

	
	public static void main(String[] args) {
		GamePanel gamePanel = new GamePanel(); 	// create our panel

		JFrame frame = new JFrame("Xmas 2020"); 	// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(1024, 768); 									// set the size
		frame.getContentPane().add(gamePanel); 					// add our panel
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 									// show the window
		gamePanel.setFocusable(true);
		gamePanel.grabFocus();
		gamePanel.run(); 								// start the game loop
	}
	
}
