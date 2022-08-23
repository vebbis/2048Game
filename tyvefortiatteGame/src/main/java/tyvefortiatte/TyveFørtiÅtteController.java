package tyvefortiatte;

import java.io.FileNotFoundException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/*
The reason for the frequent use of anchorPane.requestFocus() is because after 
a button is clicked on, the keyboard arrows switch between buttons instead of 
moving the board
*/

public class TyveFørtiÅtteController {
    
private ISaveHandler saveHandler = new SaveHandler();
private Game game;

@FXML 
private AnchorPane anchorPane;

@FXML
private Pane invalidFilenamePane;

@FXML 
private Pane tileGridPane;

@FXML 
private Pane saveGamePane;

@FXML
private ListView<String> savedFilesListView;

@FXML
private Button closeSavedFilesListView;

@FXML 
private Text scoreText;

@FXML
private Text bestScoreText;

@FXML 
private Text bestTileText;

@FXML
private Text gameStatusPaneText;

@FXML
private Pane tileGrid;

@FXML 
private Pane gameStatusPane;

@FXML
private TextField filenameTextInput;


@FXML
private void initialize() {
    
    game = new Game();
    initializeTileGrid();
    updateViewStats();

    anchorPane.requestFocus();

    System.out.println("Startgrid: ");
    game.printGrid();
    System.out.println();   
}

/** 
Starting the game over, reseting the score and tiles 
*/
@FXML
private void newGame() {
    System.out.println("-----"+"\n"+"New Game knapp trykket"+"\n"+"-----");
    game.startOver();
    updateViewStats();
    initializeTileGrid();
    anchorPane.requestFocus();
    gameStatusPane.setVisible(false);
}


/**
 * @return the filename entered in the view
 */
private String getFilename() {  
    return this.filenameTextInput.getText();
}


/**
 * Sets the pane containing the inputfield for saving when
 * the "save game" button is clicked in the view
 */
@FXML
private void handleSave() {
    System.out.println("Save Game knapp trykket");
    saveGamePane.setVisible(true);
}

/**
 * Tries to save the file with the entered filename from the user,
 * if the entered filename does not meet the demands for a valid filename, 
 * an exeption is caught and notifies the user by setting the invalidFileNamePane visible
 */
@FXML
private void saveFile() {
    
    try {
        saveHandler.save(getFilename(), game);
        invalidFilenamePane.setVisible(false);
        saveGamePane.setVisible(false);
        anchorPane.requestFocus();

        /*
        * If the saved files list view is visible when a file is saved, 
        * the listView has to be updated to ensure real time updates of the list
        */
        if(savedFilesListView.isVisible()){
            handleLoad();
        }

        
    } catch (FileNotFoundException e) {
        System.out.println(e);
        invalidFilenamePane.setVisible(true);
    }   
}

/**
 * Tries to load the file cliked on in the list view containing
 * all the files saved by the user
 */
@FXML 
private void handleClickedOnSavedFile() {
    String fileClikedOn = savedFilesListView.getSelectionModel().getSelectedItem();
    System.out.println("clicked on " + fileClikedOn);

    try {
        System.out.println("Initialiserer nytt spill fra " + fileClikedOn);
        game = saveHandler.load(fileClikedOn);
        initializeTileGrid();
        updateViewStats();
        anchorPane.requestFocus();
        savedFilesListView.setVisible(false);
        closeSavedFilesListView.setVisible(false);


    } catch (FileNotFoundException e) {
        System.out.println("Fant ikke filen");
    }

}

/**
 * Syncronizes the save files list view with the
 * folder where files are saved
 */
private void updateSavedFilesListView(){
    ObservableList<String> savedFilesObservableList = FXCollections.observableArrayList();
    List<String> savedFiles = saveHandler.getFileDirectory();
    System.out.println(savedFiles);

    
    for (String file : savedFiles) {
        if(!savedFilesObservableList.contains(file)){
            savedFilesObservableList.add(file);
        }
    }
    savedFilesListView.setItems(savedFilesObservableList);
}

/**
 * Sets the pane containing the list view displaying the saved files visible
 * when the "Load Game" button is clicked in the view
 */
@FXML
private void handleLoad() {
    System.out.println("Load game from:");

    updateSavedFilesListView();

    savedFilesListView.setVisible(true);   
    closeSavedFilesListView.setVisible(true);

}

/** 
Closes the pane displaying the saved files
*/ 
@FXML
private void closeSaveGamePane(){
    saveGamePane.setVisible(false);
    invalidFilenamePane.setVisible(false);
    anchorPane.requestFocus();
}

/** 
Closes the pane for saving the game to file
*/ 
@FXML
private void closeSavedFilesListView(){
    savedFilesListView.setVisible(false);
    closeSavedFilesListView.setVisible(false);
    anchorPane.requestFocus();
}

/**
 * Creating new tiles in the view
 * by initializing a Pane for each tile 
 */
private void initializeTileGrid() {
   
    double paneWidth = tileGridPane.getPrefWidth();
    double paneHeight = tileGridPane.getPrefHeight();

    double tileSizeX = (paneWidth / 4);
    double tileSizeY = (paneHeight / 4);

    for (int y = 0; y < 4; y++) {
        for (int x = 0; x < 4; x++) {
            Pane tile = new Pane();
            tile.setTranslateY(tileSizeY * y + 4);
            tile.setTranslateX(tileSizeX * x + 4);
            tile.setPrefHeight(tileSizeY - 8);
            tile.setPrefWidth(tileSizeX - 8);
            tile.setStyle(game.getGameGrid()[y][x].getColor());
			
            Text t = new Text(tileSizeX/2,tileSizeY/2,Integer.toString(game.getGameGrid()[y][x].getValue()));
            t.setStyle(game.getGameGrid()[y][x].getFont());
            tile.getChildren().add(t);
            
			tileGridPane.getChildren().add(tile);         
        }
    }
}

/**
 * Syncronizes the score, best score and best tile with the grid
 * and checks if the game is over or won
 */
private void updateViewStats(){
    scoreText.setText(Integer.toString(game.getScore()));
    bestScoreText.setText(Integer.toString(game.getBestScore()));
    bestTileText.setText(Integer.toString(game.getGreatestTileValue()));
    checkForGameOver();
    checkForGameWon();
    
    if(! (game.isGameOver() || game.isGameWon())){
        gameStatusPane.setVisible(false);
    }
}

private void checkForGameOver(){
    if(game.isGameOver()){
        System.out.println("Game over");
        gameStatusPaneText.setText("Game Over");
        gameStatusPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
        gameStatusPane.setVisible(true);
    }
}

private void checkForGameWon(){
    if(game.isGameWon()){
        System.out.println("Game Won");
        gameStatusPaneText.setText("Game Won");
        gameStatusPane.setStyle("-fx-background-color: rgba(0, 255, 0, 0.5);");
        gameStatusPane.setVisible(true);
    }   
}

/**
 * If the game is not over and the param is a valid move direction,
 * the tiles are moved in the direction specified by the param
 * @param code the KeyCode for the key clicked on the users keyboard
 */
public void handleKeyPress(KeyCode code) {
    
   if(!game.isGameOver()){
    
    game.moveTileGrid(code);

    initializeTileGrid();
    updateViewStats();

    anchorPane.requestFocus();}
    
    else{System.out.println("Game over, men tastatur trykket.");}
        
}



    

}
