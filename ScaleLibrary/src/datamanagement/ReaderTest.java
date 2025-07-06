package datamanagement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import util.Scale;

class ReaderTest {

	@Test
	void testReadScales() {
		Reader reader = new Reader("test1.txt", "test2.txt");
		
		// test a regular file
		List<Scale> scales = reader.readBaseScales();
		assertEquals(3, scales.size(), "incorrect number of scales read in");
		Scale scale = scales.get(0);
		assertNotEquals("major", scale.getName(), "scale list should be ordered alphabetically");
		assertEquals("harmonic minor", scale.getName(), "incorrect name read in");
		assertEquals(8, scale.getIntervalNames().size(), "incorrect number of intervals read in");
		assertEquals("1", scale.getIntervalNames().get(0), "incorrect first interval read in");
		assertEquals("b3", scale.getIntervalNames().get(2), "incorrect modified interval read in");
		assertEquals(false, scale.isSimplified(), "incorrect simplification read in");
		
		// test an empty file
		scales = reader.readCustomScales();
		assertEquals(0, scales.size(), "empty file read incorrectly");
	}
}
