package sneer.bricks.crypto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import sneer.bricks.crypto.Crypto;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;

public class CryptoTest extends BrickTestSupport {

	@Inject
	private Crypto _crypto;
	
	@Test
	public void testSneer1024() throws Exception {
	
		byte[] expected = new byte[] {
				-53, 102, 59, -12, 59, 61, -97, 57, -87, 64, 21, -73, 37, 
				-61, 124, -82, -43, 115, -119, -71, 14, -27, -115, 30, 46, 
				115, 12, 55, -16, 14, 5, -60, 28, -120, -71, -70, 34, -81, 
				2, 85, 71, 49, -57, 76, 56, 48, 80, -48, -59, 109, 42, 91, 
				-55, 26, -84, -28, -47, 117, -67, -68, -25, -101, 98, -33, 
				-104, -39, 27, 45, -71, -23, 14, -79, 90, 92, -104, 99, 101, 
				-6, -45, 35, -5, -100, -109, -11, -33, 9, -111, -100, -43, 
				-49, -66, -119, 24, -10, -28, -18, -61, 39, 47, -122, 96, 17, 
				-102, -15, -24, 99, -41, -66, -17, 41, 97, 86, -101, 123, -65, 
				123, -93, -62, -32, -50, 70, 20, 63, 17, -26, -67, 62, 0
		};
		
		String input = "Sample input";
		byte[] hash = _crypto.sneer1024(input.getBytes());
		assertEquals(1024, hash.length * 8);
		assertTrue(Arrays.equals(expected, hash));
	}
}
