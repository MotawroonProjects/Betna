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
import com.betna.activities_fragments.activity_send_order.SendOrderActivity;
import com.betna.activities_fragments.activity_update_order.UpdateOrderActivity;
import com.betna.databinding.SubTypeRowBinding;
import com.betna.databinding.TypeRowBinding;
import com.betna.models.SubTypeModel;
import com.betna.models.TypeModel;

import java.util.List;

public class SubTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TypeModel.ServicePlaces> list;
    private Context context;
    private LayoutInflater inflater;
    private int  selectedPos = 0;

    //private Fragment_Main fragment_main;
    public SubTypeAdapter(List<TypeModel.ServicePlaces> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SubTypeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.sub_type_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MyHolder myHolder = (MyHolder) holder;
        if(list.get(position).getSub_place()!=null){
        myHolder.binding.setModel(list.get(position));}
        else {
            myHolder.itemView.setVisibility(View.GONE);
        }
      //  updateSelection(selectedPos);
        if(list.get(position).isSelected()){
            selectedPos = position;
        }
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


        if (adapterPosition != -1) {
            TypeModel.ServicePlaces specialModel = list.get(selectedPos);
            if(specialModel.isSelected()){
                specialModel.setSelected(false);
            }
            else{
            specialModel.setSelected(true);}
            list.set(selectedPos, specialModel);
            if (context instanceof SendOrderActivity) {
                SendOrderActivity activity = (SendOrderActivity) context;
                activity.setsubselection(specialModel,adapterPosition);

            }
            else   if (context instanceof UpdateOrderActivity) {
                UpdateOrderActivity activity = (UpdateOrderActivity) context;
               // activity.setsubselection(specialModel,adapterPosition);

            }

            //notifyItemChanged(selectedPos);

        }
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public SubTypeRowBinding binding;

        public MyHolder(@NonNull SubTypeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
