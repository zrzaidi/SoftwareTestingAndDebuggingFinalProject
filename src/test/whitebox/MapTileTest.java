package test.whitebox;

import grid.MapTile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapTileTest {

    @Test
    void testInitialization() {
        MapTile tile = new MapTile(5, 6);

        assertEquals(5, tile.getX());
        assertEquals(6, tile.getY());
        assertEquals(0, tile.getType()); // Always 0 for MapTile
        assertEquals("0", tile.toString());
    }

    @Test
    void testMutation() {
        MapTile tile = new MapTile(0, 0);
        tile.setX(10);
        tile.setY(11);

        assertEquals(10, tile.getX());
        assertEquals(11, tile.getY());
    }
}