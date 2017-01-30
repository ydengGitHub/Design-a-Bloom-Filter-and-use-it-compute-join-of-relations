# Design-a-Bloom-Filter-and-use-it-compute-join-of-relations
Design and implement Bloom Filter with two types of hash functions-- deterministic hash functions and random hash functions.  Use them to do some primitive document search and join operation on relations.

Classes

1.	BloomFilter
This is an interface for BloomFilterDet and BloomFilterRan classes. It defines the methods that BloomFilterDet and BloomFilterRan class need to implement.

2.	BloomFilterDet
This class implements the BloomFilter interface and uses the deterministic hash function FNV (both of FNV32 and FNV64 are used).

2.1.	BloomFilterDet(int setSize, int bitsPerElement)
Constructor: initialize a Bloom filter that can store a set S of cardinality setSize. The size of the filter is the first prime number which is bigger than (setSize * bitsPerElement). The number of hash functions should be the optimal choice which is ln2×filterSize/setSize.

2.2.	add(String s)
Adds the string s to the filter. This method is case-insensitive. 
•	Convert the input string to lowercase;
•	Compute k hash values by calling computeHashValues() method;
•	And set the corresponding bits of the BitSet to 1;

2.3.	appears(String s)
Returns true if s appears in the filter; otherwise returns false. This method is also case-insensitive.
•	Convert the input string to lowercase;
•	Compute the hash values;
•	Check the corresponding bits of the BitSet;
•	If all bits are 1, return true; else return false;

2.4.	filterSize()
Returns the size of the filter(table).

2.5.	dataSize()
Returns the number of elements added to the filter.

2.6.	numHashes()
Returns the number of hash function used.

2.7.	computeHashValue(String s, int fnv)
Compute the hash value of given string using FNV method (fnv=32, using FNV32; fnv=64, using FNV64), and return the hash value in BigInteger type.
•	Set h to FNV32INIT or FNV64INIT according to the fnv argument;
•	For i in range[0,…,s.length()-1] Do
o	h=h XOR s[i];
o	h=(h*FNV32PRIME)%(2^32) or h=(h*FNV64PRIME)%(2^64)
•	return h in BigInteger type;

2.8.	computeHashValues(String s)
Compute K different hash values for given string.
•	For i in range[0,…,k-1] Do
o	h=(computeHashValue(s, 64)+computeHashValue(s,32)*i)%bitSetSize;
o	store h to array[i];
•	return the array of k hash values;

2.9.	isPrime(int num)
Check if the given integer is a prime or not, and returns true if it is a prime, false else.
•	If num=2, return true;
•	If num%2=0, return false;
•	For (i=3; i*i<=num, i+=2) Do
o	If(num%i=0) return false;
•	Return true;

2.10.	nextPrime(int num)
Find the first prime bigger than the given integer and return the value.
•	nextPrime=num;
•	if nextPrime%2=0, nextPrime=nextPrim+1;
•	while (isPrime(nextPrime)=false) nextPrime=nextPrime+2;
•	return nextPrime;

3.	BloomFilterRan
This class implements the BloomFilter interface and uses the random hash function.

3.1.	BloomFilterRan(int setSize, int bitsPerElement)
Constructor: initialize a Bloom filter that can store a set S of cardinality setSize. The size of the filter is the first prime number which  is bigger than (setSize * bitsPerElement). The number of hash functions should be the optimal choice which is ln2×filterSize/setSize. 

3.2.	add(String s)
Adds the string s to the filter. This method is case-insensitive. 
•	Convert the input string to lowercase;
•	Compute k hash values by calling computeHashValues() method;
•	And set the corresponding bits of the BitSet to 1;

3.3.	appears(String s)
Returns true if s appears in the filter; otherwise returns false. This method is also case-insensitive.
•	Convert the input string to lowercase;
•	Compute the hash values;
•	Check the corresponding bits of the BitSet;
•	If all bits are 1, return true; else return false;

3.4.	filterSize()
Returns the size of the filter(table).

3.5.	dataSize()
Returns the number of elements added to the filter.

3.6.	numHashes()
Returns the number of hash function used.

