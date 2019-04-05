/*
 * File Name: RefString.java
 * Author: Thomas Helfrich
 * Date: March 10, 2019
 * Purpose: Create and define class used to denote reference string used
 * in Simulator.java
 */

import java.util.ArrayList;

public class RefString {
    ArrayList<Integer> refString; // our actual string of numbers

    public RefString() {
        refString = new ArrayList<Integer>();
    }

    public RefString(ArrayList<Integer> rs) {
        refString = rs;
    }

    int getLength() {
        return refString.size();
    }
    int getAtIndex(int i) {
        return refString.get(i);
    }
    void print() {
        int i;
        for (i = 0; i < refString.size() - 1; i++) {
            System.out.print(refString.get(i) + ", ");
        }
        System.out.print(refString.get(i));
    }
}
