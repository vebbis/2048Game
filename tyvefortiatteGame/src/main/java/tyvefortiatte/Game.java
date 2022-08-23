package tyvefortiatte;

import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.input.KeyCode;

public class Game {
    
    private Grid grid;
    private int bestScore;


    public Game() {
        grid = new Grid();
        bestScore = 0;
    }

    public Game(ITile[][] gridList, int score, int bestScore){
            grid = new Grid(gridList, score);
            this.bestScore = bestScore;
    }

    /**
     * Static method returning an immutable list containing the valid tile values
     * @return List of valid tile values
     */
    public static Collection<Integer> getValidValues(){
        Collection<Integer> validValues = new ArrayList<>();
        validValues.add(0);
        for (int i = 1; i < 12; i++) {
            validValues.add((int)Math.pow(2,i));
        }
        return validValues;
    }

    /**
     * Initializing a new grid, thereby the score and greatest tile value are reset
     * and the bestScore is kept
     */
    public void startOver(){
        this.grid = new Grid();   
        System.out.println("Start over grid: ");
        grid.printGrid();
        System.out.println();
    }

    /**
     * @return The score of the current game
     */
    public int getScore(){
        return grid.getScore();
    }

    /**
     * @return The highest score achived during one game session.
     */
    public int getBestScore() {
        if(getScore() >= bestScore){
            bestScore = getScore();
        }
        return bestScore;
	}

    /**
     * @return The greatest tile value present at the current grid
     */
    public int getGreatestTileValue(){
        return grid.getGreatestTileValue();
    }

    /**
     * @return A copy of the list containing Tile objects that are present at the board
     */
    public ITile[][] getGameGrid(){
        return grid.getGrid();
    }

    public void printGrid(){
        grid.printGrid();
    }

    /**
     * @return true if 2048 is present at the grid
     */
    public boolean isGameWon(){
        return getGreatestTileValue() == 2048;
    }
    
    /**
     * @return true if there is no tiles in the entire grid that can merge and
     * no places for a new tile to spawn
     */
    public boolean isGameOver(){
        return !grid.isAnyMovePossible();
    }

    /**
     * If the game is over, nothing happens when a key is pressed.
     * Otherwise the grid is moved in the specified direction of a valid keypress
     * @param code Keycode from the keyboard
     */ 
    public void moveTileGrid(KeyCode code){

            Grid gridBeforeMove = new Grid(grid.getGrid(), getScore());
            
            /*
            If the grid does not move after a move is performed,
            a new tile shall not be added. gridValuesList is a copy of the grid before the move, 
            which is compared to the grid after the move. This makes it easy to 
            see if the grid has changed after the move. If it has, a tile is added.
            */
            
            if (code == KeyCode.D || code == KeyCode.RIGHT){grid.moveRight(); System.out.println("Høyre piltast trykket");}
            if (code == KeyCode.A || code == KeyCode.LEFT){grid.moveLeft(); System.out.println("Venstre piltast trykket");}
            if (code == KeyCode.W || code == KeyCode.UP){grid.moveUp(); System.out.println("Opp piltast trykket");}
            if (code == KeyCode.S || code == KeyCode.DOWN){grid.moveDown(); System.out.println("Ned piltast trykket");}
        

            if(!grid.equals(gridBeforeMove)){
            
                grid.addTile();
                
                System.out.println("Grid etter:");
                grid.printGrid();
                System.out.println("Score: "+getScore());
                System.out.println("BestScore: " + getBestScore());
                System.out.println();
            }
        }       
    }

	

    

    

    

