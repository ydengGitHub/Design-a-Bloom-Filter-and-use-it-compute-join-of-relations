import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Computes the join of the two 2-ary relations using Bloom Filter.
 * Firstly, build the bloom filter for the relation 1 on server 1 and Send the bloom filter to Server 2.
 * Secondly, create the relation 3 (r3) on server 2 based on relation 2 and the received bloom filter, 
 * and send the r3 back to server 1.
 * Finally, join the relation 1 and the relation 3 received from server 2 on server 1.
 * 
 * @author Yan Deng
 *
 */
public class BloomJoin {

	private String inputFileName1;
	private String inputFileName2;
	private String outputFileName;
	private final String TEMPFILE = "temp.txt"; // the r3 file built on server2 and sent to server1
	
	private String filterType;	// Bloom Filter type; det-deterministic; ran-random.
	private BloomFilter bf;		//Both BloomFilterDet and BloomFilterRan implement the BloomFilter interface.
	
	private HashMap<String, ArrayList<Pair>> r3map = new HashMap<String, ArrayList<Pair>>(); 
	//After server 1 received the relation 3 from server 2, stored it in a hashmap for better performance.

	private int numOfLines1 = 0; // number of lines in input file1;
	private int numOfLines2 = 0; // number of lines in input file2;
	private int numOfLines3 = 0; // number of lines in TEMPFILE;
	private int numOfLines4 = 0; // number of lines in Output File;

	/**
	 * Initialize a new BloomJoin instance.
	 * @param file1	file name for relation 1
	 * @param file2	file name for relation 2
	 * @param outputFile	file name for the output 
	 * @param bloomFilterType	"det"-use bloom filter deterministic; "ran"-use bloom filter random
	 */
	public BloomJoin(String file1, String file2, String outputFile, String bloomFilterType) {
		this.inputFileName1 = file1;
		this.inputFileName2 = file2;
		this.outputFileName = outputFile;
		this.filterType = bloomFilterType.toLowerCase();
		if (!(filterType.equals("det") || filterType.equals("ran"))) {
			throw new IllegalArgumentException("Bloom Filter Type can only be det or ran.");
		}
	}

	/**
	 * Run the bloom join with given files names and bloom filter type.
	 * @param file1	file name for relation 1
	 * @param file2	file name for relation 2
	 * @param outputFile file name for output
	 * @param bloomFilterType	"det"-use bloom filter deterministic; "ran"-use bloom filter random
	 * @throws IllegalAccessException
	 */
	public static void runBloomJoin(String file1, String file2, String outputFile, String bloomFilterType)
			throws IllegalAccessException {
	
		BloomJoin exp = new BloomJoin(file1, file2, outputFile, bloomFilterType);
		int setSize = exp.countLines(file1);
		int bitsPerElement = 10;
		if (exp.filterType.equals("det")) {
			exp.bf = new BloomFilterDet(setSize, bitsPerElement);
		} else {
			exp.bf = new BloomFilterRan(setSize, bitsPerElement);
		}
		exp.buildFilter();
		exp.buildR3();
		exp.join();
	}

	/**
	 * Create a bloom filter for R1 on server 1.
	 * @throws IllegalAccessException
	 */
	private void buildFilter() throws IllegalAccessException {
		System.out.println();
		System.out.println("Creating a bloom filter for R1 on server 1...");
		Timer timer = new Timer();
		timer.start();
		try {
			File file = new File(inputFileName1);
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				numOfLines1++;
				String[] input = s.split(" ");
				String key = input[0];
				String value = input[1];
				bf.add(key);// Add to bloom filter, assume that the join
							// attribute is the first
							// column.
			}
			scanner.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		timer.stop();
		System.out.println("Used " + timer.getTime() + " ms to build the Bloom Filter.");
		System.out.println("There are "+numOfLines1+" records in R1.");
		System.out.println();
	}

