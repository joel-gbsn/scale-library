package files;

import java.io.*;
import java.util.ArrayList;

import scales.*;

/**
 * Class for reading and parsing the input text files.
 * @author Joel Gibson
 */
public class FileLoader {
	
	/**
	 * Reads the given file into a list of lines.
	 * @param filename the path to the file
	 * @return the list of lines from the file
	 */
	private static ArrayList<String> readFile(String filename) {
		// the list to store each line
		ArrayList<String> lines = new ArrayList<String>();
		
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
				line = line.trim();
				
				// skip empty lines
				if (!line.isEmpty()) {
					lines.add(line);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
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
		
		return lines;
	}
	
	/**
	 * Reads the given file, parses the data, and creates a collection of scales from each line.
	 * @param filename the path to the file
	 * @return the list of each scale collections (one collection per line of the file)
	 */
	public static ArrayList<ScaleCollection> loadFileData(String filename) {
		// read the file into a list of lines
		ArrayList<String> lines = readFile(filename);
				
		// the list to store each scale collection
		ArrayList<ScaleCollection> scalesList = new ArrayList<ScaleCollection>();
		
		for (String line : lines) {
			// extract each data field from the line
			String[] data = line.split(";\\s*");
			
			String format = data[0].toLowerCase().trim();
			String type = data[1].toLowerCase().trim();
			String[] intervals = data[2].split(",\\s*");
			boolean simplify = Boolean.valueOf(data[3].trim());
			
			// create each scale in the scale collection
			ScaleCollection scales = new ScaleCollection(format, type, intervals, simplify);
			scales.createAllScales();
			scalesList.add(scales);
		}
		
		return scalesList;
	}
}
