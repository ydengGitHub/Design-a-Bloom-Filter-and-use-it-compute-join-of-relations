import java.math.BigInteger;
/**
 * Interface for BloomFilterDet and BloomFilterRan.
 * @author Yan Deng
 *
 */
public interface BloomFilter {
	public void add(String s);
	public boolean appears(String s);
	public int filterSize();
	public int dataSize();
	public int numHashes();
	public BigInteger computeHashValue(String s, int fnv);
	public int[] computeHashValues(String s);
}
