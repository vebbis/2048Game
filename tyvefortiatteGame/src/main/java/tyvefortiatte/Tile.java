package tyvefortiatte;

import java.util.Arrays;

    

public class Tile implements ITile {
    
    private int value;
    
    /**
     * Constructor initializing tile with value 0
     */
    public Tile() {
        setValue(0);
    }

    /**
     * The tiles color and font are by the value
     * @param value the tile value
     */
    public Tile(int value) {
        if(!Game.getValidValues().contains(value)){
            throw new IllegalArgumentException("Ikke en gyldig tile verdi");
        }
        setValue(value);
    }

    /**
     * sets the tiles value, font and color based on the parameter
     * @param value the tile value
     */
    public void setValue(int value) {
        if (!Game.getValidValues().contains(value)){throw new IllegalArgumentException("Ikke en gyldig tile verdi!");}
        this.value = value;
    }


    /*
    since all the values are one the form 2^n, I can by using the log with base two 
    get the color in the list corresponding to its value by using the index it is placed on in the list

    I find it more effective than using 12 if statements returning a unique color depending on the value of the tile
    (similar to setFont)
    */ 
    /**
     * @param value the tile value
     * @return the color correponding to the parameter
     */
    public String getColor() {
        if(value == 0){return "-fx-background-color: silver;";}
        
        return
        "-fx-background-color: " +

        Arrays.asList("#776e65","wheat","#f2b179","#f59563","#f67c5f"
        ,"#f65e3b","#edcf72","#edcc61","#edc850","#edc53f","#edc22e")
        .get((int)(Math.log(value) / Math.log(2)) - 1) +

        ";";
    }

    public String getFont() {
        if (value==0){
            return "-fx-fill: silver;";}
        else if (value==2 || value==4){
            return "-fx-fill: black;";}
        else {
            return "-fx-fill: white;";}
    }

    public int getValue() {
        return value;
    }

    /**
     * @return true if the parameter and this can merge (have the same value)
     */
    public boolean isMergePossible(ITile tile) {
        return this.getValue()==tile.getValue();
    }

    /**
     * Merges this and the parameter.
     * The tile furthest in the move direction(should me the parameter) becomes the new tile with twice the value
     * The other tile becomes a grey tile (gets value zero)
     */
    public void mergeWith(ITile tileFurthestInMoveDirection) {
        tileFurthestInMoveDirection.setValue(this.getValue() * 2);
        this.setValue(0);
    }

    @Override
    public int compareTo(ITile tile) {
        if(this.value == 0 && tile.getValue() != 0){
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    


    // public static void main(String[] args) {
    //     System.out.println(Game.getValidValues());
    //     Tile tile = new Tile();
    //     System.out.println(tile.getValue());
    //     System.out.println(tile.getColor());

    // }

   

   
 

    

    

    

}
