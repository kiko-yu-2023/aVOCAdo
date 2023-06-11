package com.example.avocado.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.databinding.FragmentWordListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Word> wordData;

    public MyItemRecyclerViewAdapter(ArrayList<Word> items) {
        wordData = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentWordListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    public void setWordList(ArrayList<Word> wordData) {
        this.wordData = wordData;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = wordData.get(position);
        holder.content.setText(wordData.get(position).getContent());
        holder.meaning.setText(wordData.get(position).getMeaning());
    }

    @Override
    public int getItemCount() {
        return wordData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView content;
        public final TextView meaning;
        public Word mItem;

        public ViewHolder(FragmentWordListBinding binding) {
            super(binding.getRoot());
            content = binding.content;
            meaning = binding.meaning;
        }

    }
}