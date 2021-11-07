package com.betna.general_ui_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.betna.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.betna.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }








    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if(endPoint!=null)
            Log.e("dlldldl",Tags.IMAGE_URL + endPoint);
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        }

    }

//    @BindingAdapter("user_image")
//    public static void user_image(View view, String endPoint) {
//        if (view instanceof CircleImageView) {
//            CircleImageView imageView = (CircleImageView) view;
//
//            if (endPoint != null) {
//                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).into(imageView);
//            }
//        } else if (view instanceof RoundedImageView) {
//            RoundedImageView imageView = (RoundedImageView) view;
//            if (endPoint != null) {
//                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().placeholder(R.drawable.ic_avatar).into(imageView);
//            }
//        } else if (view instanceof ImageView) {
//            ImageView imageView = (ImageView) view;
//            if (endPoint != null) {
//                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
//            }
//        }
//
//    }

    @BindingAdapter("date")
    public static void date(TextView view, String date) {
        if (date!=null&&!date.isEmpty()){
           String[] dates = date.split("T");
           view.setText(dates[0]);
        }

    }
    @BindingAdapter("date3")
    public static void date3(TextView view, String date2) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Format dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy");
        Date date = null;
        try {
           date= dateFormat1.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.setText(dateFormat.format(date));

    }
    @BindingAdapter("date2")
    public static void date2(TextView view, long date2) {
//        String d = Time_Ago.getTimeAgo(date2*1000,view.getContext());
//        view.setText(d);
    }
    @BindingAdapter("order_status")
    public static void orderStatus(Button textView, String status) {
        if (status.equals("sent")){
            textView.setText(textView.getContext().getString(R.string.order_sent));
        }else if (status.equals("accept")){
            textView.setText(textView.getContext().getString(R.string.accept));

        }
        else if (status.equals("start")){
            textView.setText(textView.getContext().getString(R.string.is_start));

        }
        else if (status.equals("waiting")){
            textView.setText(textView.getContext().getString(R.string.preparing));

        }
        else if (status.equals("end")){
            textView.setText(textView.getContext().getString(R.string.end));

        }
    }

}










