package tyvefortiatte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GridTest {
    
private Grid grid;

private void createGrid(){
    grid = new Grid();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    // adding 8 tiles
}

private ITile[][] onlyZeroGridList() {
    ITile[][] gridList = new Tile[4][4];
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
            gridList[i][j] = new Tile();
        }   
    }

    return gridList;
}

@BeforeEach
public void setup() {
    createGrid();
}

@Test
public void testInitialConstructor() {
    grid = new Grid();

    int valueZero = 0;
    int valueTwoOrFour = 0;
    for (ITile[] row : grid.getGrid()) {
        for (ITile tile : row) {
            if(tile.getValue()==0){valueZero++;}
            else if(tile.getValue()==2 | tile.getValue()==4){valueTwoOrFour++;}
            else {throw new IllegalStateException("Ved initialisering av ny grid skal den kunn inneholde verdi 0, 2 eller 4");}
        }
    }

    if(valueTwoOrFour !=2 || valueZero != 14){
        throw new IllegalStateException("Ved initialisering av ny grid skal den kun inneholde 14 graa tiles og 2 tiles av verdi 2 eller 4");
    }


    assertEquals(0, grid.getScore(), "Ved initilaisering av ny grid er scoren 0");
}


@Test
public void testLoadConstructor() {
    ITile[][] gridList = onlyZeroGridList();
    gridList[0][2].setValue(8);

    grid = new Grid(gridList, 32);

    assertEquals(8, grid.getGreatestTileValue(), String.format("GreatestTileValue should be 8 but is %d", grid.getGreatestTileValue()));
    assertEquals(32, grid.getScore(),String.format("Score should be 32 but is %d", grid.getScore()));

    //loading new grid with invalid score
    assertThrows(IllegalArgumentException.class, 
                () -> new Grid(gridList, -1));

    //testing that one tile from the gridlist missing is throwing exeption
    gridList[0][0] = null;
    assertThrows(IllegalArgumentException.class, 
                () -> new Grid(gridList, 0));

    //trying to load 5 x 5 grid
    ITile[][] invalidGridList = new ITile[5][5];
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 4; j++) {
            invalidGridList[i][j] = new Tile();
        }   
    }  
    assertThrows(IllegalArgumentException.class, 
                () -> new Grid(invalidGridList, 0));
}

@Test
/*
getGrid shall return a copy of gridList, not the actual gridList that is used in the Grid class
*/
public void testGetGrid() {
    grid.getGrid()[1][3].setValue(2048);
    assertNotEquals(2048, grid.getGrid()[1][3].getValue());
}

@Test
/*
When to tiles merge, the values of the tiles shoud be summed and added to the score.
Example: [0, 0, 2, 2] shoud turn to [0, 0, 0, 4] after moveRight is applied, 
and the sum 2 + 2 = 4 shoud be added to the score. If the score is 0, it should be 4 after the move.
*/
public void testUpdateScore() {

    ITile[][] gridList = onlyZeroGridList();

    gridList[0][2].setValue(2);
    gridList[0][3].setValue(2);

    grid = new Grid(gridList, 0);

    assertEquals(0, grid.getScore()
    ,String.format("Score should be 0 when no tiles has merged, but is %d", grid.getScore()));

    grid.moveRight();    

    assertEquals(4, grid.getScore()
    ,String.format("Score should be 4 when two tiles with value 2 merge, but is %d", grid.getScore()));
}

@Test
/*
When initialized two tiles is added to the grid. Since additionaly eight tiles is added to the grid object in the setup method
the total number of tiles that differ from zero should be 10.
*/
public void testAddTile() {
    int numTilesNotZero = 0;

    for (ITile[] row : grid.getGrid()) {
        for (ITile tile : row) {
            if(tile.getValue() != 0){numTilesNotZero++;}
        }
    }

    assertEquals(10, numTilesNotZero);

    //adding six tiles. If the tiles spawn in empty spots, the number of tiles should now be 16
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();
    grid.addTile();

    numTilesNotZero = 0;
    for (ITile[] row : grid.getGrid()) {
        for (ITile tile : row) {
            if(tile.getValue() != 0){numTilesNotZero++;}
        }
    }
    assertEquals(16, numTilesNotZero);
}

private boolean equalsGrid(ITile[][] g1, ITile[] g2){
    for (int i = 0; i < 4; i++) {
        if(g1[0][i].getValue() != g2[i].getValue()){
            return false;
        }
    }   
    
    return true;
}

