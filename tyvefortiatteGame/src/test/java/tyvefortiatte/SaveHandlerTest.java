package tyvefortiatte;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * This class is inspired by the snake bird example from the TDT4100 course content.
 * Since both 2048 and snake bird is operating on grids, they have many features in common
 * when it comes to testing.
 */
public class SaveHandlerTest {

    private Game game;
	private SaveHandler saveHandler = new SaveHandler();

    private boolean checkEqualGame(Game gameGrid, Game constructedTestGrid) {
        
		if(gameGrid.getBestScore() != constructedTestGrid.getBestScore()){
			return false;
		}

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(gameGrid.getGameGrid()[i][j].getValue() != constructedTestGrid.getGameGrid()[i][j].getValue()){
                    return false;
                }
            }
        }
        return true;
    }

    private Tile[][] onlyZeroGridList() {
        Tile[][] gridList = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gridList[i][j] = new Tile();
            }   
        } 
        return gridList;
    }

	private void createGame() {
        Tile[][] gridList = onlyZeroGridList();
        gridList[1][0].setValue(2);
        gridList[1][1].setValue(16);
        gridList[1][3].setValue(4);
        gridList[2][0].setValue(8);
        gridList[2][1].setValue(2);
        gridList[2][2].setValue(8);
        gridList[2][3].setValue(4);
        gridList[3][0].setValue(16);
        gridList[3][1].setValue(4);
        gridList[3][2].setValue(8);
        gridList[3][3].setValue(4);
		
        game = new Game(gridList, 0, 0);
		try {
			saveHandler.save("testfile", game);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Test failed! Unable to save testfile " + e.getMessage());
		}
	}

	@BeforeEach
	public void setup() {
		createGame();
	}

	@Test
	public void testLoad() {
        Game savedNewGame;
		try {
			savedNewGame = saveHandler.load("testfile.txt");
		} catch (FileNotFoundException e) {
			fail("Could not load saved file");
			return;
		}
		
		assertTrue(checkEqualGame(game, savedNewGame));
	}

	@Test
	public void testLoadNonExistingFile() {
		assertThrows(
				FileNotFoundException.class,
				() -> game = saveHandler.load("foo"),
				"FileNotFoundException should be thrown when file does not exist!");
	}

	@Test
	public void testLoadInvalidFile() {
		assertThrows(
				Exception.class,
				() -> game = saveHandler.load("invalidtestsave.txt"),
				"An exception should be thrown if loaded file is invalid!");
	}

	@Test
	public void testSave() {
		try {
			saveHandler.save("newfile", game);
		} catch (FileNotFoundException e) {
			fail("Could not save file");
		}

		byte[] testFile = null, newFile = null;

		try {
			newFile = Files.readAllBytes(Path.of(SaveHandler.getFilePath("newfile.txt")));
		} catch (IOException e) {
			fail("Could not load new file");
		}

		try {
			testFile = Files.readAllBytes(Path.of(SaveHandler.getFilePath("testfile.txt")));
		} catch (IOException e) {
			fail("Could not load saved file");
		}
		assertNotNull(testFile);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(testFile, newFile));

	}

	
	@AfterAll
	static void teardown() {
		File newTestSaveFile = new File(SaveHandler.getFilePath("newfile.txt"));
		File testFile = new File(SaveHandler.getFilePath("testfile.txt"));

		newTestSaveFile.delete();
		testFile.delete();
	}

}
