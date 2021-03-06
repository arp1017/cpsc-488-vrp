package Zeus;

import java.io.*; //input-output java package


/**
 *
 * <p>Title: Zeus - A Unified Object Oriented Model for Routing and Scheduling Problems</p>
 * <p>Description: The Zeus program was written with the intention of using an Object
 * Oriented Programming Insfrastucture for solving a wide variety of Routing and
 * Scheduling of Problems with reusable components. See the Word document on Zeus for more
 * explanations on the structure. </p>
 * <p>Copyright: Copyright (c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 * @author Sam R. Thangiah
 */
public class Zeus {
    private Root theRoot;

    /**
    * <p>Zeus class constructor</p>
    */
    public Zeus() {
        theRoot = new Root();
    }

    /**
    * return the instance of Root
    * @return Root  the instance of Root
    */
    public Root getRoot() {
        return theRoot;
    }

    /**
    * <p>Create an instance of the root class</p>
    * @param args command line arguments
    * @throws IOException error condition
    */
    public static void main(String[] args) throws IOException {
        Zeus z = new Zeus();

        //        z.theRoot = new Root();
    }
}
