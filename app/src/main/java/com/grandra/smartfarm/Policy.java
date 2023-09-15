package com.grandra.smartfarm;

import com.google.gson.annotations.SerializedName;

public class Policy {
    @SerializedName("seq")
    private String seq;
    @SerializedName("typeDv")
    private String typeDv;
    @SerializedName("title")
    private String title;
    @SerializedName("contents")
    private String contents;
    @SerializedName("applStDt")
    private String applStDt;
    @SerializedName("applEdDt")
    private String applEdDt;
    @SerializedName("price")
    private Object price;
    @SerializedName("totQuantity")
    private Object totQuantity;
    @SerializedName("chargeAgency")
    private String chargeAgency;
    @SerializedName("eduStDt")
    private Object eduStDt;
    @SerializedName("eduEdDt")
    private Object eduEdDt;
    @SerializedName("eduTime")
    private Object eduTime;
    @SerializedName("eduCnt")
    private Object eduCnt;
    @SerializedName("eduMethod")
    private Object eduMethod;
    @SerializedName("eduMethod2")
    private Object eduMethod2;
    @SerializedName("eduMethod3")
    private Object eduMethod3;
    @SerializedName("eduTarget")

    private String eduTarget;
    @SerializedName("area1Nm")
    private Object area1Nm;
    @SerializedName("area2Nm")
    private Object area2Nm;
    @SerializedName("chargeDept")
    private String chargeDept;
    @SerializedName("chargeTel")
    private String chargeTel;
    @SerializedName("infoUrl")
    private String infoUrl;


    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getTypeDv() {
        return typeDv;
    }

    public void setTypeDv(String typeDv) {
        this.typeDv = typeDv;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getApplStDt() {
        return applStDt;
    }

    public void setApplStDt(String applStDt) {
        this.applStDt = applStDt;
    }

    public String getApplEdDt() {
        return applEdDt;
    }

    public void setApplEdDt(String applEdDt) {
        this.applEdDt = applEdDt;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public Object getTotQuantity() {
        return totQuantity;
    }

    public void setTotQuantity(Object totQuantity) {
        this.totQuantity = totQuantity;
    }

    public String getChargeAgency() {
        return chargeAgency;
    }

    public void setChargeAgency(String chargeAgency) {
        this.chargeAgency = chargeAgency;
    }

    public Object getEduStDt() {
        return eduStDt;
    }

    public void setEduStDt(Object eduStDt) {
        this.eduStDt = eduStDt;
    }

    public Object getEduEdDt() {
        return eduEdDt;
    }

    public void setEduEdDt(Object eduEdDt) {
        this.eduEdDt = eduEdDt;
    }

    public Object getEduTime() {
        return eduTime;
    }

    public void setEduTime(Object eduTime) {
        this.eduTime = eduTime;
    }

    public Object getEduCnt() {
        return eduCnt;
    }

    public void setEduCnt(Object eduCnt) {
        this.eduCnt = eduCnt;
    }

    public Object getEduMethod() {
        return eduMethod;
    }

    public void setEduMethod(Object eduMethod) {
        this.eduMethod = eduMethod;
    }

    public Object getEduMethod2() {
        return eduMethod2;
    }

    public void setEduMethod2(Object eduMethod2) {
        this.eduMethod2 = eduMethod2;
    }

    public Object getEduMethod3() {
        return eduMethod3;
    }

    public void setEduMethod3(Object eduMethod3) {
        this.eduMethod3 = eduMethod3;
    }

    public String getEduTarget() {
        return eduTarget;
    }

    public void setEduTarget(String eduTarget) {
        this.eduTarget = eduTarget;
    }

    public Object getArea1Nm() {
        return area1Nm;
    }

    public void setArea1Nm(Object area1Nm) {
        this.area1Nm = area1Nm;
    }

    public Object getArea2Nm() {
        return area2Nm;
    }

    public void setArea2Nm(Object area2Nm) {
        this.area2Nm = area2Nm;
    }

    public String getChargeDept() {
        return chargeDept;
    }

    public void setChargeDept(String chargeDept) {
        this.chargeDept = chargeDept;
    }

    public String getChargeTel() {
        return chargeTel;
    }

    public void setChargeTel(String chargeTel) {
        this.chargeTel = chargeTel;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

}
