package com.frw.monitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frw.monitor.bean.Area;
import com.frw.monitor.bean.Device;
import com.frw.monitor.common.DataMock;
import com.frw.monitor.common.EnumLoadType;
import com.frw.monitor.common.EnumStats;
import com.frw.monitor.task.DataThread;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityNav extends AppCompatActivity implements TreeNode.TreeNodeClickListener {

    public final static int DATA_REFRESH = 1;
    public final static int DATA_STRUCT_REFRESH = 2;
    public final static int STRUCT_REFRESH = 3;


    private ListView listView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DATA_REFRESH:
                    Log.i("handler", "data refresh");
                    refreshDeviceList();
                    refreshArea();
                    break;
                default:
                    break;
            }

        }
    };

    DataThread dataThread;


    private void initTree(Area area) {
        TreeNode root;

        root = TreeNode.root();

        MyHeadHolder.IconTreeItem treeItemArea = new MyHeadHolder.IconTreeItem();
        treeItemArea.text = area.getName();
        TreeNode nodeArea = new TreeNode(treeItemArea).setViewHolder(new MyHeadHolder(this));
        root.addChild(nodeArea);


        for (Device device : area.getDevices()) {

            Level2Holder.IconTreeItem treeItem = new Level2Holder.IconTreeItem();
            treeItem.text = device.getName();
            TreeNode nodeDevice = new TreeNode(treeItem).setViewHolder(new Level2Holder(this));
            nodeArea.addChild(nodeDevice);
            nodeDevice.setClickListener(this);

        }


        AndroidTreeView tView = new AndroidTreeView(this, root);

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.id_drawer);
        layout.removeAllViewsInLayout();
        layout.addView(tView.getView());
    }

    private void refreshArea() {

        Area area = DataMock.area;
        TextView tvAreaIa = (TextView) this.findViewById(R.id.area_ia);
        TextView tvAreaIb = (TextView) this.findViewById(R.id.area_ib);
        TextView tvAreaIc = (TextView) this.findViewById(R.id.area_ic);
        TextView tvAreaLoadDegree = (TextView) this.findViewById(R.id.area_load_degree);
        tvAreaIa.setText("" + area.getIa());
        tvAreaIb.setText("" + area.getIb());
        tvAreaIc.setText("" + area.getIc());
        tvAreaLoadDegree.setText("" + area.getLoadDegree());

    }

    private void refreshDeviceList() {
        list.clear();
        list.addAll(getData());
        deviceAdapter.notifyDataSetChanged();

    }

    List<Map<String, Object>> list = new ArrayList<>();
    DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        initTree(DataMock.area);
        list = getData();
        listView = (ListView) findViewById(R.id.id_lv);
        deviceAdapter = new DeviceAdapter(this, list);
        listView.setAdapter(deviceAdapter);

    }


    @Override
    protected void onResume() {

        dataThread = new DataThread(this, this.mHandler);
        new Thread(dataThread).start();
        super.onResume();
        Log.i("test", "resume");
    }


    @Override
    protected void onPause() {
        dataThread.isRun = false;
        dataThread = null;
        super.onPause();
        Log.i("test", "pause");

    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Device device : DataMock.area.getDevices()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", "" + device.getId());
            map.put("name", device.getName());
            map.put("ia", "" + device.getIa());

            map.put("ib", "" + device.getIb());
            map.put("ic", "" + device.getIc());

            map.put("state", EnumStats.values()[device.getState()]);

            map.put("type", EnumLoadType.values()[device.getType()]);

            list.add(map);
        }
        return list;
    }

    @Override
    public void onClick(TreeNode node, Object value) {
        Intent it = new Intent(this, ActivityDevice.class);
        this.startActivity(it);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT)
                        .show();
                Intent it = new Intent(this, SettingsActivity.class);
                this.startActivity(it);
                break;


            default:
                break;
        }

        return true;
    }
}
