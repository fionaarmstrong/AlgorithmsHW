public class MyPartition {


    // Read strings from standard input, sort them, and print.
    public static void main(String[] args) {
    	String[] a = {"A", "A", "A", "A", "A", "B", "B", "B", "B", "A", "A", "A"}; 
        int lo = 0;
        int hi = 11;
    	
    	int i = lo;
        int j = hi + 1;
        String v = a[lo];
        while (true) { 
            for (int ii = 0; ii < a.length; ii++) {
            	System.out.print(a[ii]+" ");
            }
            System.out.println();
            
            // find item on lo to swap
            while (a[++i].compareTo(v) < 0)
                if (i == hi) break;

            // find item on hi to swap
            while (v.compareTo(a[--j]) < 0)
                if (j == lo) break;      // redundant since a[lo] acts as sentinel

            // check if pointers cross
            if (i >= j) break;

            String swap = a[i];
            a[i] = a[j];
            a[j] = swap;
        }

        // put v = a[j] into position
        String swap = a[lo];
        a[lo] = a[j];
        a[j] = swap;




        // display results again using select
        
        for (int ii = 0; ii < a.length; ii++) {
        	System.out.print(a[ii]+" ");
        }
        System.out.println();
    }

}