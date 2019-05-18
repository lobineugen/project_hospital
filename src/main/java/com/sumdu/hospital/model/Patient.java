package com.sumdu.hospital.model;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Patient extends RecursiveTreeObject<Patient> {
    private int patientID;
    private String fullName;
    private String passportID;
    private Date dateOfBirth;
    private String addressType;
    private String address;
    private String phoneNumber;
    private String workPlace;
    private List<Card> cardsList;
    private Card lastCard = null;
    private Date pvtStart;
    private Date repeatPvtStart;
    private Date pvtEnd;
    private Date repeatPvtEnd;
    private String allergicReactions;
    private String ogkSurvey;

    public Patient(int patientID, String fullName, String passportID)  {
        this.patientID = patientID;
        this.fullName = fullName;
        this.passportID = passportID;
        this.dateOfBirth = null;
        this.addressType = null;
        this.address = null;
        this.phoneNumber = null;
        this.workPlace = null;
    }

    public void setCardsList(List<Card> cardsList) {
        this.cardsList = cardsList;
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

    public Card getLastCard() {
        return lastCard;
    }

    public void setLastCard(Card lastCard) {
        this.lastCard = lastCard;
    }

    public void addCard(Card card) {
        if (cardsList == null) {
            cardsList = new ArrayList<>();
        }
        cardsList.add(card);
        lastCard = card;
    }

    public List<Card> getCardsList() {
        return cardsList == null ? new ArrayList<>() : cardsList;
    }


    public void removeCard(Card card) {
        cardsList.remove(card);
    }


    public String getWeek() {
        return lastCard != null ? lastCard.getWeek() : null;
    }

    public String getCardNumber() {
        return lastCard != null ? lastCard.getCardNumber() : null;
    }

    public Date getDateIn() {
        return lastCard != null ? lastCard.getDateIn() : null;
    }

    public Date getDateOut() {
        return lastCard != null ? lastCard.getDateOut() : null;
    }

    public String getMainDiagnosis() {
        return lastCard != null ? lastCard.getMainDiagnosis() : null;
    }

    public String getComplication() {
        return lastCard != null ? lastCard.getComplication() : null;
    }

    public String getPvt() {
        return lastCard != null ? lastCard.getPvt() : null;
    }

    public String getConcomitant() {
        return lastCard != null ? lastCard.getConcomitant() : null;
    }

    public Date getPvtStart() {
        return pvtStart;
    }

    public void setPvtStart(Date pvtStart) {
        this.pvtStart = pvtStart;
    }

    public Date getRepeatPvtStart() {
        return repeatPvtStart;
    }

    public void setRepeatPvtStart(Date repeatPvtStart) {
        this.repeatPvtStart = repeatPvtStart;
    }

    public Date getPvtEnd() {
        return pvtEnd;
    }

    public void setPvtEnd(Date pvtEnd) {
        this.pvtEnd = pvtEnd;
    }

    public Date getRepeatPvtEnd() {
        return repeatPvtEnd;
    }

    public void setRepeatPvtEnd(Date repeatPvtEnd) {
        this.repeatPvtEnd = repeatPvtEnd;
    }

    public String getAllergicReactions() {
        return allergicReactions;
    }

    public void setAllergicReactions(String allergicReactions) {
        this.allergicReactions = allergicReactions;
    }

    public String getOgkSurvey() {
        return ogkSurvey;
    }

    public void setOgkSurvey(String ogkSurvey) {
        this.ogkSurvey = ogkSurvey;
    }
}
