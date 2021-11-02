package com.betna.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.betna.R;
import com.betna.databinding.TopServiceRowBinding;
import com.betna.models.ServiceModel;

import java.util.List;

public class TopServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int i = 0;

    public TopServiceAdapter(List<ServiceModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TopServiceRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.top_service_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        Log.e("dldkkd",list.get(position).getColor());
        if(list.get(position).getColor().startsWith("#")){
myHolder.binding.card.setCardBackgroundColor(Color.parseColor(list.get(position).getColor()));}
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TopServiceRowBinding binding;

        public MyHolder(@NonNull TopServiceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
