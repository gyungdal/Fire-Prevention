package com.codezero.fireprevention.activity.listview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.community.network.getSensorData;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-07-27.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context = null;
    public ArrayList<Item> items = new ArrayList<Item>();
    private DBHelper database;
    private SQLiteDatabase db;

    public ListViewAdapter(Context context) {
        super();
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 2);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(String name, boolean state){
        Item addInfo = new Item(name, state, false);
        items.add(addInfo);
    }
    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.manager_list, null);

            holder.name = (TextView) convertView.findViewById(R.id.ProductName);
            holder.state = (CheckBox) convertView.findViewById(R.id.ProductState);
            holder.mSwitch = (Switch) convertView.findViewById(R.id.ProductSwitch);
            holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(position).setState(isChecked);
                    SetAlarm(items.get(position).getName());
                }
            });
            final String name = holder.name.getText().toString();
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(position).setIsdel(!items.get(position).getIsdel());
                }
            });
            holder.mSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAlarm(name);
                }
            });
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Item data = items.get(position);
        holder.name.setText(data.getName());
        holder.state.setChecked(false);
        holder.mSwitch.setChecked(data.getState());
        return convertView;
    }

    private void SetAlarm(String name){
        boolean state = true;
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex("name")).equals(name)) {
                state = (c.getInt(c.getColumnIndex("flag")) == 1 ? true : false);
                break;
            }
        }
        Log.i("State Change", "That name is " + name);
        Log.i("State Change", "That state is " + state);
        db.close();
        db = database.getWritableDatabase();
        db.execSQL("update " + DBConfig.TABLE_NAME + " set flag = "
                + (state ? 1 : 0) + " where name = \"" + name + "\";");
        db.close();
    }

}
