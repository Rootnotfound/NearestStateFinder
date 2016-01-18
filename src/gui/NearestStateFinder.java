package gui;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import xxl.core.collections.containers.Container;
import xxl.core.collections.containers.io.BlockFileContainer;
import xxl.core.collections.containers.io.BufferedContainer;
import xxl.core.collections.containers.io.ConverterContainer;
import xxl.core.functions.AbstractFunction;
import xxl.core.functions.Function;
import xxl.core.indexStructures.ORTree;
import xxl.core.indexStructures.RTree;
import xxl.core.indexStructures.Tree.Query.Candidate;
import xxl.core.io.LRUBuffer;
import xxl.core.io.converters.ConvertableConverter;
import xxl.core.io.converters.Converter;
import xxl.core.io.converters.IntegerConverter;
import xxl.core.io.converters.LongConverter;
import xxl.core.spatial.points.DoublePoint;
import xxl.core.spatial.rectangles.DoublePointRectangle;
import xxl.core.spatial.rectangles.Rectangle;
public class NearestStateFinder extends JFrame {
	  public static RTree rtree = new RTree();
	  public static ORTree.IndexEntry rootEntry = null; 
      public static Container fileContainer;
      //public static ConverterContainer converterContainer = new ConverterContainer(fileContainer, converter);
	  public NearestStateFinder() { 
		  JButton sb = new JButton("Search");
		  JButton rsb = new JButton("Range Search");
		  setLayout(new GridLayout(5, 1, 30, 30));
		  JLabel title = new JLabel("     Please Choose the Function You Need");
		  Font titleFont = new Font("SansSerif", Font.BOLD, 20);
		  Font buttonFont = new Font("SansSerif", Font.BOLD, 14);
		  title.setFont(titleFont);
		  sb.setFont(buttonFont);
		  sb.addActionListener(new sbListener());
		  rsb.setFont(buttonFont);
		  rsb.addActionListener(new rsbListener());
		  JPanel sbPanel = new JPanel();
		  sbPanel.setLayout(new GridLayout(1, 3, 0, 0));
		  sbPanel.add(new JLabel(""));
		  sbPanel.add(sb);
		  sbPanel.add(new JLabel(""));
		  JPanel rsbPanel = new JPanel();
		  rsbPanel.setLayout(new GridLayout(1, 3, 0, 0));
		  rsbPanel.add(new JLabel(""));
		  rsbPanel.add(rsb);
		  rsbPanel.add(new JLabel(""));
		  add(new JLabel(""));
		  add(title);
		  add(sbPanel);
		  add(rsbPanel);
		  add(new JLabel(""));
	  }
	  private class sbListener implements ActionListener{
		  @Override
		public void actionPerformed(ActionEvent arg0) {
			  Search sframe = new Search();
			  sframe.setTitle("Neareest State/Country Finder"); 
			  sframe.setSize(900, 700); 
			  sframe.setLocationRelativeTo(null);
			  sframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			  sframe.setVisible(true);
		  }
	  }
	  private class rsbListener implements ActionListener{
		  @Override
		public void actionPerformed(ActionEvent arg0) {
			  RangeSearch rsframe = new RangeSearch();
			  rsframe.setTitle("Neareest State/Country Finder"); 
			  rsframe.setSize(900, 700); 
			  rsframe.setLocationRelativeTo(null);
			  rsframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			  rsframe.setVisible(true);
		  }
	  }
	  /** Dimension of the data. 
       */
      public static final int dimension = 2;
                      
      /** Size of a block in bytes.
       */
      public static int blockSize = 1536;
                      
      /** Factor which the minimum capacity of nodes is smaller than the maximum capacity.
       */
      public static double minMaxFactor = 1.0/3.0;
                      
      /** Buffersize (number of node-objects).
       */
      public static int bufferSize = 100;
                                                                                      
      /**
       * Factory Function to get a leaf entry.
       */
      static Function<Object, DoublePoint> LEAFENTRY_FACTORY = new AbstractFunction<Object, DoublePoint>() {
              public DoublePoint invoke() {
                      return new DoublePoint(dimension);
              }
      };
      
