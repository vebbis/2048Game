package tyvefortiatte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

    /*
    This is the class that does all the calculations when the board is moved.
    The board is represented as an 2d array, gridList, containing ITile objects, 16 in total.
    */

public class Grid {
    
    private ITile[][] gridList;
    private int score;
    

    /*
     * Initial constructor for starting a game
     */
    public Grid() {
        gridList = new ITile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gridList[i][j] = new Tile();
            }   
        }
        addTile();
        addTile();
        score = 0;      
    }

    /**
     * Constructor for loading from file
     * @param gridList existing gridList containing ITile objects
     * @param score the score from the grid being loaded
     * @throws IllegalArgumentException If the score is not even or it is negative
     * @throws IllegalArgumentException If the gridList being loaded does not contain exclusively ITile objects
     * @throws IllegalArgumentException If the gridList being loaded is not on the form 4 x 4
     */
    public Grid(ITile[][] gridList, int score){

        if(score < 0 || score % 2 != 0){
            throw new IllegalArgumentException("Score can not be negativ nor odd");
        }

        for (ITile[] row : gridList) {
            for (ITile tile : row) {
                if(tile == null){
                    throw new IllegalArgumentException("The gridList must contain ITile objects at every index!");
                }
            }
        }

        if(gridList.length != 4){
            throw new IllegalArgumentException("The board can not have " + gridList.length + " coloumn(s).");
        }
        
        for (ITile[] row : gridList) {
            if(row.length != 4){
                throw new IllegalArgumentException("The board can not have " + row.length + " row(s)");
            }
        }

        this.gridList = new ITile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.gridList[i][j] = new Tile(gridList[i][j].getValue());
            }   
        }
        
        this.score = score;
    }

    /**
     * @return the greatest tile value present at the board
     */
    public int getGreatestTileValue(){
        int greatestTileValue = 0;
        for (ITile[] row : gridList) {
            for (ITile tile : row) {
                if(greatestTileValue < tile.getValue()){greatestTileValue = tile.getValue();}
            }
        }

        return greatestTileValue;
    }

    /**
     * Prints the grid containing the tile values, providing a visual insight to the gridlist containing Tile objects.
     */
    public void printGrid() {
        for (ITile[] tiles : gridList) {
            System.out.println(Arrays.toString(tiles));
        }      
    }

    /**
     * The gridlist containing Tile objects shall be immutable outside the Grid class, therefore a duplicate is made
     * @return The 4x4 gridlist (duplicate) containing Tile objects
     */
    public ITile[][] getGrid() {
        ITile[][] gridListDuplicate = new ITile[4][4];
        
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                gridListDuplicate[y][x] = new Tile(gridList[y][x].getValue());
            }
        }

        return gridListDuplicate;  
    }
    
    
    /**
     * Updates the score. Each time two tiles with equal values merges, the score is updated with the sum of these values. 
     * Example: two tiles with value 4 merging will update the score by adding 8 to the current score.
     * @param tile the tiles value is added to the current score
     */
    private void updateScore(ITile tile) {
        score+= tile.getValue();
    }

    /**
     * @return The score achived when playing
     */
    public int getScore() {
        return score;
    }

    /**
     * reverseRowOrder() is reversing the order of each row in the 4x4 grid
     * Example: that [4, 3, 2, 1] is [1, 2, 3, 4] after reverseRowOrder is applied
     */  
    private void reverseRowOrder() {
        for (ITile[] row : gridList) {
            ITile t0 = row [0];
            ITile t1 = row [1];
            row[0] = row[3];
            row[1] = row[2];
            row[2] = t1;
            row[3] = t0;      
        }    
    }

    /**
     * Since the grid is 4x4, it can be transposed such that when moved in a vertical direction,
     * methods for horisontal movement are being used.
     */
    private void transposeGrid() {
        ITile[][] transposedArray = new ITile[4][4];

        for(int i=0;i<4;i++){    
            for(int j=0;j<4;j++){    
            transposedArray[i][j]=gridList[j][i];  
            }    
        } 

        gridList = transposedArray;     
    }


    /**
     * Spawns a tile with value either 2 or 4 (50% probability) on a random available spot on the board
     */
    public void addTile() {
        Random rn = new Random();

        int random2Or4 = rn.nextInt(1,1001)%2==0 ? 2:4;
        
        //finding which rows has room for a new tile
        int r0 = Arrays.stream(gridList[0]).filter(t->t.getValue()==0).toList().size();
        int r1 = Arrays.stream(gridList[1]).filter(t->t.getValue()==0).toList().size();
        int r2 = Arrays.stream(gridList[2]).filter(t->t.getValue()==0).toList().size();
        int r3 = Arrays.stream(gridList[3]).filter(t->t.getValue()==0).toList().size();

        //adding the relevant rows to a list
        List<ITile[]> rows = new ArrayList<>();
        if (r0 != 0){rows.add(gridList[0]);}
        if (r1 != 0){rows.add(gridList[1]);}
        if (r2 != 0){rows.add(gridList[2]);}
        if (r3 != 0){rows.add(gridList[3]);}
        Collections.shuffle(rows);
        

        /*
        the list "rows" contains only rows that have room for one or more tiles. One of the rows with free space is randomly selected,
        then one of the available spots (grey spot) in the row is randomly selected, then assigned 2 og 4
        */
        List<ITile> canidates = Arrays.stream(rows.get(rn.nextInt(0,rows.size())))
                                .filter(t->t.getValue()==0).toList();

        canidates.get(rn.nextInt(0,canidates.size())).setValue(random2Or4);
    }


    /**
     * all tiles in the grid "slide" to the right and merge if possible
     */
    public void moveRight(){

        for (ITile[] row : gridList) {

            Arrays.sort(row);
            
            int tilesNotZero = (int) Arrays.stream(row).filter(tile -> tile.getValue() != 0).count();
            
            if(tilesNotZero >= 2){
                for (int i = 3; i > 0; i--) {
                    if(row[i].isMergePossible(row[i-1])){
                        row[i-1].mergeWith(row[i]);
                        updateScore(row[i]);
                    }
                }
            }
            Arrays.sort(row);
        }
    }

    public void moveLeft() {
        reverseRowOrder();
        moveRight();
        reverseRowOrder();    
    }

    public void moveUp() {
        transposeGrid();
        moveLeft();
        transposeGrid();    
    }

    public void moveDown() {
        transposeGrid();
        moveRight();
        transposeGrid();    
    }

    /**
     * @return true if there is no tiles in the entire grid that can merge and
     * no places for a new tile to spawn
     */
    public boolean isAnyMovePossible() {

        //if any tile value is zero, the grid is not full and the game is not over 
        for (ITile[] tiles : this.gridList ) {
            for (ITile tile : tiles) {
                if(tile.getValue()==0){return true;}
            }
        }
        
         //checking for possible horisontal merges
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                if(gridList[y][x].isMergePossible(gridList[y][x+1])){return true;}
            }
        } 

        //checking for possible vertical merges
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                if(gridList[y][x].isMergePossible(gridList[y+1][x])){return true;}
            }
        }         
        return false;
    }

    @Override
    public boolean equals(Object gridBeforeMove) {
        if(gridBeforeMove.getClass() != Grid.class){return false;} 

        Grid tempGrid = (Grid) gridBeforeMove;

        if(score != tempGrid.getScore()){return false;}

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(tempGrid.getGrid()[y][x].getValue()!= gridList[y][x].getValue()){
                    return false;
                }
            }
        }

        return true;
    }

    

    // public static void main(String[] args) {
         
    //     Grid grid = new Grid();
    //     grid.gridList[0][0].setValue(16);
    //     grid.gridList[0][1].setValue(4);
    //     grid.gridList[0][2].setValue(0);
    //     grid.gridList[0][3].setValue(4);
    //     grid.addTile();
    //     grid.printGrid();
    //     System.out.println();
    //     grid.moveRight();
    //     grid.printGrid();
    // }
}
