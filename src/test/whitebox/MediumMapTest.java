package test.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import grid.PathTile;
import grid.Tile;

public class MediumMapTest {
    
    @Test
    void testMediumMapConstructor() {
    	map.MediumMap mediumMap = new map.MediumMap();
        
        // Test dimensions
        assertEquals(15, mediumMap.getWidthOfMap());
        assertEquals(12, mediumMap.getHeightOfMap());
        assertEquals(15 * 32, mediumMap.getWidthInPixel());
        assertEquals(12 * 32, mediumMap.getHeightInPixel());
        
        // Test path input
        assertEquals("(1, 11) (1,9) (12,9) (12,7) (4,7) (4,1) (14,1)", mediumMap.getInputCorner());
        
        // Test if map is valid
        assertTrue(mediumMap.ValidityOfMap());
        
        // Test entry and exit points
        PathTile entry = mediumMap.getEntry();
        assertNotNull(entry);
        assertEquals(1, entry.getX());
        assertEquals(11, entry.getY());
        assertEquals(2, mediumMap.getTile(1, 11).getType()); // Entry type = 2
        
        // Exit should be the last coordinate
        assertEquals(3, mediumMap.getTile(14, 1).getType()); // Exit type = 3
        
        // Check path exists
//        assertPathExistsBetweenPoints(mediumMap, 1, 11, 1, 9);
//        assertPathExistsBetweenPoints(mediumMap, 1, 9, 12, 9);
//        assertPathExistsBetweenPoints(mediumMap, 12, 9, 12, 7);
//        assertPathExistsBetweenPoints(mediumMap, 12, 7, 4, 7);
//        assertPathExistsBetweenPoints(mediumMap, 4, 7, 4, 1);
//        assertPathExistsBetweenPoints(mediumMap, 4, 1, 14, 1);
        
        // Test corner array
        int[][] cornerArray = mediumMap.getCornersList();
        assertNotNull(cornerArray);
        assertEquals(7, cornerArray.length); // 7 corners in the path
        
        // Binary map conversion check
        int[][] binaryMap = mediumMap.convertToBinaryMap(mediumMap);
        assertNotNull(binaryMap);
        assertEquals(15, binaryMap[0].length);
        assertEquals(12, binaryMap.length);
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