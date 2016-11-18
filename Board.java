import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;




/**
 * 
 * @author Maciej Ruszczyk
 * 
 */


public class Board extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	
	private final static double PIXELS_PER_SECONDS=400;	
	//private final static double PIXELS_FOR_BULLETS=1000;
	//private final static double PIXELS_FOR_GIFTS=200;
	
	public final static int BOARD_WIDTH_PIX = 1024;
	public final static int BOARD_HEIGHT_PIX = 820;
	protected final static int WIDTH_KLOCKA = 32;
	protected final static int HEIGHT_KLOCKA = 32;
	
	public static final int MAX_FPS = 120;
	private double averageFPS;
	
	
	Adapter adap;
	MouseAdapter MouseAdapter;
	MouseListenerAdapter MouseListenerAdapter;

	boolean GameOver = false;
	long Time_end_round;
	Vector<Ball> ball;
	
	Platform platform;
	
	Vector<Block> blocks;
	
	Random random;
	
	boolean isRunning = false;
	boolean isActive = false;
	
	private enum OptionMenu{Nothing, Play, Load_Game, Save_Game,  Exit};
	OptionMenu MouseEnteredOption = OptionMenu.Nothing; 
	Vector<Ball> ballsInMenu;
	
	boolean isPaused = false;
	
	Loader loader;
	
	Vector<Block> MenuBlocks;
	
	Thread thread;
	
	JLabel fpsBar;
	
	
	public Board(MyFrame frejm) {
		
		
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setPreferredSize(new Dimension(BOARD_WIDTH_PIX, BOARD_HEIGHT_PIX));
		
		
		adap = new Adapter();	
		this.addKeyListener(adap);
		MouseAdapter = new MouseAdapter();
		this.addMouseMotionListener(MouseAdapter);
		MouseListenerAdapter = new MouseListenerAdapter();
		this.addMouseListener(MouseListenerAdapter);
		
		
		ball = new Vector<Ball>();
		ballsInMenu = new Vector<Ball>();
		random = new Random();
		
		
		for(int i=0; i<10; ++i){
			int x = random.nextInt(BOARD_WIDTH_PIX);
			int y = random.nextInt(BOARD_HEIGHT_PIX-10);
			int an = random.nextInt(360);
			ball.add(new Ball(x, y, an));
		}
		for(int i=0; i<10; ++i){
			int x = random.nextInt(BOARD_WIDTH_PIX);
			int y = random.nextInt(BOARD_HEIGHT_PIX-10);
			int an = random.nextInt(360);
			ballsInMenu.add(new Ball(x, y, an));
		}
		
		platform = new Platform();
		
		blocks = new Vector<Block>();
		
		
		
		for(int i=0; i<10; ++i){
			blocks.add(new Block(i*50, 100, 50));
			blocks.add(new Block(500+ i*40, 200, 40));
		}
		
		loader = new Loader();
		
		MenuBlocks = new Vector<Block>();
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 180, 120, 30));
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 280, 120, 30));
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 380, 120, 30));
		MenuBlocks.add(new Block(BOARD_WIDTH_PIX/2-50, 480, 120, 30));
		
		fpsBar = frejm.getfpsBar();
		
		thread = new Thread(this);
		isActive = true;
		thread.start();
	}
	
	
	private void ball_serve(double przes){
		
		for(int j=0; j<ball.size(); ++j){	
			Ball b = ball.get(j);
			if(b==null) continue;
			
			double przes_x = Math.cos(Math.toRadians(b.getAngle())) * przes;
			double przes_y = Math.sin(Math.toRadians(b.getAngle())) * przes;
			
			b.setPos_x(b.getPos_x()+przes_x);
			b.setPos_y(b.getPos_y()+przes_y);
			
			if(b.checkCollisionWithBorders()){
				ball.remove(j);
				continue;
			}
			
			b.checkCollisionWithPlatform(platform);
			b.checkCollisionWithBlocks(blocks);
		}
	}
	private void ballServeInMenu(double przes){
		
		for(Ball b:ballsInMenu){
			double przes_x = Math.cos(Math.toRadians(b.getAngle())) * przes;
			double przes_y = Math.sin(Math.toRadians(b.getAngle())) * przes;
			
			b.setPos_x(b.getPos_x()+przes_x);
			b.setPos_y(b.getPos_y()+przes_y);
			
			b.checkCollisionWithBordersAndMenu(MenuBlocks);
		}
	}
	private void platformServe(double przes){
		if(platform.press[0] == Platform.Direction.Left){
			platform.moveLeft(przes);
		}
		else if(platform.press[1] == Platform.Direction.Right){
			platform.moveRight(przes);
		}
	}
	
	private void drawBall(Graphics g){
		for(Ball b:ball)
			g.drawImage(Ball.img, (int)b.getPos_x(), (int)b.getPos_y(), null);
	}
	private void drawPlatform(Graphics g){
		g.setColor(Color.red);
		g.fillRect((int)platform.getPos_x(), (int)platform.getPos_y(), 80, 10);
	}
	private void drawBlocks(Graphics g){
		
		
			for(Block block:blocks){
				g.setColor(Color.black);
				g.drawRect((int)block.getPos_x(), (int)block.getPos_y(), block.getWidth(), block.getHeight());
				g.setColor(Color.green);
				g.fillRect((int)block.getPos_x()+1, (int)block.getPos_y()+1, block.getWidth()-1, block.getHeight()-1);
			}
		
		//repaint();
			//g.fillRect((int)block.getPos_x(), (int)block.getPos_y(), block.getWidth(), block.getHeight());
	}
	private void drawMenu(Graphics g){
		g.setColor(Color.black);
		g.drawString("ARKANOID", 490, 100);
	
		
		g.drawRect(BOARD_WIDTH_PIX/2-50, 180, 120, 30);
		g.drawRect(BOARD_WIDTH_PIX/2-50, 280, 120, 30);
		g.drawRect(BOARD_WIDTH_PIX/2-50, 380, 120, 30);
		g.drawRect(BOARD_WIDTH_PIX/2-50, 480, 120, 30);
		
		g.drawString("Play", BOARD_WIDTH_PIX/2, 200);
		g.drawString("Load Game", BOARD_WIDTH_PIX/2-18, 300);
		g.drawString("Save Game", BOARD_WIDTH_PIX/2-18, 400);
		g.drawString("Exit", BOARD_WIDTH_PIX/2, 500);
		
		if(MouseEnteredOption == OptionMenu.Nothing){
			//g.drawRect(BOARD_WIDTH_PIX/2-50, 180, 120, 30);
			//g.drawRect(BOARD_WIDTH_PIX/2-50, 280, 120, 30);
		}
		else if(MouseEnteredOption == OptionMenu.Play){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 180, 120, 30);
		}
		else if(MouseEnteredOption == OptionMenu.Load_Game){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 280, 120, 30);
		}
		else if(MouseEnteredOption == OptionMenu.Save_Game){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 380, 120, 30);
		}
		else if(MouseEnteredOption == OptionMenu.Exit){
			g.setColor(Color.red);
			g.drawRect(BOARD_WIDTH_PIX/2-50, 480, 120, 30);
		}

	}
	private void drawBallsInMenu(Graphics g){
		
		for(Ball b:ballsInMenu){
		//	g.drawRect((int)b.getPos_x(), (int)b.getPos_y(), 10, 10);
			g.drawImage(Ball.img, (int)b.getPos_x(), (int)b.getPos_y(), null);
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		synchronized (this) {
			if(isRunning){
				drawBall(g);
				drawPlatform(g);
				drawBlocks(g);
				
				if(isPaused){
					g.setColor(Color.black);
					g.drawString("Pause", 500, 400);
					//repaint();
				}
			}
			else{
				
				drawMenu(g);
				drawBallsInMenu(g);
			}
			//g.drawImage(Map.bg, 0, 0, null);
		}
		
		repaint();
}
	
	
//tylko obsluga gracza nr 1
	class Adapter implements KeyListener{
		
		
		public void keyPressed(KeyEvent e) {
			
			//char keyCode = e.getKeyChar();
			int keyCode = e.getKeyCode();
			//System.out.println(keyCode);
			
			switch(keyCode){
		//	case KeyEvent.VK_SPACE:		
			case KeyEvent.VK_LEFT: platform.press[0] = Platform.Direction.Left; break;
			case KeyEvent.VK_RIGHT: platform.press[1] = Platform.Direction.Right; break;
			case KeyEvent.VK_SPACE: isPaused = false; break;
			case KeyEvent.VK_P: isPaused = true; break;
		//	case KeyEvent.VK_UP: if(!player[0].isJumping) player[0].jump(); break;
		//	case KeyEvent.VK_LEFT: player[0].dir=player[0].look=Direction.Left; player[0].isMoving=true; player[0].press[2] = true; break;
		//	case KeyEvent.VK_RIGHT: player[0].dir=player[0].look=Direction.Right;player[0].isMoving=true;player[0].press[3] = true; break;
		
			case KeyEvent.VK_ESCAPE: 
				if(!isRunning) System.exit(0); 
				else{
					//stop();
					isRunning = false;
					
				}
				break;	
			// gracz 2
		/*	case KeyEvent.VK_W: if(!player[1].isJumping) player[1].jump(); break;
			case KeyEvent.VK_A: player[1].dir=player[1].look=Direction.Left; player[1].isMoving=true; player[1].press[2] = true; break;
			case KeyEvent.VK_D: player[1].dir=player[1].look=Direction.Right;player[1].isMoving=true;player[1].press[3] = true; break;
			case KeyEvent.VK_G: cannonUpOrDown(Podnoszenie.dol, 1); break;
			case KeyEvent.VK_T: cannonUpOrDown(Podnoszenie.gora, 1); break;
			case KeyEvent.VK_Y: nowy_pocisk(1); break;*/
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();
			
			switch(keyCode){
			
			case KeyEvent.VK_LEFT: platform.press[0] = Platform.Direction.None; break;
			case KeyEvent.VK_RIGHT: platform.press[1] = Platform.Direction.None; break;
			}
			//if(press[0] == true && keyCode=='w'){
			//	press[0]=false;
			//}
			/*if(press[1] == true && keyCode=='s'){
				press[1]=false;
			}*/
		//	if(player[0].press[2] == true && keyCode==KeyEvent.VK_LEFT){
		//		player[0].press[2]=false;
				//player.isMoving = false;
			//}
		//	if(player[0].press[3] == true && keyCode==KeyEvent.VK_RIGHT){
		//		player[0].press[3]=false;
				//player.isMoving = false;
		//	}
			
		
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
			
		}
			
		
	}
	
	
	
	class MouseAdapter implements MouseMotionListener{
		

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
			if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=180 && e.getY() <= 180+30){
			
				MouseEnteredOption = OptionMenu.Play;
			}
			else if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=280 && e.getY() <= 280+30){
			
				MouseEnteredOption = OptionMenu.Load_Game;
			}
			else if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=380 && e.getY() <= 380+30){
			
				MouseEnteredOption = OptionMenu.Save_Game;
			}
			else if(e.getX()>=BOARD_WIDTH_PIX/2-50 && e.getX() <= BOARD_WIDTH_PIX/2-50+120 
					&& e.getY()>=480 && e.getY() <= 480+30){
			
				MouseEnteredOption = OptionMenu.Exit;
			}
			else{
				MouseEnteredOption = OptionMenu.Nothing;
			}
			//repaint();
		}
		
	}
	class MouseListenerAdapter implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(MouseEnteredOption==OptionMenu.Play){
				
				System.out.println("Play");
				isRunning = true;
				isPaused = false;
				
				
				
			}
			else if(MouseEnteredOption==OptionMenu.Load_Game){
			
				System.out.println("Load Game");
				loader.loadGame();
				//System.out.println(loader.getPlatform().getPos_x()+"   "+loader.getPlatform().getPos_y());
				
				platform = loader.getPlatform();
				
				ball.clear();
				for(int i=0; i<loader.getBallSize(); ++i){
					ball.add(loader.getBall(i));
				}
				
				blocks.clear();
				for(int i=0; i<loader.getBlockSize(); ++i){
					blocks.add(loader.getBlock(i));
				}
				
				//System.out.println(ball.size());
				
			}
			else if(MouseEnteredOption==OptionMenu.Save_Game){
				System.out.println("Save Game");
				loader.saveGame(platform, ball, blocks);
			}
			else if(MouseEnteredOption==OptionMenu.Exit){
				System.out.println("Exit");
				isActive = false;
				System.exit(0);
			}
			else{
				System.out.println("Nothing");
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void run() {
		
		long startTime;
		long timeMillis;
        long waitTime;
        int frameCount = 0;
        double totalTime = 0;
        long targetTime = 1000/MAX_FPS;
        long lastTimeNano = System.nanoTime();
        
        
        double time_elapsed_in_sec;
		
		while(isActive){
			
			startTime = System.nanoTime();	
			time_elapsed_in_sec = (((double)(startTime - lastTimeNano)))/1000000000;
			lastTimeNano = startTime;
					
			double przes = (((time_elapsed_in_sec)) * PIXELS_PER_SECONDS);  
			
			synchronized(this){
				if(isRunning){
					ball_serve(przes);
					platformServe(przes);
				}
				else{
					ballServeInMenu(przes);
				}
			}
			
			timeMillis = (System.nanoTime() - startTime)/1000000;
			waitTime = targetTime - timeMillis;
				
					if(waitTime>0){
						
						try {
								thread.sleep(waitTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			
			if(frameCount == MAX_FPS){
				averageFPS = 1000/((totalTime/frameCount)/1000000);
				frameCount=0;
				totalTime=0;
				fpsBar.setText(String.valueOf((int)averageFPS));
			}
			
			
		}
		
	}
	

	
	
	
	
}