	/**
	 * Create the relation 3 on server 2 based on the relation 2 and bloom filter received from server 1.
	 * @throws IllegalAccessException
	 */
	private void buildR3() throws IllegalAccessException {
		System.out.println("Creating R3 on server 2 based on the relation 2 and bloom filter received from server 1...");
		Timer timer = new Timer();
		timer.start();
		File r2File = new File(inputFileName2);
		File outputFile = new File(TEMPFILE);
		try {
			Scanner scanner = new Scanner(r2File);
			PrintWriter writter = new PrintWriter(outputFile);
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				numOfLines2++;
				String[] input = s.split("\\s+"); // The \\s is equivalent to [
													// \\t\\n\\x0B\\f\\r]
				String key = input[0];
				String value = input[1];
				if (bf.appears(key)) {
					String output = key + "   " + value + "\n";
					writter.append(output);
					numOfLines3++;
				}
			}
			writter.close();
			scanner.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		timer.stop();
		System.out.println("Used " + timer.getTime() + " ms to build the R3 on server 2.");
		System.out.println("There are "+numOfLines2+" records in R2.");
		System.out.println("A temp file with "+numOfLines3+" records was sent to server 1 from server 2.");
		System.out.println();
	}

	/**
	 * Perform the join on server 1 based on relation 1 and the relation 3 received from server 2.
	 * @throws IllegalAccessException
	 */
	private void join() throws IllegalAccessException {
		System.out.println("Performing the join on server 1...");
		Timer timer = new Timer();
		timer.start();
		File r3 = new File(TEMPFILE);
		try {
			Scanner scanner = new Scanner(r3);
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String[] input = s.split("\\s+"); // The \\s is equivalent to [
													// \\t\\n\\x0B\\f\\r]
				String key = input[0];
				String value = input[1];
				ArrayList<Pair> tmpList = new ArrayList<Pair>();
				if (r3map.containsKey(key)) {
					tmpList = r3map.get(key);
					tmpList.add(new Pair(key, value));
				} else {
					tmpList.add(new Pair(key, value));
				}
				r3map.put(key, tmpList);
			}
			scanner.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		File r1 = new File(inputFileName1);
		File outputFile = new File(outputFileName);
		try {
			Scanner scanner = new Scanner(r1);
			PrintWriter writter = new PrintWriter(outputFile);
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String[] list = s.split("\\s+");
				String key = list[0];
				String value = list[1];
				if (r3map.containsKey(key)) {
					for (Pair p : r3map.get(key)) {
						String output = key + "   " + value + "   " + p.value + "\n";
						writter.append(output);
						numOfLines4++;
					}
				}
			}
			writter.close();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		timer.stop();
		System.out.println("Used " + timer.getTime() + " ms to join the R1 and R3 on server 1.");
		System.out.println("There are " + numOfLines4 + " records after join.");
	}

	/**
	 * Count the number of lines of a given file.
	 * @param fileName
	 * @return the number of lines of a given file
	 */
	private int countLines(String fileName) {
		int count = 0;
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				count++;
			}
			scanner.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return count;
	}

	/**
	 * Inner class, Timer.
	 * @author YAN
	 *
	 */
	private class Timer {
		private long startTime;
		private long endTime;
		private boolean ended = false;
		private long usedTime;

		public void start() {
			ended = false;
			startTime = System.currentTimeMillis();
		}

		public void stop() {
			ended = true;
			endTime = System.currentTimeMillis();
			usedTime = endTime - startTime;
		}

		public long getTime() throws IllegalAccessException {
			if (!ended) {
				throw new IllegalAccessException("Timer is still running.");
			}
			return usedTime;
		}
	}

	/**
	 * Inner class to store the key, value pair.
	 * @author YAN
	 *
	 */
	private class Pair implements Comparable<Pair> {
		public String key;
		public String value;

		public Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(Pair target) {
			return key.compareTo(target.key);
		}
	}
}
