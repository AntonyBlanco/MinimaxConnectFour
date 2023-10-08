
import java.util.HashMap;

public class Minimax<T extends GameStateInterface>{
    public T content;
    public int depth;
    public int possibleMoves;
    public HashMap<Integer, Minimax<T>> children;

    public Minimax(int x, T content, int depth){
        this.content = content;
		this.depth = depth;
    }
    public void generateMinimaxTree(){ // To start the game
        for(int i = 0; i < possibleMoves; i++){
            T child_cont = (T)(content.copy());
            child_cont.setMove(i); // Need to validate play move
			Minimax<T> child = new Minimax<T>(i, child_cont, depth-1);
            children.put(i, child);
        }
    }
	public void reduceTree(){

	}
    public int getMinimaxMoveWinner(){
        return content.getMinimaxMoveWinner();
    }
    public int getMinimaxMove(){
        return content.getMinimaxMove();
    }
    public void setDepth(int depth){ // For use in-game
        if(this.depth < depth){
            for(Minimax<T> child : children.values())
                child.setDepth(depth - 1);
        }
        this.depth = depth;
    }
}