3.7.	computeHashValue(String s, int j)
Compute the hash value of given string based on its order (j) in the k hash values array, and return the hash value in BigInteger type. A same string will get different hash value if the input j is different （aj, bj are pre-generated and stores in an int[k][2] array）.
•	Set h to 0;
•	For i in range[0,…,s.length()-1] Do
o	h=h*aj+bj+s.charAt(i);
o	h=h%(2^32);
•	return h in BigInteger type;

3.8.	computeHashValues(String s)
Compute K different hash values for given string. A same string will get k different hash values.
•	For i in range[0,…,k-1] Do
o	h=computeHashValue(s,i); //Calling computehashValue() method
o	store h to array[i];
•	return the array of k hash values;

3.9.	generateRan()
Generate a random integer between[1,…,p-1] for the random functions’ a and b.

3.10.	isPrime(int num)
Check if the given integer is a prime or not, and returns true if it is a prime, false else.
•	If num=2, return true;
•	If num%2=0, return false;
•	For (i=3; i*i<=num, i+=2) Do
o	If(num%i=0) return false;
•	Return true;

3.11.	nextPrime(int num)
Find the first prime bigger than the given integer and return the value.
•	nextPrime=num;
•	if nextPrime%2=0, nextPrime=nextPrim+1;
•	while (isPrime(nextPrime)=false) nextPrime=nextPrime+2;
•	return nextPrime; 

4.	FalsePositives
This clasee is used to create the random sample set and test set, and to calculate the false positive rate of two bloom filter methods.
4.1.	FalsePositives(int setSize, int bitsPerElement)
Constructor: Initialize a FalsePositives instance, define the set Size and bits Per Element. Initialize a BloomFilterDet and a BloomFilterRan instance. Create a JAVA HashSet instance for comparison (assume it will not have false positive).

4.2.	runFalsePositives(int setSize, int bitsPerElement, int lengthOfString, int testSize)
This is a public static method, which can be called directly out of the class. Create a FalsePositives instance with given setSize and bitsPerElement. Run the falsePositive() method with given lengOfString and testSize to test the false positive values of two bloom filter methods. And show the results by calling showResult() method.

4.3.	falsePositive(int lengthOfString, int testSize)
Create a random sample set with given length of string, build the BloomFilterDet and BloomFilterRan tables. And create a random test set, count the false positives of the two methods compared with the JAVA HashSet. Return the false positive rate of the BloomFilterDet and BloomFilterRan.
•	Create a random sample set, build the BloomFilterDet, BloomFilterRan tables and a Java HashSet for comparision by calling createTables() method;
•	While (count of Test strings<=test size) Do
o	Create a random string, count of Test strings ++;
o	If the string is not in the Java HashSet
♣	But is in the BloomFilterDet, count of Det’s false positive ++;
♣	But is in the BloomFilterRan, count of Ran’s false positive ++;
•	Calculate the false positive rate of BloomFilterDet and BloomFilterRan=countOfFp/countOfTest;
•	Return an array stored the two values;

4.4.	createTables(int lengthOfString)
Create a random sample set with given length of string, build the BloomFilterDet and BloomFilterRan tables.
•	For i in range[0,…,setSize-1], Do
o	Randomly pick a String with length=lengthOfString by calling randomString();
o	Add the string to bloomFilterDet;
o	Add the string to bloomFilterRan;
o	Add the string to hashSet; //Java HashSet

4.5.	randomString(int lengthOfString)
Generate a random string with given length of String using the characters in the defined alphabet.
•	StringBuilder sb;
•	For i in range[0,…,lengOfString], Do
o	Randomly pick an character from the defined alphabet(A-Z,a-z) and append to sb;
•	Return sb.toString();

4.6.	getSize()
Return the set size.

4.7.	showResult(int bitsPerElement)
Show the test result.
•	Output the Theorectical false positive probability: =0.618bitsPerElement;
•	Output the test BloomFilter deterministic’s false positive rate;
•	Output the test BloomFilter random’s false positive rate;

5.	BloomJoin
This class is used to Computes the join of the two 2-ary relations using Bloom Filter.
 Firstly, build the bloom filter for the relation 1 on server 1 and Send the bloom filter to Server 2.
 Secondly, create the relation 3 (r3) on server 2 based on relation 2 and the received bloom filter, and send the r3 back to server 1.
 Finally, join the relation 1 and the relation 3 received from server 2 on server 1.

