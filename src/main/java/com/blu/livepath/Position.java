package com.blu.livepath;

/*
    Position record to simply keep track of the position
 */
public record Position(double x, double y) {

    @Override
    public String toString() {
        return Double.toString(x) + "," + Double.toString(y);
    }
}
