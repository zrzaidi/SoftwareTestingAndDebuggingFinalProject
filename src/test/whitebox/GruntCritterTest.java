package test.whitebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GruntCritterTest {

    private critters.GruntCritter grunt;
    private int[][] path = {
        {0, 0}, {10, 0}, {10, 10}
    };

    @BeforeEach
    void setUp() {
        grunt = new critters.GruntCritter(path);
    }

    // ==== Constructor & Getters ====

    @Test
    void testNameIsGrunt() {
        assertEquals("Grunt", grunt.getName());
    }

    @Test
    void testHealthIs5() {
        assertEquals(5.0, grunt.getHealth());
    }

    @Test
    void testSpeedIs1() {
        assertEquals(1.0, grunt.getSpeed());
    }

    @Test
    void testRewardIs20() {
        assertEquals(20, grunt.getReward());
    }

    @Test
    void testTypeIsGrunt() {
        assertEquals(critters.Critter.type.GRUNT, grunt.getType());
    }

    // ==== Movement ====

    @Test
    void testMoveIncreasesXLocation() {
        float initialX = grunt.getXLoc();
        grunt.move();
        assertTrue(grunt.getXLoc() > initialX);
    }

    @Test
    void testDirectionIsRightAfterMove() {
        grunt.move();
        assertEquals(critters.Critter.direction.RIGHT, grunt.getCritterDirection());
    }

    @Test
    void testMoveUntilEndPointReached() {
        while (!grunt.isAtEndPoint()) {
            grunt.move();
        }
        assertTrue(grunt.isAtEndPoint());
    }

    @Test
    void testMoveUntilEndPointSetsInvisible() {
        while (!grunt.isAtEndPoint()) {
            grunt.move();
        }
        assertFalse(grunt.isVisible());
    }

    // ==== Damage ====

    @Test
    void testTakeNonLethalDamageReducesHealth() {
        grunt.takeDamage(2); // 2 / armor(1) = 2
        assertEquals(3.0, grunt.getHealth(), 0.01);
    }

    @Test
    void testTakeLethalDamageKillsGrunt() {
        grunt.takeDamage(10); // 10 / 1 = 10 > 5
        assertFalse(grunt.isAlive());
    }

    @Test
    void testTakeLethalDamageMakesGruntInvisible() {
        grunt.takeDamage(10);
        assertFalse(grunt.isVisible());
    }

    @Test
    void testTakeZeroDamageNoHealthChange() {
        double hpBefore = grunt.getHealth();
        grunt.takeDamage(0);
        assertEquals(hpBefore, grunt.getHealth());
    }

    // ==== Freeze Logic ====

    @Test
    void testFreezeReducesSpeed() {
        double normal = grunt.getSpeed();
        grunt.freezeCritter();
        assertTrue(grunt.getSpeed() < normal);
    }

    @Test
    void testUnfreezeRestoresSpeed() throws InterruptedException {
        double normal = grunt.getSpeed();
        grunt.freezeCritter();
        Thread.sleep(4100); // trigger unfreeze
        grunt.move();
        assertEquals(normal, grunt.getSpeed(), 0.01);
    }

    @Test
    void testSetFreezeDurationShorterWorks() throws InterruptedException {
        grunt.setFreezeDuration(100);
        grunt.freezeCritter();
        Thread.sleep(200);
        grunt.move();
        assertEquals(1.0, grunt.getSpeed(), 0.01);
    }
}
