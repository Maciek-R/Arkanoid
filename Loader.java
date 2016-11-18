import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Loader {

	private static final String BallsFileName    = "balls.txt";
	private static final String BlocksFileName   = "blocks.txt";
	private static final String PlatformFileName = "platform.txt";
	
	private Platform platform;
	private Vector<Ball> balls;
	private Vector<Block> blocks;
	
	
	public void loadGame(){
		
		FileReader fr = null;
		BufferedReader reader = null;
		String line;
		
		try {
			fr = new FileReader(new File("res/"+PlatformFileName));
			reader = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			System.out.println("There is no saved game");
		}
		
		try {
			line = reader.readLine();
			String[] currentLine = line.split(" ");
			
			platform = new Platform();
			platform.setPos_x(Double.parseDouble(currentLine[0]));
			//platform.setPos_y(Double.parseDouble(currentLine[1]));
			
			reader.close();
			fr.close();
		} 
		catch(Exception e){
			System.out.println("File platform.txt does not work");
		}
	
		
		try {
			fr = new FileReader(new File("res/"+BallsFileName));
			reader = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			System.out.println("There is no saved game");
		}
		
		try {
			balls = new Vector<Ball>();
			
			while(true){
				line = reader.readLine();
				if(line==null) break;
				
				String[] currentLine = line.split(" ");
				
				balls.add (new Ball(Double.parseDouble(currentLine[0]), 
									Double.parseDouble(currentLine[1]), 
									Double.parseDouble(currentLine[2])));
				
			}
			reader.close();
			fr.close();
		} 
		catch(Exception e){
			System.out.println("File balls.txt does not work");
		}
		
		try {
			fr = new FileReader(new File("res/"+BlocksFileName));
			reader = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			System.out.println("There is no saved game");
		}
		
		try {
			blocks = new Vector<Block>();
			
			while(true){
				line = reader.readLine();
				if(line==null) break;
				
				String[] currentLine = line.split(" ");
				
				blocks.add (new Block(Double.parseDouble(currentLine[0]), 
									Double.parseDouble(currentLine[1]), 
									Integer.parseInt(currentLine[2])));
				//platform.setPos_y(Double.parseDouble(currentLine[1]));
			}
			reader.close();
			fr.close();
		} 
		catch(Exception e){
			System.out.println("File blocks.txt does not work");
		}
		
	}
	
	public void saveGame(Platform platform, Vector<Ball> balls, Vector<Block> blocks){
		FileWriter fw = null;
		BufferedWriter writer = null;
		
		try {
			fw = new FileWriter(new File("res/"+PlatformFileName));
			writer = new BufferedWriter(fw);
			
			String out = new String(new Integer((int)platform.getPos_x()).toString());
			writer.write(out);
			
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fw = new FileWriter(new File("res/"+BallsFileName));
			writer = new BufferedWriter(fw);
			String out = null;
			
			for(Ball b:balls){
				out = new String(new Integer((int) b.getPos_x()).toString());
				out += " ";
				out += new String(new Integer((int) b.getPos_y()).toString());
				out += " ";
				out += new String(new Integer((int) b.getAngle()).toString());
				writer.write(out);
				writer.newLine();
			}
			
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fw = new FileWriter(new File("res/"+BlocksFileName));
			writer = new BufferedWriter(fw);
			String out = null;
			
			for(Block b:blocks){
				out = new String(new Integer((int) b.getPos_x()).toString());
				out += " ";
				out += new String(new Integer((int) b.getPos_y()).toString());
				out += " ";
				out += new String(new Integer((int) b.getWidth()).toString());
				writer.write(out);
				writer.newLine();
			}
			
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Platform getPlatform(){
		return platform;
	}
	public Ball getBall(int i){
		return balls.get(i);
	}
	public int getBallSize(){
		return balls.size();
	}
	public Block getBlock(int i){
		return blocks.get(i);
	}
	public int getBlockSize(){
		return blocks.size();
	}
}
