package test.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import critters.*;
import game.PlayScreen;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

public class GameMockTest {

    Critter critter;

    @BeforeEach
    public void setup() {
        int[][] path = {{0, 0}, {10, 0}, {20, 0}};
        critter = new ArmoredCritter(path);
    }

    @Test
    public void testCritterInitialization() {
        assertEquals(2, critter.getHealth());
        assertEquals(0.8, critter.getSpeed());
        assertEquals(60, critter.getReward());
        assertEquals(Critter.type.ARMORED, critter.getType());
    }

    @Test
    public void testCritterMovement() {
        float xBefore = critter.getXLoc();
        critter.move();
        float xAfter = critter.getXLoc();
        assertTrue(xAfter > xBefore, "Critter should move right along the X axis");
    }

    @Test
    public void testCritterTakeDamageKills() {
        critter.takeDamage(100);
        assertFalse(critter.isAlive());
        assertFalse(critter.isVisible());
    }

    @Test
    public void testCritterFreezeBehavior() throws InterruptedException {
        double originalSpeed = critter.getSpeed();
        critter.freezeCritter();
        Thread.sleep(10); // simulate time passing
        double frozenSpeed = critter.getSpeed();
        assertTrue(frozenSpeed < originalSpeed, "Frozen critter should move slower");
    }

    @Test
    public void testCritterUnfreeze() throws InterruptedException {
        critter.freezeCritter();
        Thread.sleep(4100); // wait past freeze duration
        critter.move(); // this should trigger unfreeze
        assertEquals(0.8, critter.getSpeed(), 0.01);
    }

    @Test
    public void testQueueManagement() {
        Queue<Critter> critterQueue = new LinkedList<>();
        critterQueue.add(critter);
        Critter retrieved = critterQueue.poll();
        assertEquals(critter, retrieved);
    }

    @Test
    public void testCritterAtEndPoint() {
        // Manually move to end
        for (int i = 0; i < 10; i++) {
            critter.move();
        }
        assertTrue(critter.isAtEndPoint() || !critter.isVisible());
    }

    @Test
    public void testCritterDirectionUpdates() {
        critter.move();
        assertNotNull(critter.getCritterDirection(), "Direction should not be null after movement");
    }

}
