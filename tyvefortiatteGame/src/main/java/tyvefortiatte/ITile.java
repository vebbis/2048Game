package tyvefortiatte;

public interface ITile extends Comparable<ITile> {
    
    

    public String getColor();

    public int getValue();

    public String getFont();

    public void setValue(int value);
                
    public boolean isMergePossible(ITile tile);

    public void mergeWith(ITile tileFurthestInMoveDirection);

}
