package datamanagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.Scale;

/**
 * The writer for saving custom scales to file.
 * @author Joel Gibson
 */
public class Writer {
	/**
	 * The file for writing the custom scales.
	 */
	private String customFilename;
	
	/**
	 * Creates a new writer with the given custom scales file.
	 * @param customFilename
	 */
	public Writer(String customFilename) {
		this.customFilename = customFilename;
	}
	
	/**
	 * Writes the given list of scales to file.
	 * @param filename the file to write to
	 */
	public void writeScales(List<Scale> scales) {
		File file = new File(customFilename);
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			// create the file writers
			fw = new FileWriter(file, false);
			pw = new PrintWriter(fw);
			
			// write each scale to the file
			for (Scale scale : scales) {
				pw.println(toFileLine(scale));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			// close the file writers
			try {
				pw.flush();
				fw.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Converts the parameters of the given scale into a string of text that can be written to file.
	 * @param scale the scale to convert
	 * @return the string containing the scale data
	 */
	protected String toFileLine(Scale scale) {
		return scale.getName() + "; " + String.join(", ", scale.getIntervalNames()) + "; " + scale.isSimplified();
	}
}
