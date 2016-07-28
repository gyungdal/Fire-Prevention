package com.codezero.fireprevention.activity.fragment;

/**
 * Created by GyungDal on 2016-07-28.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.codezero.fireprevention.R;

@SuppressLint("ValidFragment")
public class TAB extends Fragment {
    private Context context;
    private int img;

    public TAB(Context context, int img) {
        this.context = context;
        this.img = img;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_item, null);
        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        image.setImageResource(img);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "출저 : 서울소방재난본부 홈페이지", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}