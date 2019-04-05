/*
 * File Name: Frame.java
 * Author: Thomas Helfrich
 * Date: March 10, 2019
 * Purpose: Create and define class to represent page frames
 * for paging algorithms
 */


public class Frame {
    int number;
    int inserted;
    int nextUse;
    int lastUse;
    int timesUsed;

    Frame(int n) {
        number = n;
        inserted = -1;
        nextUse = -1;
        lastUse = -1;
        timesUsed = 0;
    }
    // getters and setters
    void setNum(int n) {
        number = n;
    }
    int getNum() {
        return number;
    }
    void setInserted(int n) {
        inserted = n;
    }
    int getInserted() {
        return inserted;
    }
    void setNextUse(int n) {
        nextUse = n;
    }
    int getNextUse() {
        return nextUse;
    }
    void setLastUse(int n) {
        lastUse = n;
    }
    int getLastUse() {
        return lastUse;
    }
    void incrementTimesUsed() {
        timesUsed += 1;
    }
    int getTimesUsed() {
        return timesUsed;
    }
}