      /** 
       * Function creating a descriptor for a given object. 
       */
      static Function<Object,Object> GET_DESCRIPTOR = new AbstractFunction<Object,Object>() {
              @Override
              public Object invoke (Object o) {
                      DoublePoint p = (DoublePoint)o;
                      return new DoublePointRectangle(p, p); 
              }
      };

      /**
       * Returns a comparator which evaluates the distance of two candidate objects
       * to the specified <tt>queryObject</tt>. This comparator
       * is used for nearest neighbor queries and defines an order on candidate-
       * descriptors. With the help of a priority queue (Min-heap) and this
       * comparator the nearest neighbor query can be performed.
       *
       * @param queryObject a KPE to which the nearest neighbors should be determined
       * @return a comparator defining an order on candidate objects
       */
      public static Comparator<Object> getDistanceBasedComparator (DoublePoint queryObject) {
              final Rectangle query = new DoublePointRectangle(queryObject, queryObject);
              return new Comparator<Object> () {
                      public int compare (Object candidate1, Object candidate2) {
                              Rectangle r1 = (Rectangle) (((Candidate) candidate1).descriptor()) ;
                              Rectangle r2 = (Rectangle) (((Candidate) candidate2).descriptor()) ;                            
                              double d1 = query.distance(r1, 2);
                              double d2 = query.distance(r2, 2);
                              return (d1<d2) ? -1 : ( (d1==d2) ? 0 : 1 );
                      }
              };
      }

	  public static void main(String[] args) throws Exception {
		  String filename = "RTree";
		  NearestStateFinder frame = new NearestStateFinder();
		  frame.setTitle("Neareest State/Country Finder"); 
		  frame.setSize(450, 300); 
		  frame.setLocationRelativeTo(null);
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setVisible(false);
		// check for command line argument
          if (args.length!=1) 
                  System.out.println("usage: java SimpleRTreeTest filename");
          // test if RTree exists
          boolean reopen = (new File(filename+".ctr")).canRead();          
          // size of a data entry = size of DoublePoint (1 double per dimension)
          int dataSize = dimension*8;
          // size of a decriptor = size of DoublePointRectangle (2 doubles per dimension)
          int descriptorSize = dimension*2*8; 
                                                                    
          if (!reopen) {
                  System.out.println("Building new RTree");
                  fileContainer = new BlockFileContainer(filename, blockSize);
          }
          else {
                  System.out.println("Using existing RTree");
                  fileContainer = new BlockFileContainer(filename);
                  File parameters = new File(filename+"_params.dat");
                  DataInputStream dos = new DataInputStream(new FileInputStream(parameters));
                  int height = IntegerConverter.DEFAULT_INSTANCE.read(dos);
                  long rootPageId = LongConverter.DEFAULT_INSTANCE.read(dos);                                             
                  DoublePointRectangle rootDescriptor = (DoublePointRectangle) ConvertableConverter.DEFAULT_INSTANCE.read(dos, new DoublePointRectangle(dimension));
                  rootEntry = (ORTree.IndexEntry) ((ORTree.IndexEntry)rtree.createIndexEntry(height)).initialize(rootDescriptor).initialize(rootPageId);
          }
          // determine Converters and Containers 
          Converter converter = rtree.nodeConverter(new ConvertableConverter(LEAFENTRY_FACTORY), dimension);
          ConverterContainer converterContainer = new ConverterContainer(fileContainer, converter);
          // use buffer
          BufferedContainer bufferedContainer = new BufferedContainer(converterContainer, new LRUBuffer(bufferSize), true);                               
          // initialize RTree
          rtree.initialize(rootEntry, GET_DESCRIPTOR, bufferedContainer, blockSize, dataSize, descriptorSize, minMaxFactor);
          //Insert.Insert(reopen, rtree, bufferedContainer, dimension);
          Insert.InsertData(reopen, rtree, bufferedContainer, dimension);
          //RangeQuery.RangeQuery(rtree, dimension);
          //NearestNeighbour.NearestNeighbour(rtree, reopen, filename, bufferedContainer, dimension);
          frame.setVisible(true);
		  }
	  }
		 
