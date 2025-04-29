package test.whitebox;

import grid.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DummyTile extends Tile {
    public DummyTile(int x, int y) {
        super(x, y);
    }
}

public class TileTest {

    @Test
    void testCoordinatesAndType() {
        DummyTile tile = new DummyTile(3, 4);

        assertEquals(3, tile.getX());
        assertEquals(4, tile.getY());

        tile.setX(8);
        tile.setY(9);
        assertEquals(8, tile.getX());
        assertEquals(9, tile.getY());

        tile.setType(5);
        assertEquals(5, tile.getType());
        assertEquals("5", tile.toString());
    }
}