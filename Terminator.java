import java.util.ArrayList;
public class Terminator {
	public final int line_size = 4;

	public int width;
	public int height;
	public int[][] table;
	public int depth;

	public Terminator(int width, int height, int depth){
		this.width = width;
		this.height = height;
		this.table = new int[height][width];
		this.depth = depth;
	}

	

	public String toString(){
		String str = "";
		for(int j = 0; j < height; j++){
			str += "[";
			for(int i = 0; i < width; i++){
				if(i != 0) str += " | ";
				str += table[j][i];
			}
			str += "]\n";
		}
		return str;
	}

	public static void main(String[] args){
		Terminator ter = new Terminator(10, 10, 2);
		println(ter.toString());
	}
	public static void println(){
		System.out.println();
	}
	public static void println(String str){
		System.out.println(str);
	}
	public static void print(String str){
		System.out.print(str);
	}
}
