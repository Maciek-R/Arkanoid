
public class Block {

	private double pos_x;
	private double pos_y;
	
	private int width;
	private int height = 20;
	
	
	public Block(double pos_x, double pos_y, int width){
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
	}
	
	public Block(double pos_x, double pos_y, int width, int height){
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;
	}
	
	
	
	public double getPos_x() {
		return pos_x;
	}
	public double getPos_y() {
		return pos_y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	
}