@Test
public void testMoveRight() {
    //For testing purposes, a tile is not added after a move is performed.

    ITile[][] gridList = onlyZeroGridList();
    ITile[] gridListAfterMoveRight = new Tile[4];

    /*
     If two tiles of the same number collide while moving, 
     they will merge into a tile with the total value of the two tiles that collided.
     This means a row [0, 0, 2, 2] shoud be [0, 0, 0, 4] after moveRight is applied.
    */

    // Testing that [0, 0, 2, 2] is [0, 0, 0, 4] after moveRight is applied
    gridListAfterMoveRight[0] = new Tile();
    gridListAfterMoveRight[1] = new Tile();
    gridListAfterMoveRight[2] = new Tile();
    gridListAfterMoveRight[3] = new Tile(4);

    gridList[0][2].setValue(2);
    gridList[0][3].setValue(2);

    grid = new Grid(gridList, 0);
    grid.moveRight();

    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 0, 4] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));

    /*
    While moving, the resulting tile when two tiles merge cannot merge with another tile again in the same move.

    Example of valid moves:
    first row in the grid before moveRight is applied: [2, 2, 4, 8]
    first row after moveRight is applied once: [0, 4, 4, 8]
    first row after moveRight is applied twice: [0, 0, 8, 8]
    first row after moveRight is applied for the third time: [0, 0, 0, 16]
    in these moves, the resulting tile does not merge after move.

    Example of a invalid move:
    first row in the grid before moveRight is applied: [2, 2, 4, 8]
    first row after moveRight is applied once: [0, 0, 0, 16]
    in this move, the resulting tiles merge in the same move.
    */

    // Testing the case used in example of valid moves
    gridList[0][0]= new Tile(2);
    gridList[0][1]= new Tile(2);
    gridList[0][2]= new Tile(4);
    gridList[0][3]= new Tile(8);

    gridListAfterMoveRight[0].setValue(0);
    gridListAfterMoveRight[1].setValue(4);
    gridListAfterMoveRight[2].setValue(4);
    gridListAfterMoveRight[3].setValue(8);

    grid = new Grid(gridList, 0);
    grid.moveRight();

    //test grid.moveRight applied once
    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 4, 4, 8] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));

    gridListAfterMoveRight[1].setValue(0);
    gridListAfterMoveRight[2].setValue(8);
    gridListAfterMoveRight[3].setValue(8);

    grid.moveRight();

    //test grid.moveRight applied twice
    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 8, 8] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));

    gridListAfterMoveRight[2].setValue(0);
    gridListAfterMoveRight[3].setValue(16);

    grid.moveRight();

    //test grid.moveRight applied for the third time
    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 0, 16] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));


    /*
    The tiles shall move as far as possible in the chosen direction until they are stopped by either another tile or the edge of the grid.
    Example case 1: [2, 2, 0, 0] should be [0, 0, 0, 4] after moveRight is applied
    Example case 2: [2, 2, 0, 16] should be [0, 0, 4, 16] after moveRight is applied.
    */

    //setup case 1
    gridList[0][0]= new Tile(2);
    gridList[0][1]= new Tile(2);
    gridList[0][2]= new Tile(0);
    gridList[0][3]= new Tile(0);

    gridListAfterMoveRight[0].setValue(0);
    gridListAfterMoveRight[1].setValue(0);
    gridListAfterMoveRight[2].setValue(0);
    gridListAfterMoveRight[3].setValue(4);

    grid = new Grid(gridList, 0);
    grid.moveRight();

    //testing case 1
    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 0, 4] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));

    //setup case 2
    gridList[0][0]= new Tile(2);
    gridList[0][1]= new Tile(2);
    gridList[0][2]= new Tile(0);
    gridList[0][3]= new Tile(16);

    gridListAfterMoveRight[0].setValue(0);
    gridListAfterMoveRight[1].setValue(0);
    gridListAfterMoveRight[2].setValue(4);
    gridListAfterMoveRight[3].setValue(16);

    grid = new Grid(gridList, 0);
    grid.moveRight();

    //testing case 2
    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 4, 16] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));


    /*
    If all four spaces in a row or column are filled with tiles of the same value, 
    a move parallel to that row/column will combine the first two and last two.
    This means [2, 2, 2, 2] shoud be [0, 0, 4, 4] after moveRight is applied
    */

    //setup 
    gridList[0][0]= new Tile(2);
    gridList[0][1]= new Tile(2);
    gridList[0][2]= new Tile(2);
    gridList[0][3]= new Tile(2);

    gridListAfterMoveRight[0].setValue(0);
    gridListAfterMoveRight[1].setValue(0);
    gridListAfterMoveRight[2].setValue(4);
    gridListAfterMoveRight[3].setValue(4);

    grid = new Grid(gridList, 0);
    grid.moveRight();

    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 4, 4] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));


    /*
    If a move causes three consecutive tiles of the same value to slide together,
    only the two tiles farthest along the direction of motion will combine.
    [0, 2, 2, 2] should be [0, 0, 2, 4]
    */

    //setup 
    gridList[0][0]= new Tile(2);
    gridList[0][1]= new Tile(2);
    gridList[0][2]= new Tile(2);
    gridList[0][3]= new Tile(0);

    gridListAfterMoveRight[0].setValue(0);
    gridListAfterMoveRight[1].setValue(0);
    gridListAfterMoveRight[2].setValue(2);
    gridListAfterMoveRight[3].setValue(4);

    grid = new Grid(gridList, 0);
    grid.moveRight();

    assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveRight)
    ,String.format("Row shoud be [0, 0, 2, 4] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));

}


