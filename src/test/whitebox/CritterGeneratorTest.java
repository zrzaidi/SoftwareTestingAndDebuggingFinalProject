package test.whitebox;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Queue;

class CritterGeneratorTest {

    private critters.CritterGenerator generator;
    private int[][] path = {
            {0, 0}, {10, 0}, {10, 10}
    };

    @BeforeEach
    void setUp() {
        generator = new critters.CritterGenerator(path, 1); // level 1 => index 0
    }

    // ==== Unit Tests ====

    @Test
    void testAddCritterListSizeIs1000() {
        int[][] result = generator.addCritterList(12);
        assertEquals(1000, result.length);
    }

    @Test
    void testAddCritterListEachRowHasFiveElements() {
        int[][] result = generator.addCritterList(12);
        for (int[] row : result) {
            assertEquals(5, row.length);
        }
    }

    @Test
    void testGetCritterQueueReturnsNonNull() {
        generator.createCritterQueue();
        assertNotNull(generator.getCritterQueue());
    }

    @Test
    void testRandomizeCritterQueueDoesNotThrow() {
        generator.createCritterQueue();
        assertDoesNotThrow(() -> generator.RandomizeCritterQueue());
    }

    // ==== Integration Tests ====

    @Test
    void testCreateCritterQueueFillsCorrectNumber() {
        generator.createCritterQueue();
        // CritterStream[0] = {2,1,0,0,0}
        assertEquals(3, generator.getCritterQueue().size());
    }

    @Test
    void testCreateCritterQueueFillsCorrectTypes() {
        generator.createCritterQueue();
        long grunts = generator.getCritterQueue().stream().filter(c -> c instanceof critters.GruntCritter).count();
        long scouts = generator.getCritterQueue().stream().filter(c -> c instanceof critters.ScoutCritter).count();
        assertEquals(2, grunts);
        assertEquals(1, scouts);
    }

    @Test
    void testQueueShuffleMaintainsSize() {
        generator.createCritterQueue();
        int sizeBefore = generator.getCritterQueue().size();
        generator.RandomizeCritterQueue();
        int sizeAfter = generator.getCritterQueue().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void testQueuePrintDoesNotThrow() {
        generator.createCritterQueue();
        assertDoesNotThrow(() -> generator.printCritterQueue());
    }

}