5.1.	BloomJoin(String file1, String file2, String outputFile, String bloomFilterType)
Constructor: initialize a new BloomJoin instance, define the file names of relation1, relation2 and output file, and define which bloom filter type will be used (det: deterministic; ran: random).

5.2.	runBloomJoin(String file1, String file2, String outputFile, String bloomFilterType)
A public static method, which can be called out of the class. Run the bloom join with given files names and bloom filter type. Output the result to file.
♣	Create a BloomJoin instance;
♣	Create a BloomFilter instance (depending on the type argument);
♣	Call buildFilter() method to build the bloom filter for R1 on server1;
♣	Call buildR3() method to build the R3 on server 2 based on R2 and the received bloom filter;
♣	Call join() method to join the R1 and received R3 on server 1;

5.3.	buildFilter()
Create a bloom filter for R1 on server 1.
♣	For each line of Relation 1, Do
o	Add the first element of R1 to the bloom filter;
♣	Output the used time;

5.4.	buildR3()
Create the relation 3 on server 2 based on the relation 2 and bloom filter received from server 1.
♣	For each line of Relation 2, Do
o	if the first element(key) appears in the received bloom filter, Do
♣	Add the line to R3 (A temp file); 
♣	Output the used time;

5.5.	join()
Perform the join on server 1 based on relation 1 and the relation 3 received from server 2.
♣	Build a HashMap<String, ArrayList<Pair>> to store the received R3;
o	For each line of the receivedR3, do
♣	Add the (first,second element) pair to the ArrayList corresponding to the HashMap’s key(using the first element);
♣	For each line of R1, do
o	If the R3-HashMap containts the first element of R1, do
♣	For each pair of the Arraylist corresponding to the HashMap’s key
♣	Add a line to output file, R1 e1 +  R1 e2 + R3 e2;
Output the used time;

5.6.	countLines(String fileName)
Use a scanner to Count the number of lines of a given file, and return the value.

5.7.	inner class Timer
A simple inner class to implement a timer’s functions: start(), stop(), getTime().

5.8.	inner class Pair
A simple inner class to store the key, value pair.

6.	RunTest
This class contains a main method and is used to run the FalsePositives and BloomJoin experiments.

6.1.	main()
♣	//Test the falsePostives using random sample set and test set with given setSize.
FalsePositives.runFalsePositives(setSize, bitsPerElement, lengthOfString, testSize);

♣	//Test the BloomJoin using given two files, output the results into file. bloomFilterType is det or ran
 BloomJoin.runBloomJoin(file1, file2, outputFile, bloomFilterType);


False Positives of BloomFilterDet and BloomFilterRan
bitsPerElement=4:
Theoretically, the false positive probability is 14.59%.
BloomFilter deterministic: 15.32%; BloomFilter random: 22.05%.
The bloom filter only used 1 random function.

bitsPerElement=8:
Theoretically, the false positive probability is 2.13%.
BloomFilter deterministic: 2.08%; BloomFilter random: 2.41%.
The bloom filter used 4 random functions.

bitsPerElement=10:
Theoretically, the false positive probability is 0.81%.
BloomFilter deterministic: 0.82%; BloomFilter random: 0.91%.
The bloom filter used 5 random functions.

Repeat 30 times, calculate the mean and p-values.
 BitsPer
Elements
n
Mean(%)
Variance
t
p-value
Deterministic
Random
Deterministic
Random

4
30
15.478
21.888
0.006536
0.160096
86.00812
<0.001
8
30
2.14
2.39
0.002
0.00365
8.265209
<0.001
10
30
0.835
0.9375
0.001025
0.000469
4.725854
<0.001

As we can see, for all three bitsPerElement settings, the Bloom Filter Deterministic has better false positive probability (which is very closed to the theoretical probability) compared to the Bloom Filter Random method (p-value < 0.001). It suggests that the k hash values generated by FNV deterministic hash functions might be more randomly than the ones generated by the random hash functions we used (randomly pick a, b).
BloomJoin Test by using BloomFilterRan
bitsPerElement=10
Creating a bloom filter for R1 on server 1...
Used 3335 ms to build the Bloom Filter.
There are 2000000 records in R1.

