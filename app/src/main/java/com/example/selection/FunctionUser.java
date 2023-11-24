package com.example.selection;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FunctionUser implements Serializable {
    private String uid;
    private String name;
    private boolean availableDiscountAmusement;
    private boolean availableDiscountBakery;
    private boolean availableDiscountBookStore;
    private boolean availableDiscountCafe;
    private boolean availableDiscountConvenientStore;
    private boolean availableDiscountFastFood;
    private boolean availableDiscountRestaurant;
    private boolean availableDiscountTheater;
    private ArrayList<Integer> likedKookmin;
    private ArrayList<Integer> likedShinhan;
    private ArrayList<Integer> savedKookmin;
    private ArrayList<Integer> savedShinhan;

    public FunctionUser(){}


    public FunctionUser(String uid, String name, boolean availableDiscountAmusement, boolean availableDiscountBakery, boolean availableDiscountBookStore, boolean availableDiscountCafe, boolean availableDiscountConvenientStore, boolean availableDiscountFastFood, boolean availableDiscountRestaurant, boolean availableDiscountTheater, ArrayList<Integer> likedKookmin, ArrayList<Integer> likedShinhan, ArrayList<Integer> savedKookmin, ArrayList<Integer> savedShinhan) {
        this.uid = uid;
        this.name = name;
        this.availableDiscountAmusement = availableDiscountAmusement;
        this.availableDiscountBakery = availableDiscountBakery;
        this.availableDiscountBookStore = availableDiscountBookStore;
        this.availableDiscountCafe = availableDiscountCafe;
        this.availableDiscountConvenientStore = availableDiscountConvenientStore;
        this.availableDiscountFastFood = availableDiscountFastFood;
        this.availableDiscountRestaurant = availableDiscountRestaurant;
        this.availableDiscountTheater = availableDiscountTheater;
        this.likedKookmin = likedKookmin;
        this.likedShinhan = likedShinhan;
        this.savedKookmin = savedKookmin;
        this.savedShinhan = savedShinhan;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailableDiscountAmusement() {
        return availableDiscountAmusement;
    }

    public void setAvailableDiscountAmusement(boolean availableDiscountAmusement) {
        this.availableDiscountAmusement = availableDiscountAmusement;
    }

    public boolean isAvailableDiscountBakery() {
        return availableDiscountBakery;
    }

    public void setAvailableDiscountBakery(boolean availableDiscountBakery) {
        this.availableDiscountBakery = availableDiscountBakery;
    }

    public boolean isAvailableDiscountBookStore() {
        return availableDiscountBookStore;
    }

    public void setAvailableDiscountBookStore(boolean availableDiscountBookStore) {
        this.availableDiscountBookStore = availableDiscountBookStore;
    }

    public boolean isAvailableDiscountCafe() {
        return availableDiscountCafe;
    }

    public void setAvailableDiscountCafe(boolean availableDiscountCafe) {
        this.availableDiscountCafe = availableDiscountCafe;
    }

    public boolean isAvailableDiscountConvenientStore() {
        return availableDiscountConvenientStore;
    }

    public void setAvailableDiscountConvenientStore(boolean availableDiscountConvenientStore) {
        this.availableDiscountConvenientStore = availableDiscountConvenientStore;
    }

    public boolean isAvailableDiscountFastFood() {
        return availableDiscountFastFood;
    }

    public void setAvailableDiscountFastFood(boolean availableDiscountFastFood) {
        this.availableDiscountFastFood = availableDiscountFastFood;
    }

    public boolean isAvailableDiscountRestaurant() {
        return availableDiscountRestaurant;
    }

    public void setAvailableDiscountRestaurant(boolean availableDiscountRestaurant) {
        this.availableDiscountRestaurant = availableDiscountRestaurant;
    }

    public boolean isAvailableDiscountTheater() {
        return availableDiscountTheater;
    }

    public void setAvailableDiscountTheater(boolean availableDiscountTheater) {
        this.availableDiscountTheater = availableDiscountTheater;
    }

    public ArrayList<Integer> getLikedKookmin() {
        return likedKookmin;
    }

    public void setLikedKookmin(ArrayList<Integer> likedKookmin) {
        this.likedKookmin = likedKookmin;
    }

    public ArrayList<Integer> getLikedShinhan() {
        return likedShinhan;
    }

    public void setLikedShinhan(ArrayList<Integer> likedShinhan) {
        this.likedShinhan = likedShinhan;
    }

    public ArrayList<Integer> getSavedKookmin() {
        return savedKookmin;
    }

    public void setSavedKookmin(ArrayList<Integer> savedKookmin) {
        this.savedKookmin = savedKookmin;
    }

    public ArrayList<Integer> getSavedShinhan() {
        return savedShinhan;
    }

    public void setSavedShinhan(ArrayList<Integer> savedShinhan) {
        this.savedShinhan = savedShinhan;
    }
}