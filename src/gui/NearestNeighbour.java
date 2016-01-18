package gui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import xxl.core.collections.containers.io.BufferedContainer;
import xxl.core.collections.queues.DynamicHeap;
import xxl.core.cursors.Cursor;
import xxl.core.cursors.filters.Taker;
import xxl.core.indexStructures.ORTree;
import xxl.core.indexStructures.Tree.Query.Candidate;
import xxl.core.io.converters.ConvertableConverter;
import xxl.core.io.converters.IntegerConverter;
import xxl.core.io.converters.LongConverter;
import xxl.core.spatial.points.DoublePoint;
import xxl.core.spatial.rectangles.DoublePointRectangle;
import xxl.core.spatial.rectangles.Rectangle;

public class NearestNeighbour {
	//public static ArrayList<Object> result = new ArrayList<Object>();
	public static ArrayList<DoublePoint> result = new ArrayList<DoublePoint>();
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
	public static void NearestNeighbour(ORTree rtree, boolean reopen, String args, BufferedContainer bufferedContainer, int dimension, int number, double x, double y) throws Exception{
        // create query point
        double [] point = new double[dimension]; 
        point[0] = x;
        point[1] = y;
        DoublePoint queryObject = new DoublePoint(point);               
        // lazy Iterator of all nearest neighbors
        Iterator neighbors = rtree.query(new DynamicHeap(getDistanceBasedComparator(queryObject)), 0);          
        // compute only 5 nearest neigbors
        Cursor results = new Taker(neighbors, number);
        // show results
        int counter = 0;
        System.out.println("\nResults for nearest neighbor query ("+queryObject+")");
        while (results.hasNext()) {
                DoublePoint next = (DoublePoint)((Candidate)results.next()).entry();
                result.add(next);
                System.out.println("candidate no. "+(++counter)+": "+next+" (distance="+next.distanceTo(queryObject)+")");
        }
        results.close();

        // save parameters of RTree
        if (!reopen) {
                File parameters = new File(args+"_params.dat");
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(parameters));
                IntegerConverter.DEFAULT_INSTANCE.write(dos, rtree.height());
                LongConverter.DEFAULT_INSTANCE.write(dos, (Long)rtree.rootEntry().id());                                                
                ConvertableConverter.DEFAULT_INSTANCE.write(dos, (DoublePointRectangle) rtree.rootDescriptor());
        }
        
        // close container
        bufferedContainer.flush();
        bufferedContainer.close();   
    	
    }

}
