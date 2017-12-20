package cs445.a3;

import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class SemiMagic {
    public static int currentRow = 0;
    public static int currentCol = 0;
    public static boolean [][] available;

    public static boolean isFullSolution(int[][] square) {
        int sqrLength= square.length;
        for(int y = 0; y<sqrLength;y++)
        {   
            for(int x = 0; x<sqrLength;x++)
            {
                if(square[y][x]==0)
                {
                    return false;
                }
            }

        }

        if(reject(square))
        {
            return false;
        }
        return true;    
    
    }

    public static boolean reject(int[][] square) {
        int length = square.length;
        int value = length*(length*length+1)/2;
        boolean containsZero = false;

        for(int rows = 0; rows<square.length; rows++)
        {
            containsZero = false;
            int rowSum = 0;

            for(int cols = 0; cols<square.length; cols++)
            {
                if(square[rows][cols]==0) 
                {
                    containsZero=true;
                }
                rowSum = rowSum + square[rows][cols];
            }

            if(!containsZero&&rowSum!=value) 
            {
                return true;
            }

            rowSum = 0;
            containsZero = false;

        }

        for(int cols = 0; cols<square.length; cols++)
        {
            int colSum = 0;
            containsZero = false;
            for(int rows = 0; rows<square.length;rows++)
            {
                if(square[rows][cols]==0) 
                {
                    containsZero = true;
                }
                colSum = colSum + square[rows][cols];

            }

            if(!containsZero&&colSum!=value) 
            {
                return true;
            }

            colSum = 0;
            containsZero = false;
        }


        return false;
    }

    public static int[][] extend(int[][] square) {
        // moves to next row and starts again 
        if(currentCol==square.length)
        {
            currentCol = 0;
            currentRow++;
        }

        for(int rows=0;rows<square.length;rows++){
            for(int cols=0;cols<square.length;cols++){
                if((square[rows][cols]== 0) && (available[rows][cols]==true))
                {
                    currentCol = cols;
                    currentRow = rows;
                    rows = square.length; 
                    break;
                }
            }

         }
        // sets curent column and row to the index that should be filled in bc it contains 0
      
         
         boolean found = false;
         int index = 1;
         int max = square.length * square.length;
         int value = 0;
         while(!found && index<=max)
         {

            if(!duplicate(square,index))
               { value = index;
                found = true;}
                index++;
         }

         if(found==false) 
        {
            return null;
        }
            

         int [][] temp = new int [square.length][square.length];

         for(int rows = 0; rows<square.length; rows++)
         {
            for(int cols=0; cols<square.length;cols++)
            {
                temp[rows][cols] = square[rows][cols];

            }
         }
         // sets curent column and row to the index that should be filled in bc it contains 0
      
        
         temp[currentRow][currentCol] = value;
         currentCol++;
         return temp;


     }

    public static int[][] next(int[][] square) {
        // moves index to end of prior row
        /*
        if(currentCol==0 && currentRow>0)
        {
            currentCol = square.length-1;
            currentRow--;
        }
        //moves index back a spot
        else
        {
            currentCol--;
        }
        */
        while (currentRow>= 0)
        {
            currentCol--;
            if(currentCol<0&&currentRow>0)
            {
                currentCol = square.length-1;
                currentRow--;
                // moves index to end of prior row
            }

            if (available[currentRow][currentCol]==true)
            {
                break;
            }
        }

        int [][] temp = new int[square.length][square.length];

        for(int rows=0;rows<square.length;rows++)
            for(int cols=0;cols<square.length;cols++)
                temp[rows][cols] = square[rows][cols];
            //copy array

        int value = square[currentRow][currentCol];
        
        while(value<=(square.length*square.length))
        {

            if(!duplicate(square,value))
            {

                    temp[currentRow][currentCol] = value;
                    if(!reject(temp))
                        return temp;
            }

         value++;
        }

        return null;
    }

    static void testIsFullSolution() {
        

        int [][] full1= {{8,1,6},{3,5,7},{4,9,2}};
        System.out.println("Test isFullSolution: solution is full and correct, expecting true: " + isFullSolution(full1));
        printSquare(full1);
        System.out.println("");

        int [][] full2= {{1,14,8,11},
                        {15,4,10,5},
                        {12,7,13,2},
                        {6,9,3,16}};

        System.out.println("Test isFullSolution: solution is full and correct, expecting true: " + isFullSolution(full2));
        printSquare(full2);
        System.out.println("");

        int [][] notFull1= {{10,1,6},
                            {3,5,7},
                            {4,9,2}};
        System.out.println("Test isFullSolution: not full becasue number <9, expecting false: " + isFullSolution(notFull1));

        printSquare(notFull1);
        System.out.println("");

        int [][] notFull2= {{7,1,6},
                            {3,5,7},
                            {4,9,2}};
        System.out.println("Test isFullSolution: solution has duplicates, not full solution, expecting false: " + isFullSolution(notFull2));
        printSquare(notFull2);
        System.out.println("");

        int [][] notFull3= {{8,1,7},
                            {3,5,6},
                            {4,9,2}};
        System.out.println("Test isFullSolution:top row doesnt add up, not full solution, expecting false: " + isFullSolution(notFull3));
        printSquare(notFull3);
        System.out.println("");

        
        int [][] notFull4={{0,1,6},
                           {0,0,0},
                           {0,0,0}};
        System.out.println("Test isFullSolution:not full, contains 0s, expecting false: " + isFullSolution(notFull4));
        printSquare(notFull4);
        System.out.println("");

        
        int [][] notFull7= {{2,2,0},
                            {0,0,0},
                            {0,0,0}};
        System.out.println("Test isFullSolution:not full, has duplicates and 0s, expecting false: " + isFullSolution(notFull7));
        printSquare(notFull7); 

    }

    static void testReject() {
        
        int [][] dontReject1= {{8,1,6},{3,5,7},{4,9,2}};
        System.out.println("Test reject: full accurate3x3, should return false: " + reject(dontReject1));
        printSquare(dontReject1); 
        System.out.println("");

        int [][] dontReject2= {{1,14,8,11},
                                {15,4,10,5},
                                {12,7,13,2},
                                {6,9,3,16}};
        System.out.println("Test reject:full accurate4x4, should return false: " + reject(dontReject2));
        printSquare(dontReject2); 
        System.out.println("");

        int [][] dontReject3= {{8,0,6},{3,0,7},{0,9,2}};
        System.out.println("Test reject: partial accurate3x3, should return false: " + reject(dontReject1));
        printSquare(dontReject3); 
        System.out.println("");

        int [][] reject1= {{10,1,6},
                            {3,5,7},
                            {4,9,2}};
        System.out.println("Test reject: reject solution, has <9 number, should return true: " + reject(reject1));
        printSquare(reject1); 
        System.out.println("");

        int [][] reject2= {{7,1,6},
                            {3,5,7},
                            {4,9,2}};
        System.out.println("Test reject:has duplicates, should return true: " + reject(reject2));
        printSquare(reject2); 
        System.out.println("");

        int [][] reject3= {{8,1,7},
                            {3,5,6},
                            {4,9,2}};
        System.out.println("Test reject: row doesnt add up, should return true: " + reject(reject3));
        printSquare(reject3); 
        System.out.println("");
        
        int [][] reject4={{8,1,6},
                           {5,0,0},
                           {4,0,0}};
        System.out.println("Test reject: row adds up, but column doesn't, should reuturn true: " + reject(reject4));
        printSquare(reject4); 
        System.out.println("");


        int [][] reject8= {{2,0,0},
                            {0,5,0},
                            {0,0,8}};
        System.out.println("Test reject: test example, should return false: " + reject(reject8));
        printSquare(reject8); 
        System.out.println("");

    }
    
    static void testExtend() {
        available = new boolean [3][3];
        for ( int i = 0; i<available.length; i++)
        {
            for( int j = 0; j < available.length; j++)
            {
                available [i][j] = true;
            }
        }
        System.out.println("");

        System.out.println("Test Extend:");
        int [][] test1 = {{1,2,0},{0,0,0},{0,0,0}};
        printSquare(test1);
        int [][] extendedTest1 = extend(test1);

        if( extendedTest1 ==null)
        {
            System.out.println("Test extend: failed, shouldnt ");
        }
        if (extendedTest1 != null)
        {
            System.out.println("Test extend: passed, added number 3");
            printSquare(extendedTest1);

        }
        System.out.println("");
        System.out.println("Test Extend:");
        int [][] test2= {{8,6,1},{0,0,0},{0,0,0}};
        printSquare(test2);
        int [][] extendedTest2 = extend(test2);

        if( extendedTest2 ==null)
        {
            System.out.println("Test extend: failed, shouldnt");
        }
        if (extendedTest2 != null)
        {
            System.out.println("Test extend: passed, added to new row");
            printSquare(extendedTest2);

        }
        System.out.println("");
        System.out.println("Test Extend:");
        int [][] test3= {{8,6,1},{3,5,7},{4,9,2}};
        printSquare(test3);
        int [][] extendedTest3 = extend(test3);

        if( extendedTest3 ==null)
        {
            System.out.println("Test extend: failed, solution full");
        }
        if (extendedTest3 != null)
        {
            System.out.println("Test extend: passed, shouldnt");
            printSquare(extendedTest3);

        }
        System.out.println("");
        System.out.println("Test Extend:");
        int [][] test4= {{8,6,1},{3,5,0},{0,0,0}};
        printSquare(test4);
        int [][] extendedTest4 = extend(test4);

        if( extendedTest4 ==null)
        {
            System.out.println("Test extend: failed, shouldnt");
        }
        if (extendedTest4 != null)
        {
            System.out.println("Test extend: passed, added to  array, should add a number not already used");
            printSquare(extendedTest4);

        }
                System.out.println("");

       
     }

    static void testNext() {
        currentRow = 1;
        currentCol = 0;
        System.out.println("Test Next:");
        int [][] test1 = {{1,2,3},{0,0,0},{0,0,0}};
        printSquare(test1);
        int [][] nextTest1 = next(test1);

        if( nextTest1 ==null)
        {
            System.out.println("Test next: failed, sum will never work- 1+2+'any number' wont = 15, expecting  failure");
        }
        if (nextTest1 != null)
        {
            System.out.println("Test next: passed, shouldnt");
            printSquare(nextTest1);
        }
        System.out.println("");
        System.out.println("Test Next:");

        currentRow = 1;
        currentCol = 0;

        int [][] test2= {{1,6,7},{0,0,0},{0,0,0}};
        printSquare(test2);
        int [][] nextTest2 = next(test2);

        if( nextTest2 ==null)
        {
            System.out.println("Test next: failed, shouldnt");
        }
        if (nextTest2 != null)
        {
            System.out.println("Test next: passed, changed 7 to next available number,8, expecting 7 to change to 8");
            printSquare(nextTest2);
        }
        
        System.out.println("");
        System.out.println("Test Next:");

        currentRow = 1;
        currentCol = 3;
        int [][] test5= {{8,6,1},
                        {3,5,2},
                        {0,0,0}};
        printSquare(test5);
        int [][] nextTest5 = next(test5);

        if( nextTest5 ==null)
        {
            System.out.println("Test next: failed, shouldnt");
        }
        if (nextTest5 != null)
        {
            System.out.println("Test next: passed, added next available number to problem, not a duplicate, changes 2 to 7, expecting");
            printSquare(nextTest5);

        }

        System.out.println("");
        System.out.println("Test Next:");

        currentRow = 2;
        currentCol = 1;
        int [][] test6= {{8,6,1},
                        {3,5,7},
                        {3,0,0}};
        printSquare(test6);
        int [][] nextTest6 = next(test6);

        if( nextTest6 ==null)
        {
            System.out.println("Test next: failed, shouldnt ");
        }
        if (nextTest6 != null)
        {
            System.out.println("Test next: passed, changes 3(duplicate), to a valid input, 4");
            printSquare(nextTest6);

        }
        System.out.println("");
        System.out.println("Test Next:");

        currentRow = 0;
        currentCol = 3;
        int [][] test7= {{1,5,8},
                        {0,0,0},
                        {0,0,0}};
        printSquare(test7);
        int [][] nextTest7 = next(test7);

        if( nextTest7 ==null)
        {
            System.out.println("Test next: failed, shouldnt ");
        }
        if (nextTest7 != null)
        {
            System.out.println("Test next: passed, changes 8 to 9, expecting 8 to change");
            printSquare(nextTest7);

        
        }


     }
        
    static boolean duplicate(int [][]square, int key)
    {

        for(int rows=0;rows<square.length;rows++)
            for(int cols=0;cols<square.length;cols++)
                if(square[rows][cols]==key) return true;

        return false; 

    }
    /**
     * Returns a string representation of a number, padded with enough zeros to
     * align properly for the current size square.
     * @param num the number to pad
     * @param size the size of the square that we are padding to
     * @return the padded string of num
     */
    static String pad(int num, int size) {
        // Determine the max value for a square of this size
        int max = size * size;
        // Determine the length of the max value
        int width = Integer.toString(max).length();
        // Convert num to string
        String result = Integer.toString(num);
        // Pad string with 0s to the desired length
        while (result.length() < width) {
            result = " " + result;
        }
        return result;
    }

    /**
     * Prints a square
     * @param square the square to print
     */
    public static void printSquare(int[][] square) {
        if (square == null) {
            System.out.println("Null (no solution)");
            return;
        }
        int size = square.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(pad(square[i][j], size) + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Reads a square of a specified size from a plaintext file
     * @param filename the name of the file to read from
     * @param size the size of the square in the file
     * @return the square
     * @throws FileNotFoundException if the named file is not found
     */
    public static int[][] readSquare(String filename, int size)
                throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        int[][] square = new int[size][size];
        int val = 1;
        available = new boolean [square.length][square.length];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                square[i][j] = scanner.nextInt();
                if ( square[i][j]== 0)
                {
                    available [i][j]= true;
                }
                else
                {
                    available[i][j]= false;
                }
                
            } 
        }
        return square;
    }

    /**
     * Solves the magic square
     * @param square the partial solution
     * @return the solution, or null if none
     */
    public static int[][] solve(int[][] square) {
        if (reject(square)) return null;
        if (isFullSolution(square)) return square;
        int[][] attempt = extend(square);
        while (attempt != null) {
            int[][] solution;
            solution = solve(attempt);
            if (solution != null) return solution;
            attempt = next(attempt);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("-t")) {
            System.out.println("Running tests...");
            testIsFullSolution();
            testReject();
            testExtend();
            testNext();
        } else if (args.length >= 1) {
            try {
                // First get the specified size
                int size = Integer.parseInt(args[0]);

                int[][] square;
                if (args.length >= 2) {
                    // Read the square from the file
                    square = readSquare(args[1], size);
                } else {
                    // Initialize to a blank square
                    square = new int[size][size];
                    available = new boolean [size][size];
                    for ( int i = 0; i <size; i++)
                    {
                        for ( int j = 0; j < size; j++ )
                        {
                            available[i][j] = true;
                        }
                    }
                }

                System.out.println("Initial square:");
                printSquare(square);

                System.out.println("\nSolution:");
                int[][] result = solve(square);
                printSquare(result);
            } catch (NumberFormatException e) {
                // This happens if the first argument isn't an int
                System.err.println("First argument must be the size");
            } catch (FileNotFoundException e) {
                // This happens if the second argument isn't an existing file
                System.err.println("File " + args[1] + " not found");
            }
        } else {
            System.err.println("See usage in assignment description");
        }
    }
}

