
public class Subset {
public static void main(String[] args) {
int numOfSubsets = 0;
try {
numOfSubsets = Integer.parseInt(args[0]);
} catch (java.lang.NumberFormatException ex) {
System.out.println("Error Message");
}
RandomizedQueue<String> queue = new RandomizedQueue<String>();

String[] subString = StdIn.readLine().split(" ");
for (int i = 0; i < subString.length; i++)
queue.enqueue(subString[i]);

java.util.Iterator<String> it = queue.iterator();
for (int i = 0; i < numOfSubsets; i++) {
if (it.hasNext()) {
System.out.println(it.next());
}
}

}
}