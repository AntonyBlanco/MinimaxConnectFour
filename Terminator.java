import java.util.*;

public class Terminator implements FourInARowInterface{
	public GameState game_state;

	public int line_size = 4; // Four in a row

	private int width;
	private int height;

	private static final int EXTERNAL_PIECE = -1;
	private static final int COMPUTER_PIECE = 1;

	public Terminator(int width, int height, int line_size, int depth_search, int turn){
		this.width = width;
		this.height = height;
		this.line_size = line_size;
		this.game_state = new GameState(depth_search, turn);
		this.restart();
	}
	
	public void restart(){
		this.game_state.restart();
	}

	public void restart(int turn){
		this.game_state.restart(turn);
	}
        
	public boolean play(int x){
		return game_state.play(x);
	}

	public int getLastComputerPlay(){
		return this.game_state.getLastComputerPlay(); // Computer play on x axis
	}
	public int getLastExternalPlay(){
		return this.game_state.getLastExternalPlay(); // Computer play on x axis
	}

	public boolean getGameEnded(){
		return this.game_state.getGameEnded();
	}

	public int getWinner(){
		return this.game_state.getWinner();
	}

	public String toString(){
		String str = this.game_state.toString() + "\n";
		//str += this.game_state.str_tree();
		return str;
	}

	// ################## == START MINIMAX == ##################

	public class GameState implements GameStateInterface{
		/**
		* The table will contain different values:
		*  -1 : Jugadas ingresadas por el otro jugador
		*  +1 : Jugadas de la computadora (Terminator)
		*   0 : Celda vacía
		*/
		private int[][] table;

		// Saves how many pieces there are in a column
		private int[] filledColumns;


		// depth_search for minimax search in the tree
		private int depth_search;

		// Last computer play on x axis
		private int lastComputerPlay;
		// Last external play on x axis
		private int lastExternalPlay;

		private boolean gameEnded;

		// Can take -1, +2 or 0 values representing EXTERNAL, COMPUTER and TIE
		private int winner;

		// Can take -1, +2 or 0 values representing EXTERNAL, COMPUTER and TIE
		// It's used to get 
		private int minimaxMoveWinner;
		// Stores the move used to get that minimaxMoveWinner1
		private int minimaxMove;

		// Player turns: EXTERNAL_PIECE or COMPUTER_PIECE
		private int turn;

		public GameState(int depth_search, int turn){
			this.table = new int[height][width];
			this.depth_search = depth_search;
		}
		
		public void restart(){
			this.table = new int[height][width];
			this.filledColumns = new int[width];
			this.lastComputerPlay = -1; // Computer play on x axis
			this.gameEnded = false;
			this.winner = 0;
		}

		public void restart(int turn){
			restart();
			this.turn = turn;
		}

		public boolean play(int x){ // Only x because of falling pieces
				if(this.gameEnded) return false; // Game ended
				if(!isValidMove(x)) return false; // couldn't perform the play because full column
				int y = height - filledColumns[x] - 1;
				table[y][x] = EXTERNAL_PIECE;
				print("Game State (External play): \n" + this.toString());
				filledColumns[x]++;

				checkWinLose();

				calculateTerminatorPlay(); // Minimax calculation play for Terminator
				return true;
		}

		public int getLastComputerPlay(){
			return this.lastComputerPlay; // Computer play on x axis
		}
		public int getLastExternalPlay(){
			return this.lastExternalPlay; // Computer play on x axis
		}

		public boolean getGameEnded(){
			return this.gameEnded;
		}

		public int getWinner(){
			if(!this.gameEnded) return -2; // Game not ended yet
			return winner;
		}
		@Override
		public int getMinimaxMoveWinner(){
			return this.minimaxMoveWinner;
		}
		@Override
		public int getMinimaxMove(){
			return this.minimaxMove;
		}

		private void setPiece(int x, int piece){
			this.table[height - filledColumns[x] - 1][x] = piece;
		}

		// Minimax use for calculating the posibbly next best move
		private void calculateTerminatorPlay(){
			if(this.gameEnded) return; // Game ended
			int y, x;
			do{
				//x = (int)(Math.random() * this.width);
				Scanner sc = new Scanner(System.in);
				x = sc.nextInt() - 1;
			}while(!isValidMove(x));
			y = height - filledColumns[x] - 1;
			table[y][x] = COMPUTER_PIECE;
			print("Game State (Computer play): \n" + this.toString());
			filledColumns[x]++;
			lastComputerPlay = x;

			checkWinLose();
		}

		@Override
		public boolean setMove(int x){
			if(x < 0 || x >= width) return false;
			setPiece(x, turn);
			switchTurn();
			return true;
		}
		private void switchTurn(){
			if(turn == Terminator.COMPUTER_PIECE)
				this.turn = Terminator.EXTERNAL_PIECE;
			else this.turn = Terminator.COMPUTER_PIECE;
		}

