package com.betna.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.betna.R;
import com.betna.activities_fragments.activity_my_rates.MyRatesActivity;
import com.betna.activities_fragments.activity_send_order.SendOrderActivity;
import com.betna.activities_fragments.activity_update_order.UpdateOrderActivity;
import com.betna.databinding.RateRowBinding;
import com.betna.databinding.TypeRowBinding;
import com.betna.models.RateModel;
import com.betna.models.TypeModel;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TypeModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int oldPos = -1, selectedPos = 0;

    //private Fragment_Main fragment_main;
    public TypeAdapter(List<TypeModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TypeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.type_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
      //  updateSelection(selectedPos);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateSelection(holder.getLayoutPosition());

            }
        });
//Log.e("eeee",list.get(position).getOffer_value()+""+(list.get(position).getAmount()%list.get(position).getOffer_min()));
// Log.e("ssss",((list.get(position).getHave_offer().equals("yes")?(list.get(position).getOffer_type().equals("per")?(list.get(position).getProduct_default_price().getPrice()-((list.get(position).getProduct_default_price().getPrice()*list.get(position).getOffer_value())/100)):list.get(position).getProduct_default_price().getPrice()-list.get(position).getOffer_value()):list.get(position).getProduct_default_price().getPrice())+""));

    }

    public void updateSelection(int adapterPosition) {
        selectedPos = adapterPosition;

        if (oldPos != -1) {
            TypeModel oldModel = list.get(oldPos);
            oldModel.setSelected(false);
            list.set(oldPos, oldModel);
            //notifyItemChanged(oldPos);

        }
        if (adapterPosition != -1) {
            TypeModel specialModel = list.get(selectedPos);
            specialModel.setSelected(true);
            list.set(selectedPos, specialModel);
            if (context instanceof SendOrderActivity) {
                SendOrderActivity activity = (SendOrderActivity) context;
                activity.setselection(specialModel);

            }
            else   if (context instanceof UpdateOrderActivity) {
                UpdateOrderActivity activity = (UpdateOrderActivity) context;
                activity.setselection(specialModel);

            }
            //notifyItemChanged(selectedPos);

        } else {
            TypeModel oldModel = list.get(oldPos);
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

    public class MyHolder extends RecyclerView.ViewHolder {
        public TypeRowBinding binding;

        public MyHolder(@NonNull TypeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
