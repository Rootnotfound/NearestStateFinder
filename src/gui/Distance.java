package gui;

public class Distance {
	public static double getDistance(double lat1, double lat2, double longi1, double longi2){
		double dist, x, y, a ,b;
		double R = 6371;
		x = (longi1 - longi2) * Math.cos((lat1 + lat2)/2);
		y = (lat1 - lat2);
		dist = Math.sqrt(x * x + y * y) * R;
		return dist;
	}

}
