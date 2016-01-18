package gui;

import java.util.Random;

import xxl.core.collections.containers.io.BufferedContainer;
import xxl.core.indexStructures.RTree;
import xxl.core.spatial.points.DoublePoint;
import java.util.regex.*;
import java.util.Random;
import java.io.*;

import xxl.core.collections.containers.io.BufferedContainer;
import xxl.core.indexStructures.RTree;
import xxl.core.spatial.points.DoublePoint;
public class Insert {
	public static void Insert(boolean reopen, RTree rtree, BufferedContainer bufferedContainer, int dimension) throws Exception{
    	if (!reopen) {
            Random random = new Random(42);
            for (int j=0; j<100000; j++) {                          
                    // create random coordinates
                    double [] point = new double[dimension];
                    for (int i=0; i<dimension; i++) 
                            point[i] = random.nextDouble();
                    // insert new point
                    rtree.insert(new DoublePoint(point));
                    
                    if (j%10000==0)
                            System.out.print(j/1000+"%, ");
            }
            System.out.println("100%");
            // flush buffers
            bufferedContainer.flush();
       }  	
    }
public static final String FName = "/Users/wuweilun/Downloads/EC521Project/NationalFile.txt";
	
	public static void Create(boolean reopen, RTree rtree, BufferedContainer bufferedContainer, int dimension) throws Exception
	{
    	if (!reopen) 
    	{
            Random random = new Random(42);
            for (int j=0; j<100000; j++) 
            {                          
                    // create random coordinates
                    double [] point = new double[dimension];
                    for (int i=0; i<dimension; i++) 
                    {
                    	point[i] = random.nextDouble();
                    }
                    // insert new point
                    rtree.insert(new DoublePoint(point));
                    
                    if (j%10000==0)
                    {
                    	System.out.print(j/1000+"%, ");
                    }
            }
            System.out.println("100%");
            // flush buffers
            bufferedContainer.flush();
       }  	
    }
	
	public static void InsertData(boolean reopen, RTree rtree, BufferedContainer bufferedContainer, int dimension) throws Exception
	{
		int i = 0;
		
		int lineNum = getTotalLines(FName);
		
		double [] point = new double[dimension];
		
		Pattern pattern;
		
		Matcher matcher;
		
		String temp;
		
		boolean flag = false;
		
		if (!reopen) 
    	{
			BufferedReader reader = new BufferedReader(new InputStreamReader( new FileInputStream(FName)));
			
			String line = reader.readLine();
			
			while( (i < lineNum) && (line != null))
			{
				//��ȡ����
				pattern = Pattern.compile("([0-9]{2}).([0-9]{7})");
				matcher = pattern.matcher(line);
				
				if(matcher.find() == true)
				{
					point[0] = Double.valueOf(matcher.group(0)).doubleValue();
					
					pattern = Pattern.compile("-([0-9]{2,3}).([0-9]{7})");
					matcher = pattern.matcher(line);
					
					if(matcher.find() == true)
					{
						point[1] = Double.valueOf(matcher.group(0)).doubleValue();
						
						flag = true;
					}
				}
				
				if(flag == true)				
				{
					System.out.println("Inserting " + point[0] + " , " + point[1]);
					
					flag = false;
				}
				
				rtree.insert(new DoublePoint(point));
				
				line = reader.readLine();
				
				i++;
			}
			
			bufferedContainer.flush();
			
			reader.close();
    	}
	}
	
	private static int getTotalLines(String fileName) throws IOException 
	{
        
		BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream(fileName)));

        LineNumberReader reader = new LineNumberReader(in);
        
		String s = reader.readLine();
        
		int lines = 0;
        
		while (s != null) 
		{
			lines++;
            s = reader.readLine();
        }
       
		reader.close();
        
		in.close();
        
		return lines;
	}
	
	private static String ReadLine(String fileName, int lineNumber) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader( new FileInputStream(fileName)));
		
		String line = reader.readLine();

		if (lineNumber < 0 || lineNumber > getTotalLines(fileName)) 
		{
			System.out.println("�����ļ���������Χ֮�ڡ�");
			
			reader.close();
			
			return null; 
		}
				        
		int num = 0;
				        
		while (line != null) 
		{
			if (lineNumber == ++num) 
			{
				reader.close();
				
				return line;
			}
			
			line = reader.readLine();
		}
		reader.close();
		
		return null;
	}
}
