package com.grandra.smartfarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.RuntimeExecutionException;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> seq;
    private ArrayList<String> title;
    private ArrayList<String> applStDt;
    private ArrayList<String> applEdDt;
    private RecyclerView recyclerView;
    private EditText editText;

    private AppUpdateManager appUpdateManager;
    private final int REQUEST_CODE = 366;
    private final int MY_REQUEST_CODE = 700;
    private AdView mAdview; //애드뷰 변수 선언
    private NativeAd mNativeAd;
    private boolean isAdLoaded = false;
    private Context mContext;
    private static final String PREF_LAST_UPDATE_VERSION_CODE = "last_update_version_code";


    private FrameLayout TOS;
    private Animation slidingLeft;
    private Animation slidingRight;
    private ImageButton menu;
    private boolean isOpen = false;
    private TextView TosText;
    private TextView person_info;
    private ProgressBar progressBar;
    private ApiService apiService;
    private Button location_btn;

    private Button seoul_btn;
    private Button Busan_btn;
    private Button Daegu_btn;
    private Button Incheon_btn;
    private Button Gwangju_btn;
    private Button Daejeon_btn;
    private Button Ulsan_btn;
    private Button Saejong_btn;
    private Button gyeonggi_btn;
    private Button Gwangwon_btn;
    private Button Chungbuk_btn;
    private Button Chungnam_btn;
    private Button jeonbuk_btn;
    private Button Jeonnam_btn;
    private Button Gyeongbuk_btn;
    private Button Gyeongnam_btn;
    private Button Jeju_btn;
    private String firstDayOfYear;
    private String lastDayOfYear;
    private static final int TIMEOUT_MILLISECONDS = 7000; // 10초
    private Handler handler = new Handler();
    private boolean retry = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        mContext = this;
        loadNativeAd();

        this.Init();
        this.menu();
        this.pop_up();
        this.Textsearch();
        this.getJson("11");
        this.location_popup();


        // 앱의 현재 버전 코드와 마지막 업데이트 확인 버전 코드 비교
        int currentVersionCode = BuildConfig.VERSION_CODE;
        int lastUpdateVersionCode = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getInt(PREF_LAST_UPDATE_VERSION_CODE, 0);

        if (currentVersionCode > lastUpdateVersionCode) {
            // 업데이트 확인 시작
            check_update();

            // 마지막 업데이트 확인 버전 코드 업데이트
            getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit()
                    .putInt(PREF_LAST_UPDATE_VERSION_CODE, currentVersionCode)
                    .apply();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() { //광고 초기화
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdview = findViewById(R.id.adView); //배너광고 레이아웃 가져오기
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER); //광고 사이즈는 배너 사이즈로 설정
        adView.setAdUnitId("\n" + "ca-app-pub-4268507364131475/4289845860");


    }

    private void getJson(String searchArea1){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        apiService = PolicyApiClient.getClient().create(ApiService.class);

        Call<FarmPolicy> call = apiService.getPolicyList(
                "SEyTD6N9tYDcENXw1Yd08q3Snfv+dPJOGaXAv74WZInaJlTQ3ZAGiEbcb4PbpVOwH7y5WEuEoTHmP1GmlT6+6w==",
                "사업",
                firstDayOfYear,
                lastDayOfYear,
                "json",
                searchArea1,
                300,
                1
        );
        String url = call.request().url().toString();
        Log.v("태그","값:"+url);

        // 타임아웃 설정
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                call.cancel(); // API 호출 취소
                Log.e("PolicyAPI", "API 요청 타임아웃");
                // 여기에서 필요한 작업 수행: 예를 들어 다시 호출 시도
                if(!retry) {
                    retryApiCall(searchArea1);
                }
            }
        }, TIMEOUT_MILLISECONDS);

        call.enqueue(new Callback<FarmPolicy>() {
            @Override
            public void onResponse(@NonNull Call<FarmPolicy> call, @NonNull Response<FarmPolicy> response) {
                if (response.isSuccessful()) {
                    retry = true;
                    progressBar.setVisibility(View.GONE);
                    FarmPolicy farmPolicy = response.body();
                    Log.v("태그","성공");
                    if (farmPolicy != null) {
                        List<Policy> policyItems = farmPolicy.getPolicyList();

                        Log.v("태그","값:"+policyItems.get(0).getApplEdDt());
                        // 데이터를 처리하는 코드 작성
                        for(int i = 0; i < policyItems.size(); i++){
                            seq.add(policyItems.get(i).getSeq());
                            title.add(policyItems.get(i).getTitle());
                            applStDt.add(policyItems.get(i).getApplStDt());
                            applEdDt.add(policyItems.get(i).getApplEdDt());
                        }

                        recycleview_set();
                    }
                } else {
                    retry = false;
                    Log.e("PolicyAPI", "API 요청 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FarmPolicy> call, @NonNull Throwable t) {
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
        seq = new ArrayList<>();
        title = new ArrayList<>();
        applStDt = new ArrayList<>();
        applEdDt = new ArrayList<>();

        editText = findViewById(R.id.search);
        menu = findViewById(R.id.leftButton);
        TOS = findViewById(R.id.TOS);
        TOS.setVisibility(View.GONE);
        slidingLeft = AnimationUtils.loadAnimation(this,R.anim.sliding_left);
        slidingRight = AnimationUtils.loadAnimation(this,R.anim.sliding_right);
        SlidingAnimationListener listener = new SlidingAnimationListener();
        slidingLeft.setAnimationListener(listener);
        slidingRight.setAnimationListener(listener);

        TosText = findViewById(R.id.Tos_popup);
        person_info = findViewById(R.id.person_info);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        location_btn = findViewById(R.id.location_btn);
        recyclerView = findViewById(R.id.recyclerView);
        firstDayOfYear = getFirstDayOfYear();
        lastDayOfYear = getLastDayOfYear();
    }


    private void location_popup(){
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 레이아웃을 인플레이트
                View popupView = getLayoutInflater().inflate(R.layout.location_popup, null);

                // 팝업 창을 만들고 팝업 레이아웃을 설정
                PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // 팝업 내부 배경 설정
                popupWindow.setElevation(50); // 그림자의 크기를 설정 (원하는 크기로 조정)

                // 팝업 레이아웃 안의 닫기 버튼을 찾아서 클릭 이벤트 처리
                Button closeButton = popupView.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 팝업 창 닫기
                        popupWindow.dismiss();
                    }
                });

                // 팝업 창을 화면에 표시
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                seoul_btn = popupView.findViewById(R.id.Seoul);
                Busan_btn = popupView.findViewById(R.id.Busan);
                Daegu_btn = popupView.findViewById(R.id.Daegu);
                Incheon_btn = popupView.findViewById(R.id.incheon);
                Gwangju_btn = popupView.findViewById(R.id.Gangju);
                Daejeon_btn = popupView.findViewById(R.id.Daejeon);
                Ulsan_btn = popupView.findViewById(R.id.Ulsan);
                Saejong_btn = popupView.findViewById(R.id.saejong);
                gyeonggi_btn = popupView.findViewById(R.id.gyeonggi);
                Gwangwon_btn = popupView.findViewById(R.id.Gangwon);
                Chungbuk_btn = popupView.findViewById(R.id.Chungbuk);
                Chungnam_btn = popupView.findViewById(R.id.chungnam);
                jeonbuk_btn = popupView.findViewById(R.id.jeonbuk);
                Jeonnam_btn = popupView.findViewById(R.id.Jeonnam);
                Gyeongbuk_btn = popupView.findViewById(R.id.Gyeongbuk);
                Gyeongnam_btn = popupView.findViewById(R.id.Gyeongnam);
                Jeju_btn = popupView.findViewById(R.id.Jeju);

                seoul_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("11");
                        popupWindow.dismiss();

                        location_btn.setText("지역기관 : 서울");
                    }
                });

                Busan_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("21");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 부산");
                    }
                });

                Daegu_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("22");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 대구");
                    }
                });

                Incheon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("23");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 인천");
                    }
                });

                Gwangju_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("24");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 광주");
                    }
                });

                Daejeon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("25");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 대전");
                    }
                });

                Ulsan_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("26");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 울산");
                    }
                });

                Saejong_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("29");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 세종");
                    }
                });

                gyeonggi_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("31");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 경기");
                    }
                });

                Gwangwon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("32");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 강원");
                    }
                });

                Chungbuk_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("33");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 충북");
                    }
                });

                Chungnam_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("34");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 충남");
                    }
                });

                jeonbuk_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("35");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 전북");
                    }
                });

                Jeonnam_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("36");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 전남");
                    }
                });

                Gyeongbuk_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("37");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 경북");
                    }
                });

                Gyeongnam_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("38");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 경남");
                    }
                });

                Jeju_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seq.clear();
                        title.clear();
                        applStDt.clear();
                        applEdDt.clear();

                        getJson("39");
                        popupWindow.dismiss();
                        location_btn.setText("지역기관 : 제주");
                    }
                });



            }
        });
    }


    private void menu(){
        menu.setOnClickListener(new View.OnClickListener() { // 열기 버튼을 누르면
            @Override
            public void onClick(View v) {
                if (isOpen){ // 슬라이딩 레이아웃이 열려져 있으면
                    TOS.startAnimation(slidingRight); // 슬라이딩 레이아웃 닫기
                } else { // 슬라이딩 레이아웃이 닫혀져 있으면
                    TOS.setVisibility(View.VISIBLE); // 슬라이딩 레이아웃을 보이게하기
                    TOS.startAnimation(slidingLeft); // 슬라이딩 레이아웃 열기
                }
            }
        });
    }

    private void Textsearch(){
        // EditText에 텍스트 변경 이벤트 리스너 등록
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 변경될 때마다 호출되는 부분
                String searchText = charSequence.toString();
                // 여기에 검색 동작을 수행하는 로직을 작성합니다.
                // searchText를 가지고 원하는 동작을 수행합니다.

                ArrayList<String> filteredappSlDt = new ArrayList<>();
                ArrayList<String> filteredfarmTitle = new ArrayList<>();
                ArrayList<String> filteredfarmNum = new ArrayList<>();
                ArrayList<String> filteredfarappEdlt = new ArrayList<>();

                for (int j = 0; j < title.size(); j++) {
                    if (title.get(j).toLowerCase().contains(searchText)) {
                        filteredappSlDt.add(applStDt.get(j));
                        filteredfarmTitle.add(title.get(j));
                        filteredfarmNum.add(seq.get(j));
                        filteredfarappEdlt.add(applEdDt.get(j));
                    }
                }

                // 검색 결과를 리사이클뷰에 업데이트
                CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, filteredfarmTitle,
                        filteredfarmNum, filteredappSlDt,filteredfarappEdlt);

                customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClicked(int position, String data,String image_data) {

                    }

                    @Override
                    public void onItemClicked(int position, String data) {
                        Intent intent = new Intent(getApplicationContext(),business.class);
                        intent.putExtra("farm_num", data);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(customAdapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void check_update(){
        // 앱 업데이트 매니저 초기화
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        // 업데이트를 체크하는데 사용되는 인텐트를 리턴한다.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> { // appUpdateManager이 추가되는데 성공하면 발생하는 이벤트
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE // UpdateAvailability.UPDATE_AVAILABLE == 2 이면 앱 true
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) { // 허용된 타입의 앱 업데이트이면 실행 (AppUpdateType.IMMEDIATE || AppUpdateType.FLEXIBLE)
                // 업데이트가 가능하고, 상위 버전 코드의 앱이 존재하면 업데이트를 실행한다.
                requestUpdate (appUpdateInfo);
            }
        });
    }


    // 업데이트 요청
    private void requestUpdate (AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    // 'getAppUpdateInfo()' 에 의해 리턴된 인텐트
                    appUpdateInfo,
                    // 'AppUpdateType.FLEXIBLE': 사용자에게 업데이트 여부를 물은 후 업데이트 실행 가능
                    // 'AppUpdateType.IMMEDIATE': 사용자가 수락해야만 하는 업데이트 창을 보여줌
                    AppUpdateType.IMMEDIATE,
                    // 현재 업데이트 요청을 만든 액티비티, 여기선 MainActivity.
                    this,
                    // onActivityResult 에서 사용될 REQUEST_CODE.
                    REQUEST_CODE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            Toast myToast = Toast.makeText(this.getApplicationContext(), "MY_REQUEST_CODE", Toast.LENGTH_SHORT);
            myToast.show();

            // 업데이트가 성공적으로 끝나지 않은 경우
            if (resultCode != RESULT_OK) {
                Log.v("태그", "Update flow failed! Result code: " + resultCode);
                // 업데이트가 취소되거나 실패하면 업데이트를 다시 요청할 수 있다.,
                // 업데이트 타입을 선택한다 (IMMEDIATE || FLEXIBLE).
                Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

                appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            // flexible한 업데이트를 위해서는 AppUpdateType.FLEXIBLE을 사용한다.
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // 업데이트를 다시 요청한다.
                        requestUpdate(appUpdateInfo);
                    }
                });
            }
        }
    }

    // 앱이 포그라운드로 돌아오면 업데이트가 UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS 상태로 중단되지 않았는지 확인해야 합니다.
    // 업데이트가 이 상태로 중단된 경우 아래와 같이 업데이트를 계속하세요.
    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            MY_REQUEST_CODE);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    private void loadNativeAd(){

        AdLoader.Builder adBuilder = new AdLoader.Builder(mContext,getResources().getString(R.string.admob_native_ad_id));

        adBuilder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                if(isDestroyed() || isFinishing() || isChangingConfigurations()){
                    nativeAd.destroy();
                    return;
                }

                if(mNativeAd != null){
                    mNativeAd.destroy();
                }

                mNativeAd = nativeAd;
                isAdLoaded = true;
            }
        });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions nativeAdOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        AdLoader adLoader = adBuilder.withAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        if (isAdLoaded) {
            // 다이얼로그를 열고 광고 표시
            showNativeAdDialog();
        } else {
            // 광고가 로드되지 않은 경우 기본 뒤로가기 동작 수행
            super.onBackPressed();
        }
    }

    private void showNativeAdDialog() {
        // 다이얼로그 레이아웃을 inflate하여 표시합니다.
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ad_native);
        dialog.setCancelable(true);
        dialog.show();

        Button review = dialog.findViewById(R.id.btnReview);
        Button exit = dialog.findViewById(R.id.btnExit);

        // 네이티브 광고를 다이얼로그 내의 NativeAdView에 표시합니다.
        NativeAdView adView = dialog.findViewById(R.id.nativeAdView);
        populateNativeAdView(mNativeAd, adView);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInAppReviewPopup();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
    }

    private void showInAppReviewPopup() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                manager.launchReviewFlow(MainActivity.this, reviewInfo).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            } else {
                // There was some problem, log or handle the error code.
                @ReviewErrorCode int reviewErrorCode = ((RuntimeExecutionException) Objects.requireNonNull(task.getException())).getErrorCode();
            }
        });
    }

    class  SlidingAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) { // 애니메이션이 끝날 때 자동 호출됨
            if(isOpen) { // 슬라이딩 레이아웃의 열린 상태가 끝나면
                TOS.setVisibility(View.INVISIBLE); // 슬라이딩 레이아웃 안보이게 하고
                isOpen = false; // 닫기 상태가 됨
            } else { // 슬라이딩 레이아웃의 닫힌 상태가 끝나면
                isOpen = true; // 열기 상태가됨
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void recycleview_set(){
        //--------------------------------------------------------

        recyclerView.setVisibility(View.VISIBLE);

        //--- LayoutManager는 아래 3가지중 하나를 선택하여 사용 ----
        // 1) LinearLayoutManager()
        // 2) GridLayoutManager()
        // 3) StaggeredGridLayoutManager()
        //---------------------------------------------------------
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) this);
        recyclerView.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

        CustomAdapter customAdapter = new CustomAdapter(this,title,seq,applStDt,applEdDt);
        //===== [Click 이벤트 구현을 위해 추가된 코드] ==============
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {

            @Override
            public void onItemClicked(int position, String data,String image_data) {

            }

            @Override
            public void onItemClicked(int position, String data) {
                Log.v("태그","클릭");
                Intent intent = new Intent(MainActivity.this,business.class);
                intent.putExtra("Farm_num", data);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(customAdapter); // 어댑터 설정
    }

    private void pop_up(){
        TosText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder menu = new AlertDialog.Builder(MainActivity.this);
                menu.setIcon(R.mipmap.ic_launcher);
                menu.setTitle("이용약관"); // 제목
                menu.setMessage(R.string.tos); // 문구


                // 확인 버튼
                menu.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog 제거
                        dialog.dismiss();
                    }
                });

                menu.show();
            }
        });

        person_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder menu = new AlertDialog.Builder(MainActivity.this);
                menu.setIcon(R.mipmap.ic_launcher);
                menu.setTitle("개인정보처리방침"); // 제목
                menu.setMessage(R.string.person_info); // 문구


                // 확인 버튼
                menu.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog 제거
                        dialog.dismiss();
                    }
                });
                menu.show();
            }
        });
    }

    public static String getFirstDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return sdf.format(calendar.getTime());
    }

    public static String getLastDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return sdf.format(calendar.getTime());
    }

}