package com.example.avocado.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avocado.R;
import com.example.avocado.db.dict;

import java.util.ArrayList;
import com.example.avocado.databinding.DictItemListBinding;

import org.w3c.dom.Text;

public class dictAdapter extends RecyclerView.Adapter<dictAdapter.ViewHolder>{

    private ArrayList<dict> dictData;
    private DictItemListBinding binding;

    public dictAdapter(ArrayList<dict> dictData){
        this.dictData = dictData;
    }
    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dict_item_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull dictAdapter.ViewHolder holder, int position) {
        holder.dictNAme.setText(dictData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dictData.size();
    } //임시

    public void setItems(ArrayList<dict> list)
    {
        dictData = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dictNAme;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dictNAme = (TextView)itemView.findViewById(R.id.dictName);
        }
    }
}