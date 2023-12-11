package com.example.selection;

import java.io.Serializable;
import java.util.ArrayList;

public class FunctionCard implements Serializable {
    private int cardIndex;
    private String cardName;
    private String cardApplicationLink;
    private boolean ifDiscountAmusement = false, ifDiscountAmusementAll = false;
    private boolean ifDiscountBakery = false, ifDiscountBakeryAll= false;
    private boolean ifDiscountBookStore = false , ifDiscountBookStoreAll = false;
    private boolean ifDiscountCafe = false, ifDiscountCafeAll = false;
    private boolean ifDiscountConvenientStore = false, ifDiscountConvenientStoreAll = false;
    private boolean ifDiscountFastFood = false, ifDiscountFastFoodAll = false;
    private boolean ifDiscountRestaurant = false, ifDiscountRestaurantAll = false;
    private boolean ifDiscountTheater = false, ifDiscountTheaterAll = false;
    private ArrayList<FunctionLocationSpecificDiscount> amusementDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> bakeryDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> bookstoreDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> cafeDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> convenientStoreDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> fastFoodDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> restaurantDiscount;
    private ArrayList<FunctionLocationSpecificDiscount> theaterDiscount;


    public FunctionCard(){}

    public FunctionCard(
            int cardIndex, String cardName, String cardApplicationLink,
            boolean ifDiscountAmusement,boolean ifDiscountAmusementAll,
            boolean ifDiscountBakery, boolean ifDiscountBakeryAll,
            boolean ifDiscountBookStore, boolean ifDiscountBookStoreAll,
            boolean ifDiscountCafe, boolean ifDiscountCafeAll,
            boolean ifDiscountConvenientStore, boolean ifDiscountConvenientStoreAll,
            boolean ifDiscountFastFood, boolean ifDiscountFastFoodAll,
            boolean ifDiscountRestaurant, boolean ifDiscountRestaurantAll,
            boolean ifDiscountTheater, boolean ifDiscountTheaterAll){
        this.cardIndex = cardIndex; this.cardName = cardName; this.cardApplicationLink = cardApplicationLink;
        this.ifDiscountAmusement = ifDiscountAmusement; this.ifDiscountAmusementAll = ifDiscountAmusementAll;
        this.ifDiscountBakery = ifDiscountBakery; this.ifDiscountBakeryAll = ifDiscountBakeryAll;
        this.ifDiscountBookStore = ifDiscountBookStore; this.ifDiscountBookStoreAll = ifDiscountBookStoreAll;
        this.ifDiscountConvenientStore = ifDiscountConvenientStore; this.ifDiscountConvenientStoreAll = ifDiscountConvenientStoreAll;
        this.ifDiscountCafe = ifDiscountCafe; this.ifDiscountCafeAll = ifDiscountCafeAll;
        this.ifDiscountFastFood = ifDiscountFastFood; this.ifDiscountFastFoodAll = ifDiscountFastFoodAll;
        this.ifDiscountRestaurant = ifDiscountRestaurant; this.ifDiscountRestaurantAll = ifDiscountRestaurantAll;
        this.ifDiscountTheater = ifDiscountTheater; this.ifDiscountTheaterAll = ifDiscountTheaterAll;

    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardApplicationLink() {
        return cardApplicationLink;
    }

    public void setCardApplicationLink(String cardApplicationLink) {
        this.cardApplicationLink = cardApplicationLink;
    }

    public boolean isIfDiscountAmusement() {
        return ifDiscountAmusement;
    }

    public void setIfDiscountAmusement(boolean ifDiscountAmusement) {
        this.ifDiscountAmusement = ifDiscountAmusement;
    }

    public boolean isIfDiscountAmusementAll() {
        return ifDiscountAmusementAll;
    }

    public void setIfDiscountAmusementAll(boolean ifDiscountAmusementAll) {
        this.ifDiscountAmusementAll = ifDiscountAmusementAll;
    }

    public boolean isIfDiscountBakery() {
        return ifDiscountBakery;
    }

    public void setIfDiscountBakery(boolean ifDiscountBakery) {
        this.ifDiscountBakery = ifDiscountBakery;
    }

    public boolean isIfDiscountBakeryAll() {
        return ifDiscountBakeryAll;
    }

    public void setIfDiscountBakeryAll(boolean ifDiscountBakeryAll) {
        this.ifDiscountBakeryAll = ifDiscountBakeryAll;
    }

    public boolean isIfDiscountBookStore() {
        return ifDiscountBookStore;
    }

    public void setIfDiscountBookStore(boolean ifDiscountBookStore) {
        this.ifDiscountBookStore = ifDiscountBookStore;
    }

    public boolean isIfDiscountBookStoreAll() {
        return ifDiscountBookStoreAll;
    }

    public void setIfDiscountBookStoreAll(boolean ifDiscountBookStoreAll) {
        this.ifDiscountBookStoreAll = ifDiscountBookStoreAll;
    }

    public boolean isIfDiscountCafe() {
        return ifDiscountCafe;
    }

    public void setIfDiscountCafe(boolean ifDiscountCafe) {
        this.ifDiscountCafe = ifDiscountCafe;
    }

    public boolean isIfDiscountCafeAll() {
        return ifDiscountCafeAll;
    }

    public void setIfDiscountCafeAll(boolean ifDiscountCafeAll) {
        this.ifDiscountCafeAll = ifDiscountCafeAll;
    }

    public boolean isIfDiscountConvenientStore() {
        return ifDiscountConvenientStore;
    }

    public void setIfDiscountConvenientStore(boolean ifDiscountConvenientStore) {
        this.ifDiscountConvenientStore = ifDiscountConvenientStore;
    }

    public boolean isIfDiscountConvenientStoreAll() {
        return ifDiscountConvenientStoreAll;
    }

    public void setIfDiscountConvenientStoreAll(boolean ifDiscountConvenientStoreAll) {
        this.ifDiscountConvenientStoreAll = ifDiscountConvenientStoreAll;
    }

    public boolean isIfDiscountFastFood() {
        return ifDiscountFastFood;
    }

    public void setIfDiscountFastFood(boolean ifDiscountFastFood) {
        this.ifDiscountFastFood = ifDiscountFastFood;
    }

    public boolean isIfDiscountFastFoodAll() {
        return ifDiscountFastFoodAll;
    }

    public void setIfDiscountFastFoodAll(boolean ifDiscountFastFoodAll) {
        this.ifDiscountFastFoodAll = ifDiscountFastFoodAll;
    }

    public boolean isIfDiscountRestaurant() {
        return ifDiscountRestaurant;
    }

    public void setIfDiscountRestaurant(boolean ifDiscountRestaurant) {
        this.ifDiscountRestaurant = ifDiscountRestaurant;
    }

    public boolean isIfDiscountRestaurantAll() {
        return ifDiscountRestaurantAll;
    }

    public void setIfDiscountRestaurantAll(boolean ifDiscountRestaurantAll) {
        this.ifDiscountRestaurantAll = ifDiscountRestaurantAll;
    }

    public boolean isIfDiscountTheater() {
        return ifDiscountTheater;
    }

    public void setIfDiscountTheater(boolean ifDiscountTheater) {
        this.ifDiscountTheater = ifDiscountTheater;
    }

    public boolean isIfDiscountTheaterAll() {
        return ifDiscountTheaterAll;
    }

    public void setIfDiscountTheaterAll(boolean ifDiscountTheaterAll) {
        this.ifDiscountTheaterAll = ifDiscountTheaterAll;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getAmusementDiscount() {
        return amusementDiscount;
    }

    public void setAmusementDiscount(ArrayList<FunctionLocationSpecificDiscount> amusementDiscount) {
        this.amusementDiscount = amusementDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getBakeryDiscount() {
        return bakeryDiscount;
    }

    public void setBakeryDiscount(ArrayList<FunctionLocationSpecificDiscount> bakeryDiscount) {
        this.bakeryDiscount = bakeryDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getBookstoreDiscount() {
        return bookstoreDiscount;
    }

    public void setBookstoreDiscount(ArrayList<FunctionLocationSpecificDiscount> bookstoreDiscount) {
        this.bookstoreDiscount = bookstoreDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getCafeDiscount() {
        return cafeDiscount;
    }

    public void setCafeDiscount(ArrayList<FunctionLocationSpecificDiscount> cafeDiscount) {
        this.cafeDiscount = cafeDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getConvenientStoreDiscount() {
        return convenientStoreDiscount;
    }

    public void setConvenientStoreDiscount(ArrayList<FunctionLocationSpecificDiscount> convenientStoreDiscount) {
        this.convenientStoreDiscount = convenientStoreDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getFastFoodDiscount() {
        return fastFoodDiscount;
    }

    public void setFastFoodDiscount(ArrayList<FunctionLocationSpecificDiscount> fastFoodDiscount) {
        this.fastFoodDiscount = fastFoodDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getRestaurantDiscount() {
        return restaurantDiscount;
    }

    public void setRestaurantDiscount(ArrayList<FunctionLocationSpecificDiscount> restaurantDiscount) {
        this.restaurantDiscount = restaurantDiscount;
    }

    public ArrayList<FunctionLocationSpecificDiscount> getTheaterDiscount() {
        return theaterDiscount;
    }

    public void setTheaterDiscount(ArrayList<FunctionLocationSpecificDiscount> theaterDiscount) {
        this.theaterDiscount = theaterDiscount;
    }
}
