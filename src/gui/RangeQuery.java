package gui;

import java.util.ArrayList;

import xxl.core.cursors.Cursor;
import xxl.core.indexStructures.ORTree;
import xxl.core.indexStructures.RTree;
import xxl.core.spatial.points.DoublePoint;
import xxl.core.spatial.rectangles.DoublePointRectangle;

public class RangeQuery {
	public static ArrayList<Object> rangeList = new ArrayList<Object>();
	public static void RangeQuery(RTree rtree, int dimension, double lat1, double lat2, double long1, double long2, int number) {
    	// create query window
        double [] leftCorner = new double[dimension];
        double [] rightCorner = new double[dimension];
        //leftCorner[0] = 0.4975;
        //rightCorner[0] = 0.5025;
        //leftCorner[1] = 0.4975;
        //rightCorner[1] = 0.5025;
        leftCorner[0] = lat1;
        rightCorner[0] = lat2;
        leftCorner[1] = long1;
        rightCorner[1] = long2;
        DoublePointRectangle queryRange = new DoublePointRectangle(leftCorner, rightCorner);            
        // perform query
        Cursor results = rtree.query(queryRange);
        // show results
        int counter = 0;
        //System.out.println("Results for range query ("+queryRange+")");
        int i = 0;
        while (results.hasNext() && i < number) {
            DoublePoint next = (DoublePoint)results.next();
            rangeList.add(next);
            //rangeList.add("AAA");
            System.out.println("result no. "+(++counter)+": "+next);
            i++;
        }
        results.close();
        System.out.println("###############" + rangeList.size());
    }
}
