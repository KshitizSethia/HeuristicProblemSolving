
public class NNestedForLoop {

    // Number of for loops.
    static int n;

    // Arrays of length 'n'.
    static int[] indices = {7, 6, 5, 4, 3, 2, 1}; // Keeps each index in for loops.
    static int[] ranges;  // Range of each index in for loops.

    public static void main(String[] args) {

        // Change the variables here.
        n = 6;
        ranges = new int[] {240, 240, 240, 240, 240, 240};


        //indices = new int[n];
        //indices = {1, 2, 3, 4, 5, 6, 7};

        // Start off at indices = [0,...,0]
        System.out.println( java.util.Arrays.toString(indices) );
        do {
            advanceIndices();
            System.out.println( java.util.Arrays.toString(indices) );
        }
        while( !allMaxed() );

    }

    // Advances 'indices' to the next in sequence.
    static void advanceIndices() {

        for(int i=n-1; i>=1; i--) {
            if(indices[i]+1 == ranges[i]) {
                indices[i] = 0;
            }
            else {
                indices[i] += 1;
                break;
            }
        }

    }

    // Tests if indices are in final position.
    static boolean allMaxed() {

        for(int i=n-1; i>=1; i--) {
            if(indices[i] != ranges[i]-1) {
                return false;
            }
        }
        return true;

    }

}