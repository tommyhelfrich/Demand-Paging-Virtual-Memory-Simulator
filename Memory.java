/*
 * File Name: Memory.java
 * Author: Thomas Helfrich
 * Date: March 10, 2019
 * Purpose: Create and define class that represents physical
 * and virtual memory used in Simulator project. Contains logic
 * for all four algorithms.
 */

import java.util.Scanner;

public class Memory {
    RefString rs;
    int[] removed;
    int[] pageCalled;
    boolean[] pageFault;
    int rsLen;
    int numOfPhysicalFrames;
    int numOfVirtualFrames;
    int[][] physicalMemory;

    Frame[] frameArray;
    String algoType;

    Memory(RefString refs, int phys, int virt) {
        rs = refs;
        rsLen = rs.getLength();
        removed = new int[rsLen];
        pageCalled = new int[rsLen];
        numOfPhysicalFrames = phys;
        numOfVirtualFrames = virt;
        physicalMemory = new int[rs.getLength()][phys];
        frameArray = new Frame[virt];
        pageFault = new boolean[rsLen];
    }

    void generate(String alg) {
        initialize();
        algoType = alg;
        int slice = 0;
        int frameToInsert;
        int empty;
        int frameToReplace;
        int[] listOfFrames;
        int inMemory;
        // the while loops through each call of the simulation
        while (slice < rsLen) {
            frameToInsert = rs.getAtIndex(slice);
            if (alg == "LRU") {
                frameArray[frameToInsert].setLastUse(slice);
            } else if (alg == "LFU") {
                frameArray[frameToInsert].incrementTimesUsed();
            }
            empty = findIndex(physicalMemory[slice], -1);
            inMemory = findIndex(physicalMemory[slice], frameToInsert);
            if (inMemory != -1) {
                pageCalled[slice] = inMemory;
                pageFault[slice] = false;
            }

            else if (empty >= 0) {
                pageCalled[slice] = empty;
                physicalMemory[slice][empty] = frameToInsert;
                frameArray[frameToInsert].setInserted(slice);
            }
            else {

                switch (alg) {
                    case "FIFO":
                        // find the oldest frame
                        frameToReplace = findOldest(physicalMemory[slice]);
                        frameArray[frameToInsert].setInserted(slice);
                        break;
                    case "OPT":
                        calculateNextUses(slice);
                        // find the least optimal page
                        frameToReplace = findLeastOptimal(physicalMemory[slice]);
                        break;
                    case "LFU":
                        // find least frequently used
                        frameToReplace = findLfu(physicalMemory[slice]);
                        break;
                    case "LRU":
                        // find least recently used
                        frameToReplace = findLru(physicalMemory[slice]);
                        break;
                    default:
                        System.out.println("Error: algorithm not recognized!");
                        return;
                }
                removed[slice] = physicalMemory[slice][frameToReplace];
                pageCalled[slice] = frameToReplace;
                physicalMemory[slice][frameToReplace] = frameToInsert;


            }
            if ((slice + 1) < rsLen) {
                for (int i = 0; i < numOfPhysicalFrames; i ++) {
                    physicalMemory[slice +1][i] = physicalMemory[slice][i];
                }
            }
            slice += 1;
        }
    }

    int findOldest(int[] a) {
        int oldest = frameArray[a[0]].getInserted();
        int oldestIndex = 0;
        int checking;
        for (int i = 1; i < a.length; i++) {
            checking = frameArray[a[i]].getInserted();
            if (checking < oldest) {
                oldest = checking;
                oldestIndex = i;
            }
        }
        return oldestIndex;
    }

    int findLfu(int[] a) {
        int lfuIndex = 0;
        int lfuTimesUsed = frameArray[a[lfuIndex]].getTimesUsed();

        for (int i = 1; i < a.length; i++) {
            int temp = a[i];
            int tempTimesUsed = frameArray[a[i]].getTimesUsed();

            if (tempTimesUsed < lfuTimesUsed) {
                lfuIndex = i;
                lfuTimesUsed = tempTimesUsed;
            }
        }

        return lfuIndex;
    }

