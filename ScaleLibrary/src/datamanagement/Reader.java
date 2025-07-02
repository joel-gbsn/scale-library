package datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Scale;
import util.Interval;

/**
 * The reader for loading the scale data from the input files.
 * @author Joel Gibson
 */
public class Reader {
	/**
	 * The file containing the base scale set.
	 */
	private String baseFilename;
	
	/**
	 * The file containing the custom scale set.
	 */
	private String customFilename;
	
	/**
	 * Creates a new reader 
	 * @param baseFilename
	 * @param customFilename
	 */
	public Reader(String baseFilename, String customFilename) {
		this.baseFilename = baseFilename;
		this.customFilename = customFilename;
	}
	
	/**
	 * Reads in the data from the file containing the base scales.
	 * @return the list of base scales
	 */
	public List<Scale> readBaseScales() {
		return readScales(baseFilename);
	}
	
	/**
	 * Reads in the data from the file containing the custom scales.
	 * @return the list of custom scales
	 */
	public List<Scale> readCustomScales() {
		return readScales(customFilename);
	}
	
	/**
	 * Reads in the data from the given file into a list.
	 * @param filename the name of the file to read
	 * @return the list of scales from the file
	 */
	public List<Scale> readScales(String filename) {
		List<Scale> scales = new ArrayList<>();
		
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
				
				// extract the scale parameters
				String name = data[0].trim();
				String[] intervalList = data[1].split(",\\s*");
				boolean simplified = Boolean.valueOf(data[2].trim());
				
				// convert each interval name into the associated Interval object
				List<Interval> intervals = new ArrayList<>();
				for (String interval : intervalList) {
					intervals.add(Interval.getInterval(interval));
				}
				
				// create the scale
				scales.add(new Scale(name, intervals, simplified));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			// close the file readers
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Collections.sort(scales);
		return scales;
	}
}
