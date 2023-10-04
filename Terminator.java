import java.util.*;

public class Terminator {
	public final int line_size = 4;

	private int width;
	private int height;

	/**
	* The table will contain different values:
	*  -1 : Jugadas ingresadas por el otro jugador
	*  +1 : Jugadas de la computadora (Terminator)
	*   0 : Celda vacía
	*/
	private int[][] table;

	// Saves how many pieces there are in a column
	private int[] filledColumns;

	private static final int EXTERNAL_PIECE = -1;
	private static final int COMPUTER_PIECE = 1;

	// depth_search for minimax search in the tree
	private int depth_search;

	private int[] lastTerminatorPlay;

	public Terminator(int width, int height, int depth_search){
		this.width = width;
		this.height = height;
		this.depth_search = depth_search;
		this.restart();
		this.lastTerminatorPlay = new int[2]; // Computer play in [x, y] format
	}

	public void restart(){
		this.table = new int[this.height][this.width];
		this.filledColumns = new int[this.width];
	}

	public boolean play(int x){ // Only x because of falling pieces
		if(!isValidMove(x)) return false; // couldn't perform the play because full column
		int y = this.height - filledColumns[x] - 1;
		table[y][x] = EXTERNAL_PIECE;
		print("Game State (External play): \n" + this.toString());
		filledColumns[x]++;
		calculateTerminatorPlay(); // Minimax calculation play for Terminator
		return true;
	}

	public int[] getLastTerminatorPlay(){
		return this.lastTerminatorPlay;// Computer play in [x, y] format
	}

	// Minimax use for calculating the posibbly next best move
	private void calculateTerminatorPlay(){
		int y, x;
		do{
			x = (int)(Math.random() * this.width);
		}while(!isValidMove(x));
		y = this.height - filledColumns[x] - 1;
		table[y][x] = COMPUTER_PIECE;
		print("Game State (Computer play): \n" + this.toString());
		filledColumns[x]++;
	}

	// Función optimizada para verificar qué jugador ganó el juego
	private int verifyGameStatus(){
		for(int j = 0; j < height; j++){
			for(int i = 0; i < width; i++){
				int positionWinner = verifyPosition(i, j);
				if(positionWinner != 0) return positionWinner;
			}
		}
		return 0;
	}
	// For exclusive use by verifyGameStatus
	private int verifyPosition(int x, int y){
		int verticalLine = 0;
		int horizontalLine = 0;
		int diagonal01Line = 0;
		int diagonal02Line = 0;
		// Verificar si existe linea en los 4 sentidos posibles
		for(int i = 0; i < 3; i++){
			if(table[y+i][x] == table[y+i+1][x]) verticalLine++;
			if(verticalLine == 3) return table[y][x];
			if(table[y][x+i] == table[y][x+i+1]) horizontalLine++;
			if(horizontalLine == 3) return table[y][x];
			if(table[y+i][x+i] == table[y+i+1][x+i+1]) diagonal01Line++;
			if(diagonal01Line == 3) return table[y][x];
			if(table[y+i][x-i] == table[y+i+1][x-i-1]) diagonal02Line++;
			if(diagonal02Line == 3) return table[y][x];
		}
		return 0;
	}

	private boolean isValidMove(int x){
		if(x < 0 || x >= this.width) return false;
		return this.height - filledColumns[x] - 1 >= 0;
	}

	public String toString(){
		String str = "";
		for(int j = 0; j < height; j++){
			str += "[";
			for(int i = 0; i < width; i++){
				if(i != 0) str += "|";
				str += table[j][i] < 0 ? table[j][i] : " " + table[j][i];
				str += " ";
			}
			str += "]\n";
		}
		return str;
	}

	public static void main(String[] args){
		Terminator ter = new Terminator(10, 10, 2);
		print("Game Start:\n" + ter.toString());
		Scanner sc = new Scanner(System.in);
		int ext_play;
		while(true){
			print("Insert move (1-10): ");
			ext_play = sc.nextInt();
			if(!ter.play(ext_play-1)){
				println("Couldn't play that move. Full column. Play another.");
			}
		}
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
