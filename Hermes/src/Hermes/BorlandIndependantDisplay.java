package Hermes;

import Zeus.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * <p>Title: BorlandIndependantDisplay</p>
 * <p>Description: This class will create a visual display of all the routes in
 * a depot linked list.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class BorlandIndependantDisplay extends JPanel {
    private DepotLinkedList mainDepots;
    private int bigX;
    private int smallX;
    private int bigY;
    private int smallY;

    /**
    * Create a new display
    * @param dLL the depot linked list to display
    */
    public BorlandIndependantDisplay(DepotLinkedList dLL) {
        mainDepots = dLL;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Create the visual elements of the display
    * @throws Exception something went wrong
    */
    private void jbInit() throws Exception {
        this.setBackground(Color.white);
        this.setMinimumSize(new Dimension(400, 400));
        this.setPreferredSize(new Dimension(400, 400));
        this.setSize(new Dimension(400, 400));
    }

    /**
    * Paint the routes
    * @param g graphics class
    */
    public void paint(Graphics g) {
        //loop through the depot linked list and find the biggest x and y
        bigX = 0;
        bigY = 0;
        smallX = Integer.MAX_VALUE;
        smallY = Integer.MAX_VALUE;

        Depot depot = mainDepots.getFirst();

        while (depot != null) {
            if (depot.getX() > bigX) {
                bigX = (int) depot.getX();
            }

            if (depot.getY() > bigY) {
                bigY = (int) depot.getY();
            }

            if (depot.getX() < smallX) {
                smallX = (int) depot.getX();
            }

            if (depot.getY() > smallY) {
                smallY = (int) depot.getY();
            }

            Truck truck = depot.mainTrucks.getFirst();

            while (truck != null) {
                PointCell cell = truck.mainVisitNodes.first();

                while (cell != null) {
                    if (cell.getXCoord() > bigX) {
                        bigX = (int) cell.getXCoord();
                    }

                    if (cell.getYCoord() > bigY) {
                        bigY = (int) cell.getYCoord();
                    }

                    if (cell.getXCoord() < smallX) {
                        smallX = (int) cell.getXCoord();
                    }

                    if (cell.getYCoord() < smallY) {
                        smallY = (int) cell.getYCoord();
                    }

                    cell = cell.next;
                }

                truck = truck.next;
            }

            depot = depot.next;
        }

        System.out.println("height = " + this.getHeight() + " width = " +
            this.getWidth());
        System.out.println("bigX = " + bigX + " bigY = " + bigY);
        System.out.println("smallX = " + smallX + " smallY = " + smallY);

        Color[] truckColors = {
            Color.black, Color.blue, Color.cyan, Color.green, Color.magenta,
            Color.orange, Color.pink, Color.yellow
        };
        int colorCounter = 0;

        //loop through the depot linked list again and paint the points
        depot = mainDepots.getFirst();

        while (depot != null) {
            int x = transX((int) depot.getX());
            int y = transY((int) depot.getY());
            int[] xs = { x, x + 7, x - 7 };
            int[] ys = { y - 7, y, y };

            //            System.out.println("drawing depot (" + depot.x + "," + depot.y +
            //                ") @ (" + x + "," + y + ")");
            //draw a triangle for the depot
            g.setColor(Color.red);
            g.fillPolygon(new Polygon(xs, ys, 3));

            Truck truck = depot.mainTrucks.getFirst();

            while (truck != null) {
                PointCell cell = truck.mainVisitNodes.first();
                g.setColor(truckColors[colorCounter++ % 8]);

                while (cell != null) {
                    //dont redraw the cell again if its a depot
                    if (cell.getCellIndex() > 0) {
                        x = transX((int) cell.getXCoord());
                        y = transY((int) cell.getYCoord());

                        //                        System.out.println("drawing cell (" + cell.getXCoord() +
                        //                            "," + cell.getYCoord() + ") @ (" + x + "," + y +
                        //                            ")");
                        g.fillOval(x - 4, y - 4, 8, 8);
                    }

                    PointCell next = cell.next;

                    if (next != null) {
                        //draw the path
                        g.drawLine(transX((int) cell.getXCoord()),
                            transY((int) cell.getYCoord()),
                            transX((int) next.getXCoord()),
                            transY((int) next.getYCoord()));
                    }

                    cell = next;
                }

                truck = truck.next;
            }

            depot = depot.next;
        }
    }

    /**
    * will translate the X coordinate from Zeus into the X coordinate for the
    * display
    * @param x x coordinate to translate
    * @return x coordinate to draw
    */
    private int transX(int x) {
        return (int) ((double) (x - smallX) / (double) (bigX - smallX) * (double) (this.getWidth() -
        10)) + 5;
    }

    /**
    * Will translate the y coordinate from Zeus into the X coordinate for the
    * display.
    * @param y y coordinate to translate
    * @return y coordinate to draw
    */
    private int transY(int y) {
        return (int) ((double) (y - smallY) / (double) (bigY - smallY) * (double) (this.getHeight() -
        10)) + 5;
    }
}