@Test
public void testMoveLeft() {
    
    //For testing purposes, a tile is not added after a move is performed.

    ITile[][] gridList = onlyZeroGridList();
    ITile[] gridListAfterMoveLeft = new ITile[4];

    /*
    The moveLeft method contains the same logic as moveRight, therfore
    the moveLeft method will not be tested as thorough as moveRight, since
    it is implicitly tested in the moveRight tests.

    As a consequence of this, only basic functionality is tested for the moveLeft method.
    */

    // Testing that [0, 0, 2, 2] is [4, 0, 0, 0] after moveRight is applied
     gridListAfterMoveLeft[0] = new Tile(4);
     gridListAfterMoveLeft[1] = new Tile();
     gridListAfterMoveLeft[2] = new Tile();
     gridListAfterMoveLeft[3] = new Tile();
 
     gridList[0][2].setValue(2);
     gridList[0][3].setValue(2);
 
     grid = new Grid(gridList, 0);
     grid.moveLeft();
 
     assertTrue(equalsGrid(grid.getGrid(), gridListAfterMoveLeft)
     ,String.format("Row shoud be [4, 0, 0, 0] after moveRight is applied but is %s", Arrays.toString(grid.getGrid()[0])));
}

private boolean equalTestVertical(ITile[][] g1, ITile[] g2) {
    for (int i = 0; i < 4; i++) {
        if(g1[i][0].getValue() != g2[i].getValue()){
            return false;
        }
    }   
    
    return true;
}


@Test
public void testMoveUp() {
    
    //For testing purposes, a tile is not added after a move is performed.
    //Only the first column needs to be tested (same logic goes for the others), the other columns will always be zero.

    ITile[][] gridList = onlyZeroGridList();
    ITile[] gridListAfterMoveUp = new Tile[4];

    /*
    The moveUp method contains the same logic as moveRight, therfore
    the moveUp method will not be tested as thorough as moveRight, since
    it is implicitly tested in the moveRight tests.

    As a consequence of this, only basic functionality is tested for the moveUp method.
    */

    /*
    Testing that [0] is [4] after moveUp is applied
                 [0]    [0]
                 [2]    [0]
                 [2]    [0]
    */

     gridListAfterMoveUp[0] = new Tile(4);
     gridListAfterMoveUp[1] = new Tile();
     gridListAfterMoveUp[2] = new Tile();
     gridListAfterMoveUp[3] = new Tile();
 
     gridList[2][0].setValue(2);
     gridList[3][0].setValue(2);
 
     grid = new Grid(gridList, 0);
     grid.moveUp();
 
     assertTrue(equalTestVertical(grid.getGrid(), gridListAfterMoveUp)
     ,String.format("Column shoud be [4, 0, 0, 0] after moveUp is applied but is %s"
     ,"[" + (grid.getGrid()[0][0].getValue()) + "," + (grid.getGrid()[1][0].getValue() 
     + "," + (grid.getGrid()[2][0].getValue()) + "," + (grid.getGrid()[3][0].getValue()) + "]")));
}

@Test
public void testMoveDown() {
    
    //For testing purposes, a tile is not added after a move is performed.
    //Only the first column needs to be tested (same logic goes for the others), the other columns will always be zero.

    ITile[][] gridList = onlyZeroGridList();
    ITile[] gridListAfterMoveDown = new Tile[4];

    /*
    The moveDown method contains the same logic as moveRight, therfore
    the moveDown method will not be tested as thorough as moveRight, since
    it is implicitly tested in the moveRight tests.

    As a consequence of this, only basic functionality is tested for the moveDown method.
    */

    /*
    Testing that [2] is [0] after moveDown is applied
                 [2]    [0]
                 [0]    [0]
                 [0]    [4]
    */

     gridListAfterMoveDown[0] = new Tile();
     gridListAfterMoveDown[1] = new Tile();
     gridListAfterMoveDown[2] = new Tile();
     gridListAfterMoveDown[3] = new Tile(4);
 
     gridList[0][0].setValue(2);
     gridList[1][0].setValue(2);
 
     grid = new Grid(gridList, 0);
     grid.moveDown();
 
     assertTrue(equalTestVertical(grid.getGrid(), gridListAfterMoveDown)
     ,String.format("Column shoud be [4, 0, 0, 0] after moveUp is applied but is %s"
     ,"[" + (grid.getGrid()[0][0].getValue()) + "," + (grid.getGrid()[1][0].getValue() 
     + "," + (grid.getGrid()[2][0].getValue()) + "," + (grid.getGrid()[3][0].getValue()) + "]")));
}


}
