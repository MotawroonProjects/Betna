package com.betna.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.betna.R;
import com.betna.databinding.SpinnerGoveRowBinding;
import com.betna.models.Cities_Model;
import com.betna.models.Governate_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SpinnerGoveAdapter extends BaseAdapter {
    private List<Governate_Model.Data> dataList;
    private Context context;
    private String lang;
    public SpinnerGoveAdapter(List<Governate_Model.Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerGoveRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_gove_row,viewGroup,false);
        binding.setLang(lang);
        binding.setCityModel(dataList.get(i));
        return binding.getRoot();
    }
}
