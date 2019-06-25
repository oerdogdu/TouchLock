package com.atoi.touchlock.POJO;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.atoi.touchlock.Utils.DateConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "advertisement", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "userId",
        onDelete = CASCADE))
@TypeConverters(DateConverter.class)
public class Advertisement implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int price;
    private int minDay;
    private int numberOfRoom;
    private int numberOfBathroom;
    private int numberOfBed;
    @SerializedName("title")
    private String adName;
    private String  type, currency, address, country, city, description;
    private boolean wifi;
    private boolean petAllowed;
    private boolean fireExt;
    private boolean airConditioner;
    private boolean tv;
    private boolean basicNeeds;
    @SerializedName("email")
    private String userEmail;
    private Date checkInDate ,checkOutDate;
    public Advertisement(String userEmail, int userId, String adName, String type, Date checkInDate, Date checkOutDate,
                         int minDay, int numberOfRoom, int numberOfBathroom, int numberOfBed, String country, String city, String address,
                         int price, String currency, String description, boolean wifi, boolean petAllowed, boolean fireExt, boolean airConditioner, boolean tv,
                         boolean basicNeeds) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.adName = adName;
        this.minDay = minDay;
        this.numberOfRoom = numberOfRoom;
        this.numberOfBathroom = numberOfBathroom;
        this.numberOfBed = numberOfBed;
        this.address = address;
        this.price = price;
        this.type = type;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.country = country;
        this.city = city;
        this.currency = currency;
        this.description = description;
        this.wifi = wifi;
        this.petAllowed = petAllowed;
        this.fireExt = fireExt;
        this.airConditioner = airConditioner;
        this.tv = tv;
        this.basicNeeds = basicNeeds;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public int getMinDay() {
        return minDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    public int getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(int numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public int getNumberOfBathroom() {
        return numberOfBathroom;
    }

    public void setNumberOfBathroom(int numberOfBathroom) {
        this.numberOfBathroom = numberOfBathroom;
    }

    public int getNumberOfBed() {
        return numberOfBed;
    }

    public void setNumberOfBed(int numberOfBed) {
        this.numberOfBed = numberOfBed;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isPetAllowed() {
        return petAllowed;
    }

    public void setPetAllowed(boolean petAllowed) {
        this.petAllowed = petAllowed;
    }

    public boolean isFireExt() {
        return fireExt;
    }

    public void setFireExt(boolean fireExt) {
        this.fireExt = fireExt;
    }

    public boolean isAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isBasicNeeds() {
        return basicNeeds;
    }

    public void setBasicNeeds(boolean basicNeeds) {
        this.basicNeeds = basicNeeds;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }
}
