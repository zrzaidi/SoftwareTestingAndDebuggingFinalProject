package test.whitebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private game.Player player;

    @BeforeEach
    void setUp() {
        player = game.Player.getPlayer();
        player.reset(); // reset shared static state before each test
    }

    @Test
    void testSingletonInstance() {
    	game.Player p1 = game.Player.getPlayer();
    	game.Player p2 = game.Player.getPlayer();
        assertSame(p1, p2, "getPlayer should return the same instance");
    }

    @Test
    void testResetInitializesCorrectly() {
        player.reset();
        assertEquals(200, player.getCredits(), "Credits should be reset to 200");
        assertEquals(16, player.getLives(), "Lives should be reset to 16");
    }

    @Test
    void testAddCredits() {
        player.addCredits(50);
        assertEquals(250, player.getCredits(), "Credits should increase by 50");
    }

    @Test
    void testAddLife() {
        player.addLife();
        assertEquals(17, player.getLives(), "Lives should increase by 1");
    }

    @Test
    void testDecreaseLife() {
        player.decreaseLife();
        assertEquals(15, player.getLives(), "Lives should decrease by 1");
    }

    @Test
    void testMultipleCreditAndLifeUpdates() {
        player.addCredits(30);
        player.addCredits(-10); // decrease credits
        player.addLife();
        player.decreaseLife();
        player.decreaseLife();

        assertEquals(220, player.getCredits(), "Credits should be correctly updated");
        assertEquals(15, player.getLives(), "Lives should reflect the changes correctly");
    }
}