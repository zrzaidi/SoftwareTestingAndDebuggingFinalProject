package test.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grid.PathTile;
import grid.Tile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class MapTest {
    private map.Map map;
    
    @BeforeEach
    void setUp() {
        map = new map.Map();
    }
    
    @Test
    void testConstructor() {
        assertEquals(0, map.getWidthOfMap());
        assertEquals(0, map.getHeightOfMap());
        assertEquals("", map.getInputCorner());
        assertTrue(map.ValidityOfMap());
    }
    
    @Test
    void testSetMapSize() {
        map.setMapSize(10, 15);
        assertEquals(10, map.getWidthOfMap());
        assertEquals(15, map.getHeightOfMap());
        assertEquals(10 * 32, map.getWidthInPixel());
        assertEquals(15 * 32, map.getHeightInPixel());
    }
    
    @Test
    void testSetInputCorner() {
        map.setInputCorner("(0,0) (5,5)");
        assertEquals("(0,0) (5,5)", map.getInputCorner());
    }
    
    @Test
    void testInitializeMap() {
        map.setMapSize(5, 5);
        map.initializeMap();
        
        // Check if all tiles are initialized as MapTile
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Tile tile = map.getTile(i, j);
                assertNotNull(tile);
                assertEquals(i, tile.getX());
                assertEquals(j, tile.getY());
            }
        }
        
        // Test with invalid dimensions
        map.Map invalidMap = new map.Map();
        invalidMap.setMapSize(0, 0);
        invalidMap.initializeMap();
        assertNull(invalidMap.getTile(0, 0));
    }
    
    @Test
    void testGetTile() {
        map.setMapSize(5, 5);
        map.initializeMap();
        
        // Valid coordinates
        assertNotNull(map.getTile(2, 3));
        
        // Invalid coordinates should return null
        assertNull(map.getTile(10, 10));
    }
    
    @Test
    void testGetPixelSize() {
        assertEquals(32, map.getPixelSize());
    }
    
    @Test
    void testPlaceEntryAndGetEntry() {
        map.setMapSize(5, 5);
        map.initializeMap();
        map.placeEntry(0, 0);
        
        Tile entryTile = map.getTile(0, 0);
        assertEquals(2, entryTile.getType());
        assertTrue(entryTile instanceof PathTile);
        
        PathTile entry = map.getEntry();
        assertEquals(0, entry.getX());
        assertEquals(0, entry.getY());
    }
    
    @Test
    void testPlaceExit() {
        map.setMapSize(5, 5);
        map.initializeMap();
        map.placeExit(4, 4);
        
        Tile exitTile = map.getTile(4, 4);
        assertEquals(3, exitTile.getType());
        assertTrue(exitTile instanceof PathTile);
    }
    
    @Test
    void testPlacePathPoint() {
        map.setMapSize(5, 5);
        map.initializeMap();
        map.placePathPoint(2, 2);
        
        assertTrue(map.getTile(2, 2) instanceof PathTile);
        
        // Test placing path on an already path tile
        map.placePathPoint(2, 2);
        assertTrue(map.getTile(2, 2) instanceof PathTile);
    }
    
    @Test
    void testArrangePathPoint() {
        ArrayList<Integer> pathPoints = new ArrayList<>();
        pathPoints.add(0);
        pathPoints.add(0);
        pathPoints.add(5);
        pathPoints.add(5);
        
        String result = map.arrangePathPoint(pathPoints);
        assertEquals("(0,0) (5,5) ", result);
        assertEquals(result, map.getInputCorner());
    }
    
    @Test
    void testMultipleCoordinatesSplit() {
        // Test with valid input
        Queue<PathTile> path = map.multipleCoordinatesSplit("(0,0) (5,5)");
        assertNotNull(path);
        assertEquals(2, path.size());
        
        PathTile first = path.poll();
        assertEquals(0, first.getX());
        assertEquals(0, first.getY());
        
        PathTile second = path.poll();
        assertEquals(5, second.getX());
        assertEquals(5, second.getY());
        
        // Test with empty input
        path = map.multipleCoordinatesSplit("");
        assertNull(path);
    }
    
    @Test
    void testCornerArray() {
        map.setMapSize(10, 10);
        
        Queue<PathTile> path = new LinkedList<>();
        path.add(new PathTile(0, 0));  // Entry at left edge
        path.add(new PathTile(5, 0));
        path.add(new PathTile(9, 9));  // Exit
        
        int[][] cornerArray = map.cornerArray(path);
        
        // First point (entry) - edge case
        assertEquals(0, cornerArray[0][0]); // x=0 is on left edge
        assertEquals(16, cornerArray[0][1]); // y centered
        
        // Middle point
        assertEquals(5 * 32 + 16, cornerArray[1][0]);
        assertEquals(0 * 32 + 16, cornerArray[1][1]);
        
        // Last point
        assertEquals(9 * 32 + 16, cornerArray[2][0]);
        assertEquals(9 * 32 + 16, cornerArray[2][1]);
        
        // Test getting the corner list
        int[][] retrievedCorners = map.getCornersList();
        assertArrayEquals(cornerArray, retrievedCorners);
    }
    
    @Test
    void testBuildPathEmptyOrNull() {
        map.setMapSize(5, 5);
        map.initializeMap();
        
        // Test with null path
        map.buildPath(null);
        
        // Test with empty path
        Queue<PathTile> emptyPath = new LinkedList<>();
        map.buildPath(emptyPath);
    }
    
    @Test
    void testBuildPathValid() {
        map.setMapSize(10, 10);
        map.initializeMap();
        
        Queue<PathTile> path = new LinkedList<>();
        path.add(new PathTile(0, 0));  // Entry
        path.add(new PathTile(0, 5));  // Path point
        path.add(new PathTile(9, 5));  // Path point
        path.add(new PathTile(9, 9));  // Exit
        
        map.buildPath(path);
        
        // Check entry point
        assertEquals(2, map.getTile(0, 0).getType());
        
        // Check path points
        assertTrue(map.getTile(0, 1) instanceof PathTile);
        assertTrue(map.getTile(0, 5) instanceof PathTile);
        assertTrue(map.getTile(5, 5) instanceof PathTile);
        
        // Check exit point
        assertEquals(3, map.getTile(9, 9).getType());
    }
    
    @Test
    void testLinkTwoPoints() {
        map.setMapSize(10, 10);
        map.initializeMap();
        
        // Test horizontal connection (same y)
        PathTile start = new PathTile(1, 1);
        PathTile end = new PathTile(5, 1);
        map.placePathPoint(start.getX(), start.getY());
        map.linkTwoPoints(start, end);
        
        for (int i = start.getX(); i <= end.getX(); i++) {
            assertTrue(map.getTile(i, 1) instanceof PathTile);
        }
        
        // Test vertical connection (same x)
        start = new PathTile(7, 2);
        end = new PathTile(7, 6);
        map.placePathPoint(start.getX(), start.getY());
        map.linkTwoPoints(start, end);
        
        for (int j = start.getY(); j <= end.getY(); j++) {
            assertTrue(map.getTile(7, j) instanceof PathTile);
        }
        
        // Test reverse directions
        // Horizontal right to left
        start = new PathTile(5, 8);
        end = new PathTile(2, 8);
        map.placePathPoint(start.getX(), start.getY());
        map.linkTwoPoints(start, end);
        
        for (int i = end.getX(); i <= start.getX(); i++) {
            assertTrue(map.getTile(i, 8) instanceof PathTile);
        }
        
        // Vertical bottom to top
        start = new PathTile(9, 8);
        end = new PathTile(9, 3);
        map.placePathPoint(start.getX(), start.getY());
        map.linkTwoPoints(start, end);
        
        for (int j = end.getY(); j <= start.getY(); j++) {
            assertTrue(map.getTile(9, j) instanceof PathTile);
        }
        
        // Test diagonal connection (should place only the endpoints)
        start = new PathTile(1, 1);
        end = new PathTile(3, 3);
        map.linkTwoPoints(start, end);
        
        assertTrue(map.getTile(1, 1) instanceof PathTile);
        assertTrue(map.getTile(3, 3) instanceof PathTile);
    }
    
    @Test
    void testConvertToBinaryMap() {
        map.setMapSize(5, 5);
        map.initializeMap();
        
        // Create a simple path
        Queue<PathTile> path = new LinkedList<>();
        path.add(new PathTile(0, 0));  // Entry
        path.add(new PathTile(0, 4));  // Path point
        path.add(new PathTile(4, 4));  // Exit
        
        map.buildPath(path);
        
        int[][] binaryMap = map.convertToBinaryMap(map);
        
        // Check entry point (type 2)
        assertEquals(2, binaryMap[0][0]);
        
        // Check path points (type 1)
        for (int i = 1; i < 4; i++) {
            assertEquals(1, binaryMap[i][0]);
        }
        
        // Check exit point (type 3)
        assertEquals(3, binaryMap[4][4]);
        
        // Check non-path points (type 0)
        assertEquals(0, binaryMap[2][2]);
    }
    
    @Test
    void testConvertFromBinaryMap() {
        int[][] binaryMap = {
            {2, 0, 0, 0, 0},
            {1, 0, 0, 0, 0},
            {1, 1, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 1, 3}
        };
        
        map.Map newMap = map.convertFromBinaryMap(binaryMap);
        
        // Check dimensions
        assertEquals(5, newMap.getWidthOfMap());
        assertEquals(5, newMap.getHeightOfMap());
        
        // Check entry point (type 2)
        assertEquals(2, newMap.getTile(0, 0).getType());
        
        // Check path points (type 1)
        assertEquals(1, newMap.getTile(0, 1).getType());
        assertEquals(1, newMap.getTile(1, 2).getType());
        
        // Check exit point (type 3)
        assertEquals(3, newMap.getTile(4, 4).getType());
        
        // Check non-path points (type 0)
        assertEquals(0, newMap.getTile(2, 0).getType());
    }
    
    @Test
    void testValidityOfMap() {
        map.setMapSize(5, 5);
        map.initializeMap();
        
        // By default, the map should be valid
        assertTrue(map.ValidityOfMap());
    }
    
    @Test
    void testToString() {
        map.setMapSize(3, 2);
        map.initializeMap();
        map.placeEntry(0, 0);
        map.placeExit(2, 1);
        map.placePathPoint(1, 0);
        map.placePathPoint(1, 1);
        map.placePathPoint(2, 0);
        
        String mapString = map.toString();
        assertNotNull(mapString);
        assertTrue(mapString.contains("\n"));
    }
    
    @Test
    void testCornerArrayEdgeCases() {
        map.setMapSize(10, 10);
        
        // Test entry on right edge
        Queue<PathTile> path = new LinkedList<>();
        path.add(new PathTile(9, 5));  // Entry at right edge
        path.add(new PathTile(5, 5));  // Middle point
        
        int[][] cornerArray = map.cornerArray(path);
        assertEquals(10 * 32, cornerArray[0][0]); // x=9 is on right edge
        
        // Test entry on top edge
        path.clear();
        path.add(new PathTile(5, 0));  // Entry at top edge
        path.add(new PathTile(5, 5));  // Middle point
        
        cornerArray = map.cornerArray(path);
        assertEquals(5 * 32 + 16, cornerArray[0][0]);
        assertEquals(0 * 32 + 16, cornerArray[0][1]);
    }
    
    @Test
    void testLinkTwoPointsSpecialCases() {
        map.setMapSize(10, 10);
        map.initializeMap();
        
        // Test when points are the same
        PathTile start = new PathTile(5, 5);
        PathTile end = new PathTile(5, 5);
        map.placePathPoint(start.getX(), start.getY());
        map.linkTwoPoints(start, end);
        
        // Check that the point exists
        assertTrue(map.getTile(5, 5) instanceof PathTile);
    }
}