Creating R3 on server 2 based on the relation 2 and bloom filter received from server 1...
Used 4227 ms to build the R3 on server 2.
There are 2000000 records in R2.
A temp file with 207434 records was sent to server 1 from server 2.

Performing the join on server 1...
Used 3597 ms to join the R1 and R3 on server 1.
There are 190365 records after join.

If a naive strategy was followed, server 2 must send all 2000000 records to the server 1. So the communication cost is about 2,000,000*22*2 = 88,000,000 bytes;
By using the bloom filter strategy, server 1 send a bloom filter to server1 and server 2 send R3 back to server 1, the total communication cost is about (bloomFilterSize+R3size)=20,000,000/8+207434*22*2 = 11,627,096 bytes, which is just about 1/8 of the naive strategy.

BloomJoin Test by using BloomFilterDet
Create a new BloomFilterDet instance.

Creating a bloom filter for R1 on server 1...
Used 97542 ms to build the Bloom Filter.
There are 2000000 records in R1.

Creating R3 on server 2 based on the relation 2 and bloom filter received from server 1...
Used 100773 ms to build the R3 on server 2.
There are 2000000 records in R2.
A temp file with 205671 records was sent to server 1 from server 2.

Performing the join on server 1...
Used 3512 ms to join the R1 and R3 on server 1.
There are 190365 records after join.

By using the bloom filter strategy, server 1 sent a bloom filter to server1 and server 2 send R3 back to server 1, the total communication cost is about (bloomFilterSize+R3Size)=20000000/8+205671*22*2 =11,549,524 bytes, which is just about 1/10 of the naive strategy (a little less than the BloomFilterRan method because of the better false positive rates).
Comparison between BloomFilterDet and BloomFilterRan
♣	Regarding to the False positive rate (communication cost), BloomFilterDet performs better than the BloomFilterRan. BloomFilterDet sent 205671 records back to server 1 while BloomFilterRan sent 207434 records back to server 1. However, both of two BloomFilter give the exactly same result (190365 records in the output file). 

♣	Regarding to the processing time, BloomFilterRan performs much better than BloomFilterDet, 
			Ran	Det
Build bloom filter:	3335 vs 97542 ms
Build R3:		4227 vs 100773 ms

It’s probably caused by the much complicated computation in the fnv hash functions (FNVINIT and FNVPRIME are two large numbers). 
Extra Credit
http://archive.ics.uci.edu/ml/machine-learning-databases/00339/train.csv.zip	

This data set contains 1710671 entries. Each entry brings the information of a completed trip. It contains 9 attributes. They are unique trip ID, CALL_TYPE, ORIGIN_CALL, TAXI_ID, TIMESTAMP, DAYTYPE, IMPORTANT NOTICE, MISSING_DATA, and POLYLINE.
The purpose of our experiment is to find the drivers who drive though the same place at different trips. Therefore, in our experiment, we just need the following three attributes to reach our goal.
1.	Trip ID: identifier for each trip
2.	Taxi_ID: identifier for each driver
3.	POLYLINE: a list of coordinates mapped as a String (we plan to treated this variable as the joined attributes) 
Since the size of the data set is too large, we used the 010 editor to check the data set and split the data set into two parts (Train1.csv and Train2.csv).
In order to retrieve the three necessary variables from the above 9 attributes. We used PrintWriter in java to create the two new text files that only contain the three attributes we need.(train1.txt and train.txt). Finally, we ran distributed join on train1.txt and train2.txt with the random bloom filter. We got the following results.
Create a new BloomFilterRan Instance.
a= 4856271 b= 6413082
a= 3139081 b= 4970172
a= 61504 b= 7721231
a= 7477681 b= 3335260
a= 8536513 b= 1864719

Creating a bloom filter for R1 on server 1...
Used 2516 ms to build the Bloom Filter.
There are 855020 records in R1.

Creating R3 on server 2 based on the relation 2 and bloom filter received from server 1...
Used 14303 ms to build the R3 on server 2.
There are 848264 records in R2.
A temp file with 498659 records was sent to server 1 from server 2.

Performing the join on server 1...
Used 553671 ms to join the R1 and R3 on server 1.
There are 13637331 records after join.
