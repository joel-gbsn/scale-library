package datamanagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.Scale;

public class Writer {
	
	private String customFilename;
	
	public Writer(String customFilename) {
		this.customFilename = customFilename;
	}
	
	/**
	 * Writes the current list of custom scales to the given file.
	 * @param filename the file to write to
	 */
	public void writeScales(List<Scale> scales) {
		
		// prepare the file for writing
		File file = new File(customFilename);
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			// create the file writers
			fw = new FileWriter(file, false);
			pw = new PrintWriter(fw);
			
			// write each custom scale to the file
			for (Scale scale : scales) {
				pw.println(scale.toFileLine());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				// close the file writers
				pw.flush();
				fw.close();
				pw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
