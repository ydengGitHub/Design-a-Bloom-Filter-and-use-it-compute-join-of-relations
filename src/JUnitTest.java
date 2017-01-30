import static org.junit.Assert.*;

import org.junit.Test;

public class JUnitTest {

	@Test
	public void testIsPrime() {
		assertTrue("11 should be a prime.",BloomFilterDet.isPrime(11));
	}

	@Test
	public void testNextPrime(){
		assertEquals("12's next prime should be 13.",13,BloomFilterDet.nextPrime(12));
	}
}
