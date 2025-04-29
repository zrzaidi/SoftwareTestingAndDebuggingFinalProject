package test.whitebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TankCritterTest {

    private critters.TankCritter tank;
    private int[][] path = {
        {0, 0}, {10, 0}, {10, 10}
    };

    @BeforeEach
    void setUp() {
        tank = new critters.TankCritter(path);
    }

    // === Constructor & Attributes ===

    @Test
    void testNameIsTank() {
        assertEquals("Tank", tank.getName());
    }

    @Test
    void testHealthIs15() {
        assertEquals(15.0, tank.getHealth());
    }

    @Test
    void testSpeedIs0Point6() {
        assertEquals(0.6, tank.getSpeed());
    }

    @Test
    void testRewardIs80() {
        assertEquals(80, tank.getReward());
    }

    @Test
    void testTypeIsTank() {
        assertEquals(critters.Critter.type.TANK, tank.getType());
    }

    // === Movement ===

    @Test
    void testMoveIncreasesXLoc() {
        float initialX = tank.getXLoc();
        tank.move();
        assertTrue(tank.getXLoc() > initialX);
    }

    @Test
    void testDirectionIsRightAfterMove() {
        tank.move();
        assertEquals(critters.Critter.direction.RIGHT, tank.getCritterDirection());
    }

    @Test
    void testMoveUntilEndPointReached() {
        while (!tank.isAtEndPoint()) {
            tank.move();
        }
        assertTrue(tank.isAtEndPoint());
    }

    @Test
    void testMoveUntilEndPointSetsInvisible() {
        while (!tank.isAtEndPoint()) {
            tank.move();
        }
        assertFalse(tank.isVisible());
    }

    // === Damage Logic ===

    @Test
    void testTakeNonLethalDamageReducesHealth() {
        tank.takeDamage(4); // 4 / 4 = 1
        assertEquals(14.0, tank.getHealth(), 0.01);
    }

    @Test
    void testTakeLethalDamageKillsTank() {
        tank.takeDamage(100); // 100 / 4 = 25 > 15
        assertFalse(tank.isAlive());
    }

    @Test
    void testTakeLethalDamageMakesTankInvisible() {
        tank.takeDamage(100);
        assertFalse(tank.isVisible());
    }

    @Test
    void testTakeZeroDamageKeepsHealthUnchanged() {
        double hp = tank.getHealth();
        tank.takeDamage(0);
        assertEquals(hp, tank.getHealth());
    }

    // === Freeze Logic ===

    @Test
    void testFreezeReducesSpeed() {
        double normal = tank.getSpeed();
        tank.freezeCritter();
        assertTrue(tank.getSpeed() < normal);
    }

    @Test
    void testUnfreezeRestoresSpeed() throws InterruptedException {
        double normal = 0.6;
        tank.freezeCritter();
        Thread.sleep(4100); // default freezeDuration is 4000
        tank.move();
        assertEquals(normal, tank.getSpeed(), 0.01);
    }

    @Test
    void testSetFreezeDurationShortAndUnfreeze() throws InterruptedException {
        tank.setFreezeDuration(100);
        tank.freezeCritter();
        Thread.sleep(150);
        tank.move();
        assertEquals(0.6, tank.getSpeed(), 0.01);
    }
}
