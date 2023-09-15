
package com.grandra.smartfarm;


import com.google.gson.annotations.SerializedName;

public class DesBusiness {

    @SerializedName("policy_result")
    private PolicyResult policyResult;

    public PolicyResult getPolicyResult() {
        return policyResult;
    }

    public void setPolicyResult(PolicyResult policyResult) {
        this.policyResult = policyResult;
    }

}
