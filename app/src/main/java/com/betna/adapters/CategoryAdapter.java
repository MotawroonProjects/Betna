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
import com.betna.activities_fragments.activity_home.fragments.FragmentDepartments;
import com.betna.activities_fragments.activity_send_order.SendOrderActivity;
import com.betna.databinding.CategoryRowBinding;
import com.betna.databinding.DepartmentRowBinding;
import com.betna.models.CategoryModel;
import com.betna.models.TypeModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int i = 0;
    private int oldPos = -1, selectedPos = 0;
    public CategoryAdapter(List<CategoryModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSelection(holder.getLayoutPosition());

            }
        });


    }
    public void updateSelection(int adapterPosition) {
        selectedPos = adapterPosition;

        if (oldPos != -1) {
            CategoryModel oldModel = list.get(oldPos);
            oldModel.setSelected(false);
            list.set(oldPos, oldModel);
            //notifyItemChanged(oldPos);

        }
        if (adapterPosition != -1) {
            CategoryModel specialModel = list.get(selectedPos);
            specialModel.setSelected(true);
            list.set(selectedPos, specialModel);
            if(fragment instanceof FragmentDepartments){
                FragmentDepartments fragmentDepartments=(FragmentDepartments) fragment;

                fragmentDepartments.show(list.get(selectedPos).getId()+"");
            }
            //notifyItemChanged(selectedPos);

        } else {
            CategoryModel oldModel = list.get(oldPos);
            oldModel.setSelected(false);
            list.set(oldPos, oldModel);
            //notifyItemChanged(oldPos);

        }
        notifyDataSetChanged();
        oldPos = selectedPos;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryRowBinding binding;

        public MyHolder(@NonNull CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
