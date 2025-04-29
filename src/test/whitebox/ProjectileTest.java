package test.whitebox;

import critters.Critter;
import critters.Critter.type;

import org.junit.jupiter.api.Test;
import towers.Projectile.projectileType;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    static class TestCritter extends Critter {
        double damageTaken = 0;
        int freezeDurationSet = 0;
        boolean frozen = false;

        // Default constructor for simple tests
        public TestCritter() {
            super(new int[][]{{0, 0}}, 10, 0, 0, 1, "test", type.GRUNT);
        }

        public TestCritter(int[][] pLocations, double pHealth, double pArmor, double pSpeed, int pReward, String pName,
                           type pCritterType) {
            super(pLocations, pHealth, pArmor, pSpeed, pReward, pName, pCritterType);
        }

        @Override
        public void takeDamage(double damage) {
            damageTaken += damage;
        }

        public void setFreezeDuration(int duration) {
            freezeDurationSet = duration;
        }

        @Override
        public void freezeCritter() {
            frozen = true;
        }
    }

    @Test
    void testGenericProjectileAngleAndInit() {
        TestCritter critter = new TestCritter();
        towers.Projectile p = new towers.Projectile(0, 0, 100, 0, 10, critter, projectileType.GENERIC, 1);

        assertEquals(0.0, p.angleOfProjectileInDegrees(), 0.001);
        assertEquals(12.0, p.getX(), 0.001); // cos(0) * 12
        assertEquals(0.0, p.getY(), 0.001);  // sin(0) * 12
        assertEquals(20.0, p.getSpeed(), 0.001);
        assertEquals(projectileType.GENERIC, p.getType());
    }

    @Test
    void testSniperProjectileHasHigherSpeed() {
        TestCritter critter = new TestCritter(new int[][]{{0, 0}}, 1.0, 0, 0, 1, "sniperTest", type.GRUNT);
        towers.Projectile p = new towers.Projectile(0, 0, 100, 0, 10, critter, projectileType.SNIPER, 1);
        assertEquals(30.0, p.getSpeed(), 0.001);
    }

    @Test
    void testGenericProjectileHitAndDamage() {
        TestCritter critter = new TestCritter(new int[][]{{0, 0}}, 1.0, 0, 0, 1, "damageTest", type.GRUNT);
        towers.Projectile p = new towers.Projectile(0, 0, 13, 0, 10, critter, projectileType.GENERIC, 1);
        p.move();
        assertTrue(p.hasArrived());
        assertEquals(10.0, critter.damageTaken, 0.001);
    }

    @Test
    void testFreezeProjectileAppliesFreezeEffects() {
        TestCritter critter = new TestCritter(new int[][]{{0, 0}}, 1.0, 0, 0, 1, "freezeTest", type.GRUNT);
        towers.Projectile p = new towers.Projectile(0, 0, 13, 0, 15, critter, projectileType.FREEZE, 2);
        p.move();

        assertTrue(p.hasArrived());
        assertEquals(15.0, critter.damageTaken, 0.001);
        assertTrue(critter.frozen);
        assertEquals(5000, critter.freezeDurationSet); // 4000 + (2 - 1) * 1000
    }

    @Test
    void testProjectileMovesUntilHit() {
        TestCritter critter = new TestCritter(new int[][]{{0, 0}}, 1.0, 0, 0, 1, "moveTest", type.GRUNT);
        towers.Projectile p = new towers.Projectile(0, 0, 100, 0, 7, critter, projectileType.GENERIC, 1);

        int steps = 0;
        while (!p.hasArrived() && steps < 100) {
            p.move();
            steps++;
        }

        assertTrue(p.hasArrived());
        assertTrue(steps < 100); // make sure it didn't loop forever
        assertEquals(7.0, critter.damageTaken, 0.001);
    }

    @Test
    void testProjectileDoesNotHitTooSoon() {
        TestCritter critter = new TestCritter(new int[][]{{0, 0}}, 1.0, 0, 0, 1, "delayTest", type.GRUNT);
        towers.Projectile p = new towers.Projectile(0, 0, 100, 0, 5, critter, projectileType.GENERIC, 1);
        p.move(); // one step should not be enough

        assertFalse(p.hasArrived());
        assertEquals(0.0, critter.damageTaken, 0.001);
    }
}