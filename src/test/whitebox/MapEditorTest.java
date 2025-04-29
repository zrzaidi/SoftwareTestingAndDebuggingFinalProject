package test.whitebox;

import grid.PathTile;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class MapEditorTest {

    @Test
    void testMapInitialization() {
    	map.Map map = new map.Map();
        map.setMapSize(10, 10);
        map.initializeMap();

        assertEquals(10, map.getWidthOfMap());
        assertEquals(10, map.getHeightOfMap());
        assertNotNull(map.getTile(0, 0));
    }

    @Test
    void testEntryAndExitPlacement() {
    	map.Map map = new map.Map();
        map.setMapSize(5, 5);
        map.initializeMap();

        map.placeEntry(0, 0);
        map.placeExit(4, 4);

        assertTrue(map.getTile(0, 0) instanceof PathTile);
        assertTrue(map.getTile(4, 4) instanceof PathTile);
        assertEquals(2, map.getTile(0, 0).getType()); // Entry type
        assertEquals(3, map.getTile(4, 4).getType()); // Exit type
    }

    @Test
    void testMultipleCoordinatesSplitAndBuildPath() {
    	map.Map map = new map.Map();
        map.setMapSize(6, 6);
        map.initializeMap();

        String input = "(0,0) (0,1) (0,2) (0,3)";
        Queue<PathTile> path = map.multipleCoordinatesSplit(input);
        map.buildPath(path);

        // Entry check
        assertEquals(2, map.getTile(0, 0).getType());

        // Intermediate path tiles should be path tiles
        assertTrue(map.getTile(0, 1) instanceof PathTile);
        assertTrue(map.getTile(0, 2) instanceof PathTile);

        // Exit check
        assertEquals(3, map.getTile(0, 3).getType());
    }

    @Test
    void testCornerArrayConversion() {
    	map.Map map = new map.Map();
        map.setMapSize(10, 10);
        map.initializeMap();

        String input = "(0,0) (1,0) (1,1)";
        Queue<PathTile> path = map.multipleCoordinatesSplit(input);
        int[][] corners = map.cornerArray(path);

        assertEquals(3, corners.length);
        assertEquals(0, corners[0][0]); // Left edge correction
        assertTrue(corners[1][0] > 0);
        assertTrue(corners[2][1] > 0);
    }
}