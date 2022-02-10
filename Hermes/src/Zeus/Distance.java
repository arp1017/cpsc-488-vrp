package Zeus;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: This class implements the Distance class. The Distance class is used
 * to create a matrix data structure to maintain all distances between customers and depots. NOTE- this
 * class needs to be expanded </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class Distance implements java.io.Serializable {
    public Distance() {
    }

    //-------------------------------------------------------------------------
    //Method to get distance matrix between all cust's and depots

    /* public double[][] getMatrix(CustomerLinkedList theCust, int size, int type)
   {
    int i,j;                                         //lcv
    int start;                                       //start index, for loop
    CustomerDataArray custData = theCust;            //array of customers
    double [][] distMatrix = new double[size][size]; //allocate matrix for
                                                     //distances
    double [] x = new double[size];                  //x coordinates
    double [] y = new double[size];                  //y coordinates
    if (type == 0)
      start = 1;
    else
      start = 0;
    for (i = start; i < size; i++)
    {
      x[i] = custData.getX(i);
      y[i] = custData.getY(i);
    }
    double bigcost = 1.0;     //value down diagonal
    for (i = start; i < size; i++)
      for (j = i+1; j < size; j++)
      {
        double distance = java.lang.Math.sqrt((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j]));
        distMatrix[i][j] = distance;                //assign in both directions
        distMatrix[j][i] = distance;
        bigcost += distance;
      }
      //set diagonals to bigcost;
      for (int e=start; e < size; e++)
        distMatrix[e][e] = bigcost;
     //print matrix
      for (i = 0; i < size; i++)
      {
        for (j = 0; j < size; j++)
          System.out.print(distMatrix[i][j] + " ");
        System.out.println("");
      }
      try
      {outputMatrix(distMatrix, size);}    //send to file
      catch(IOException ioe){System.out.println("error "+ioe.getMessage());}
      return distMatrix;
   }//end getMatrix
   //-------------------------------------------------------
   public void outputMatrix(double[][] matrix, int size) throws IOException,
                                      ArrayIndexOutOfBoundsException
   {
      PrintWriter out=null;
      try
      {
        out = new PrintWriter(new FileWriter
                  ("h:/Projects/pvrp/output.txt"));
        for(int i = 0; i<size; i++)
        {
        for(int j = 0; j<size; j++)
        {
          String s = Double.toString(matrix[i][j]);
          out.print(s);
          out.print("  ");
        }
        out.print('\n');
      }
      out.close();
      }//end try
      catch(ArrayIndexOutOfBoundsException e)
      {System.out.println("Array Index error " + e.getMessage());}
      catch(IOException ioe)
      {  System.out.println ("IO error "+ ioe.getMessage());}
      finally
      {
        if (out != null)
        { System.out.println ("closing file");
          out.close();
        }
        else
        { System.out.println("File not open.");}
      }
   }//end outputMatrix
 */
}
