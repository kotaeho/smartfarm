package com.grandra.smartfarm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class business extends AppCompatActivity {
    private AdView mAdview; //애드뷰 변수 선언
    private des_ApiService apiService;
    private String contents;
    LinearLayout linearLayout;

    private String InfoUrl;
    private boolean retry = false;

    private static final int TIMEOUT_MILLISECONDS = 3000; // 3초
    private Handler handler = new Handler();

    private ProgressBar progressBar;

    String farmNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Intent intent = getIntent();
        if (intent != null) {
            farmNum = intent.getStringExtra("Farm_num");
        }
        this.Init();
        this.getJson(farmNum);

        MobileAds.initialize(this, new OnInitializationCompleteListener() { //광고 초기화
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdview = findViewById(R.id.des_adView); //배너광고 레이아웃 가져오기
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER); //광고 사이즈는 배너 사이즈로 설정
        adView.setAdUnitId("\n" + "ca-app-pub-4268507364131475/4289845860");
    }

    private void getJson(String farmNum){
        apiService = DesPolicyApiClient.getClient().create(des_ApiService.class);

        Call<DesBusiness> call = apiService.getDesPolicyList(
                "SEyTD6N9tYDcENXw1Yd08q3Snfv+dPJOGaXAv74WZInaJlTQ3ZAGiEbcb4PbpVOwH7y5WEuEoTHmP1GmlT6+6w==",
                farmNum,
                "json"
        );

        // 타임아웃 설정
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                call.cancel(); // API 호출 취소
                Log.e("PolicyAPI", "API 요청 타임아웃");
                // 여기에서 필요한 작업 수행: 예를 들어 다시 호출 시도
                if(!retry) {
                    retryApiCall(farmNum);
                }
            }
        }, TIMEOUT_MILLISECONDS);


        call.enqueue(new Callback<DesBusiness>() {
            @Override
            public void onResponse(@NonNull Call<DesBusiness> call, @NonNull Response<DesBusiness> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    retry = true;
                    DesBusiness desBusiness = response.body();
                    if (desBusiness != null) {
                        PolicyResult policyItems = desBusiness.getPolicyResult();
                        contents = policyItems.getContents();
                        InfoUrl = policyItems.getInfoUrl();
                        setContents();
                    }
                } else {
                    retry = false;
                    Log.e("PolicyAPI", "API 요청 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DesBusiness> call, @NonNull Throwable t) {
                retry = false;
                Log.e("PolicyAPI", "API 요청 실패: " + t.getMessage());
            }
        });
    }

    private void retryApiCall(String searchArea1) {
        // 여기에서 API 호출을 다시 시도할 수 있는 로직을 구현
        // 예를 들어, 다시 호출할 조건을 검사하고 getJson(searchArea1)을 호출
        getJson(searchArea1);
    }

    private void Init(){
        linearLayout = findViewById(R.id.layout_contents);
        progressBar = findViewById(R.id.business_progressBar);
    }

    private void setContents(){
        String[] lines = contents.split("ㅇ");

        for (int i = 1; i < lines.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(lines[i]);
            textView.setTextSize(20);
            textView.setAutoLinkMask(Linkify.WEB_URLS);
            linearLayout.addView(textView);
        }

        TextView infoUrl = new TextView(this);
        infoUrl.setText(InfoUrl);
        infoUrl.setTextSize(20);
        infoUrl.setTextColor(Color.BLUE);
        infoUrl.setPaintFlags(infoUrl.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // 링크 텍스트에 밑줄 추가
        infoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 링크를 클릭했을 때 처리할 작업을 여기에 추가
                openWebPage(InfoUrl);
            }
        });
        linearLayout.addView(infoUrl);
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
