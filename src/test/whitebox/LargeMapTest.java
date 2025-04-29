package test.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import grid.PathTile;
import grid.Tile;

public class LargeMapTest {
    
    @Test
    void testLargeMapConstructor() {
        map.LargeMap largeMap = new map.LargeMap();
        
        // Test dimensions
        assertEquals(20, largeMap.getWidthOfMap());
        assertEquals(18, largeMap.getHeightOfMap());
        assertEquals(20 * 32, largeMap.getWidthInPixel());
        assertEquals(18 * 32, largeMap.getHeightInPixel());
        
        // Test path input
        assertEquals("(0, 3) (16,3) (16,9) (5,9) (5,15) (19,15)", largeMap.getInputCorner());
        
        // Test if map is valid
        assertTrue(largeMap.ValidityOfMap());
        
        // Test entry and exit points
        PathTile entry = largeMap.getEntry();
        assertNotNull(entry);
        assertEquals(0, entry.getX());
        assertEquals(3, entry.getY());
        assertEquals(2, largeMap.getTile(0, 3).getType()); // Entry type = 2
        
        // Exit should be the last coordinate
        assertEquals(3, largeMap.getTile(19, 15).getType()); // Exit type = 3
        
        // Check path exists
//        assertPathExistsBetweenPoints(largeMap, 0, 3, 16, 3);
//        assertPathExistsBetweenPoints(largeMap, 16, 3, 16, 9);
//        assertPathExistsBetweenPoints(largeMap, 16, 9, 5, 9);
//        assertPathExistsBetweenPoints(largeMap, 5, 9, 5, 15);
//        assertPathExistsBetweenPoints(largeMap, 5, 15, 19, 15);
        
        // Test corner array
        int[][] cornerArray = largeMap.getCornersList();
        assertNotNull(cornerArray);
        assertEquals(6, cornerArray.length); // 6 corners in the path
        
        // Binary map conversion check
        int[][] binaryMap = largeMap.convertToBinaryMap(largeMap);
        assertNotNull(binaryMap);
        assertEquals(20, binaryMap[0].length);
        assertEquals(18, binaryMap.length);
    }
    
    private void assertPathExistsBetweenPoints(map.Map map, int x1, int y1, int x2, int y2) {
        // Horizontal path
        if (y1 == y2) {
            int start = Math.min(x1, x2);
            int end = Math.max(x1, x2);
            for (int x = start; x <= end; x++) {
                Tile tile = map.getTile(x, y1);
                assertTrue(tile.getType() > 0, "Path should exist at (" + x + "," + y1 + ")");
            }
        }
        // Vertical path
        else if (x1 == x2) {
            int start = Math.min(y1, y2);
            int end = Math.max(y1, y2);
            for (int y = start; y <= end; y++) {
                Tile tile = map.getTile(x1, y);
                assertTrue(tile.getType() > 0, "Path should exist at (" + x1 + "," + y + ")");
            }
        }
    }
}