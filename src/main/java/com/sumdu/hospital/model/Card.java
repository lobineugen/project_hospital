package com.sumdu.hospital.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Card {
    private int cardID;
    private int patientID;
    private String cardNumber;
    private String cureType;
    private Date dateIn;
    private Date dateOut;
    private String mainDiagnosis;
    private String complication;
    private String pvt;
    private String concomitant;
    private String week;
    private List<ExpertConsultation> expertConsultationList;
    private String etiotropicTherapy;
    private String secondTherapy;
    private String recommendations;
    private String doctor;

    public Card() {
    }

    public Card(int cardID, String cardNumber, Date dateIn, Date dateOut, String week) {
        this.cardID = cardID;
        this.cardNumber = cardNumber;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.week = week;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCureType() {
        return cureType;
    }

    public void setCureType(String cureType) {
        this.cureType = cureType;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public String getMainDiagnosis() {
        return mainDiagnosis;
    }

    public void setMainDiagnosis(String mainDiagnosis) {
        this.mainDiagnosis = mainDiagnosis;
    }

    public String getComplication() {
        return complication;
    }

    public void setComplication(String complication) {
        this.complication = complication;
    }

    public String getPvt() {
        return pvt;
    }

    public void setPvt(String pvt) {
        this.pvt = pvt;
    }

    public String getConcomitant() {
        return concomitant;
    }

    public void setConcomitant(String concomitant) {
        this.concomitant = concomitant;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<ExpertConsultation> getExpertConsultationList() {
        return expertConsultationList;
    }

    public void addExpertConsultation(ExpertConsultation expertConsultation) {
        if (expertConsultationList == null) {
            expertConsultationList = new ArrayList<>();
        }
        expertConsultationList.add(expertConsultation);
    }

    public void removeExpertConsultation(ExpertConsultation expertConsultation){
        expertConsultationList.remove(expertConsultation);
    }

    public String getEtiotropicTherapy() {
        return etiotropicTherapy;
    }

    public void setEtiotropicTherapy(String etiotropicTherapy) {
        this.etiotropicTherapy = etiotropicTherapy;
    }

    public String getSecondTherapy() {
        return secondTherapy;
    }

    public void setSecondTherapy(String secondTherapy) {
        this.secondTherapy = secondTherapy;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
