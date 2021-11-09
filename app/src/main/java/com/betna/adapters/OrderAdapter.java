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
import com.betna.activities_fragments.activity_home.fragments.FragmentOrders;
import com.betna.databinding.CategoryRowBinding;
import com.betna.databinding.OrderRowBinding;
import com.betna.models.CategoryModel;
import com.betna.models.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int i = 0;

    public OrderAdapter(List<OrderModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));

        myHolder.binding.btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(fragment instanceof FragmentOrders){
    FragmentOrders fragmentOrders=(FragmentOrders) fragment;
    fragmentOrders.showorder(list.get(holder.getLayoutPosition()).getId());
}
            }
        });
        myHolder.binding.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment instanceof FragmentOrders){
                    FragmentOrders fragmentOrders=(FragmentOrders) fragment;
                    fragmentOrders.update(list.get(holder.getLayoutPosition()));
                }
            }
        });
        myHolder.binding.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment instanceof FragmentOrders){
                    FragmentOrders fragmentOrders=(FragmentOrders) fragment;
                    fragmentOrders.delete(list.get(holder.getLayoutPosition()));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public MyHolder(@NonNull OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
