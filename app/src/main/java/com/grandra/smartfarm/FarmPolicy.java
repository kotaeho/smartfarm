package com.grandra.smartfarm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmPolicy {

    @SerializedName("policy_list")
    private List<Policy> policyList;
    private PolicyPaging policyPaging;

    public List<Policy> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<Policy> policyList) {
        this.policyList = policyList;
    }

    public PolicyPaging getPolicyPaging() {
        return policyPaging;
    }

    public void setPolicyPaging(PolicyPaging policyPaging) {
        this.policyPaging = policyPaging;
    }

}