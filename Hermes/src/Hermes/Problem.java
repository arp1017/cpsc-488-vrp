package Hermes;

import java.io.Serializable;

import java.util.Vector;


/**
 * <p>Title: Problem</p>
 * <p>Description: A configuration for a Hermes problem</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 1.0
 */
public class Problem implements Serializable {
    public String problemName;
    public String problemFile;
    public Vector carriers;

    /**
 * construct a new problem
 * @param name name of the problem
 * @param file the problem input file
 * @param cs a vector of carriers
 */
    public Problem(String name, String file, Vector cs) {
        problemName = name;
        problemFile = file;
        carriers = cs;
    }

    public Problem() {
        problemName = "";
        problemFile = "";
        carriers = new Vector();
    }

    public void addCarrier(String depotFile) {
        carriers.add(depotFile);
    }

    public void removeCarrier(String depotFile) {
        for (int i = 0; i < carriers.size(); i++) {
            String file = (String) carriers.elementAt(i);

            if (file.equals(depotFile)) {
                carriers.remove(i);

                break;
            }
        }
    }

    public boolean equals(Problem p) {
        if (!p.problemName.equals(problemName)) {
            return false;
        } else if (!p.problemFile.equals(this.problemFile)) {
            return false;
        } else if (p.carriers.size() != p.carriers.size()) {
            return false;
        }

        //same number of carriers, check if all the same
        for (int i = 0; i < carriers.size(); i++) {
            String c1 = (String) carriers.elementAt(i);
            String c2 = (String) p.carriers.elementAt(i);

            if (!c1.equals(c2)) {
                return false;
            }
        }

        //everything matches... return true
        return true;
    }

    public String toString() {
        return problemName + " " + carriers.size() + " " + problemFile;
    }
}
