package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.arcaderacer.ArcadeRacerGameSceneEffect;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ArcadeRacerTest {

    private static class SwingEffectCanvas implements EffectCanvas {

        private final Graphics2D graphics2D;

        public SwingEffectCanvas(Graphics2D aGraphics2D) {
            graphics2D = aGraphics2D;
        }

        @Override
        public void setPaint(de.mirkosertic.gameengine.type.Color aColor) {
            graphics2D.setPaint(new Color(aColor.r, aColor.g, aColor.b));
        }

        @Override
        public void drawSingleDot(Position aPosition) {

        }

        @Override
        public void fillRect(float aX, float aY, float aWidth, float aHeight) {
            graphics2D.fillRect((int) aX, (int) aY, (int) aWidth, (int) aHeight);
        }

        @Override
        public void fillPolygon(float[] aXPositions, float[] aYPositions, int aNumberOfPositions) {
            int[] theX = new int[aNumberOfPositions];
            int[] theY = new int[aNumberOfPositions];
            for (int i=0;i<aNumberOfPositions;i++) {
                theX[i] = (int) aXPositions[i];
                theY[i] = (int) aYPositions[i];
            }
            graphics2D.fillPolygon(theX, theY, aNumberOfPositions);
        }

        @Override
        public void drawScaled(GameResource aResource, float aX, float aY, float aWidth, float aHeight) {
            SwingResource theResource = (SwingResource) aResource;
            graphics2D.drawImage(theResource.bufferedImage, (int) aX, (int) aY,(int) aWidth,(int) aHeight, null);
        }
    }

    private static class ArcadePanel extends JPanel {

        private final ArcadeRacerGameSceneEffect effect;

        public ArcadePanel(ArcadeRacerGameSceneEffect aEffect) {
            effect = aEffect;
        }

        @Override
        public void paint(Graphics aGraphics) {
            Graphics2D theGraphics2D = (Graphics2D) aGraphics;
            SwingEffectCanvas theEffectCanvas = new SwingEffectCanvas(theGraphics2D);
            effect.render(theEffectCanvas, null);
            theGraphics2D.dispose();
        }
    }

    public static class SwingResource implements GameResource {

        private final BufferedImage bufferedImage;

        public SwingResource(BufferedImage aImage) {
            bufferedImage = aImage;
        }

        @Override
        public GameResourceType getType() {
            return GameResourceType.BITMAP;
        }
    }

    public static void main(String[] args) {

        final GameEventManager theEventManager = new GameEventManager();
        GameResourceLoader theLoader = new GameResourceLoader() {
            @Override
            public GameResource load(ResourceName aResourceName) throws IOException {
                try {
                    return new SwingResource(ImageIO.read(ArcadeRacerTest.class.getResourceAsStream("/" + aResourceName.name)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void flush() {
            }
        };

        GameRuntime theRuntime = new GameRuntime(theEventManager, theLoader,
                new LuaJScriptEngineFactory(new JDKBuiltInFunctions()));
        GameScene theScene = new GameScene(null, theRuntime);

        ArcadeRacerGameSceneEffect theEffect = new ArcadeRacerGameSceneEffect(theScene, theEventManager);

        ArcadePanel thePanel = new ArcadePanel(theEffect);

        JFrame theFrame = new JFrame("Arcade Racer");
        theFrame.setMinimumSize(new Dimension(800,600));
        theFrame.setSize(new Dimension(800, 600));
        theFrame.setContentPane(thePanel);
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension theNewSize = e.getComponent().getSize();
                theEventManager.fire(new SetScreenResolution(new Size(theNewSize.width, theNewSize.height)));
            }
        });

        theEventManager.fire(new SetScreenResolution(new Size(800, 800)));

        Thread theRunner = new Thread() {
            @Override
            public void run() {

                long theLastRun = System.currentTimeMillis();

                while(!isInterrupted()) {

                    thePanel.invalidate();
                    thePanel.repaint();
                    try {
                        Thread.sleep(18);
                    } catch (InterruptedException e) {
                    }

                    long theRuntime = System.currentTimeMillis() - theLastRun;
                    if (theRuntime > 0) {
                        theEffect.proceesGame(System.currentTimeMillis(), theRuntime);
                        theLastRun = System.currentTimeMillis();
                    }
                }
            }
        };
        theRunner.start();
    }
}
