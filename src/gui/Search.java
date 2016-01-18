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
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import xxl.core.collections.containers.io.BufferedContainer;
import xxl.core.collections.containers.io.ConverterContainer;
import xxl.core.functions.AbstractFunction;
import xxl.core.functions.Function;
import xxl.core.io.LRUBuffer;
import xxl.core.io.converters.ConvertableConverter;
import xxl.core.io.converters.Converter;
import xxl.core.spatial.points.DoublePoint;

public class Search extends JFrame {
	private static JTextField latText = new JTextField(10);
	private static JTextField longText = new JTextField(10);
	public JTextField k = new JTextField(10);
	public Search() {
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
		  JPanel kPanel = new JPanel();
		  kPanel.setLayout(new FlowLayout(FlowLayout.LEFT ,0,0));
		  kPanel.add(new JLabel(" k: "));
		  kPanel.add(k);
		  searchPanel.setLayout(new GridLayout(1,5,0,0));
		  searchPanel.add(new JLabel(""));
		  searchPanel.add(input);
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
		  JButton st = new JButton("Search");
		  JButton qt = new JButton("Clear");
		  qt.addActionListener(
			 new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					latText.setText("");
					longText.setText("");
					k.setText("");
				}
			 }
		  );
		  st.addActionListener(new SearchButtonListener());
		  button.add(new JLabel(""));
		  button.add(new JLabel(""));
		  button.add(st);
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
	
	static Function<Object, DoublePoint> LEAFENTRY_FACTORY = new AbstractFunction<Object, DoublePoint>() {
        public DoublePoint invoke() {
                return new DoublePoint(NearestStateFinder.dimension);
        }
    };
	
	  static class mapPanel extends JPanel{
			private String message = "P";
			private int x1;
			private int y1;
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
						double lati;
						double longi;
						double xd = x1;
						//System.out.println(x1);
						double yd = y1;
						//System.out.println(y1);
						if (x1 < 165 && y1 > 287){
							lati = 72 - (y1 -287) * 22 / 113;
							longi = x1 * 50 / 165 - 180;
						}
						else{
							lati = 50 - yd * 25 / 400;
						    longi = xd * 65 / 880 - 130;
						}
						DecimalFormat df = new DecimalFormat("0.000");
						latText.setText(String.valueOf(df.format(lati)));
						longText.setText(String.valueOf(df.format(longi)));
						repaint();
					}
				});
			}
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				g.drawString(message, x1, y1);
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
	  
	  private class SearchButtonListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Converter converter = NearestStateFinder.rtree.nodeConverter(new ConvertableConverter(LEAFENTRY_FACTORY), NearestStateFinder.dimension);
		        ConverterContainer converterContainer = new ConverterContainer(NearestStateFinder.fileContainer, converter);
		          // use buffer
		        BufferedContainer bufferedContainer = new BufferedContainer(converterContainer, new LRUBuffer(NearestStateFinder.bufferSize), true);                               
		          // initialize RTree
				try {
					NearestNeighbour.NearestNeighbour(NearestStateFinder.rtree, false, "RTree", bufferedContainer, NearestStateFinder.dimension, Integer.valueOf(k.getText()), Double.valueOf(latText.getText()), Double.valueOf(longText.getText()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Result result = new Result();
				result.setTitle("Search Result"); 
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
			    boolean la = isInteger(latText.getText()) || isDouble(latText.getText());
			    boolean lo = isInteger(longText.getText()) || isDouble(longText.getText());
			    int max = Integer.parseInt(k.getText());
			    if (max > 9){
			    	max = 9;
			    }
			    if (la && lo){
			    	double latitude = Double.parseDouble(latText.getText());
				    double longitude = Double.parseDouble(longText.getText());
				    int count = 0;
			        for (int n = 0; n < max * 2; n += 2){
			    	    DoublePoint returnResult = NearestNeighbour.result.get(count);
			    	    double [] point = new double[2]; 
			    	    double x , y, dist;
			    	    point = (double[]) NearestNeighbour.result.get(count).getPoint();
			    	    x = point[0];
			    	    y = point[1];
			    	    DecimalFormat df = new DecimalFormat("0.000");
			    	    dist = Distance.getDistance(x, latitude, y, longitude);
			        	result.labelList.get(n).setText(returnResult.toString());
			    	    result.labelList.get(n+1).setText(df.format(dist));
			    	    count++;
			        }
				    result.setVisible(true);
				}
				else{
					noresult.setVisible(true);	
				}
			}
	  }
}
