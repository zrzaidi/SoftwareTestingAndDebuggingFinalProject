package test.whitebox;

import critters.Critter;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TowerTest {

    private towers.Tower basicTower;
    private towers.Tower freezeTower;
    private towers.Tower sniperTower;

    @BeforeEach
    public void setup() {
        Player player = new Player();
		player.reset(); // Ensure fresh credits
        Player.getPlayer().addCredits(1000);

        basicTower = new towers.BasicTower(0, 0);
        freezeTower = new towers.FreezeTower(1, 1);
        sniperTower = new towers.SniperTower(2, 2);
    }

    @Test
    public void testInitialValuesBasicTower() {
        assertEquals(100, basicTower.getBuyingCost());
        assertEquals(90, basicTower.getRefundValue());
        assertEquals(100, basicTower.getUpgradeCost());
        assertEquals(100, basicTower.getRange(), 0.01);
        assertEquals(0.6, basicTower.getPower(), 0.01);
        assertEquals(towers.Tower.type.GENERIC, basicTower.getType());
        assertFalse(basicTower.isFreezeTower());
    }

    @Test
    public void testInitialValuesFreezeTower() {
        assertEquals(200, freezeTower.getBuyingCost());
        assertEquals(180, freezeTower.getRefundValue());
        assertEquals(180, freezeTower.getUpgradeCost());
        assertEquals(80, freezeTower.getRange(), 0.01);
        assertEquals(0.0, freezeTower.getPower(), 0.01);
        assertTrue(freezeTower.isFreezeTower());
        assertEquals(towers.Tower.type.FREEZE, freezeTower.getType());
    }

    @Test
    public void testInitialValuesSniperTower() {
        assertEquals(300, sniperTower.getBuyingCost());
        assertEquals(250, sniperTower.getRefundValue());
        assertEquals(280, sniperTower.getUpgradeCost());
        assertEquals(300, sniperTower.getRange(), 0.01);
        assertEquals(20.0, sniperTower.getPower(), 0.01);
        assertEquals(towers.Tower.type.SNIPER, sniperTower.getType());
    }

    @Test
    public void testCanAttackCooldown() {
        basicTower.setTimeOfLastAttack(System.currentTimeMillis() - 1000);
        assertTrue(basicTower.canAttack());

        basicTower.setTimeOfLastAttack(System.currentTimeMillis());
        assertFalse(basicTower.canAttack());
    }

    @Test
    public void testUpgradeSuccessAndFailure() {
        int initialCredits = Player.getPlayer().getCredits();
        assertTrue(basicTower.upgrade());
        assertEquals(2, basicTower.getLevel());
        assertEquals(initialCredits - basicTower.getUpgradeCost(), Player.getPlayer().getCredits());

        Player.getPlayer().addCredits(-100000); // not enough for upgrade
        assertFalse(basicTower.upgrade());
        assertEquals(2, basicTower.getLevel());
    }

    @Test
    public void testMaxLevelLimit() {
        basicTower.upgrade();
        basicTower.upgrade();
        assertFalse(basicTower.upgrade()); // level 3 max
        assertEquals(3, basicTower.getLevel());
    }

    @Test
    public void testRefundTower() {
        int creditsBefore = Player.getPlayer().getCredits();
        basicTower.refundTower();
        assertEquals(creditsBefore + 90, Player.getPlayer().getCredits());
    }

    @Test
    public void testSettersAndGetters() {
        basicTower.setBuyingCost(500);
        assertEquals(500, basicTower.getBuyingCost());

        basicTower.setRefundValue(250);
        assertEquals(250, basicTower.getRefundValue());

        basicTower.setRange(50);
        assertEquals(50 * basicTower.getModifier(), basicTower.getRange(), 0.01);

        basicTower.setPower(2);
        assertEquals(2 * basicTower.getModifier(), basicTower.getPower(), 0.01);

        basicTower.setUpgradeCost(300);
        assertEquals(300, basicTower.getUpgradeCost());

        assertEquals(0, basicTower.getCritterTravelDistanceMaximum());
        basicTower.setCritterTravelDistanceMaximum(123.4);
        assertEquals(123.4, basicTower.getCritterTravelDistanceMaximum());
    }

    @Test
    public void testRotationAngleWithNullCritter() {
        assertEquals(0.0, basicTower.getRotationAngleInDegrees(), 0.01);
    }
    
    @Test
    public void testTowerUpgradeAffectsPlayerCredits() {
        Player.getPlayer().reset();
        Player.getPlayer().addCredits(1000);

        int creditsBefore = Player.getPlayer().getCredits();
        boolean upgraded = basicTower.upgrade();
        int creditsAfter = Player.getPlayer().getCredits();

        assertTrue(upgraded, "Upgrade should succeed");
        assertEquals(creditsBefore - 100, creditsAfter, "Credits should be deducted by upgrade cost");
    }

    @Test
    public void testRefundIncreasesPlayerCredits() {
        Player.getPlayer().reset();
        Player.getPlayer().addCredits(500);

        int beforeRefund = Player.getPlayer().getCredits();
        basicTower.refundTower();
        int afterRefund = Player.getPlayer().getCredits();

        assertEquals(beforeRefund + 90, afterRefund, "Refund should increase player's credits correctly");
    }


}