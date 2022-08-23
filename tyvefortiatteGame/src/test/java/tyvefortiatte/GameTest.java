package tyvefortiatte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.scene.input.KeyCode;


public class GameTest {



private ITile[][] onlyZeroGridList() {
    ITile[][] gridList = new Tile[4][4];
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
            gridList[i][j] = new Tile();
        }   
    }

    return gridList;
}


    
@Test
/*
When a new Game (not from file) is initialized, a new Grid should be initialized.
Therefore, the score and the bestScore should be zero.
The game is not won nor over
The greatest tile value shoud either be two or four
*/
public void testInitialConstructor() {
    Game game = new Game();
    List<ITile> tilesNotZero = new ArrayList<>();

    assertEquals(0, game.getBestScore());
    assertEquals(0, game.getScore());
    assertFalse(game.isGameOver());
    assertFalse(game.isGameWon());
    assertTrue(game.getGreatestTileValue()==4 || game.getGreatestTileValue()==2);

    Arrays.stream(game.getGameGrid())
    .forEach(a -> Arrays.stream(a)
    .filter(t -> t.getValue() != 0)
    .forEach(t -> tilesNotZero.add(t)));

    assertEquals(2, tilesNotZero.size(),"It should only be two nonzero tiles when initialized");

    assertTrue(tilesNotZero.get(0).getValue() == 2 || tilesNotZero.get(0).getValue() == 4
    ,"When initialized only two or four or both should be present at the board");

    assertTrue(tilesNotZero.get(1).getValue() == 2 || tilesNotZero.get(1).getValue() == 4
    ,"When initialized only two or four or both should be present at the board");
    
}

@Test
public void testLoadConstructor() {
    ITile[][] gridList = onlyZeroGridList();         
    Game game = new Game(gridList, 32, 64);

    assertEquals(64, game.getBestScore());
    assertEquals(32, game.getScore());

    assertFalse(game.isGameOver());
    assertFalse(game.isGameWon());

    //since the grid loaded contains the 2048 tile, gameWon should be true
    gridList[0][0].setValue(2048);
    game = new Game(gridList, 0, 0);

    assertEquals(2048, game.getGreatestTileValue());
    assertTrue(game.isGameWon());
}


@Test
public void testGetValidTileValues() {
    assertFalse(Game.getValidValues().contains(3));
    assertFalse(Game.getValidValues().contains(4096));
    assertFalse(Game.getValidValues().contains(-2));

    assertTrue(Game.getValidValues().contains(0));
    assertTrue(Game.getValidValues().contains(4));
    assertTrue(Game.getValidValues().contains(2048));

}

@Test
/*
Applying startOver to a game that has bestScore = 64, score = 32 and greatest tile value = 16
When startet over, the game shall keep its bestScore, score should be zero and the greatest tile value should be either two or four.
*/
public void testStartOver() {
    ITile[][] gridList = onlyZeroGridList();    
    gridList[0][2].setValue(4);
    gridList[0][3].setValue(16);
    gridList[3][2].setValue(2);     

    //initializing a game from file, therefore the bestScore shoud be 64 and score shoud be 32
    Game game = new Game(gridList, 32, 64);

    assertEquals(64, game.getBestScore());
    assertEquals(32, game.getScore());
    assertTrue(game.getGreatestTileValue()==16);

    game.startOver();

    //now the game is started over, and the score and greatestTileValue should be changed
    assertEquals(64, game.getBestScore());
    assertEquals(0, game.getScore());
    assertTrue(game.getGreatestTileValue()==4 || game.getGreatestTileValue()==2);

}

@Test
//The bestScore should always be equal to or higher than the score.
public void testGetBestScore() {

    //initializing game with bestscore = 0 and score = 100
    ITile[][] gridList = onlyZeroGridList();
    gridList[0][2].setValue(1024);
    gridList[0][3].setValue(1024);
    
    Game game = new Game(gridList, 100, 0);

    //bestscore should be 100, because it is always updated to the current score when returned
    assertEquals(100, game.getBestScore());

    //since this move merges two 1024 tiles, the score and bestScore should be 2148 (2048 added to the score) and gameWon shoud be true
    game.moveTileGrid(KeyCode.RIGHT);
    assertEquals(2148, game.getBestScore());
    assertEquals(2148, game.getScore());
    assertTrue(game.isGameWon());

}


