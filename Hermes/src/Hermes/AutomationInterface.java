package Hermes;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.Vector;

import javax.swing.*;
import javax.swing.tree.*;

import java.sql.*;

/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
* <p>Log: The Automation Interface class was previously only partially implemented. The run function lacked any code, and
* 		the interface GUI was written in a nonfunctioning XYLayout format. Functionality has been added to buttons that
* 		previously did not work, and the GUI has been converted to GridBagLayout in line with the other GUIs.
* 		The Automation Interface currently only functions with 1 shipper agent and 1 carrier agent.</p>
*/
public class AutomationInterface extends JFrame {
    public boolean setGUI = false;
    private JScrollPane jScrollPane1 = new JScrollPane();
    private GridBagLayout xYLayout1 = new GridBagLayout();
    private JTree jTree1 = new JTree();
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Problem Set");
    private JButton jbRun = new JButton();
    private JMenuBar jMenuBar = new JMenuBar();
    private JMenu jMenuFile = new JMenu();
    private JMenuItem jmiNew = new JMenuItem();
    private JMenuItem jmiOpen = new JMenuItem();
    private JMenuItem jmiSave = new JMenuItem();
    private JMenuItem jmiExit = new JMenuItem();
    private Vector problems = new Vector();
    private Problem currentProblem = new Problem();
    private Vector carriers = new Vector();
    private JLabel jLabel1 = new JLabel();
    private JTextField jtfProblemFile = new JTextField();
    private JButton jbProblemFileChooser = new JButton();
    private JLabel jLabel2 = new JLabel();
    private JTextField jtfProblemName = new JTextField();
    private JLabel jLabel3 = new JLabel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JList jlCarriers = new JList();
    private JButton jbAddCarrier = new JButton();
    private JButton jbDelCarrier = new JButton();
    private JLabel jLabel4 = new JLabel();
    private JTextField jtfDepotFile = new JTextField();
    private JButton jbDepotFileChooser = new JButton();
    private JButton jbAddProblem = new JButton();
    private JButton jbLoad = new JButton();
    private JButton jbDelete = new JButton();

