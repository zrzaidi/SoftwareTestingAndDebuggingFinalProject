package test.whitebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoutCritterTest {

    private critters.ScoutCritter scout;
    private int[][] path = {
        {0, 0}, {9, 0}, {9, 9}
    };

    @BeforeEach
    void setUp() {
        scout = new critters.ScoutCritter(path);
    }

    // === Constructor & Attributes ===

    @Test
    void testNameIsScout() {
        assertEquals("Scout", scout.getName());
    }

    @Test
    void testHealthIs3() {
        assertEquals(3.0, scout.getHealth());
    }

    @Test
    void testSpeedIs3() {
        assertEquals(3.0, scout.getSpeed());
    }

    @Test
    void testRewardIs10() {
        assertEquals(10, scout.getReward());
    }

    @Test
    void testTypeIsScout() {
        assertEquals(critters.Critter.type.SCOUT, scout.getType());
    }

    // === Movement ===

    @Test
    void testMoveIncreasesXLoc() {
        float initialX = scout.getXLoc();
        scout.move();
        assertTrue(scout.getXLoc() > initialX);
    }

    @Test
    void testDirectionIsRightAfterMove() {
        scout.move();
        assertEquals(critters.Critter.direction.RIGHT, scout.getCritterDirection());
    }

    @Test
    void testMoveUntilEndPointReached() {
        while (!scout.isAtEndPoint()) {
            scout.move();
        }
        assertTrue(scout.isAtEndPoint());
    }

    @Test
    void testMoveUntilEndPointSetsInvisible() {
        while (!scout.isAtEndPoint()) {
            scout.move();
        }
        assertFalse(scout.isVisible());
    }

    // === Damage Logic ===

    @Test
    void testTakeLethalDamageKillsScout() {
        scout.takeDamage(10); // 10 / 0.5 = 20 > 3
        assertFalse(scout.isAlive());
    }

    @Test
    void testTakeLethalDamageMakesScoutInvisible() {
        scout.takeDamage(10);
        assertFalse(scout.isVisible());
    }

    @Test
    void testTakeNonLethalDamageReducesHealth() {
        scout.takeDamage(1); // 1 / 0.5 = 2
        assertEquals(1.0, scout.getHealth(), 0.01);
    }

    @Test
    void testTakeZeroDamageKeepsHealthUnchanged() {
        double hp = scout.getHealth();
        scout.takeDamage(0);
        assertEquals(hp, scout.getHealth());
    }

    // === Freeze Logic ===

    @Test
    void testFreezeReducesSpeed() {
        double normal = scout.getSpeed();
        scout.freezeCritter();
        assertTrue(scout.getSpeed() < normal);
    }

    @Test
    void testUnfreezeRestoresSpeed() throws InterruptedException {
        double normal = scout.getSpeed();
        scout.freezeCritter();
        Thread.sleep(4100);
        scout.move();
        assertEquals(normal, scout.getSpeed(), 0.01);
    }

    @Test
    void testSetFreezeDurationShort() throws InterruptedException {
        scout.setFreezeDuration(100);
        scout.freezeCritter();
        Thread.sleep(150);
        scout.move();
        assertEquals(3.0, scout.getSpeed(), 0.01);
    }
}
