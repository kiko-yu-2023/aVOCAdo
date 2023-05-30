package com.example.avocado.ui.home;

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
        holder.dictNAme.setText(dictData.get(position).getTitle());
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
            dictNAme = (TextView)itemView.findViewById(R.id.dictName);
        }
    }

}