private Game moveTileGridTestCase() {
    ITile[][] gridList = onlyZeroGridList();
    gridList[0][0].setValue(4);
    gridList[0][1].setValue(4);
    gridList[0][2].setValue(8);
    gridList[0][3].setValue(16);
    gridList[2][0].setValue(2);
    gridList[2][1].setValue(2);
    gridList[2][2].setValue(2);
    gridList[2][3].setValue(2);
    gridList[3][1].setValue(4);
    gridList[3][2].setValue(4);
    gridList[3][3].setValue(4);
    
    return new Game(gridList, 0, 0);

}


/*
This method checks if every tile at the index in the gameGrid has the 
same value as the tile at the same index in the constructed grid that is constructed 
the way the gameGrid should look after a move is performed.

Since a tile is added to the gameGrid for every move that moves the grid (the method works for cases that moves and does not move the grid),
this has to be accounted for.
The the counter is increased by one each time a tile value in the two grids differ. Since only
one tile should be added to the gameGrid, the counter should always be one. If not something is wrong.

The tile added should always be two or four, therfore this is also checked by the int addedTileValue
*/
private boolean checkEqualGame(ITile[][] gameGrid, ITile[][] constructedTestGrid, boolean gridShallMove) {
    int counter = 0;
    int addedTileValue = 0;

    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
            if(gameGrid[i][j].getValue() != constructedTestGrid[i][j].getValue()){
                counter++;
                addedTileValue = gameGrid[i][j].getValue();
            }
        }
    }

    if(gridShallMove){

        if(counter!=1){return false;}

        return addedTileValue ==2 || addedTileValue == 4;
    }

    else {
        System.out.println(counter);
        return counter == 0;
    }

}

@Test
/*
Testcase for the MoveTileGrid:
[4, 4, 8, 16]
[0, 0, 0,  0]
[2, 2, 2,  2]
[0, 4, 4,  4]


After moved left it should be:
[8, 8, 16, 0]
[0, 0, 0,  0]
[4, 4, 0,  0]
[8, 4, 0,  0]


After moved right it should be:
[0, 8, 8, 16]
[0, 0, 0,  0]
[0, 0, 4,  4]
[0, 0, 4,  8]


After moved up it should be:
[4, 4, 8, 16]
[2, 2, 2,  2]
[0, 4, 4,  4]
[0, 0, 0,  0]


After moved down it should be:
[0, 0, 0,  0]
[0, 4, 8, 16]
[4, 2, 2,  2]
[2, 4, 4,  4]


*/

public void testMoveTileGridLeft() {
    Game game = moveTileGridTestCase();
    game.moveTileGrid(KeyCode.LEFT);

    ITile[][] gridList = onlyZeroGridList();
    
    //testing move left
    gridList[0][0].setValue(8);
    gridList[0][1].setValue(8);
    gridList[0][2].setValue(16);
    gridList[0][3].setValue(0);
    gridList[2][0].setValue(4);
    gridList[2][1].setValue(4);
    gridList[2][2].setValue(0);
    gridList[2][3].setValue(0);
    gridList[3][0].setValue(8);
    gridList[3][1].setValue(4);
    gridList[3][2].setValue(0);

    //since the move moves the grid, a tile should be added
    assertTrue(checkEqualGame(game.getGameGrid(), gridList, true));
}

@Test
public void testMoveTileGridRight() {
    Game game = moveTileGridTestCase();
    game.moveTileGrid(KeyCode.RIGHT);

    ITile[][] gridList = onlyZeroGridList();
    
    //testing move right
    gridList[0][1].setValue(8);
    gridList[0][2].setValue(8);
    gridList[0][3].setValue(16);
    gridList[2][2].setValue(4);
    gridList[2][3].setValue(4);
    gridList[3][2].setValue(4);
    gridList[3][3].setValue(8);

    //since the move moves the grid, a tile should be added
    assertTrue(checkEqualGame(game.getGameGrid(), gridList, true));
}

