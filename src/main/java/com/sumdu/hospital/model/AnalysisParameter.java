package com.sumdu.hospital.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class AnalysisParameter extends RecursiveTreeObject<AnalysisParameter> {
    private int id;
    private String attr;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
