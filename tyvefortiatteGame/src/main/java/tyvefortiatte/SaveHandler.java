package tyvefortiatte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * This class is inspired by the snake bird example from the TDT4100 course content.
 * When the game is saved, the value of the tile and its position are written to file.
 * When the game is loaded, the program will read tile by tile and initialize a new game.
 * The score and the best score are also saved and loaded, because two identical boards can have different score.
 */

public class SaveHandler implements ISaveHandler  {

	public void save(String filename, Game game) throws FileNotFoundException {

		if(!Pattern.matches("[a-z]{1,9}", filename)){
			throw new FileNotFoundException("Filnavnet er ikke gyldig");
		}

		try (PrintWriter writer = new PrintWriter(new File(getFilePath(filename + ".txt")))) {
			
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					writer.print(game.getGameGrid()[y][x] + " ");
				}
			}
			writer.println();

			writer.print(game.getScore());
			writer.println();

			writer.print(game.getBestScore());
			writer.println();		
		}
	}

	public Game load(String filename) throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {
			

			Tile[][] gridList = new Tile[4][4];
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					gridList[y][x] = new Tile(scanner.nextInt());
				}
			}

			Game game = new Game(gridList, scanner.nextInt(),scanner.nextInt());


			

			return game;
		}
	}

	public static String getFilePath(String filename) {
		
		return SaveHandler.class.getResource("saves/").getFile() + filename;
		
	}

	
	public List<String> getFileDirectory() {
		
		File dir = new File(SaveHandler.class.getResource("saves/").getFile());

		/*
		The folder contains .gitinclude because it cannot be empty.
		Since .gitinclude is not a saved game, it is removed when the
		file directory is returned.
		*/
		List<String> files = new ArrayList<>(List.of(dir.list()));
		if((files).contains(".gitinclude")){
			files.remove(".gitinclude");
		}

		return files;
		
	}

	


}