		private void checkWinLose(){
			int gameStatus = verifyGameStatus();
			println("## Received position winner: " + gameStatus);
			switch(gameStatus){
				case 0: // TIE
					for(int i = 0; i < width; i++)
						if(filledColumns[i] != height) return;
					gameEnded = true;
					break;
				case -1: // Winner EXTERNAL_PIECE
					gameEnded = true;
					break;
				case 1: // Winner COMPUTER_PIECE
					gameEnded = true;
					break;
			}
		}
		
		// Función optimizada para verificar qué jugador ganó el juego
		private int verifyGameStatus(){
			for(int i = 0; i < width; i++){
				int positionWinner = verifyPosition(i);
				if(positionWinner != 0) return positionWinner;
			}
			return 0;
		}
		// For exclusive use by verifyGameStatus
		private int verifyPosition(int x){
			int y = height - filledColumns[x];
			if(y >= height) return 0; // Empty column
			//println("Verifying at [" + x + ":" + y + "]");
			/** Verify winning move on every 7 possible lines
			*   (1) Horizontal to right
			*   (1) Horizontal to left
			*   (1) Vertical to bottom
			*   (4) Four Diagonals
			*/
			boolean vrf_vertical = y <= height - line_size;
			boolean vrf_horizontal_r = x <= width - line_size;
			boolean vrf_horizontal_l = x >= line_size-1;
			boolean vrf_diagonal_11 = x <= width - line_size && y >= line_size-1;
			boolean vrf_diagonal_01 = x >= line_size-1 && y >= line_size-1;
			boolean vrf_diagonal_10 = x <= width - line_size && y <= height - line_size;
			boolean vrf_diagonal_00 = x >= line_size-1 && y <= height - line_size;
			for(int i = 0; i < line_size-1; i++){
				if(vrf_vertical){
					if(table[y+i][x] != table[y+i+1][x])
						vrf_vertical = false;
				}
				//print(vrf_vertical + " ");
				if(vrf_horizontal_r){
					if(table[y][x+i] != table[y][x+i+1])
						vrf_horizontal_r = false;
				}
				//print(vrf_horizontal_r + " ");
				if(vrf_horizontal_l){
					if(table[y][x-i] != table[y][x-i-1])
						vrf_horizontal_l = false;
				}
				//print(vrf_horizontal_l + " ");
				if(vrf_diagonal_11){
					if(table[y-i][x+i] != table[y-i-1][x+i+1])
						vrf_diagonal_11 = false;
				}
				//print(vrf_diagonal_11 + " ");
				if(vrf_diagonal_10){
					if(table[y+i][x+i] != table[y+i+1][x+i+1])
						vrf_diagonal_10 = false;
				}
				//print(vrf_diagonal_10 + " ");
				if(vrf_diagonal_00){
					if(table[y+i][x-i] != table[y+i+1][x-i-1])
						vrf_diagonal_00 = false;
				}
				//print(vrf_diagonal_00 + " ");
				if(vrf_diagonal_01){
					if(table[y-i][x-i] != table[y-i-1][x-i-1])
						vrf_diagonal_01 = false;
				}
				//print(vrf_diagonal_01 + " ");
				//println();
			}
			return (vrf_vertical ||
					vrf_horizontal_r ||
					vrf_horizontal_l ||
					vrf_diagonal_11 ||
					vrf_diagonal_01 ||
					vrf_diagonal_10 ||
					vrf_diagonal_00) ? table[y][x] : 0;
		}

		private boolean isValidMove(int x){
			if(x < 0 || x >= width) return false;
			return height - filledColumns[x] - 1 >= 0;
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
			str += "game ended? " + gameEnded + "\n";
			return str;
		}

		/*public String str_tree(){
			String str = this.winner + ":[";
            for(Object child : children.values()){
			for(int i = 0; i < children_size; i++){
				if(i != 0) str += ", ";
				str += children.get(i).toString() + "";
			}
			str += "]";
			return str;
		}*/

		@Override
		public GameStateInterface copy(){
			GameState copy = new GameState(depth_search, turn);
			return copy;
		}

		@Override
		public int compareTo(GameStateInterface gameState) {
			int item_0 = this.getMinimaxMove();
			int item_1 = gameState.getMinimaxMove();
			if(item_0 > item_1) return 1;
			if(item_0 < item_1) return -1;
			return 0;
		}
	}

	// ################## == END MINIMAX == ##################

	public static void main(String[] args){
		int w, h;
		w = h = 6;
		Terminator ter = new Terminator(w, h, 4, 2, Terminator.EXTERNAL_PIECE);
		print("Game Start:\n" + ter.toString());
		Scanner sc = new Scanner(System.in);
		int ext_play;
		while(!ter.getGameEnded()){
			print("Insert move (1-10): ");
			ext_play = sc.nextInt();
			if(!ter.play(ext_play-1)){
				println("Couldn't play that move.");
				println("Full column or unexistent column. Play another.");
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