    /**
    * Constructor
    */
    public AutomationInterface() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    /**
    * Initializes the visual elements of the display
    * @throws Exception bad element initializaiton
    */
    private void jbInit() throws Exception {
        this.setTitle("Automation Configuration");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(xYLayout1);

        //add the menu
        jMenuFile.setText("File");
        jmiNew.setText("New");
        jmiNew.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //new button event here
                }
            });
        jmiOpen.setText("Open");
        jmiOpen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openAutomationFile();
                }
            });
        jmiSave.setText("Save");
        jmiSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveAutomationFile();
                }
            });
        jmiExit.setText("Exit");
        jmiExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(1);
                }
            });
        jLabel1.setText("Problem File: ");

        jLabel2.setText("Problem Name");
        jtfProblemName.setText("New Problem");
        currentProblem.problemName = "New Problem";
        jtfProblemName.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    jtfProblemName_focusGained(e);
                }
            });
        jLabel3.setText("Carriers");
        jLabel4.setText("Depot File: ");

        jMenuFile.add(jmiNew);
        jMenuFile.add(jmiOpen);
        jMenuFile.add(jmiSave);
        jMenuFile.insertSeparator(3);
        jMenuFile.add(jmiExit);
        jMenuBar.add(jMenuFile);
        this.setJMenuBar(jMenuBar);

        //set up the tree
        jTree1 = new JTree(root);
        jScrollPane1.getViewport().setView(jTree1);

        //set buttons
        jbRun.setText("Run");
        jbRun.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    run();
                }
            });
        jbAddProblem.setText("Add Problem");
        jbAddProblem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addProblem();
                }
            });
        jbAddCarrier.setText("Add Carrier");
        jbAddCarrier.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addCarrier();
                }
            });
        jbDelCarrier.setText("Del Carrier");
        jbDelCarrier.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    delCarrier();
                }
            });
        jbLoad.setText("Load");
        jbLoad.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadProblem();
                }
            });
        jbDelete.setText("Delete");
        jbDelete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteProblem();
                }
            });
        jbProblemFileChooser.setText("...");
        jbProblemFileChooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseProblemFile();
                }
            });
        jbDepotFileChooser.setText("...");
        jbDepotFileChooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseDepotFile();
                }
            });

        //xYLayout1.setWidth = (539);
        //xYLayout1.setHeight(510);
        contentPane.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
        		GridBagConstraints.NORTHWEST, 
        		GridBagConstraints.NONE, new Insets(-75, -60, 0, 0), 140, 440));
        contentPane.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        		GridBagConstraints.NORTHWEST,
        		GridBagConstraints.NONE, new Insets(15, 140, 0, 0), 80, 24));
        contentPane.add(jtfProblemFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.NORTHWEST,
    			GridBagConstraints.NONE, new Insets(25, 220, 0, 0), 107, -1));
        contentPane.add(jbProblemFileChooser, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.NORTHWEST,
    			GridBagConstraints.NONE, new Insets(25, 335, 0, 0), -5, -5));
        contentPane.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.NORTHWEST,
    			GridBagConstraints.NONE, new Insets(-50, 130, 0, 0), 21, 85));
        contentPane.add(jtfProblemName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.NORTHWEST,
    			GridBagConstraints.NONE, new Insets(0, 220, 0, 0), 102, -1));
        contentPane.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.NORTHWEST,
    			GridBagConstraints.NONE, new Insets(35, 140, 0, 0), 21, 77));
        contentPane.add(jScrollPane2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER,
    			GridBagConstraints.NONE, new Insets(-30, 150, 0, 0), 243, 135));
        contentPane.add(jbAddCarrier, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER,
    			GridBagConstraints.NONE, new Insets(165, 20, 0, 0), 25, 3));
        contentPane.add(jbDelCarrier, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER,
    			GridBagConstraints.NONE, new Insets(165, 275, 0, 0), 25, 3));
        contentPane.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER,
    			GridBagConstraints.NONE, new Insets(240, 10, 0, 0), 1, -1)); 
        contentPane.add(jtfDepotFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER,
    			GridBagConstraints.NONE, new Insets(240, 180, 0, 0), 100, -1));
        contentPane.add(jbDepotFileChooser, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER,
    			GridBagConstraints.NONE, new Insets(240, 330, 0, 0), -5, -5));
        contentPane.add(jbAddProblem, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.SOUTHWEST,
    			GridBagConstraints.NONE, new Insets(0, 150, -27, 0), 5, 12));
        contentPane.add(jbRun, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.SOUTHWEST,
    			GridBagConstraints.NONE, new Insets(0, 300, -27, 0), 45, 12));
        contentPane.add(jbDelete, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.SOUTH,
    			GridBagConstraints.NONE, new Insets(0, -285, -27, 0), 15, -1));
        contentPane.add(jbLoad, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.SOUTH,
    			GridBagConstraints.NONE, new Insets(0, -445, -27, 0), 15, -1));
        jScrollPane2.getViewport().add(jlCarriers, null);

        this.setSize(new Dimension(539, 518));
        this.setVisible(true);

        new MasterCredentials(setGUI);
    }

    /**
    * This will run the list of problems
    */
    /*Function previously empty, now implemented
    */
    private void run() {
    	String problem = "problems/" + currentProblem.problemFile;
    	int carNum = currentProblem.carriers.size();
    	String depot = currentProblem.carriers.toString();
    	depot = depot.replaceAll("[\\(\\)\\[\\]\\{\\}]","");
    	depot = "problems/" + depot;
    	new Automation(problem, carNum, depot);
    	
    }

    /**
    * Will add a problem configuration to the problem list
    */
    private void addProblem() {
        currentProblem.problemName = jtfProblemName.getText();

        if (currentProblem.problemFile.equals("")) {
            JOptionPane.showMessageDialog(this,
                "Problem File not Specified " + currentProblem.problemFile);
            jtfProblemFile.transferFocus();
        } else if (currentProblem.problemName.equals("")) {
            JOptionPane.showMessageDialog(this, "Problem Name not Specified");
            jtfProblemName.transferFocus();
        } else if (currentProblem.carriers.size() == 0) {
            JOptionPane.showMessageDialog(this, "No carriers for problem");
            jtfDepotFile.transferFocus();
        } else {
            //see if problem is already on tree
            boolean alreadyThere = false;

            for (int i = 0; i < root.getChildCount(); i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
                Problem thisProb = (Problem) node.getUserObject();

                if (thisProb.problemName.equals(currentProblem.problemName)) {
                    alreadyThere = true;
                    node = new DefaultMutableTreeNode(currentProblem);
                    problems.setElementAt(currentProblem, i);
                }
            }

            if (!alreadyThere) {
                //add problem to the tree
                root.add(new DefaultMutableTreeNode(currentProblem));
                problems.add(currentProblem);
            }

            jTree1 = new JTree(root);
            jScrollPane1.setViewportView(jTree1);

            //clear out interface
            currentProblem = new Problem();
            jlCarriers.setListData(new Vector());
            jtfProblemName.setText("New Problem");
            jtfProblemFile.setText("");
            jtfDepotFile.setText("");
        }
    }

    /**
    * This will add a carrier to the problem configuration
    */
    private void addCarrier() {
        if (jtfDepotFile.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "No depot file specified");
            jtfDepotFile.transferFocus();
        } else {
            currentProblem.addCarrier(jtfDepotFile.getText());
            jlCarriers.setListData(currentProblem.carriers);
            jtfDepotFile.setText("");
        }
    }

    /**
    * Removes a carrier from the problem configuration
    */
    private void delCarrier() {
        if ((currentProblem.carriers.size() != 0) &&
                (jlCarriers.getSelectedIndex() > -1)) {
            currentProblem.carriers.remove(jlCarriers.getSelectedIndex());
            jlCarriers.setListData(currentProblem.carriers);
        }
    }

    /**
    * Starts a file chooser to select the problem file
    */
    private void chooseProblemFile() {
        JFileChooser chooser = new JFileChooser("./problems");

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentProblem.problemFile = chooser.getSelectedFile().getName();
            jtfProblemFile.setText(currentProblem.problemFile);
        }
    }

    /**
    * Starts a file chooser to select a depot file
    */
    private void chooseDepotFile() {
        JFileChooser chooser = new JFileChooser("./problems");

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            jtfDepotFile.setText(chooser.getSelectedFile().getName());
        }
    }

    /**
    * Will load a problem set file
    */
    private void openAutomationFile() {
        JFileChooser chooser = new JFileChooser(".");

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File inFile = chooser.getSelectedFile();
            ObjectInputStream in;

            try {
                in = new ObjectInputStream(new FileInputStream(inFile));
                problems = (Vector) in.readObject();

                //update interface
                for (int i = 0; i < problems.size(); i++) {
                    root.add(new DefaultMutableTreeNode(
                            (Problem) problems.elementAt(i)));
                }

                jTree1 = new JTree(root);
                jScrollPane1.setViewportView(jTree1);
            } catch (InvalidClassException ice) {
                JOptionPane.showMessageDialog(this,
                    "The file: " + inFile +
                    " is corrupted or not properly formatted");
                System.err.println(
                    "This file is corrupted or not properly formatted");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        }
    }

    /**
    * will save the problem set
    */
    private void saveAutomationFile() {
        JFileChooser chooser = new JFileChooser(".");

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outFile = chooser.getSelectedFile();
            ObjectOutputStream out;

            try {
                out = new ObjectOutputStream(new FileOutputStream(outFile));
                out.writeObject(problems);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
    * Will load a problem from the tree to the text fields for editing
    */
    private void loadProblem() {
        if (jTree1.getLastSelectedPathComponent() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
            currentProblem = (Problem) node.getUserObject();

            jlCarriers.setListData(currentProblem.carriers);
            jtfProblemName.setText(currentProblem.problemName);
            jtfProblemFile.setText(currentProblem.problemFile);
            jtfDepotFile.setText("");
        }
    }

    /**
    * Will delete a problem from the problem list
    */
    private void deleteProblem() {
        if (jTree1.getLastSelectedPathComponent() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
            root.remove(node);
            jTree1 = new JTree(root);
            jScrollPane1.setViewportView(jTree1);
        }
    }

    /**
    * Will clear the problem name field the first time you click on it
    * @param e the problem name text field has gained focus
    */
    void jtfProblemName_focusGained(FocusEvent e) {
        if (jtfProblemName.getText().equals("New Problem")) {
            jtfProblemName.setText("");
        }
    }

    /**
    * Starts the automation interface
    * @param args command line arguments
    */
    public static void main(String[] args) {
        new AutomationInterface();
    }
}
