package test.blackbox;

import critters.Critter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrittersBlackbox {

    private Critter critter;
    private int[][] path;

    // Minimal concrete Critter subclass for testing
    private class TestCritter extends Critter {
        public TestCritter(int[][] pLocations, double pHealth, double pArmor, double pSpeed, int pReward, String pName, type pCritterType) {
            super(pLocations, pHealth, pArmor, pSpeed, pReward, pName, pCritterType);
        }
    }

    @BeforeEach
    public void setup() {
        path = new int[][]{{0, 0}, {100, 0}};
        critter = new TestCritter(path, 100, 1.0, 1.0, 10, "TestCritter", Critter.type.SCOUT);
    }

    @Test
    public void testInitialState() {
        assertEquals(100, critter.getHealth());
        assertEquals("TestCritter", critter.getName());
        assertEquals(10, critter.getReward());
        assertTrue(critter.isAlive());
        assertEquals(0, critter.getXLoc());
        assertEquals(0, critter.getYLoc());
        assertFalse(critter.isVisible());
        assertFalse(critter.isAtEndPoint());
    }

    @Test
    public void testTakeDamageKillsCritter() {
        critter.takeDamage(100); // With armor 1.0
        assertFalse(critter.isAlive());
        assertFalse(critter.isVisible());
        assertTrue(critter.getHealth() <= 0);
    }
    
    @Test
    public void testTakeNegativeDamage() {
    	assertThrows(IllegalArgumentException.class, () -> critter.takeDamage(-10),
    	        "Taking negative damage should throw IllegalArgumentException");
    }

    @Test
    public void testTakeDamageWithArmor() {
        critter.takeDamage(50); // Should reduce health by 50 (armor = 1.0)
        assertEquals(50.0, critter.getHealth(), 0.001);
    }

    @Test
    public void testFreezeAndUnfreeze() throws InterruptedException {
        double normalSpeed = critter.getSpeed();
        critter.freezeCritter();
        double frozenSpeed = critter.getSpeed();

        assertTrue(frozenSpeed < normalSpeed);
        assertNotEquals(normalSpeed, frozenSpeed);

        critter.setFreezeDuration(10);
        Thread.sleep(20);
        critter.move(); // will trigger unfreeze
        assertEquals(normalSpeed, critter.getSpeed(), 0.001);
    }

    @Test
    public void testMoveIncreasesDistance() {
        double oldX = critter.getXLoc();
        double oldDistance = critter.getDistanceTravelled();
        critter.move();
        assertTrue(critter.getXLoc() > oldX);
        assertTrue(critter.getDistanceTravelled() > oldDistance);
        assertTrue(critter.isVisible());
    }

    @Test
    public void testCritterDirectionUpdates() {
        critter.move();
        assertEquals(Critter.direction.RIGHT, critter.getCritterDirection());
    }

    @Test
    public void testEndPointDetection() {
        // Move until the end
        for (int i = 0; i < 200; i++) critter.move();
        assertTrue(critter.isAtEndPoint());
        assertFalse(critter.isVisible());
    }

    @Test
    public void testCanMoveReturnsTrue() {
        assertTrue(critter.CanMove());
    }

    @Test
    public void testCritterTypeIsCorrect() {
        assertEquals(Critter.type.SCOUT, critter.getType());
    }
}
