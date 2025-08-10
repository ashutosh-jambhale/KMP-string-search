The KMP algorithm improves upon naive string searching by using a preprocessed "skip table" to avoid unnecessary character comparisons.

How to Use It:
Compile the code: javac XSort.java
Run it: java KMPsearch "pattern" OR java KMPsearch "pattern" filename.txt

How It Works:
1)Preprocessing: Builds a skip table.
2)Search: Scans text left-to-right. Uses skip values to avoid re-checking characters.
3)File Search for each line.
