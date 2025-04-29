package test.blackbox;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import towers.Tower;
import critters.Critter;
import game.Player;

public class TowerBlackbox {

    private Tower mockTower;

    @Before
    public void setUp() {
        // Create tower for testing
        mockTower = new Tower(100, 200) {};
        
        // Set necessary defaults
        mockTower.setBuyingCost(100);
        mockTower.setRefundValue(50);
        mockTower.setRange(5.0);
        mockTower.setPower(10);
        mockTower.setUpgradeCost(30);

        // Ensure player has credits
        Player.getPlayer().addCredits(100);
    }

    @Test
    public void testBuyingCostSetterGetter() {
        mockTower.setBuyingCost(150);
        assertEquals(150, mockTower.getBuyingCost());
    }

    @Test
    public void testRefundValueSetterGetter() {
        mockTower.setRefundValue(80);
        assertEquals(80.0, mockTower.getRefundValue(), 0.001);
    }

    @Test
    public void testRangeModifier() {
        mockTower.setRange(10);
        // Should return base range * modifier (level 1 → modifier = 1.0)
        assertEquals(10.0, mockTower.getRange(), 0.001);
    }

    @Test
    public void testPowerModifier() {
        mockTower.setPower(20);
        // Should return base power * modifier (level 1 → modifier = 1.0)
        assertEquals(20.0, mockTower.getPower(), 0.001);
    }

    @Test
    public void testLevelAndUpgradeSuccess() {
        Player.getPlayer().addCredits(100); // Ensure enough credits

        assertTrue(mockTower.upgrade()); // Level should now be 2
        assertEquals(2, mockTower.getLevel());

        assertTrue(mockTower.upgrade()); // Level should now be 3
        assertEquals(3, mockTower.getLevel());

        // Max level reached; upgrade should fail
        assertFalse(mockTower.upgrade());
    }

    @Test
    public void testUpgradeFailsWhenInsufficientCredits() {
        Player.getPlayer().addCredits(-Player.getPlayer().getCredits()); // Set credits to 0
        assertFalse(mockTower.upgrade()); // Should not be able to upgrade
    }

    @Test
    public void testRefundTowerAddsCredits() {
        int initialCredits = Player.getPlayer().getCredits();
        mockTower.setRefundValue(70);
        mockTower.refundTower();
        assertEquals(initialCredits + 70, Player.getPlayer().getCredits());
    }

    @Test
    public void testLocationGetters() {
        assertEquals(100.0, mockTower.getXLoc(), 0.001);
        assertEquals(200.0, mockTower.getYLoc(), 0.001);
    }

    @Test
    public void testCritterTravelDistance() {
        mockTower.setCritterTravelDistanceMaximum(150.5);
        assertEquals(150.5, mockTower.getCritterTravelDistanceMaximum(), 0.001);
    }

    @Test
    public void testGetTypeReturnsNullNoType() {
        assertNull(mockTower.getType());
    }

    @Test
    public void testIsFreezeTowerDefaultFalse() {
        assertFalse(mockTower.isFreezeTower());
    }

    @Test
    public void testGetModifierLevelOne() {
        // Modifier should be 1.0 at level 1
        assertEquals(1.0, mockTower.getModifier(), 0.001);
    }
}
