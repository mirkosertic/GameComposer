package de.mirkosertic.gameengine.javafx;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ArcadeRacerTest {

    private static class Point2D {
        private final int x;
        private final int y;

        public Point2D(int aX, int aY) {
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

    private static class Camera {

        private final int halfViewWidth;
        private final int halfViewHeight;
        private final double cameraHeight;
        private final double distanceProjectionPlane;
        private final double zProjectionFactor;
        private final double positionOnTrack;

        public Camera(Dimension aSceeenSize, double aCameraHeight, double aDistanceProjectionPlane,
                double aZProjectionFactor, double aPositionOnTrack) {
            halfViewWidth = aSceeenSize.width / 2;
            halfViewHeight = aSceeenSize.height / 2;
            cameraHeight = aCameraHeight;
            distanceProjectionPlane = aDistanceProjectionPlane;
            zProjectionFactor = aZProjectionFactor;
            positionOnTrack = aPositionOnTrack;
        }

        public Point2D project(Point3D aPoint) {

            double theProjectedDistance = aPoint.z - positionOnTrack;

            double theYFactor = cameraHeight * distanceProjectionPlane / (theProjectedDistance * zProjectionFactor);
            double theXFactor = aPoint.x * distanceProjectionPlane / (theProjectedDistance * zProjectionFactor);

            int x = (int) (halfViewWidth + (halfViewWidth * theXFactor));
            int y = (int) (halfViewHeight + (halfViewHeight * theYFactor));

            return new Point2D(x, y);
        }
    }


    private static class ArcadePanel extends JPanel {

        double positionOnTrack = 0;
        double distanceProjectionPlane = 3;
        double distanceCar = 6;
        double cameraHeight = 2;

        double zProjectionFactor = 2.5;

        @Override
        public void paint(Graphics aGraphics) {
            Dimension theCurrentSize = getSize();

            Graphics2D theGraphics = (Graphics2D) aGraphics;
            theGraphics.setPaint(Color.BLACK);
            theGraphics.fillRect(0, 0, (int) theCurrentSize.getWidth(), (int) theCurrentSize.getHeight());

            Camera theCamera = new Camera(theCurrentSize, cameraHeight, distanceProjectionPlane,
                                          zProjectionFactor, positionOnTrack);

            int theViewDepth = 60;

            // Ok, first of all we need to compute the track to draw
            int theFaresPosition = (int) Math.ceil(positionOnTrack + distanceCar + theViewDepth);
            int theNearestPosition = theFaresPosition - theViewDepth - 4;

            // Now we draw the track
            for (double theZ = theFaresPosition; theZ >= theNearestPosition; theZ--) {

                // Calculate the color according the the real track data
                theGraphics.setPaint(theZ % 2 == 0 ? new Color(0, 200, 0) : new Color(0, 220, 0));

                Point2D theRoadLeftFarOutside  = theCamera.project(new Point3D(-2, 0, theZ));
                Point2D theRoadRightFarOutside  = theCamera.project(new Point3D(2, 0, theZ));

                Point2D theRoadLeftFarInside  = theCamera.project(new Point3D(-1.8, 0, theZ));
                Point2D theRoadRightFarInside  = theCamera.project(new Point3D(1.8, 0, theZ));

                Point2D theRoadLeftNearOutside  = theCamera.project(new Point3D(-2, 0, theZ - 1));
                Point2D theRoadRightNearOutside  = theCamera.project(new Point3D(2, 0, theZ - 1));

                Point2D theRoadLeftNearInside  = theCamera.project(new Point3D(-1.8, 0, theZ - 1));
                Point2D theRoadRightNearInside  = theCamera.project(new Point3D(1.8, 0, theZ - 1));


                // Left and right part of the Road
                theGraphics.fillPolygon(new int[] {0, theRoadLeftFarOutside.x, theRoadLeftNearOutside.x, 0}, new int[] {theRoadLeftFarOutside.y, theRoadLeftFarOutside.y, theRoadLeftNearOutside.y, theRoadLeftNearOutside.y}, 4);
                theGraphics.fillPolygon(new int[] {theRoadRightFarOutside.x, theCurrentSize.width, theCurrentSize.width, theRoadRightNearOutside.x}, new int[] {theRoadRightFarOutside.y, theRoadRightFarOutside.y, theRoadRightNearOutside.y, theRoadRightNearOutside.y}, 4);

                // The curbs
                theGraphics.setPaint(theZ % 2 == 0 ? Color.white : Color.red);
                theGraphics.fillPolygon(new int[] {theRoadLeftFarOutside.x, theRoadLeftFarInside.x, theRoadLeftNearInside.x, theRoadLeftNearOutside.x}, new int[] {theRoadLeftFarOutside.y, theRoadLeftFarInside.y, theRoadLeftNearInside.y, theRoadLeftNearOutside.y}, 4);
                theGraphics.fillPolygon(new int[] {theRoadRightFarOutside.x, theRoadRightFarInside.x, theRoadRightNearInside.x, theRoadRightNearOutside.x}, new int[] {theRoadRightFarOutside.y, theRoadRightFarInside.y, theRoadRightNearInside.y, theRoadRightNearOutside.y}, 4);

                // The road itself
                // Calculate the color according the the real track data
                theGraphics.setPaint(theZ % 2 == 0 ? new Color(160,160,160) : new Color(180,180,180));
                theGraphics.fillPolygon(new int[] {theRoadLeftFarInside.x, theRoadRightFarInside.x, theRoadRightNearInside.x, theRoadLeftNearInside.x}, new int[] {theRoadLeftFarInside.y, theRoadRightFarInside.y, theRoadRightNearInside.y, theRoadLeftNearInside.y}, 4);
            }

            positionOnTrack += 0.1;

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
