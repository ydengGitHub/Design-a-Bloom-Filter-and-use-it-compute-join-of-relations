/**
 * Test the falsepositives and bloom join.
 * @author Yan Deng
 *
 */
public class RunTest {

	public static void main(String[] args) throws IllegalAccessException {

		/*
		 * Test the falsePostives using random sample set and test set with given setSize.
		 * FalsePositives.runFalsePositives(setSize, bitsPerElement,
		 * lengthOfString, testSize);
		 */
		FalsePositives.runFalsePositives(10000, 10, 10, 100000);
		
		/*
		 * Test the BloomJoin using given two files, output the results into file.
		 * BloomJoin.runBloomJoin(file1, file2, outputFile, bloomFilterType); 
		 */
		BloomJoin.runBloomJoin("Relation1.txt", "Relation2.txt", "Output.txt", "ran");
//		BloomJoin.runBloomJoin("Relation1.txt", "Relation2.txt", "Output.txt", "det");
	}
}
