package datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.Scale;
import util.Interval;

public class Reader {
	
	private String baseFilename;
	private String customFilename;
	
	public Reader(String baseFilename, String customFilename) {
		this.baseFilename = baseFilename;
		this.customFilename = customFilename;
	}
	
	public List<Scale> readBaseScales() {
		return readScales(baseFilename);
	}
	
	public List<Scale> readCustomScales() {
		return readScales(customFilename);
	}
	
	public List<Scale> readScales(String filename) {
		// the list to store each line
		List<Scale> scales = new ArrayList<>();
		
		// prepare the file for reading
		File file = new File(filename);
		FileReader fr = null;
		BufferedReader br = null;
		
		try {	
			// create readers to read the file
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			// read each line from the file
			String line = "";
			while ((line = br.readLine()) != null) {
				String data[] = line.trim().split(";\\s*");
				
				String name = data[0].toLowerCase().trim();
				String[] intervalList = data[1].split(",\\s*");
				boolean simplify = Boolean.valueOf(data[2].trim());
				
				Interval[] intervals = new Interval[intervalList.length];
				for (int i = 0; i < intervalList.length; i++) {
					intervals[i] = Interval.getInterval(intervalList[i]);
				}
				
				scales.add(new Scale(name, intervals, simplify));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				// close the file readers
				fr.close();
				br.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return scales;
	}

}
