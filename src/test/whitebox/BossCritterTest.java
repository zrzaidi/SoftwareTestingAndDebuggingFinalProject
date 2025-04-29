package test.whitebox;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

class BossCritterTest {


    private critters.BossCritter boss;
    private int[][] path = {
        {0, 0}, {10, 0}, {10, 10}
    };

    @BeforeEach
    void setUp() {
        boss = new critters.BossCritter(path);
    }

    // === Constructor property tests ===
    @Test
    void testNameIsBoss() {
        assertEquals("Boss", boss.getName());
    }

    @Test
    void testHealthIs15() {
        assertEquals(15.0, boss.getHealth());
    }

    @Test
    void testSpeedIs0Point5() {
        assertEquals(0.5, boss.getSpeed());
    }

    @Test
    void testRewardIs100() {
        assertEquals(100, boss.getReward());
    }

    @Test
    void testTypeIsBoss() {
        assertEquals(critters.Critter.type.BOSS, boss.getType());
    }

    @Test
    void testIsInitiallyAlive() {
        assertTrue(boss.isAlive());
    }

    @Test
    void testIsNotAtEndInitially() {
        assertFalse(boss.isAtEndPoint());
    }

    // === Movement tests ===
    @Test
    void testMoveIncreasesXLoc() {
        float initialX = boss.getXLoc();
        boss.move();
        assertTrue(boss.getXLoc() > initialX);
    }

    @Test
    void testDirectionIsRightAfterMove() {
        boss.move();
        assertEquals(critters.Critter.direction.RIGHT, boss.getCritterDirection());
    }

    @Test
    void testMoveUntilEndPointReached() {
        while (!boss.isAtEndPoint()) {
            boss.move();
        }
        assertTrue(boss.isAtEndPoint());
    }

    @Test
    void testMoveUntilEndPointThenInvisible() {
        while (!boss.isAtEndPoint()) {
            boss.move();
        }
        assertFalse(boss.isVisible());
    }

    // === Freeze logic tests ===
    @Test
    void testFreezeReducesSpeed() {
        double originalSpeed = boss.getSpeed();
        boss.freezeCritter();
        assertTrue(boss.getSpeed() < originalSpeed);
    }

    @Test
    void testUnfreezeRestoresSpeed() throws InterruptedException {
        double originalSpeed = boss.getSpeed();
        boss.freezeCritter();
        Thread.sleep(4100);
        boss.move();  // triggers unfreeze
        assertEquals(originalSpeed, boss.getSpeed(), 0.01);
    }

    @Test
    void testFreezeDurationCanBeChanged() throws InterruptedException {
        boss.setFreezeDuration(100);  // very short
        boss.freezeCritter();
        Thread.sleep(150);
        boss.move(); // should unfreeze
        assertEquals(0.5, boss.getSpeed(), 0.01);
    }

    // === Damage logic tests ===
    @Test
    void testLethalDamageMakesBossDead() {
        boss.takeDamage(200); // 200/8 = 25 > 15
        assertFalse(boss.isAlive());
    }

    @Test
    void testLethalDamageMakesBossInvisible() {
        boss.takeDamage(200);
        assertFalse(boss.isVisible());
    }

    @Test
    void testNonLethalDamageReducesHealth() {
        boss.takeDamage(8); // 8/8 = 1
        assertEquals(14.0, boss.getHealth());
    }

    @Test
    void testTakeZeroDamageDoesNotChangeHealth() {
        double before = boss.getHealth();
        boss.takeDamage(0);
        assertEquals(before, boss.getHealth());
    }
}
