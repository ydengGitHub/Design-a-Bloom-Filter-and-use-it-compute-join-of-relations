import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

/**
 * Bloom Filter with random hash function.
 * 
 * @author Yan Deng
 *
 */
public class BloomFilterRan implements BloomFilter{

	private int setSize; // the size of S
	private int bitsPerElement; // the number of bits per element
	private int dataSize; // the number of elements added to the filter
	private int k; // the number of hash functions used
	public BitSet checkSet; // T[h(x)]=1 if x is in S
	private int checkSetSize;
	private int[][] functions;

	public BloomFilterRan(int setSize, int bitsPerElement) {
		System.out.println("Create a new BloomFilterRan Instance.");
		this.setSize = setSize;
		this.bitsPerElement = bitsPerElement;
		this.dataSize = 0;
		this.k = (int) (Math.log(2) * bitsPerElement);
		this.checkSetSize = nextPrime(setSize * bitsPerElement);
		this.checkSet = new BitSet(checkSetSize);
		this.functions = new int[k][2];
		for (int i = 1; i < k; i++) {
			functions[i][0] = generateRan(); // Generate a;
			functions[i][1] = generateRan(); // Generate b;
			System.out.println("a= " + functions[i][0] + " b= " + functions[i][1]);
		}
		// System.out.println("Created a new BloomFilterRan Instance.");
	}

	/**
	 * Adds the string s to the filter. This method should be case-insensitive.
	 * For example, it should not distinguish between “Galaxy” and “galaxy”.
	 * 
	 * @param s
	 */
	public void add(String s) {
		s = s.toLowerCase();
		int[] hs = computeHashValues(s);
		for (int h : hs) {
			checkSet.set(h);
		}
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
				if (!checkSet.get(h))
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
		return this.checkSetSize;
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
	 * Compute one hash value of given string.
	 * 
	 * @param s
	 *            given string
	 * @param j
	 *            jth hash value for the given string （j is in range [1,...,k]）
	 * @return a hahs value of given string in BigInteger type
	 */
	public BigInteger computeHashValue(String s, int j) {
		long h=0;
		long dominator=(long) Math.pow(2, 32);
		for (int i = 0; i < s.length(); i++) {
			//h=a_j*h+b_j+s.charAt(i); 
			h=(h*functions[j][0]+functions[j][1]+s.charAt(i));
			h=h%dominator;
		}
		return BigInteger.valueOf(h);
	}

	/**
	 * Compute K different hash values for given string using a,b values stored
	 * in functions[][].
	 * 
	 * @param s
	 *            given string
	 * @return an array stored the K hash values
	 */
	public int[] computeHashValues(String s) {
		int[] hs = new int[k];
		BigInteger h;
		for (int i = 0; i < k; i++) {
			h=computeHashValue(s,i);
			h = h.mod(BigInteger.valueOf(checkSetSize));
			hs[i] = h.intValue();
		}
		return hs;

	}

	/**
	 * Generate a random integer between[1,...,p-1] for the random function's a and
	 * b.
	 * 
	 * @return a random generated integer between[1,...,p-1];
	 */
	private int generateRan() {
		Random rand = new Random();
		int par = rand.nextInt(checkSetSize - 1) + 1;
		return par;
	}

	/**
	 * Check if the given integer is a prime or not.
	 * 
	 * @param num
	 * @return true: is a prime; false: else.
	 */
	private static boolean isPrime(int num) {
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
	 * 
	 * @param num
	 * @return a prime number
	 */
	private static int nextPrime(int num) {
		int nextPrime = num;
		if (nextPrime % 2 == 0)
			nextPrime += 1;

		while (!isPrime(nextPrime)) {
			nextPrime += 2;
		}

		// System.out.println("Next prime is " + nextPrime);
		return nextPrime;
	}

	/**
	 * An alternative string hash function.
	 * 
	 * @param str
	 *            given string
	 * @return hash value of the string
	 */
	public static int djb2(String str) {
		int hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + ((hash << 5) - hash);
		}
		return hash;
	}

}
