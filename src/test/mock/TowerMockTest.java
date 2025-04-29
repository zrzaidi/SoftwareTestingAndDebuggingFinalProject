package test.mock;

import critters.Critter;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TowerMockTest {

    private towers.BasicTower basicTower;
    private towers.FreezeTower freezeTower;
    private towers.SniperTower sniperTower;

    @BeforeEach
    void setup() {
        basicTower = new towers.BasicTower(50, 100);
        freezeTower = new towers.FreezeTower(30, 70);
        sniperTower = new towers.SniperTower(10, 10);
    }

    @Test
    void testCanAttack_true() {
        basicTower.setTimeOfLastAttack(System.currentTimeMillis() - 1000); // > 0.3s ago
        assertTrue(basicTower.canAttack());
    }

    @Test
    void testCanAttack_false() {
        basicTower.setTimeOfLastAttack(System.currentTimeMillis());
        assertFalse(basicTower.canAttack());
    }

    @Test
    void testRefundTower_addsCredits() {
        try (MockedStatic<Player> mocked = mockStatic(Player.class)) {
            Player mockPlayer = mock(Player.class);
            mocked.when(Player::getPlayer).thenReturn(mockPlayer);

            basicTower.refundTower();
            verify(mockPlayer).addCredits((int) basicTower.getRefundValue());
        }
    }

    @Test
    void testUpgrade_successful() {
        try (MockedStatic<Player> mocked = mockStatic(Player.class)) {
            Player mockPlayer = mock(Player.class);
            mocked.when(Player::getPlayer).thenReturn(mockPlayer);
            when(mockPlayer.getCredits()).thenReturn(200);

            boolean result = basicTower.upgrade();
            assertTrue(result);
            assertEquals(2, basicTower.getLevel());
            verify(mockPlayer).addCredits((int) -basicTower.getUpgradeCost());
        }
    }

    @Test
    void testUpgrade_fails_whenNoCredits() {
        try (MockedStatic<Player> mocked = mockStatic(Player.class)) {
            Player mockPlayer = mock(Player.class);
            mocked.when(Player::getPlayer).thenReturn(mockPlayer);
            when(mockPlayer.getCredits()).thenReturn(50);

            boolean result = basicTower.upgrade();
            assertFalse(result);
            assertEquals(1, basicTower.getLevel());
        }
    }

    @Test
    void testRotationAngleCalculation() {
        Critter mockCritter = mock(Critter.class);
        when(mockCritter.getXLoc()).thenReturn((float) 100.0);
        when(mockCritter.getYLoc()).thenReturn((float) 100.0);

        basicTower.setTargetCritter(mockCritter);
        double angle = basicTower.getRotationAngleInDegrees();

        assertTrue(angle > 0);
    }

    @Test
    void testBasicTowerAttributes() {
        assertEquals(100, basicTower.getBuyingCost());
        assertEquals(90, basicTower.getRefundValue());
        assertEquals(100, basicTower.getUpgradeCost());
        assertEquals(towers.Tower.type.GENERIC, basicTower.getType());
    }

    @Test
    void testFreezeTowerIsFreezeTower() {
        assertTrue(freezeTower.isFreezeTower());
        assertEquals(towers.Tower.type.FREEZE, freezeTower.getType());
    }

    @Test
    void testSniperTowerAttributes() {
        assertEquals(300, sniperTower.getBuyingCost());
        assertEquals(towers.Tower.type.SNIPER, sniperTower.getType());
        assertEquals(20 * sniperTower.getModifier(), sniperTower.getPower());
    }

    @Test
    void testGetModifierAndRangeAndPower() {
        double baseModifier = basicTower.getModifier(); // should be 1.0 initially
        assertEquals(100 * baseModifier, basicTower.getRange());
        assertEquals(0.6 * baseModifier, basicTower.getPower(), 0.01);
    }
}
