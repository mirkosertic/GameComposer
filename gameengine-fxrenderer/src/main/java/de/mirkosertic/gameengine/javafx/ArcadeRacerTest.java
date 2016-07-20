package de.mirkosertic.gameengine.javafx;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class ArcadeRacerTest {

    private static class ArcadePanel extends JPanel {

        double theZOffset = 0;

        @Override
        public void paint(Graphics aGraphics) {
            Dimension theCurrentSize = getSize();

            Graphics2D theGraphics = (Graphics2D) aGraphics;
            theGraphics.setPaint(Color.BLACK);
            theGraphics.fillRect(0, 0, (int) theCurrentSize.getWidth(), (int) theCurrentSize.getHeight());

            double theDistanceProjectionPlane = 3;
            double theDistanceCar = 6;
            double theHeight = 2;

            double theHalfViewHeight = theCurrentSize.height / 2;

            double theViewDepth = 30;
            for (double theZ = theDistanceCar + theViewDepth; theZ>=theDistanceCar; theZ--) {

                double theViewHeightFar = theHeight * theDistanceProjectionPlane / (theZ - theZOffset);
                double theViewHeightNear = theHeight * theDistanceProjectionPlane / (theZ - 1 - theZOffset);

                int theYOffsetFar = (int) (theHalfViewHeight + (theHalfViewHeight * theViewHeightFar));
                int theYOffsetNear = (int) (theHalfViewHeight + (theHalfViewHeight * theViewHeightNear));

                theGraphics.setPaint(theZ % 2 == 0 ? new Color(0, 200, 0) : new Color(0, 220, 0));
                theGraphics.fillRect(0, theYOffsetFar, theCurrentSize.width, theYOffsetNear - theYOffsetFar);
            }

            theZOffset+=0.01;
            if (theZOffset>=1) {
                theZOffset = 0;
            }


            theGraphics.dispose();
        }
    }

    public static void main(String[] args) {

        ArcadePanel thePanel = new ArcadePanel();

        JFrame theFrame = new JFrame("Arcade Racer");
        theFrame.setMinimumSize(new Dimension(800,600));
        theFrame.setSize(new Dimension(800, 600));
        theFrame.setContentPane(thePanel);
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread theRunner = new Thread() {
            @Override
            public void run() {
                while(!isInterrupted()) {
                    thePanel.invalidate();
                    thePanel.repaint();
                    try {
                        Thread.sleep(18);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        theRunner.start();
    }
}
