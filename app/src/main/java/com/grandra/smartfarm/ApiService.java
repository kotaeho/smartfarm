package com.grandra.smartfarm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("policyListV2")
    Call<FarmPolicy> getPolicyList(
            @Query("serviceKey") String serviceKey,
            @Query("search_keyword") String searchKeyword,
            @Query("sd") String startDate,
            @Query("ed") String endDate,
            @Query("typeDv") String typeDv,
            @Query("search_area1") String searchArea1,
            @Query("rowCnt") int rowCnt,
            @Query("cp") int cp
    );
}
