package test.whitebox;

import grid.PathTile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PathTileTest {

    @Test
    void testInitializationAndType() {
        PathTile pathTile = new PathTile(1, 2);
        assertEquals(1, pathTile.getX());
        assertEquals(2, pathTile.getY());
        assertEquals(1, pathTile.getType()); // Always 1
    }

    @Test
    void testNextTile() {
        PathTile tile1 = new PathTile(0, 0);
        PathTile tile2 = new PathTile(1, 1);

        assertNull(tile1.getNextTile());
        tile1.setNextTile(tile2);
        assertEquals(tile2, tile1.getNextTile());
    }

    @Test
    void testToStringAndMutation() {
        PathTile tile = new PathTile(5, 6);
        tile.setX(7);
        tile.setY(8);
        assertEquals(7, tile.getX());
        assertEquals(8, tile.getY());
        assertEquals("1", tile.toString());
    }
}