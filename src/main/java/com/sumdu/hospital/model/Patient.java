package com.sumdu.hospital.model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int patientID;
    private String fullName;
    private String passportID;
    private Date dateOfBirth;
    private String addressType;
    private String address;
    private String phoneNumber;
    private String workPlace;
    private List<Card> cardsList;

    public Patient(int patientID, String fullName, String passportID) {
        this.patientID = patientID;
        this.fullName = fullName;
        this.passportID = passportID;
        this.dateOfBirth = null;
        this.addressType = null;
        this.address = null;
        this.phoneNumber = null;
        this.workPlace = null;
    }


    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void addCard(Card card) {
        if (cardsList == null) {
            cardsList = new ArrayList<>();
        }
        cardsList.add(card);
    }

    public List<Card> getCardsList() {
        return cardsList;
    }


    public void removeCard(Card card) {
        cardsList.remove(card);
    }
}
