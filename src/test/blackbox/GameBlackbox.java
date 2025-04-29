package test.blackbox;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import game.*;

public class GameBlackbox {

    // MenuScreen Tests
    public static class MenuScreenTests {
        private MenuScreen menu;

        @Before
        public void setUp() throws SlickException, NoSuchFieldException, IllegalAccessException {
            menu = new MenuScreen(0);

            GameContainer container = mock(GameContainer.class);
            when(container.getWidth()).thenReturn(800);
            when(container.getHeight()).thenReturn(600);

            setPrivateField(menu, "SandTileGraphic", mockImage(64, 64));
            setPrivateField(menu, "StartGameButtonGraphic", mockImage(100, 40));
            setPrivateField(menu, "EditMapButtonGraphic", mockImage(100, 40));
            setPrivateField(menu, "TowerDefenseTitleGraphic", mockImage(200, 60));
            setPrivateField(menu, "ExitButtonGraphic", mockImage(80, 30));

            menu.createRectangleButtons(container);
        }

        private Image mockImage(int width, int height) {
            Image img = mock(Image.class);
            when(img.getWidth()).thenReturn(width);
            when(img.getHeight()).thenReturn(height);
            return img;
        }

        private void setPrivateField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        }

        @Test
        public void testMouseClickOutsideButtonsDoesNothing() throws SlickException {
            GameContainer container = mock(GameContainer.class);
            StateBasedGame sbg = mock(StateBasedGame.class);
            menu.mouseClicked(5, 5, sbg, container);
            verify(sbg, never()).enterState(anyInt());
        }

        @Test
        public void testClickCooldownBlocksRapidClicks() throws Exception {
            GameContainer container = mock(GameContainer.class);
            StateBasedGame sbg = mock(StateBasedGame.class);
            setPrivateField(menu, "lastClick", System.currentTimeMillis());
            menu.mouseClicked(400, 300, sbg, container);
            verify(sbg, never()).enterState(anyInt());
        }
    }

    // PlayScreen Tests
    public static class PlayScreenTests {
        private PlayScreen playScreen;

        @Before
        public void setUp() {
            playScreen = new PlayScreen(1);
        }

        private Object invokePrivateMethod(String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
            Method method = PlayScreen.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(playScreen, parameters);
        }

        @Test
        public void testDrawTowers() throws Exception {
            Graphics graphics = mock(Graphics.class);
            invokePrivateMethod("drawTowers", new Class<?>[] { Graphics.class }, new Object[] { graphics });
            verify(graphics, times(0)).drawString(anyString(), anyFloat(), anyFloat()); // Adjust as needed
        }
    }

    // EditMapScreen Tests
    public static class EditMapScreenTests {
        private EditMapScreen editMapScreen;
        private GameContainer container;
        private StateBasedGame game;
        
        @Before
        public void setUp() {
            editMapScreen = new EditMapScreen(3);
            container = mock(GameContainer.class);
            game = mock(StateBasedGame.class);
            mock(Graphics.class);
        }

        @Test
        public void testInitialState_mouseOnMapReturnsFalse() throws Exception {
            Method method = EditMapScreen.class.getDeclaredMethod("mouseOnMap", int.class, int.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(editMapScreen, 10, 10);
            assertFalse(result);
        }

        @Test
        public void testGetClosestTileCenter_returnsCorrectCenter() {
            float result = editMapScreen.getClosestTileCenter(65f);
            assertEquals(80f, result);
        }

        @Test
        public void testMapGridClicked_outOfBoundsClick() throws Exception {
            Method mapGridClicked = EditMapScreen.class.getDeclaredMethod(
                "mapGridClicked", int.class, int.class, StateBasedGame.class, GameContainer.class
            );
            mapGridClicked.setAccessible(true);
            mapGridClicked.invoke(editMapScreen, 5, 5, game, container);
        }
    }

    // Player Tests
    public static class PlayerTests {
        private Player player;

        @Before
        public void setUp() {
            player = Player.getPlayer();
            player.reset();
        }

        @Test
        public void testInitialCredits() {
            assertEquals(200, player.getCredits());
        }

        @Test
        public void testInitialLives() {
            assertEquals(16, player.getLives());
        }

        @Test
        public void testAddCredits() {
            player.addCredits(50);
            assertEquals(250, player.getCredits());
        }

        @Test
        public void testAddLife() {
            player.addLife();
            assertEquals(17, player.getLives());
        }

        @Test
        public void testDecreaseLife() {
            player.decreaseLife();
            assertEquals(15, player.getLives());
        }

        @Test
        public void testReset() {
            player.addCredits(50);
            player.addLife();
            player.reset();
            assertEquals(200, player.getCredits());
            assertEquals(16, player.getLives());
        }

        @Test
        public void testSingletonInstance() {
            Player player1 = Player.getPlayer();
            Player player2 = Player.getPlayer();
            assertSame(player1, player2);
        }
    }
}
