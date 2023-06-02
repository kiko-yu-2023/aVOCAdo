package com.example.avocado.ui.home;

import android.content.ClipData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avocado.R;
import com.example.avocado.db.Dict;

import java.util.ArrayList;
import java.util.List;

import com.example.avocado.databinding.DictItemListBinding;

public class DictAdapter extends RecyclerView.Adapter<DictAdapter.ViewHolder>{

    private ArrayList<Dict> dictData;
    private List<Dict> items;
    private OnItemClickListener listener;
    private DictItemListBinding binding;

    public DictAdapter(ArrayList<Dict> dictData){
        this.dictData = dictData;
    }
    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dict_item_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DictAdapter.ViewHolder holder, int position) {

        //아이템 바인딩, 클릭 이벤트 처리
        Dict item = dictData.get(position);
        holder.dictNAme.setText(item.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(listener !=null)
                {
                    listener.onItemClick(position,item.getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dictData.size();
    } //임시

    public void setItems(ArrayList<Dict> newItems) {
        dictData = newItems; // 기존 데이터를 새로운 데이터로 교체
        notifyDataSetChanged(); // 어댑터에 데이터 변경을 알림
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dictNAme;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dictNAme = itemView.findViewById(R.id.dictName);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position,String dictName);
    }

}