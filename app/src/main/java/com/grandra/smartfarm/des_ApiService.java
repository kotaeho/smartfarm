package com.grandra.smartfarm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface des_ApiService {
    @GET("policyViewV2")
    Call<DesBusiness> getDesPolicyList(
            @Query("serviceKey") String serviceKey,
            @Query("seq") String seq,
            @Query("typeDv") String typeDv
    );
}
