package com.example.avocado.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.databinding.FragmentWordListBinding;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Word}.
 * TODO: Replace the implementation with code for your data type.
 */
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private ArrayList<Word> wordData;
    public ArrayList<Boolean> isSelected;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public WordListAdapter(ArrayList<Word> items) {
        wordData = items;
        isSelected = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            isSelected.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentWordListBinding binding = FragmentWordListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    public void setWordList(ArrayList<Word> wordData) {
        this.wordData = wordData;
        isSelected.clear();
        for (int i = 0; i < wordData.size(); i++) {
            isSelected.add(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.content.setText(wordData.get(position).getContent());
        holder.meaning.setText(wordData.get(position).getMeaning());

        if (isSelected.get(position)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#90CAF9"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return wordData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView content;
        public final TextView meaning;
        public final LinearLayout layout;

        public ViewHolder(FragmentWordListBinding binding) {
            super(binding.getRoot());
            content = binding.content;
            meaning = binding.meaning;
            layout = binding.layout;

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }
}