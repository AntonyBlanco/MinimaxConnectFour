
interface FourInARowInterface{
    public boolean play(int x);
    public int getLastComputerPlay();
    public int getLastExternalPlay();
    public void restart();
    public boolean getGameEnded();
    public int getWinner();
}