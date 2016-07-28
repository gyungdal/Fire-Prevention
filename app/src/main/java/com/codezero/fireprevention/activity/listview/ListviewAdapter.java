package com.codezero.fireprevention.activity.listview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-07-27.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context = null;
    public ArrayList<Item> items = new ArrayList<Item>();

    public ListViewAdapter(Context context) {
        super();
        this.context = context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(String name, Boolean state){
        Item addInfo = new Item(name, state);
        items.add(addInfo);
    }
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.manager_list, null);

            holder.name = (TextView) convertView.findViewById(R.id.ProductName);
            holder.state = (CheckBox) convertView.findViewById(R.id.ProductState);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Item data = items.get(position);

        holder.name.setText(data.getName());
        holder.state.setChecked(data.getState());
        return convertView;
    }

}
