package test.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import grid.PathTile;
import grid.Tile;

public class SmallMapTest {
    
    @Test
    void testSmallMapConstructor() {
    	map.SmallMap smallMap = new map.SmallMap();
        
        // Test dimensions
        assertEquals(12, smallMap.getWidthOfMap());
        assertEquals(12, smallMap.getHeightOfMap());
        assertEquals(12 * 32, smallMap.getWidthInPixel());
        assertEquals(12 * 32, smallMap.getHeightInPixel());
        
        // Test path input
        assertEquals("(0,2) (3,2) (3,9) (8,9) (8,3) (11,3)", smallMap.getInputCorner());
        
        // Test if map is valid
        assertTrue(smallMap.ValidityOfMap());
        
        // Test entry and exit points
        PathTile entry = smallMap.getEntry();
        assertNotNull(entry);
        assertEquals(0, entry.getX());
        assertEquals(2, entry.getY());
        assertEquals(2, smallMap.getTile(0, 2).getType()); // Entry type = 2
        
        // Exit should be the last coordinate
        assertEquals(3, smallMap.getTile(11, 3).getType()); // Exit type = 3
        
        // Check path exists
//        assertPathExistsBetweenPoints(smallMap, 0, 2, 3, 2);
//        assertPathExistsBetweenPoints(smallMap, 3, 2, 3, 9);
//        assertPathExistsBetweenPoints(smallMap, 3, 9, 8, 9);
//        assertPathExistsBetweenPoints(smallMap, 8, 9, 8, 3);
//        assertPathExistsBetweenPoints(smallMap, 8, 3, 11, 3);
        
        // Test corner array
        int[][] cornerArray = smallMap.getCornersList();
        assertNotNull(cornerArray);
        assertEquals(6, cornerArray.length); // 6 corners in the path
        
        // Binary map conversion check
        int[][] binaryMap = smallMap.convertToBinaryMap(smallMap);
        assertNotNull(binaryMap);
        assertEquals(12, binaryMap.length);
        assertEquals(12, binaryMap[0].length);
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