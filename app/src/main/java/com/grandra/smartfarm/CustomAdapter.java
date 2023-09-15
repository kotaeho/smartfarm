package com.grandra.smartfarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private boolean hideCheckBoxLayout = false;


    private ArrayList<String> farmName;

    private ArrayList<String> farmapplStDt;
    private ArrayList<String> farmapplEdDt;
    private ArrayList<String> farmNum;
    private Context context; // Context 변수 추가

    public CustomAdapter(Context context, ArrayList<String> farmName,
                         ArrayList<String> farmNum,ArrayList<String> farmapplStDt,ArrayList<String> farmapplEdDt) {
        this.farmName = farmName;
        this.farmNum = farmNum;
        this.farmapplEdDt = farmapplEdDt;
        this.farmapplStDt = farmapplStDt;
        this.context = context;
    }


    //===== [Click 이벤트 구현을 위해 추가된 코드] ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String data,String image_data);

        void onItemClicked(int position, String data);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
    //======================================================================

    //===== 뷰홀더 클래스 =====================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView institutions;

        private TextView applStDt;

        private TextView applEdDt;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            institutions = itemView.findViewById(R.id.institutions);
            applStDt = itemView.findViewById(R.id.text_applStDt);
            applEdDt = itemView.findViewById(R.id.text_applEdDt);
        }

        public TextView getinstitutions(){return institutions;}
        public TextView getapplStDt() {
            return applStDt;
        }

    }
    //========================================================================

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String data = farmNum.get(position);
                        itemClickListener.onItemClicked(position, data);
                    }
                }
            }
        });

        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = farmName.get(position);
        holder.institutions.setText(name);
        String applStDt = farmapplStDt.get(position);
        holder.applStDt.setText(applStDt); // 원본 문자열을 사용하거나 다른 처리를 수행할 수 있습니다.
        String applEdDt = farmapplEdDt.get(position);
        holder.applEdDt.setText(applEdDt);
    }
    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return farmName.size();
    }
}