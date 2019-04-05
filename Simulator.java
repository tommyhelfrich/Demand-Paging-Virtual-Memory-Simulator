/*
* File Name: Simulator.java
* Author: Thomas Helfrich
* Date: March 10, 2019
* Purpose: To provide the main method for the project that
* simulates the demand paging algorithms
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.InputMismatchException;

public class Simulator {
    static final int V_PG = 10; 
    static final int P_PG = 7;

    public static void main(String[] args) {
        // read in physical frame numbers
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of Physical Frames");
        int numOfPhysicalFrames = sc.nextInt();
        System.out.println("Number of page frames set to " + numOfPhysicalFrames + ".");


        Scanner in = new Scanner(System.in);
        String line;
        RefString rs = null;
        Memory sim;


        // begin main loop:
        while (true) {
            System.out.println();
            System.out.println("Please choose from the following options:");
            System.out.println("0 - Exit");
            System.out.println("1 - Read reference string");
            System.out.println("2 - Generate reference string");
            System.out.println("3 - Display current reference string");
            System.out.println("4 - Simulate FIFO");
            System.out.println("5 - Simulate OPT");
            System.out.println("6 - Simulate LRU");
            System.out.println("7 - Simulate LFU");
            System.out.println();

            // read input
            line = in.next();
            in.nextLine();
            switch (line) {
                case "0":
                    // exit
                    System.out.println("Ending Simulation.");
                    System.exit(0);
                    break;
                case "1":
                    // read reference string
                    rs = readRefString(in);
                    // confirm
                    stringConfirm(rs);
                    break;
                case "2":
                    // generate reference string
                    System.out.println("How long do you want the reference string to be?");
                    int stringSize = getStringSize(in);
                    // generate the string
                    rs = generateString(stringSize, V_PG);
                    stringConfirm(rs);
                    break;
                case "3":
                    // print reference string
                    if (rs != null) {
                        System.out.print("Current reference string: ");
                        rs.print();
                        System.out.print(".");
                    } else {
                        System.out.println("Error: no reference string entered.");
                    }
                    break;
                case "4":
                    // check that refString has been set:
                    // test rs:
                    if (rsIsSet(rs)) {
                        // create simulation conditions, run it, and print
                        sim = new Memory(rs, numOfPhysicalFrames, V_PG);
                        sim.generate("FIFO");
                        sim.print();
                    }
                    break;
                case "5":
                    // check that refString has been set:
                    if (rsIsSet(rs)) {
                        // create simulation conditions, run it, and print
                        sim = new Memory(rs, numOfPhysicalFrames, V_PG);
                        sim.generate("OPT");
                        sim.print();
                    }
                    break;
                case "6":
                    // check that refString has been set:
                    if (rsIsSet(rs)) {
                        // create simulation conditions, run it, and print
                        sim = new Memory(rs, numOfPhysicalFrames, V_PG);
                        sim.generate("LRU");
                        sim.print();
                    }
                    break;
                case "7":
                    // check that refString has been set:
                    if (rsIsSet(rs)) {
                        // create simulation conditions, run it, and print
                        sim = new Memory(rs, numOfPhysicalFrames, V_PG);
                        sim.generate("LFU");
                        sim.print();
                    }
                    break;
                default:
                    break;
            } // end switch
        } // end while (true)
    } // end main


    static RefString readRefString(Scanner in) {
        System.out.println("Enter a series of numbers: ");
        ArrayList<Integer> al = new ArrayList<Integer>();

        // create RefString
        RefString rs = null;

        do {
            // read in a line
            String line = in.nextLine();
            // create a scanner to operate on that line
            Scanner lineScanner = new Scanner(line);
            // extract the ints
            String temp;
            int tempInt = -1;
            boolean isInt;
            while (lineScanner.hasNext()) {
                temp = lineScanner.next();
                isInt = false;
                try {
                    tempInt = Integer.parseInt(temp);
                    isInt = true;
                } catch (NumberFormatException e) {
                    System.out.println("Warning: you entered a non-integer; \"" + temp + "\" ignored.");
                }
                // ensure that the numbers entered are between 0 and 9:
                if (isInt && (tempInt < 0 || tempInt >= V_PG)) {
                    System.out.println("Warning: numbers must be between 0 and " + (V_PG - 1) + "; \"" + temp + "\" ignored.");
                } else if (isInt) {
                    al.add(tempInt);
                }
            }
            // make sure at least 1 valid int entered:
            if (al.size() < 1) {
                System.out.println("Error: you must enter at least 1 valid integer between 0 and 9. Please try again.");
            }
        } while (al.size() < 1);
        rs = new RefString(al);
        return rs;
    }

    static int getStringSize(Scanner in) {
        int stringSize = 0;
        while (stringSize < 1) {
            try {
                stringSize = in.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("You must enter an integer.");
            }
            in.nextLine();
            if (stringSize < 1) {
                System.out.println("You must enter a positive integer.");
            }
        }
        return stringSize;
    }

    static RefString generateString(int n, int max) {
        if (n < 1) {
            System.out.println("Error: Reference string shorter than 1.");
            return null;
        }
        Random rand = new Random();

        // create ArrayList for ints
        ArrayList<Integer> ar = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            ar.add(rand.nextInt(max));
        }

        // use the ArrayList to create a RefString
        RefString rs = new RefString(ar);
        return rs;
    }

    static void stringConfirm(RefString rs) {
        if (rs != null) {
            System.out.print("Valid reference string saved: ");
            rs.print();
            System.out.print(".");
        } else {
            System.out.println("Invalid reference string. Please try again.");
        }
    }

    static boolean rsIsSet(RefString rs) {
        if (rs != null) {
            return true;
        }
        System.out.println("Error: Reference string not yet entered/generated!");
        return false;
    }
}
