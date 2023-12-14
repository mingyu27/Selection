package com.example.selection;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<Integer> likedKookminIndexList;
    private List<Integer> likedShinhanIndexList;
    private List<Integer> savedKookminIndexList;
    private List<Integer> savedShinhanIndexList;

    private List<FunctionCard> savedKookminFunctionCardList = new ArrayList<>();
    private List<FunctionCard> savedShinhanFunctionCardList = new ArrayList<>();
    private List<FunctionCard> likedKookminFunctionCardList = new ArrayList<>();
    private List<FunctionCard> likedShinhanFunctionCardList = new ArrayList<>();
    public FunctionUser(){}


    public FunctionUser(String uid, String name,
                        boolean availableDiscountAmusement, boolean availableDiscountBakery,
                        boolean availableDiscountBookStore, boolean availableDiscountCafe,
                        boolean availableDiscountConvenientStore, boolean availableDiscountFastFood,
                        boolean availableDiscountRestaurant, boolean availableDiscountTheater,
                        List<Integer> likedKookminIndexList, List<Integer> likedShinhanIndexList,
                        List<Integer> savedKookminIndexList, List<Integer> savedShinhanIndexList) {
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
        this.likedKookminIndexList = likedKookminIndexList;
        this.likedShinhanIndexList = likedShinhanIndexList;
        this.savedKookminIndexList = savedKookminIndexList;
        this.savedShinhanIndexList = savedShinhanIndexList;
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

    public List<Integer> getLikedKookminIndexList() {
        return likedKookminIndexList;
    }

    public void setLikedKookminIndexList(List<Integer> likedKookminIndexList) {
        this.likedKookminIndexList = likedKookminIndexList;
    }

    public List<Integer> getLikedShinhanIndexList() {
        return likedShinhanIndexList;
    }

    public void setLikedShinhanIndexList(List<Integer> likedShinhanIndexList) {
        this.likedShinhanIndexList = likedShinhanIndexList;
    }

    public List<Integer> getSavedKookminIndexList() {
        return savedKookminIndexList;
    }

    public void setSavedKookminIndexList(List<Integer> savedKookminIndexList) {
        this.savedKookminIndexList = savedKookminIndexList;
    }

    public List<Integer> getSavedShinhanIndexList() {
        return savedShinhanIndexList;
    }

    public void setSavedShinhanIndexList(List<Integer> savedShinhanIndexList) {
        this.savedShinhanIndexList = savedShinhanIndexList;
    }

    public List<FunctionCard> getSavedKookminFunctionCardList() {
        return savedKookminFunctionCardList;
    }

    public void setSavedKookminFunctionCardList(List<FunctionCard> savedKookminFunctionCardList) {
        this.savedKookminFunctionCardList = savedKookminFunctionCardList;
    }

    public List<FunctionCard> getSavedShinhanFunctionCardList() {
        return savedShinhanFunctionCardList;
    }

    public void setSavedShinhanFunctionCardList(List<FunctionCard> savedShinhanFunctionCardList) {
        this.savedShinhanFunctionCardList = savedShinhanFunctionCardList;
    }

    public List<FunctionCard> getLikedKookminFunctionCardList() {
        return likedKookminFunctionCardList;
    }

    public void setLikedKookminFunctionCardList(List<FunctionCard> likedKookminFunctionCardList) {
        this.likedKookminFunctionCardList = likedKookminFunctionCardList;
    }

    public List<FunctionCard> getLikedShinhanFunctionCardList() {
        return likedShinhanFunctionCardList;
    }

    public void setLikedShinhanFunctionCardList(List<FunctionCard> likedShinhanFunctionCardList) {
        this.likedShinhanFunctionCardList = likedShinhanFunctionCardList;
    }
}