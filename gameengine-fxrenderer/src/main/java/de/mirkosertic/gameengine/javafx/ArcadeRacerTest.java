package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.arcaderacer.ArcadeRacerGameSceneEffect;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
        public void fillRect(int aX, int aY, int aWidth, int aHeight) {
            graphics2D.fillRect(aX, aY, aWidth, aHeight);
        }

        @Override
        public void fillPolygon(int[] aXPositions, int[] aYPositions, int aNumberOfPositions) {
            graphics2D.fillPolygon(aXPositions, aYPositions, aNumberOfPositions);
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

    public static void main(String[] args) {

        final GameEventManager theEventManager = new GameEventManager();
        ArcadeRacerGameSceneEffect theEffect = new ArcadeRacerGameSceneEffect(null, theEventManager);

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
