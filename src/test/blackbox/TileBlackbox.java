package test.blackbox;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import grid.MapTile;
import grid.PathTile;
import grid.Tile;

class TileBlackbox {

    // Tile

    @Test
    public void testSetAndGetXEdgeCases() {
        Tile tile = new PathTile(0, 0);
        tile.setX(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, tile.getX());

        tile.setX(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, tile.getX());
    }

    @Test
    public void testSetAndGetYEdgeCases() {
        Tile tile = new PathTile(0, 0);
        tile.setY(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, tile.getY());

        tile.setY(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, tile.getY());
    }

    @Test
    public void testSetTypeNegative() {
        Tile tile = new PathTile(0, 0);
        tile.setType(-1);
        assertEquals(-1, tile.getType(), "Should accept negative types.");
    }

    @Test
    public void testSetTypeExtreme() {
        Tile tile = new PathTile(0, 0);
        tile.setType(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, tile.getType(), "Should accept large positive types.");
    }

    @Test
    public void testToStringReflectsTypeChange() {
        Tile tile = new PathTile(0, 0);
        tile.setType(42);
        assertEquals("42", tile.toString());
    }

    // PathTile

    @Test
    public void testDefaultNextTileIsNull() {
        PathTile tile = new PathTile(5, 5);
        assertNull(tile.getNextTile(), "Next tile should initially be null.");
    }

    @Test
    public void testSetNextTileToNull() {
        PathTile tile = new PathTile(0, 0);
        tile.setNextTile(null);
        assertNull(tile.getNextTile(), "Explicitly setting nextTile to null should be supported.");
    }

    @Test
    public void testSelfReferentialNextTile() {
        PathTile tile = new PathTile(1, 1);
        tile.setNextTile(tile);
        assertSame(tile, tile.getNextTile(), "Tile should be able to reference itself.");
    }

    @Test
    public void testPathTileWithNegativeCoordinates() {
        PathTile tile = new PathTile(-10, -20);
        assertEquals(-10, tile.getX());
        assertEquals(-20, tile.getY());
    }

    // MapTile

    @Test
    public void testMapTileNegativeCoordinates() {
        MapTile tile = new MapTile(-100, -200);
        assertEquals(-100, tile.getX());
        assertEquals(-200, tile.getY());
        assertEquals(0, tile.getType(), "MapTile should default type to 0.");
    }

    @Test
    public void testMapTileExtremeCoordinates() {
        MapTile tile = new MapTile(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, tile.getX());
        assertEquals(Integer.MIN_VALUE, tile.getY());
    }

    @Test
    public void testMapTileToStringAfterChangingType() {
        MapTile tile = new MapTile(0, 0);
        tile.setType(9);
        assertEquals("9", tile.toString(), "toString should reflect updated type.");
    }

}
