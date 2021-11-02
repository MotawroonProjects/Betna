package com.betna.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betna.R;
import com.betna.activities_fragments.activity_previousorder.PreviousOrderActivity;
import com.betna.databinding.PreOrderRowBinding;
import com.betna.models.OrderModel;

import java.util.List;

public class PreviousOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;

    //private Fragment_Main fragment_main;
    public PreviousOrderAdapter(List<OrderModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        PreOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.pre_order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));


//Log.e("eeee",list.get(position).getOffer_value()+""+(list.get(position).getAmount()%list.get(position).getOffer_min()));
        // Log.e("ssss",((list.get(position).getHave_offer().equals("yes")?(list.get(position).getOffer_type().equals("per")?(list.get(position).getProduct_default_price().getPrice()-((list.get(position).getProduct_default_price().getPrice()*list.get(position).getOffer_value())/100)):list.get(position).getProduct_default_price().getPrice()-list.get(position).getOffer_value()):list.get(position).getProduct_default_price().getPrice())+""));
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviousOrderActivity currentOrderActivity=(PreviousOrderActivity)context;
                currentOrderActivity.show(list.get(holder.getLayoutPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public PreOrderRowBinding binding;

        public MyHolder(@NonNull PreOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
