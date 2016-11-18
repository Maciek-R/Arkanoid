import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Main {

	public static void main(String[] args) {
		
			EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				MyFrame frame = new MyFrame();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				
				
			}
		});

	}

}

class MyFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2679927640786318568L;
	
	
	private JLabel fpsBar;
	
	public MyFrame(){
		
				fpsBar = new JLabel("0");
				add(fpsBar, BorderLayout.SOUTH);
		
				setTitle("Arkanoid");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setLocationRelativeTo(null);
				
				Board board = new Board(this);
				add(board);
				pack();
				
			
	}
	public JLabel getfpsBar(){
		return fpsBar;
	}
}