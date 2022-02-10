package Zeus;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: Class containing functions used to make standard calculations</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Anthony Pitluga
 * @version 1.0
 */
public class Utility {
    public Utility() {
    }

    /**
 * Find the polar angle between (x1,y1) and (x,y)
 * @param x1 X coordinate of first object
 * @param y1 Y coordinate of first object
 * @param x X coordinate of second object
 * @param y Y coordinate of second object
 * @return polar angle between (x1,y1) and (x,y)
 */
    static public double calcPolarAngle(double x1, double y1, double x, double y) {
        //find the polar coordinate angle between (x1,y1) and (x,y)
        double radian = 57.29578;
        double slope = 0;
        double xrun;
        double yrise;
        double angle;

        xrun = x - x1;
        yrise = y - y1;

        if (xrun > 0) {
            slope = yrise / xrun;

            if (yrise >= 0) {
                angle = Math.atan(slope) * radian;
            } else {
                angle = 360 + (Math.atan(slope) * radian);
            }
        } else if (xrun == 0) {
            if (yrise >= 0) {
                angle = 90.0;
            } else {
                angle = 270.0;
            }
        } else {
            slope = yrise / xrun;
            angle = 180 + (Math.atan(slope) * radian);
        }

        return angle;
    }

    /**
 * Calculated the Eucludian Distance between (x1,y1) and (x2,y2)
 * @param x1 X coordinate of first object
 * @param y1 Y coordinate of first object
 * @param x2 X coordinate of second object
 * @param y2 Y coordinate of second object
 * @return Eucludian distance between (x1,y1) and (x2,y2)
 */
    static public double calcDist(double x1, double x2, double y1, double y2) {
        double d = 0;

        try {
            d = (double) Math.sqrt((double) (((x2 - x1) * (x2 - x1)) +
                    ((y2 - y1) * (y2 - y1))));
        } catch (ArithmeticException e) {
            System.out.println(e);
        }

        return d;
    }
}
