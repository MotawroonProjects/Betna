package com.betna.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.betna.R;
import com.betna.activities_fragments.activity_home.fragments.Fragment_Home;
import com.betna.databinding.DepartmentRowBinding;
import com.betna.databinding.PreworkRowBinding;
import com.betna.models.CategoryModel;
import com.betna.models.ServiceModel;

import java.util.List;

public class PreWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceModel.Images> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    public PreWorkAdapter(List<ServiceModel.Images> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        PreworkRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.prework_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public PreworkRowBinding binding;

        public MyHolder(@NonNull PreworkRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
