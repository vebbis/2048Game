package tyvefortiatte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TileTest {
    
    /*
    It is a lot of tests, but they have covered every faults when I
    have improved the code and made mistakes every here and then
    */
    @Test
    @DisplayName("Testing that the right values are returned when a tile is initialized")
    public void testConstructor(){
        ITile defaultTile = new Tile();
        assertEquals("-fx-background-color: silver;", defaultTile.getColor());
        assertEquals("-fx-fill: silver;", defaultTile.getFont());
        assertEquals(0, defaultTile.getValue());

        ITile tile2 = new Tile(2);
        assertEquals("-fx-background-color: #776e65;", tile2.getColor());
        assertEquals("-fx-fill: black;", tile2.getFont());
        assertEquals(2, tile2.getValue());

        ITile tile4 = new Tile(4);
        assertEquals("-fx-background-color: wheat;", tile4.getColor());
        assertEquals("-fx-fill: black;", tile4.getFont());
        assertEquals(4, tile4.getValue());

        ITile tile8 = new Tile(8);
        assertEquals("-fx-background-color: #f2b179;", tile8.getColor());
        assertEquals("-fx-fill: white;", tile8.getFont());
        assertEquals(8, tile8.getValue());

        ITile tile16 = new Tile(16);
        assertEquals("-fx-background-color: #f59563;", tile16.getColor());
        assertEquals("-fx-fill: white;", tile16.getFont());
        assertEquals(16, tile16.getValue());

        ITile tile32 = new Tile(32);
        assertEquals("-fx-background-color: #f67c5f;", tile32.getColor());
        assertEquals("-fx-fill: white;", tile32.getFont());
        assertEquals(32, tile32.getValue());

        ITile tile64 = new Tile(64);
        assertEquals("-fx-background-color: #f65e3b;", tile64.getColor());
        assertEquals("-fx-fill: white;", tile64.getFont());
        assertEquals(64, tile64.getValue());

        ITile tile128 = new Tile(128);
        assertEquals("-fx-background-color: #edcf72;", tile128.getColor());
        assertEquals("-fx-fill: white;", tile128.getFont());
        assertEquals(128, tile128.getValue());

        ITile tile256 = new Tile(256);
        assertEquals("-fx-background-color: #edcc61;", tile256.getColor());
        assertEquals("-fx-fill: white;", tile256.getFont());
        assertEquals(256, tile256.getValue());

        ITile tile512 = new Tile(512);
        assertEquals("-fx-background-color: #edc850;", tile512.getColor());
        assertEquals("-fx-fill: white;", tile512.getFont());
        assertEquals(512, tile512.getValue());

        ITile tile1024 = new Tile(1024);
        assertEquals("-fx-background-color: #edc53f;", tile1024.getColor());
        assertEquals("-fx-fill: white;", tile1024.getFont());
        assertEquals(1024, tile1024.getValue());

        ITile tile2048 = new Tile(2048);
        assertEquals("-fx-background-color: #edc22e;", tile2048.getColor());
        assertEquals("-fx-fill: white;", tile2048.getFont());
        assertEquals(2048, tile2048.getValue());     
    }

    
    @Test
    @DisplayName("Test that only valid numbers 2, 4, 8,....,2048 are accepted when a tile is initialized")
    public void testInvalidNumbers(){
        assertThrows(IllegalArgumentException.class, () -> new Tile(-1));
        assertThrows(IllegalArgumentException.class, () -> new Tile(3));
        assertThrows(IllegalArgumentException.class, () -> new Tile(127));
        assertThrows(IllegalArgumentException.class, () -> new Tile(4096));
        assertThrows(IllegalArgumentException.class, () -> new Tile(-4));
    }


    @Test
    @DisplayName("Testing that only tiles with equal values can merge")
    public void  testisMergePossible(){
        
        assertTrue(new Tile(0).isMergePossible(new Tile(0)));

        for (int i = 1; i < 12; i++) {
            assertTrue(new Tile((int) Math.pow(2, i)).isMergePossible(new Tile((int) Math.pow(2, i))),
            String.format("%d shall only merge with equal valued tiles", (int) Math.pow(2, i)));
        }

        for (int i = 1; i < 12; i++) {
            assertFalse(new Tile((int) Math.pow(2, i)).isMergePossible(new Tile(0)),
            String.format("%d shall not merge with 0", (int) Math.pow(2, i)));
        }

        List<ITile> listOfTiles = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            listOfTiles.add(new Tile((int) Math.pow(2, i)));
        }

        listOfTiles.add(new Tile(0));

        for (int i = 1; i < 12; i++){

        listOfTiles.remove(0);


        for (ITile tile : listOfTiles) {
            assertFalse(new Tile((int) Math.pow(2, i)).isMergePossible(tile),
            String.format("%d shall only merge with an equal valued tile, merging the value %d shall not be possible"
            ,(int) Math.pow(2, i), tile.getValue()));

        }

        listOfTiles.add(new Tile((int) Math.pow(2, i)));
    }

    }
}
