package sneer.bricks.crypto.tests;

import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import sneer.bricks.crypto.Crypto;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;

public class CryptoTest extends BrickTestSupport {

	@Inject
	private Crypto _crypto;
	
	@Test
	public void testWhirlPool() throws Exception {
		
		/**
		 * See http://en.wikipedia.org/wiki/WHIRLPOOL
		 */
		String input = "The quick brown fox jumps over the lazy dog";
		String expected = 	"B97DE512E91E3828B40D2B0FDCE9CEB3C4A71F9BEA8D88E75C4FA854DF36725F" +
							"D2B52EB6544EDCACD6F8BEDDFEA403CB55AE31F03AD62A5EF54E42EE82C3FB35";

		MessageDigest digester = MessageDigest.getInstance("WHIRLPOOL", "BC");
		byte[] hash = digester.digest(input.getBytes());
		assertEquals(512, hash.length * 8);
		String asHexa = new String(Hex.encode(hash));
		assertEquals(expected, asHexa.toUpperCase());
	}
	
	@Test
	public void testSneer1024() throws Exception {

		String input = "The quick brown fox jumps over the lazy dog";
		String expected = 	"07e547d9586f6a73f73fbac0435ed76951218fb7d0c8d788a309d785436bbb642" +
							"e93a252a954f23912547d1e8a3b5ed6e1bfd7097821233fa0538f3db854feb97de" +
							"512e91e3828b40d2b0fdce9ceb3c4a71f9bea8d88e75c4fa854df36725fd2b52eb6" +
							"544edcacd6f8beddfea403cb55ae31f03ad62a5ef54e42ee82c3fb3500";
		
		byte[] hash = _crypto.sneer1024(input.getBytes());
		assertEquals(1024, hash.length * 8);
		String asHexa = new String(Hex.encode(hash));
		assertEquals(expected, asHexa);
	}
}
