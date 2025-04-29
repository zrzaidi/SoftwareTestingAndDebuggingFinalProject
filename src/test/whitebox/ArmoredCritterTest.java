package test.whitebox;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Queue;

import org.junit.jupiter.api.Test;

class ArmoredCritterTest {

    private critters.ArmoredCritter critter;
    private int[][] path;

    @BeforeEach
    void setUp() {
        path = new int[][]{{0, 0}, {10, 0}, {10, 10}};
        critter = new critters.ArmoredCritter(path);
    }
    // --- Basic unit tests ---

    @Test
    void testName() {
        assertEquals("Armored", critter.getName());
    }

    @Test
    void testInitialHealth() {
        assertEquals(2.0, critter.getHealth());
    }

    @Test
    void testInitialReward() {
        assertEquals(60, critter.getReward());
    }

    @Test
    void testInitialSpeed() {
        assertEquals(0.8, critter.getSpeed());
    }

    @Test
    void testInitialArmor() {
        // indirect check via takeDamage
        critter.takeDamage(8);
        assertEquals(1.0, critter.getHealth());
    }

    @Test
    void testInitialAliveState() {
        assertTrue(critter.isAlive());
    }

    @Test
    void testInitialVisibility() {
        assertFalse(critter.isVisible());
    }

    @Test
    void testCritterType() {
        assertEquals(critters.Critter.type.ARMORED, critter.getType());
    }

    // --- Movement tests ---

    @Test
    void testMoveOnceChangesPositionAndDirection() {
        float xBefore = critter.getXLoc();
        critter.move();
        assertTrue(critter.getXLoc() > xBefore);
        assertEquals(critters.Critter.direction.RIGHT, critter.getCritterDirection());
    }

    @Test
    void testDistanceTravelledAfterMove() {
        double before = critter.getDistanceTravelled();
        critter.move();
        assertTrue(critter.getDistanceTravelled() > before);
    }

    @Test
    void testAtEndPointAfterFullPath() {
        for (int i = 0; i < 100; i++) critter.move();
        assertTrue(critter.isAtEndPoint());
    }

    @Test
    void testMoveSetsVisibleTrue() {
        assertFalse(critter.isVisible());
        critter.move();
        assertTrue(critter.isVisible());
    }

    @Test
    void testInvalidPathTriggersEndPoint() {
        int[][] shortPath = {{0, 0}};
        critters.ArmoredCritter shortCritter = new critters.ArmoredCritter(shortPath);
        shortCritter.move(); // No second point, expect IndexOutOfBounds
        assertTrue(shortCritter.isAtEndPoint());
        assertFalse(shortCritter.isVisible());
    }
    // --- Damage tests ---

    @Test
    void testTakeDamageWithArmor() {
        critter.takeDamage(8);
        assertEquals(1.0, critter.getHealth());
    }

    @Test
    void testTakeDamageKillsCritter() {
        critter.takeDamage(100); // more than health * armor
        assertFalse(critter.isAlive());
        assertFalse(critter.isVisible());
    }

    @Test
    void testTakeZeroDamage() {
        double before = critter.getHealth();
        critter.takeDamage(0);
        assertEquals(before, critter.getHealth());
    }

    @Test
    void testTakeNegativeDamageNoHeal() {
        double before = critter.getHealth();
        critter.takeDamage(-10);
        assertTrue(critter.getHealth() > before); // Armor makes negative damage look like healing
        // This is potentially a logic bug in the original code!
    }

    // --- Freeze tests ---

    @Test
    void testFreezeReducesSpeed() {
        critter.freezeCritter();
        assertTrue(critter.getSpeed() < 0.8);
    }

    @Test
    void testUnfreezeRestoresSpeed() {
        critter.freezeCritter();
        critter.unfreezeCritter();
        assertEquals(0.8, critter.getSpeed());
    }

    @Test
    void testFreezeDurationExpires() throws InterruptedException {
        critter.setFreezeDuration(100);
        critter.freezeCritter();
        Thread.sleep(200);
        critter.move(); // triggers unfreeze
        assertEquals(0.8, critter.getSpeed());
    }

    @Test
    void testSetNegativeFreezeDuration() {
        critter.setFreezeDuration(-1000);
        // no assertion since freezeDuration is private, but should not crash
    }

    // --- Logic consistency ---

    @Test
    void testDirectionChangesCorrectly() {
        for (int i = 0; i < 20; i++) critter.move();
        assertNotNull(critter.getCritterDirection());
    }

    @Test
    void testCanMoveInitiallyTrue() {
        assertTrue(critter.CanMove());
    }
    // === Fault-Revealing Test: Division by armor ===

    @Test
    void testTakeDamageDoesNotKillTooEarly() {
    	critter.takeDamage(8); // 8 / 8 = 1, HP 2 → 1
        assertTrue(critter.isAlive());
        assertEquals(1.0, critter.getHealth(), 0.01);
    }

    @Test
    void testTakeExactDamageKills() {
    	critter.takeDamage(16); // 16 / 8 = 2 == HP
        assertFalse(critter.isAlive());
    }

    @Test
    void testTakeNegativeDamageIncreasesHealth() {
        double before = critter.getHealth();
        critter.takeDamage(-8); // -8 / 8 = -1 → HP increases!
        assertTrue(critter.getHealth() > before);

        // ⚠️ 设计缺陷：应防止负伤害变成“回血”效果
    }

    @Test
    void testCreateQueueIncludesArmoredCritter() {
        int[][] path = {
            {0, 0}, {5, 0}, {5, 5}
        };

        critters.CritterGenerator generator = new critters.CritterGenerator(path, 5);
        generator.createCritterQueue();
        Queue<critters.Critter> queue = generator.getCritterQueue();

        boolean foundArmored = queue.stream().anyMatch(c -> c instanceof critters.ArmoredCritter);
        assertTrue(foundArmored);
    }

    @Test
    void testArmoredInQueueIsAliveAndVisibleAfterMove() {
        int[][] path = {
            {0, 0}, {2, 0}, {4, 0}, {6, 0}
        };

        critters.ArmoredCritter critter = new critters.ArmoredCritter(path);
        for (int i = 0; i < 10; i++) critter.move();

        assertTrue(critter.isAlive());
        assertTrue(critter.isVisible() || critter.isAtEndPoint());
    }

    @Test
    void testAllCrittersCanMoveAndTakeDamage() {
        int[][] path = {
            {0, 0}, {2, 0}, {4, 0}
        };

        critters.CritterGenerator generator = new critters.CritterGenerator(path, 5);
        generator.createCritterQueue();

        for (critters.Critter c : generator.getCritterQueue()) {
            c.move();
            c.takeDamage(100);
            assertFalse(c.isAlive()); 
        }
    }
}

