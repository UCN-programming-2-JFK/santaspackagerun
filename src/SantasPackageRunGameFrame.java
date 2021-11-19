import javax.swing.JFrame;
import javax.swing.RepaintManager;

public class SantasPackageRunGameFrame extends JFrame {

	
	public static void main(String[] args) {
		//direct3d.
		//System.setProperty("sun.java2d.d3d", "True");
		
		GamePanel gamePanel = new GamePanel(); 	// create our panel

		JFrame frame = new JFrame("Xmas 2020"); 	// create a Frame (window)
		frame.setResizable(false); 									// lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// set the X button click to close the window
		frame.setSize(1024, 768); 									// set the size
		frame.setIgnoreRepaint(true);
//		frame.setLocationRelativeTo(null);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//		frame.setUndecorated(true);
		
		frame.getContentPane().add(gamePanel); 					// add our panel
		frame.setVisible(true); 									// show the window
		gamePanel.setFocusable(true);
		gamePanel.grabFocus();
		gamePanel.run(); 								// start the game loop
		//RepaintManager.currentManager(frame).setDoubleBufferingEnabled(true);
		
	}
	
}
