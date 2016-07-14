package de.mirkosertic.gameengine.javafx;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class ArcadeRacerTest {

    private static class Point2D {
        private final double x;
        private final double y;

        public Point2D(double aX, double aY) {
            x = aX;
            y = aY;
        }
    }

    private static class Point3D {
        private final double x;
        private final double y;
        private final double z;

        public Point3D(double aX, double aY, double aZ) {
            x = aX;
            y = aY;
            z = aZ;
        }
    }

    private static Point2D from3D(Point3D aPoint) {
        double theX = aPoint.x / aPoint.z;
        double theY = aPoint.y / aPoint.z;
        return new Point2D(theX, theY);
    }


    private static class ArcadePanel extends JPanel {

        private final static int CLIP_SIZE = 150;

        private Point2D projectOnScreen(Point3D aPoint, Dimension aScreenSize) {

            int theHalfWidth = aScreenSize.width / 2;
            int theHalfHeight = aScreenSize.height / 2;

            Point2D the2DPoint = from3D(aPoint);

            double theXPositionOnScreen = theHalfWidth + the2DPoint.x * (theHalfWidth / CLIP_SIZE);
            double theYPositionOnScreen = theHalfHeight + the2DPoint.y * (theHalfHeight / CLIP_SIZE);

            return new Point2D(theXPositionOnScreen, theYPositionOnScreen);
        }

        double currentZOffset = 1;
        boolean negated;

        @Override
        public void paint(Graphics aGraphics) {
            Dimension theCurrentSize = getSize();

            Graphics2D theGraphics = (Graphics2D) aGraphics;
            theGraphics.setPaint(Color.BLACK);
            theGraphics.fillRect(0, 0, (int) theCurrentSize.getWidth(), (int) theCurrentSize.getHeight());

            double[] theOffset = new double[31];
            double theMultiply = 1;
            for (int i=0;i<theOffset.length;i++) {
                theOffset[i] = theMultiply * i;
                theMultiply *= 1.8;
            }

            for (double z = 30.01; z>=1; z--) {

                Paint theColorBackGround;
                theColorBackGround = ((int) z) % 2 == 0 ? new Color(0, 100, 0) : new Color(0, 200, 0);

                Point2D theRoadFarLeft = projectOnScreen(new Point3D(-100 + theOffset[(int) z], 100, z), theCurrentSize);
                Point2D theRoadFarRight = projectOnScreen(new Point3D(100 + theOffset[(int) z], 100, z), theCurrentSize);

                Point2D theRoadNearLeft = projectOnScreen(new Point3D(-100 + theOffset[(int) z - 1], 100, z - 1), theCurrentSize);
                Point2D theRoadNearRight = projectOnScreen(new Point3D(100 + theOffset[(int) z - 1], 100, z - 1), theCurrentSize);

                theGraphics.setPaint(theColorBackGround);
                // Linker Rasen
                int[] theXPointsLeft = new int[] {0, (int) theRoadFarLeft.x, (int) theRoadNearLeft.x, 0};
                int[] theYPointsLeft = new int[] {(int) theRoadFarLeft.y, (int) theRoadFarLeft.y, (int) theRoadNearLeft.y, (int) theRoadNearLeft.y};
                theGraphics.fillPolygon(theXPointsLeft, theYPointsLeft, 4);

                // Rechter rasen
                int[] theXPointsRight = new int[] {(int) theCurrentSize.width, (int) theRoadFarRight.x, (int) theRoadNearRight.x, theCurrentSize.width};
                int[] theYPointsRight = new int[] {(int) theRoadFarLeft.y, (int) theRoadFarRight.y, (int) theRoadNearRight.y, (int) theRoadNearRight.y};
                theGraphics.fillPolygon(theXPointsRight, theYPointsRight, 4);

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
