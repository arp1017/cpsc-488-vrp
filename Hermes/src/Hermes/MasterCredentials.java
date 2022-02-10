package Hermes;
import java.awt.*;

import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;

import java.sql.*;

/*<p>Title: MasterCredentials.java </p>
* <p>Description: New .java which acts as new front for Hermes; accepts user 
* credentials for connection to SQL rather than having it hardcoded as before.
* Uses a simple JFrame to contain the essential fields.
* 
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
*/



public class MasterCredentials extends JFrame implements ActionListener {
	GridBagLayout gbl = new GridBagLayout();
	public static JTextField username;
	public static JPasswordField password;
	public static JButton submit;
	public static boolean checkGUI = true;

	public MasterCredentials(boolean GUI) {
		this.setVisible(true);
		setLayout(gbl);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JLabel usr = new JLabel("Username:");
		JLabel pas = new JLabel("Password:");
		username = new JTextField(10);
		password = new JPasswordField(10);
		submit = new JButton("Submit.");
		checkGUI = GUI;
		
		setTitle("Please Enter User Credentials.");
		setSize(420,120);
		setLocationRelativeTo(null);
		
		submit.addActionListener(this);
		submit.setActionCommand("Go");

		JPanel main = new JPanel();
		add(main);
		makeConstraints(gbl, main, 1, 2, 0, 0, 2.0, 1.0);
		main.add(usr);
		main.add(username);
		main.add(pas);
		main.add(password);
		main.add(submit);

	}

	public void makeConstraints(GridBagLayout gbl, JComponent comp, int w, int h, int x, int y,
			double weightx, double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		gbl.setConstraints(comp, constraints);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		String result1 = username.getText();
		String result2 = password.getText();

		if(action.equals("Go")){
			DatabaseInterface.setUnP(result1, result2);
			super.dispose();
			if (checkGUI == true) {
				new Master();
			}
			else if (checkGUI == false) {
				new Master(checkGUI);
			}
		}
	}
	public static void main (String [] args) {
		new MasterCredentials(checkGUI).setVisible(true);
	}
}

