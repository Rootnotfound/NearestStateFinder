package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class NoResult extends JFrame{
	public NoResult(){
		setLayout(new GridLayout(3,1));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		JButton bt = new JButton("Back");
		JButton qt = new JButton("Quit");
		bt.setBounds(150, 0, 80, 20);
		qt.setBounds(250,0,80,20);
		buttonPanel.add(bt);
		bt.addActionListener(
				new ActionListener() {
	            @Override
				public void actionPerformed(ActionEvent e) {
	                dispose();
	            }
	        });
		buttonPanel.add(qt);
		qt.addActionListener(
				new ActionListener() {
	            @Override
				public void actionPerformed(ActionEvent e) {
	            	System.exit(0);
	            }
	        });
		add(new JLabel(""));
		Font resultFont = new Font("SansSerif", Font.BOLD, 15);
		JLabel searchresult = new JLabel("            Warning: Invalid input. Please input again");
		searchresult.setFont(resultFont);
		add(searchresult);
		add(buttonPanel);
	}

}
