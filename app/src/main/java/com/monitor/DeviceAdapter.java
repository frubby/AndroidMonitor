package com.monitor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.frw.monitor.R;

import java.util.List;
import java.util.Map;


public class DeviceAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public DeviceAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final class ItemViews {
        public AwesomeTextView name;
        public BootstrapLabel id;
        public AwesomeTextView ia;
        public AwesomeTextView ib;
        public AwesomeTextView ic;
        public TextView outLoad;
        public TextView act;
        public TextView type;
        public TextView state;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViews itemViews = null;
        if (convertView == null) {
            itemViews = new ItemViews();
            convertView = layoutInflater.inflate(R.layout.item_device, null);
            itemViews.name = (AwesomeTextView) convertView.findViewById(R.id.item_device_name);
            itemViews.id = (BootstrapLabel) convertView.findViewById(R.id.item_device_id);
            itemViews.ia = (AwesomeTextView) convertView.findViewById(R.id.item_device_load_ia);
            itemViews.ib = (AwesomeTextView) convertView.findViewById(R.id.item_device_load_ib);
            itemViews.ic = (AwesomeTextView) convertView.findViewById(R.id.item_device_load_ic);
            itemViews.outLoad = (TextView) convertView.findViewById(R.id.item_device_out_load);

            itemViews.act = (TextView) convertView.findViewById(R.id.item_device_act);

            itemViews.type = (TextView) convertView.findViewById(R.id.item_device_type);
            itemViews.state = (TextView) convertView.findViewById(R.id.item_device_state);

            convertView.setTag(itemViews);
        } else {
            itemViews = (ItemViews) convertView.getTag();
        }
        //绑定数据
        itemViews.id.setText("" + data.get(position).get("id"));
        itemViews.name.setText((String) data.get(position).get("name"));
        itemViews.ia.setText("" + data.get(position).get("ia"));
        itemViews.ib.setText("" + data.get(position).get("ib"));

        itemViews.ic.setText("" + data.get(position).get("ic"));
        itemViews.outLoad.setText("" + (Float) data.get(position).get("load"));
        itemViews.act.setText("" + data.get(position).get("actNum"));
        itemViews.type.setText((String) data.get(position).get("type"));

        itemViews.state.setText((String) data.get(position).get("state"));


        return convertView;
    }
}