@Test
public void testMoveTileGridUp() {
    Game game = moveTileGridTestCase();
    game.moveTileGrid(KeyCode.UP);

    ITile[][] gridList = onlyZeroGridList();
    
    //testing move up
    gridList[0][0].setValue(4);
    gridList[0][1].setValue(4);
    gridList[0][2].setValue(8);
    gridList[0][3].setValue(16);
    gridList[1][0].setValue(2);
    gridList[1][1].setValue(2);
    gridList[1][2].setValue(2);
    gridList[1][3].setValue(2);
    gridList[2][1].setValue(4);
    gridList[2][2].setValue(4);
    gridList[2][3].setValue(4);

    //since the move moves the grid, a tile should be added
    assertTrue(checkEqualGame(game.getGameGrid(), gridList, true));
}

@Test
public void testMoveTileGridDown() {
    Game game = moveTileGridTestCase();
    game.moveTileGrid(KeyCode.DOWN);

    ITile[][] gridList = onlyZeroGridList();
    
    //testing move left
    gridList[1][1].setValue(4);
    gridList[1][2].setValue(8);
    gridList[1][3].setValue(16);
    gridList[2][0].setValue(4);
    gridList[2][1].setValue(2);
    gridList[2][2].setValue(2);
    gridList[2][3].setValue(2);
    gridList[3][0].setValue(2);
    gridList[3][1].setValue(4);
    gridList[3][2].setValue(4);
    gridList[3][3].setValue(4);

    //since the move moves the grid, a tile should be added
    assertTrue(checkEqualGame(game.getGameGrid(), gridList, true));
}

@Test
/*
If you move the grid in any dicrection and no tiles change their position due to no tiles merge
and all tiles are at the edge, a new tile shall not spawn.
Example:
[0, 0, 0,  0]
[0, 0, 0,  4]
[0, 0, 2,  4]
[0, 0, 2,  4]

When the grid is moved to the right, nothing happends - it stays the same after the move.
This is because two and four will not merge nor move because they are at the edge.
*/

public void testTileNotSpawnWhenGridNotChangeWhenMoved() {
    ITile[][] gridList = onlyZeroGridList();
    gridList[2][2].setValue(2);
    gridList[2][3].setValue(4);
    gridList[3][2].setValue(2);
    gridList[3][3].setValue(4);

    //testing the example described above
    Game game = new Game(gridList, 0, 0);
    game.moveTileGrid(KeyCode.RIGHT);

    ITile[][] constructedTestGrid = onlyZeroGridList();
    constructedTestGrid[2][2].setValue(2);
    constructedTestGrid[2][3].setValue(4);
    constructedTestGrid[3][2].setValue(2);
    constructedTestGrid[3][3].setValue(4);

    assertTrue(checkEqualGame(game.getGameGrid(), constructedTestGrid, false));
}

@Test
/*
When the grid is full of nonezero tiles and no tiles can merge, then the game is over.
Example:
[4, 2, 16,  4]
[8, 64, 4,  8]
[16, 32, 8, 2]
[8, 2, 16,  4]

*/
public void testisGameOver() {
    ITile[][] gridList = onlyZeroGridList();
    gridList[0][0].setValue(4);
    gridList[0][1].setValue(2);
    gridList[0][2].setValue(16);
    gridList[0][3].setValue(4);
    gridList[1][0].setValue(8);
    gridList[1][1].setValue(64);
    gridList[1][2].setValue(4);
    gridList[1][3].setValue(8);
    gridList[2][0].setValue(16);
    gridList[2][1].setValue(32);
    gridList[2][2].setValue(8);
    gridList[2][3].setValue(2);
    gridList[3][0].setValue(8);
    gridList[3][1].setValue(2);
    gridList[3][2].setValue(16);
    gridList[3][3].setValue(4);

    //testing the example above

    assertTrue(new Game(gridList, 0, 0).isGameOver(), "Should be game over. No tiles can merge nor move");
}



}
