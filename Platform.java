
public class Platform {

	private double pos_x;
	private double pos_y;
	
	private int height = 10;
	private int width = 80;
	
	public enum Direction {None, Left, Right};
	
	public Direction[] press = new Direction[]{Direction.None, Direction.None};//Left Right
	
	public Platform(){
		pos_x = 512;
		pos_y = 780;
	}
	
	public void moveLeft(double przes){
		double newPos_X = pos_x - przes;
		if(newPos_X < 0)
			pos_x = 0;
		else
			pos_x = newPos_X;
			
	}
	public void moveRight(double przes){
		double newPos_X = pos_x + przes;
		if(newPos_X + width >= Board.BOARD_WIDTH_PIX)
			pos_x = Board.BOARD_WIDTH_PIX - width;
		else
			pos_x = newPos_X;
	}
	
	public double getPos_x() {
		return pos_x;
	}
	public double getPos_y() {
		return pos_y;
	}
	public void setPos_x(double pos_x) {
		this.pos_x = pos_x;
	}
	public void setPos_y(double pos_y) {
		this.pos_y = pos_y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
	
}
