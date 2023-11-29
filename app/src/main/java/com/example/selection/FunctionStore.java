package com.example.selection;

import java.io.Serializable;

public class FunctionStore implements Serializable {
    private String name;
//        private double distance;

    public FunctionStore(String name) {
        this.name = name;
//            this.distance = distance;
    }

    public String getName() {
        return name;
    }
//        public double getDistance() {
//            return distance;
//        }
}
