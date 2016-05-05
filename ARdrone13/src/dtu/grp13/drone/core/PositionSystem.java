package dtu.grp13.drone.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dtu.grp13.drone.vector.Vector2;

public class PositionSystem {
	private Map<String, Vector2> wallMarks;
	
	
	public Vector2 getVec(String name) {
		return wallMarks.get(name);
	}
	
	public PositionSystem() throws IOException {
		wallMarks = new HashMap<>();
		
		String csvMarks ="./resources/WallCoordinates.csv";
		BufferedReader reader = null;
		String csvSplit = ";";
		String line = "";
		
		try{
			reader = new BufferedReader(new FileReader(csvMarks));
			reader.readLine();
			while((line = reader.readLine()) != null){
				System.out.println(line);
				String[] splitedLine = line.split(csvSplit);
				double x = Double.parseDouble(splitedLine[1].equals("-")? "-1" : splitedLine[1]);
				double y = Double.parseDouble(splitedLine[2].equals("-")? "-1" : splitedLine[2]);
				wallMarks.put(splitedLine[0], new Vector2(x,y));
			}
			
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} finally {
			reader.close();
		}
		
	}

}
