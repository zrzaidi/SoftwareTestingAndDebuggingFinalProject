package test.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import map.Map;
import grid.PathTile;
import grid.Tile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

public class MapMockTest {

    private Map testMap;

    @BeforeEach
    public void setup() {
        testMap = new Map();
        testMap.setMapSize(5, 5);
        testMap.initializeMap();
    }

    @Test
    public void testSetAndGetTile() {
        Tile tile = testMap.getTile(2, 2);
        assertNotNull(tile, "Tile at (2,2) should not be null");
    }

    @Test
    public void testPlaceEntryAndExit() {
        testMap.placeEntry(0, 0);
        testMap.placeExit(4, 4);
        assertEquals(2, testMap.getTile(0, 0).getType(), "Entry tile type should be 2");
        assertEquals(3, testMap.getTile(4, 4).getType(), "Exit tile type should be 3");
    }

    @Test
    public void testPathBuildingAndConversion() {
        Queue<PathTile> path = new LinkedList<>();
        path.add(new PathTile(0, 0));
        path.add(new PathTile(0, 1));
        path.add(new PathTile(0, 2));
        testMap.buildPath(path);

        assertEquals(2, testMap.getTile(0, 0).getType(), "First tile should be entry");
        assertEquals(3, testMap.getTile(0, 2).getType(), "Last tile should be exit");
        assertEquals(1, testMap.getTile(0, 1).getType(), "Middle tile should be a path tile");

        int[][] binaryMap = testMap.convertToBinaryMap(testMap);
        assertEquals(2, binaryMap[0][0]);
        assertEquals(1, binaryMap[1][0]);
        assertEquals(3, binaryMap[2][0]);
    }



    @Test
    public void testValidityOfMap() {
        testMap.placeEntry(0, 0);
        testMap.placeExit(4, 4);
        assertTrue(testMap.ValidityOfMap(), "Map with valid entry and exit should be valid");
    }
}