    int findLru(int[] a) {
        int lruIndex = 0;
        int lruLastUse = frameArray[a[lruIndex]].getLastUse();

        for (int i = 1; i < a.length; i++) {
            int temp = a[i];
            int tempLastUse = frameArray[a[i]].getLastUse();

            if (tempLastUse < lruLastUse) {
                lruIndex = i;
                lruLastUse = tempLastUse;
            }
        }
        return lruIndex;
    }

    int findLeastOptimal(int[] a) {
        int leastOptimal = a[0];
        int leastOptimalIndex = 0;
        int leastOptNextUse = frameArray[leastOptimal].getNextUse();
        for (int i = 1; i < a.length; i++) {
            int temp = a[i];
            int tempNextUse = frameArray[temp].getNextUse();
            if (tempNextUse > leastOptNextUse) {
                leastOptimal = temp;
                leastOptNextUse = frameArray[leastOptimal].getNextUse();
                leastOptimalIndex = i;
            }
        }
        return leastOptimalIndex;
    }
    void calculateNextUses(int n) {
        for (int i = 0; i < numOfVirtualFrames; i++) {
            frameArray[i].setNextUse(rsLen + 1);
        }
        for (int i = rsLen - 1; i >= n; i--) {
            int called = rs.getAtIndex(i);
            frameArray[called].setNextUse(i);
        }
    }

    void initialize() {
        for (int i = 0; i < pageFault.length; i++) {
            pageFault[i] = true;
        }
        for (int i = 0; i < removed.length; i++) {
            removed[i] = -1;
        }
        for (int i = 0; i < pageCalled.length; i++) {
            pageCalled[i] = -1;
        }
        for (int i = 0; i < numOfVirtualFrames; i++) {
            frameArray[i] = new Frame(i);
        }
        for (int i = 0; i < rsLen; i++) {
            for (int j = 0; j < numOfPhysicalFrames; j ++) {
                physicalMemory[i][j] = -1;
            }
        }
        algoType = "";
    }

    void print() {
        System.out.println("Basic information: ");
        System.out.println("Algorithm type: " + algoType);
        System.out.println("Length of reference string: " + rsLen);
        System.out.println("Number of virtual pages: " + numOfVirtualFrames);
        System.out.println("Number of physical pages: " + numOfPhysicalFrames);
        System.out.println("---");
        System.out.println("[brackets] around a page number indicate it was called.");
        System.out.println("Press enter to step through snapshots of physical memory after each string call.");
        System.out.println("Or, enter \"q\" at any time to return to main menu.");

        Scanner sc = new Scanner(System.in);
        int steppingSlice = 0;
        String prompt;
        int frameNum;
        int removedInt;
        while (steppingSlice < rsLen) {
            prompt = sc.nextLine();
            if (prompt.equals("q")) {
                System.out.println("Quitting printout.");
                break;
            }
            System.out.println("Snapshot at call " + (steppingSlice + 1) + ":");
            System.out.println("Program called virtual frame # " + rs.getAtIndex(steppingSlice));
            for (int i = 0; i < numOfPhysicalFrames; i ++) {
                System.out.print("Physical frame " + i + ":");
                frameNum = physicalMemory[steppingSlice][i];
                if (frameNum >= 0) {
                    if (i == pageCalled[steppingSlice]) {
                        System.out.println("[" + frameNum + "]");
                    } else {
                        System.out.println(" " + frameNum);
                    }
                } else {
                    System.out.println("x");
                }
            }
            removedInt = removed[steppingSlice];
            System.out.println("Page fault: " + (pageFault[steppingSlice] ? "Yes." : "No."));
            System.out.println("Victim frame: " + (removedInt == -1 ? "None." : removedInt));
            steppingSlice += 1;
        }
        System.out.print("Simulation complete. Press enter to continue.");
        sc.nextLine();
    }

    int findIndex(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == n) {
                return i;
            }
        }
        return -1;
    }
}
