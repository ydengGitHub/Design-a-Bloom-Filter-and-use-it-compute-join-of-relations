import java.math.BigInteger;
import java.util.BitSet;

/**
 * Bloom Filter with deterministic hash function.
 * 
 * @author Yan Deng
 *
 */
public class BloomFilterDet implements BloomFilter{
	private int setSize; // the size of S
	private int bitsPerElement; // the number of bits per element
	private int dataSize; // the number of elements added to the filter
	private int k; // the number of hash functions used
	public BitSet bitSet; // T[h(x)]=1 if x is in S
	private int bitSetSize;

	private static final BigInteger FNV64INIT = new BigInteger("cbf29ce484222325", 16);
	private static final BigInteger FNV64PRIME = new BigInteger("100000001b3", 16);
	private static final BigInteger DOMINATOR64 = new BigInteger("2").pow(64);
	private static final BigInteger FNV32INIT = new BigInteger("811c9dc5", 16);
	private static final BigInteger FNV32PRIME = new BigInteger("01000193", 16);
	private static final BigInteger DOMINATOR32 = new BigInteger("2").pow(32);

	/**
	 * Initialize a Bloom filter that can store a set S of cardinality setSize. The
	 * size of the filter should approximately be setSize * bitsPerElement. The
	 * number of hash functions should be the optimal choice which is
	 * ln2×filterSize/setSize.
	 * 
	 * @param setSize
	 * @param bitsPerElement
	 */
	public BloomFilterDet(int setSize, int bitsPerElement) {
		System.out.println("Create a new BloomFilterDet instance.");
		this.setSize = setSize;
		this.bitsPerElement = bitsPerElement;
		this.dataSize = 0;
		this.k = (int) (Math.log(2) * bitsPerElement);
		this.bitSetSize = nextPrime(setSize * bitsPerElement);
		this.bitSet = new BitSet(bitSetSize);
		// System.out.println("Created a new BloomFilterDet instance.");
	}

	/**
	 * Adds the string s to the filter. This method should be case-insensitive.
	 * For example, it should not distinguish between “Galaxy” and “galaxy”.
	 * 
	 * @param s
	 *            The input string
	 */
	public void add(String s) {
		s = s.toLowerCase();
		int[] hs = computeHashValues(s);
		for (int h : hs) {
			bitSet.set(h);
		}
		this.dataSize++;
	}

	/**
	 * Returns true if s appears in the filter; otherwise returns false. This
	 * method must also be case-insensitive.
	 * 
	 * @param s
	 *            The query String
	 * @return true if s appears in the filter false otherwise
	 */
	public boolean appears(String s) {
		s = s.toLowerCase();
		if (s.length() == 0 || s == null) {
			throw new IllegalArgumentException("Querry string should not be empty.");
		} else {
			int[] hs = computeHashValues(s);
			for (int h : hs) {
				if (!bitSet.get(h))
					return false;
			}
			return true;
		}
	}

	/**
	 * 
	 * @return the size of the filter table.
	 */
	public int filterSize() {
		return this.bitSetSize;
	}

	/**
	 * 
	 * @return the number of elements added to the filter.
	 */
	public int dataSize() {
		return this.dataSize;
	}

	/**
	 * 
	 * @return the number of hash function used.
	 */
	public int numHashes() {
		return this.k;
	}

	/**
	 * Compute the hash value of given string using FNV method.
	 * 
	 * @param s
	 *            given string
	 * @param fnv
	 *            32: using FNV32; 64: using FNV64
	 * @return hash value in BigInteger type
	 */
	public BigInteger computeHashValue(String s, int fnv) {
		BigInteger h;
		BigInteger prime;
		BigInteger dominator;

		if (fnv == 32) {
			h = FNV32INIT;
			prime = FNV32PRIME;
			dominator = DOMINATOR32;
		} else if (fnv == 64) {
			h = FNV64INIT;
			prime = FNV64PRIME;
			dominator = DOMINATOR64;

		} else {
			String ex = "The value of 2nd argument of computeHashValue method can be only 32 or 64.";
			throw new IllegalArgumentException(ex);
		}
		for (int i = 0; i < s.length(); i++) {
			h = h.xor(BigInteger.valueOf(s.charAt(i))); // XOR
			h = h.multiply(prime).mod(dominator); // *FNVPRIME%(2^64)(or 2^32)
		}
		return h;
	}

	/**
	 * Compute K different hash values for given string.
	 * 
	 * @param s
	 *            given string
	 * @return an array stored the K hash values
	 */
	public int[] computeHashValues(String s) {
		int[] hs = new int[k];
		BigInteger h;
		for (int i = 0; i < k; i++) {
			// FNV64(s)+FNV32(s)*i
			h = (computeHashValue(s, 64).add(computeHashValue(s, 32).multiply(BigInteger.valueOf(i))))
					.mod(BigInteger.valueOf(bitSetSize));
			hs[i] = h.intValue();
		}
		return hs;
	}

	/**
	 * Check if the given integer is a prime or not.
	 * 
	 * @param num
	 * @return true: is a prime; false: else.
	 */
	public static boolean isPrime(int num) {
		if (num == 2)
			return true;
		if (num % 2 == 0)
			return false;
		for (int i = 3; i * i <= num; i += 2)
			if (num % i == 0)
				return false;
		return true;
	}
	
	/**
	 * Find the first prime bigger than the given integer.
	 * @param num
	 * @return a prime number
	 */
	public static int nextPrime(int num) {
		int nextPrime = num;
		if (nextPrime % 2 == 0)
			nextPrime += 1;

		while (!isPrime(nextPrime)) {
			nextPrime += 2;
		}
		// System.out.println("Next prime is " + nextPrime);
		return nextPrime;
	}
}
