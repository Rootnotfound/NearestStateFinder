package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class RangeSearchResult extends JFrame{
	public ArrayList<JLabel> labelList = new ArrayList<JLabel>();
	public RangeSearchResult(){
		GridLayout Layout = new GridLayout(12, 1, 5, 1);
		setLayout(Layout);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		JButton bt = new JButton("Back");
		JButton qt = new JButton("Quit");
		bt.setBounds(250, 0, 80, 20);
		qt.setBounds(350,0,80,20);
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
		Font resultFont = new Font("SansSerif", Font.BOLD, 15);
		JLabel searchresult = new JLabel("The states/cities in range are ");
		searchresult.setFont(resultFont);
		JPanel sPanel = new JPanel();
		sPanel.add(searchresult, BorderLayout.CENTER);
		add(sPanel);
		for (int j = 0; j < 10; j++){
			labelList.add(new JLabel(""));
		}
		for (int m = 0; m < labelList.size(); m++){
			JPanel labelPanel = new JPanel();
			labelPanel.add(labelList.get(m), BorderLayout.CENTER);
			add(labelPanel);
		}
		add(buttonPanel);
		}
}
