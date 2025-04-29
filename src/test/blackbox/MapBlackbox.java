package test.blackbox;

import static org.junit.jupiter.api.Assertions.*;

import map.*;
import org.junit.jupiter.api.Test;

class MapBlackbox {

    @Test
    public void testSmallMapSize() {
        SmallMap map = new SmallMap();
        assertEquals(12, map.getWidthOfMap());
        assertEquals(12, map.getHeightOfMap());
    }

    @Test
    public void testMediumMapSize() {
        MediumMap map = new MediumMap();
        assertEquals(15, map.getWidthOfMap());
        assertEquals(12, map.getHeightOfMap());
    }

    @Test
    public void testMapPixelDimensions() {
        SmallMap map = new SmallMap();
        assertEquals(12 * 32, map.getWidthInPixel());
        assertEquals(12 * 32, map.getHeightInPixel());
    }

    @Test
    public void testMapEntryPointExists() {
        SmallMap map = new SmallMap();
        assertNotNull(map.getEntry());
    }

    @Test
    public void testMapEditorStoresValidInput() {
        String input = "(0,0) (0,1) (0,2)";
        MapEditor editor = new MapEditor(10, 10, input);
        assertEquals(10, editor.getWidthFromFile());
        assertEquals(10, editor.getHeightFromFile());
        assertEquals(input, editor.getUserInput());
    }

    @Test
    public void testCornerArrayHasExpectedFormat() {
        MediumMap map = new MediumMap();
        int[][] corners = map.getCornersList();
        assertNotNull(corners);
        for (int[] corner : corners) {
            assertEquals(2, corner.length);
        }
    }

    @Test
    public void testToStringReturnsNonNull() {
        SmallMap map = new SmallMap();
        assertNotNull(map.toString());
    }

    @Test
    public void testMapEditorWithEmptyInput() {
    	try {
    		MapEditor editor = new MapEditor(5, 5, "");
    		assertEquals("", editor.getUserInput());
            assertNotNull(editor.getUserInputFromFile());
    	} catch (Exception e) {
    		// Should throw some sort of exception
    	}
    }

    @Test
    public void testMapEditorWithNullInput() {
    	try {
    		MapEditor editor = new MapEditor(5, 5, null);
    		assertNull(editor.getUserInput());
    		assertNotNull(editor.getUserInputFromFile());
    	} catch (Exception e) {
    		// Should throw exception
    	}
    }

    @Test
    public void testMapEditorWithMalformedPath() {
    	try {
    		String input = "(0,0) (not_a_coord) (5,5)";
    		MapEditor editor = new MapEditor(10, 10, input);
    		assertEquals(input, editor.getUserInput());
    	} catch (Exception e) {
    		//Should throw error
    	}
    }

    @Test
    public void testEditorWithNegativeCoordinates() {
    	try {
    		String input = "(-1,0) (0,-1)";
    		MapEditor editor = new MapEditor(5, 5, input);
    		assertTrue(editor.getUserInput().contains("-1"));
    	} catch (Exception e) {
    		// Should throw error
    	}
    }

    @Test
    public void testEditorWithOutOfBoundsCoordinates() {
        String input = "(100,100)";
        MapEditor editor = new MapEditor(5, 5, input);
        assertFalse(editor.getUserInput().contains("100")); // Should fail cause out of bounds
    }

    @Test
    public void testZeroByZeroMap() {
        MapEditor editor = new MapEditor(0, 0, "(0,0)");
        assertEquals(0, editor.getWidthFromFile());
        assertEquals(0, editor.getHeightFromFile());
    }

}
