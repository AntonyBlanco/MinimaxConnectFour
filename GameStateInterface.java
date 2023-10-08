
interface GameStateInterface extends Comparable<GameStateInterface>{
    public GameStateInterface copy();
    public int getMinimaxMoveWinner();
    public int getMinimaxMove();
    public boolean setMove(int x);
}