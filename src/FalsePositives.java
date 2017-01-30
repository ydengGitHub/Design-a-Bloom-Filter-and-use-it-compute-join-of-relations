import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Random;

/**
 * Evaluate the false probability rate of both the filters—BloomFilterDet,
 * BloomFilterRan.
 * 
 * @author YAN Deng
 *
 */
public class FalsePositives {
	private BloomFilterDet bloomFilterDet;
	private BloomFilterRan bloomFilterRan;
	private HashSet<String> hashSet; //to compare with the bloomFilter hash tables;
	private int setSize; // set size
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private Random rand;
	private double[] result = new double[2];//store the false positive rate of bloomFilterDet and bloomFilterRan

	/**
	 * Constructor: Initialize a FalsePositives instance, define the set Size
	 * and bits Per Element.
	 * 
	 * @param setSize
	 * @param bitsPerElement
	 */
	public FalsePositives(int setSize, int bitsPerElement) {
		System.out.println("Creating a new FalsePositives instance...");
		bloomFilterDet = new BloomFilterDet(setSize, bitsPerElement);
		bloomFilterRan = new BloomFilterRan(setSize, bitsPerElement);
		hashSet = new HashSet<String>();
		this.setSize = setSize;
		rand = new Random();
	}

	/**
	 * Create a FalsePositives instance with given setSize and bitsPerElement.
	 * Run the falsePositive method with given lengOfString and testSize to test
	 * the false positive values of two bloom filter methods. And show the
	 * results.
	 * 
	 * @param setSize
	 * @param bitsPerElement
	 * @param lengthOfString
	 * @param testSize
	 */
	public static void runFalsePositives(int setSize, int bitsPerElement, int lengthOfString, int testSize) {
		FalsePositives exp = new FalsePositives(setSize, bitsPerElement);
		exp.falsePositive(lengthOfString, testSize);
		exp.showResult(bitsPerElement);
	}

	/**
	 * Create a random sample set with given length of string, build the
	 * BloomFilterDet and BloomFilterRan tables. And create a random test set,
	 * test the false positive values of the two methods.
	 * 
	 * @param lengthOfString
	 * @param testSize
	 * @return false positive values of the two methods. arr[0]: det;
	 *         arr[1]:ran.
	 */
	public double[] falsePositive(int lengthOfString, int testSize) {
		if (setSize <= 0 || lengthOfString <= 0)
			throw new IllegalArgumentException("sample Size & length of string must be positive integer.");
		// add strings to the two filters and the hashSet
		createTables(lengthOfString);

		int countOfDetFP = 0; // Count of Deterministic method's false positives
		int countOfRanFP = 0; // Count of Random method's false positives
		int countOfTest = 0; // Count of total tested strings
		String testString;

		do {
			testString = randomString(lengthOfString);
			if (!hashSet.contains(testString)) {
				if (bloomFilterDet.appears(testString))
					countOfDetFP++;
				if (bloomFilterRan.appears(testString))
					countOfRanFP++;
			}
			countOfTest++;
		} while (countOfTest <= testSize);
		double detFP = (double) countOfDetFP / countOfTest;
		double ranFP = (double) countOfRanFP / countOfTest;
		result[0] = detFP;
		result[1] = ranFP;
		return result;
	}

	/**
	 * Create a random sample set with given length of string, build the
	 * BloomFilterDet and BloomFilterRan tables.
	 * 
	 * @param lengthOfString
	 */
	private void createTables(int lengthOfString) {
		for (int i = 0; i < setSize; i++) {
			String s = randomString(lengthOfString);
			s = s.toLowerCase();
			bloomFilterDet.add(s);
			bloomFilterRan.add(s);
			hashSet.add(s);
		}
	}

	/**
	 * Generate a random string with given length of String using the characters
	 * in the defined alphabet.
	 * 
	 * @param lengthOfString
	 * @return a random string
	 */
	private String randomString(int lengthOfString) {
		StringBuilder sb = new StringBuilder(lengthOfString);
		for (int i = 0; i < lengthOfString; i++) {
			sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}
		return sb.toString();
	}

	/**
	 * Return the set size.
	 * 
	 * @return the set size.
	 */
	public int getSize() {
		return setSize;
	}

	/**
	 * Show the test results.
	 * 
	 * @param bitsPerElement
	 */
	private void showResult(int bitsPerElement) {
		System.out.printf("%nTheoretically, the false positive probability is %.2f%%.%n",
				Math.pow(0.618, bitsPerElement) * 100);
		System.out.printf("BloomFilter deterministic: %.2f%%; BloomFilter random: %.2f%%.%n", result[0] * 100,
				result[1] * 100);
	}

}
