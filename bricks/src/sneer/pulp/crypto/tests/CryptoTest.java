package sneer.pulp.crypto.tests;

import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.crypto.Crypto;
import wheel.lang.StringUtils;

public class CryptoTest extends TestThatIsInjected {

	@Inject
	private Crypto _crypto;
	
	private static final String INPUT = "The quick brown fox jumps over the lazy dog"; 


	/**
	 * See http://en.wikipedia.org/wiki/SHA1
	 */
	private static final String SHA512 = 	"07e547d9586f6a73f73fbac0435ed76951218fb7d0c8d788a309d785436bbb64" +
	   										"2e93a252a954f23912547d1e8a3b5ed6e1bfd7097821233fa0538f3db854fee6";
	
	/**
	 * See http://en.wikipedia.org/wiki/WHIRLPOOL
	 */
	private static final String WHIRLPOOL = "b97de512e91e3828b40d2b0fdce9ceb3c4a71f9bea8d88e75c4fa854df36725f" +
											"d2b52eb6544edcacd6f8beddfea403cb55ae31f03ad62a5ef54e42ee82c3fb35";
 
	
	@Test
	public void testSha512() throws Exception {
		MessageDigest digester = MessageDigest.getInstance("SHA-512", "SUN");
		byte[] hash = digester.digest(INPUT.getBytes());
		assertEquals(512, hash.length * 8);
		assertHexa(SHA512, hash);
	}
	
	@Test
	public void testWhirlPool() throws Exception {
		MessageDigest digester = MessageDigest.getInstance("WHIRLPOOL", "BC");
		byte[] hash = digester.digest(INPUT.getBytes());
		assertEquals(512, hash.length * 8);
		assertHexa(WHIRLPOOL, hash);
	}
	
	@Test
	public void testSneer1024() throws Exception {
		byte[] hash = _crypto.digest(INPUT.getBytes()).bytes();
		assertEquals(1024, hash.length * 8);
		assertHexa(SHA512 + WHIRLPOOL, hash);
		
//		hash = _crypto.sneer1024("input".getBytes());
//		assertEquals(1024, hash.length * 8);
//		assertNotEquals("","");
//		assertHexa(SHA512 + WHIRLPOOL, hash);
		
	}

	private void assertHexa(String expected, byte[] hash) {
		assertEquals(expected, StringUtils.toHexa(hash));
	}
}
