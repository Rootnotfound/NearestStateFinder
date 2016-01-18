package gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class RangeSearch extends JFrame {
	  private static JTextField latText = new JTextField(10);
	  private static JTextField longText = new JTextField(10);
	  private static JTextField latText2 = new JTextField(10);
	  private static JTextField longText2 = new JTextField(10);
	  public JTextField k = new JTextField(10);
	  public RangeSearch() {
		  setLayout(new BorderLayout());
		  mapPanel map = new mapPanel("P");
		  map.setBorder(BorderFactory.createTitledBorder(""));
		  map.setSize(900, 400);
		  JPanel mapPanel = new JPanel();
		  mapPanel.setLayout(null);
		  mapPanel.add(map);
		  JPanel searchPanel = new JPanel();
		  JPanel input = new JPanel();
		  input.setLayout(new GridLayout(4,1));
		  JPanel Latitude = new JPanel();
		  Latitude.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		  Latitude.add(new JLabel("Lat:     "));
		  Latitude.add(latText);
		  JPanel Longitude = new JPanel();
		  Longitude.setLayout(new FlowLayout(FlowLayout.LEFT ,0,0));
		  Longitude.add(new JLabel("Long:  "));
		  Longitude.add(longText);
		  input.add(Latitude);
		  input.add(new JLabel(""));
		  input.add(Longitude);
		  input.add(new JLabel(""));
		  JPanel secondinput = new JPanel();
		  JPanel secondLatitude = new JPanel();
		  secondLatitude.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		  secondLatitude.add(new JLabel(" to   "));
		  secondLatitude.add(latText2);
		  JPanel secondLongitude = new JPanel();
		  secondLongitude.setLayout(new FlowLayout(FlowLayout.LEFT ,0,0));
		  secondLongitude.add(new JLabel(" to   "));
		  secondLongitude.add(longText2);
		  secondinput.setLayout(new GridLayout(4,1));
		  secondinput.add(secondLatitude);
		  secondinput.add(new JLabel(""));
		  secondinput.add(secondLongitude);
		  secondinput.add(new JLabel(""));
		  JPanel kPanel = new JPanel();
		  kPanel.setLayout(new FlowLayout(FlowLayout.LEFT ,0,0));
		  kPanel.add(new JLabel(" k: "));
		  kPanel.add(k);
		  searchPanel.setLayout(new GridLayout(1,5,0,0));
		  searchPanel.add(new JLabel(""));
		  searchPanel.add(input);
		  searchPanel.add(secondinput);
		  searchPanel.add(kPanel);
		  searchPanel.add(new JLabel(""));
		  Font titleFont = new Font("SansSerif", Font.BOLD, 20);
		  JPanel titlePanel = new JPanel();
		  titlePanel.setLayout(new GridLayout(3,1));
		  JLabel title = new JLabel("                                             Please Type in Latitude and Longitude");
		  title.setFont(titleFont);
		  titlePanel.add(new JLabel(""));
		  titlePanel.add(title);
		  JPanel buttonPanel = new JPanel();
		  buttonPanel.setLayout(new GridLayout(2,1));
		  JPanel button = new JPanel();
		  button.setLayout(new GridLayout(2,5,15,5));
		  JButton rs = new JButton("Range Search");
		  JButton qt = new JButton("Clear");
		  qt.addActionListener(
			 new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					latText.setText("");
					longText.setText("");
					latText2.setText("");
					longText2.setText("");
					k.setText("");
				}
			 }
		  );
		  rs.addActionListener(new RangeSearchButtonListener());
		  button.add(new JLabel(""));
		  button.add(new JLabel(""));
		  button.add(rs);
		  button.add(qt);
		  for (int i = 0; i < 6; i++){
			  button.add(new JLabel(""));
		  }
		  JPanel main = new JPanel();
		  main.setLayout(new BorderLayout());
		  main.add(mapPanel, BorderLayout.CENTER);
		  main.add(searchPanel, BorderLayout.SOUTH);
		  add(main, BorderLayout.CENTER);
		  add(titlePanel, BorderLayout.NORTH);
		  add(button, BorderLayout.SOUTH);
      }

	  static class mapPanel extends JPanel{
			private String message = "P";
			private int x1;
			private int y1;
			private int x2;
			private int y2;
			private ImageIcon ImageIcon = new ImageIcon("USA.png"); 
			private Image background = ImageIcon.getImage();
			/** Construct a panel to draw string s */
			public mapPanel(String s) {
				message = s;
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e){
						x1 = e.getX();
					    //System.out.println("++++++++++" + x1);
						y1 = e.getY();
						//System.out.println("____________" + y1);
						double lati1;
						double longi1;
						double xd1 = x1;
						double yd1 = y1;
						if (xd1 < 165 && yd1 > 287){
							lati1 = 72 - (yd1 -287) * 22 / 113;
							longi1 = xd1 * 50 / 165 - 180;
						}
						else{
							lati1 = 50 - yd1 * 25 / 400;
						    longi1 = xd1 * 65 / 880 - 130;
						}
						DecimalFormat df = new DecimalFormat("0.000");
						latText.setText(String.valueOf(df.format(lati1)));
						longText.setText(String.valueOf(df.format(longi1)));
					}
				});
				addMouseMotionListener(new MouseMotionAdapter() {
				    @Override
					public void mouseDragged(MouseEvent e) {
				    	x2 = e.getX();
				    	//System.out.println(x2);
				        y2 = e.getY();
				        //System.out.println(y2);
				        double lati2;
						double longi2;
						double xd2 = x2;
						double yd2 = y2;
						if (xd2 < 165 && yd2 > 287){
							lati2 = 72 - (yd2 -287) * 22 / 113;
							longi2 = xd2 * 50 / 165 - 180;
						}
						else{
							lati2 = 50 - yd2 * 25 / 400;
						    longi2 = xd2 * 65 / 880 - 130;
						}
						DecimalFormat df = new DecimalFormat("0.000");
						latText2.setText(String.valueOf(df.format(lati2)));
						longText2.setText(String.valueOf(df.format(longi2)));
				        repaint();
				    }
			    });
			}
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				g.drawString(message, x1, y1);
				g.drawRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1));
				g.drawString(message, x2, y2);
			}
		} 
	  
	  public static boolean isInteger(String value) {
		  try {
			  Integer.parseInt(value);
		      return true;
		  } catch (NumberFormatException e) {
			  return false;
		  }
	  }
	  
	  public static boolean isDouble(String value) {
		  try {
			  Double.parseDouble(value);
		      if (value.contains("."))
		    	  return true;
		      return false;
		  } catch (NumberFormatException e) {
			  return false;
		  }
	  }
	  
	  private class RangeSearchButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean la1 = isInteger(latText.getText()) || isDouble(latText.getText());
		    boolean lo1 = isInteger(longText.getText()) || isDouble(longText.getText());
		    boolean la2 = isInteger(latText2.getText()) || isDouble(latText2.getText());
		    boolean lo2 = isInteger(longText2.getText()) || isDouble(longText2.getText());
		    RangeSearchResult result = new RangeSearchResult();
			result.setTitle("Range Search Result"); 
			result.setSize(500, 400); 
			result.setLocationRelativeTo(null);
			result.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
			result.setVisible(false);
			NoResult noresult = new NoResult();
		    noresult.setTitle("Invalid Input"); 
		    noresult.setSize(400, 200); 
		    noresult.setLocationRelativeTo(null);
		    noresult.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
		    noresult.setVisible(false);
		    if (la1 && la2 && lo1 && lo2){		    
		    	double latitude = Double.parseDouble(latText2.getText());
			    double longitude = Double.parseDouble(longText.getText());
			    double latitude2 = Double.parseDouble(latText.getText());
			    double longitude2 = Double.parseDouble(longText2.getText());
			    int max = Integer.valueOf(k.getText());
			    if (max > 9){
			    	max = 9;
			    }
			    RangeQuery.RangeQuery(NearestStateFinder.rtree, NearestStateFinder.dimension, latitude, latitude2, longitude, longitude2, max);
		        //String Nearest = String.valueOf(latitude);
			    System.out.println("latitude1" + latitude);
			    System.out.println("latitude2" + latitude2);
			    System.out.println("longitude1" + longitude);
			    System.out.println("longitude2" + longitude2);
	    	    for (int n = 0; n < max; n += 1){
			   	    result.labelList.get(n).setText(RangeQuery.rangeList.get(n).toString());
		    	    //result.labelList.get(n+1).setText(RangeQuery.rangeList.get(n+1).toString());
		        }
		        //System.out.println(result.labelList.size());	
			    result.setVisible(true);    
		    }
			else{
				noresult.setVisible(true);	
			}
		} 
	}
}
