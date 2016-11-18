import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

public class Ball {

	private double pos_x;
	private double pos_y;
	private double angle;
	
	private final static int width = 15;
	private final static int height = 15;
	
	static Image img = new ImageIcon("res/"+"ball.png").getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
	
	public Ball(){
		pos_x = 600;
		pos_y = 160;
		angle = -10;
	}
	public Ball(int x, int y, int an){
		pos_x = x;
		pos_y = y;
		angle = an;
	}
	public Ball(double x, double y, double an){
		pos_x = x;
		pos_y = y;
		angle = an;
	}
	
	public void checkCollisionWithPlatform(Platform platform){
		if(getPos_y()+getheight() >= platform.getPos_y() && getPos_y() <= platform.getPos_y()+platform.getHeight()){
			if(getPos_x()+getwidth()>=platform.getPos_x() && getPos_x() <= platform.getPos_x()+platform.getWidth()){
					
				double left = platform.getPos_x()-width/2;
				double right = platform.getPos_x() + platform.getWidth() + width/2;
				double length = right - left;
				
				double part = getCenterX() - left;
				
				double ang = (part / length)*160 + 190;					//angle between 10 and 170 degrees
				
				angle = ang;
			}
		}
	}
	public void checkCollisionWithBlocks(Vector<Block> blocks){
		for(int i=0; i<blocks.size(); ++i){
			
			if(getPos_y()+getheight() >= blocks.get(i).getPos_y() && getPos_y() <= blocks.get(i).getPos_y()+blocks.get(i).getHeight())
				if(getPos_x()+getwidth()>=blocks.get(i).getPos_x() && getPos_x() <= blocks.get(i).getPos_x()+blocks.get(i).getWidth()){
					
					if(getPos_x()+getwidth() -8> blocks.get(i).getPos_x() && getPos_x()<blocks.get(i).getPos_x()+blocks.get(i).getWidth()-8){
						
						setAngle(((-getAngle()) % 360));
					}
					else{
						setAngle(((180-getAngle()) % 360));
					}
					blocks.remove(i);
					break;
				}
		}
	}
	
	public boolean checkCollisionWithBorders(){		//return true if ball has to be removed
		if(pos_x+width>=Board.BOARD_WIDTH_PIX) {
			setAngle(((180-angle) % 360));
			setPos_x(Board.BOARD_WIDTH_PIX-width);
		}
		else if(pos_x<0) {
			setAngle(((180-angle) % 360));
			setPos_x(0);
		}
		
		if(pos_y>=Board.BOARD_HEIGHT_PIX){
			
			return true;
		}
		else if(pos_y<0){
			setAngle(((-angle) % 360));
			setPos_y(0);
		}
		
		return false;
		
	}
	public void checkCollisionWithBordersAndMenu(Vector<Block> MenuBlocks){
		if(getPos_x()+getwidth()>=Board.BOARD_WIDTH_PIX) {
			setAngle(((180-getAngle()) % 360));
			setPos_x(Board.BOARD_WIDTH_PIX-getwidth());
		}
		else if(getPos_x()<0) {
			setAngle(((180-getAngle()) % 360));
			setPos_x(0);
		}
		
		if(getPos_y()+getheight()>=Board.BOARD_HEIGHT_PIX){
			setAngle(((-getAngle()) % 360));
			setPos_y(Board.BOARD_HEIGHT_PIX-getheight());
		}
		else if(getPos_y()<0){
			setAngle(((-getAngle()) % 360));
			setPos_y(0);
		}
		
		for(int i=0; i<MenuBlocks.size(); ++i){
			Block bl = MenuBlocks.get(i);
			
			if(getPos_y()+getheight() >= bl.getPos_y() && getPos_y() <= bl.getPos_y()+bl.getHeight())
				if(getPos_x()+getwidth()>=bl.getPos_x() && getPos_x() <= bl.getPos_x()+bl.getWidth()){
					
					if(getPos_x()+getwidth() -8> bl.getPos_x() && getPos_x()<bl.getPos_x()+bl.getWidth()-8){
						
						setAngle(((-getAngle()) % 360));
					}
					else{
						setAngle(((180-getAngle()) % 360));
					}
					//blocks.remove(i);
					//break;
				}
		}
	}

	public double getCenterX(){
		return pos_x+width/2;
	}
	
	public double getPos_x() {
		return pos_x;
	}

	public double getPos_y() {
		return pos_y;
	}

	public double getAngle() {
		return angle;
	}

	public void setPos_x(double pos_x) {
		this.pos_x = pos_x;
	}

	public void setPos_y(double pos_y) {
		this.pos_y = pos_y;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	public int getwidth(){
		return width;
	}
	public int getheight(){
		return height;
	}
	
}
