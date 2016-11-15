package com.frw.monitor;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.frw.monitor.bean.Data;
import com.frw.monitor.bean.SwitchData;
import com.frw.monitor.dialog.FileChooserDialog;
import com.frw.monitor.task.DataThread;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityNav extends AppCompatActivity implements TreeNode.TreeNodeClickListener {

    public final static int DATA_REFRESH = 1;
    public final static int DATA_STRUCT_REFRESH = 2;
    public final static int STRUCT_REFRESH = 3;

    public static final String TAG = "ActivityNav";

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


    private void initTree(Data area) {
        TreeNode root;

        root = TreeNode.root();

        MyHeadHolder.IconTreeItem treeItemArea = new MyHeadHolder.IconTreeItem();
        treeItemArea.text = area.name;
        TreeNode nodeArea = new TreeNode(treeItemArea).setViewHolder(new MyHeadHolder(this));
        root.addChild(nodeArea);


        for (SwitchData device : area.sdata) {

            Level2Holder.IconTreeItem treeItem = new Level2Holder.IconTreeItem();
            treeItem.text = device.name;
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

//        Area area = DataMock.area;
//        TextView tvAreaIa = (TextView) this.findViewById(R.id.area_ia);
//        TextView tvAreaIb = (TextView) this.findViewById(R.id.area_ib);
//        TextView tvAreaIc = (TextView) this.findViewById(R.id.area_ic);
//        TextView tvAreaLoadDegree = (TextView) this.findViewById(R.id.area_load_degree);
//        tvAreaIa.setText("" + area.getIa());
//        tvAreaIb.setText("" + area.getIb());
//        tvAreaIc.setText("" + area.getIc());
//        tvAreaLoadDegree.setText("" + area.getLoadDegree());

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


//        initTree(DataMock.area);
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
//        for (Device device : DataMock.area.getDevices()) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("id", "" + device.getId());
//            map.put("name", device.getName());
//            map.put("ia", "" + device.getIa());
//
//            map.put("ib", "" + device.getIb());
//            map.put("ic", "" + device.getIc());
//
//            map.put("state", EnumStats.values()[device.getState()]);
//
//            map.put("type", EnumLoadType.values()[device.getType()]);
//
//            list.add(map);
//        }
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
        Intent it;
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Log.i(TAG, "setting");
                it = new Intent(this, SettingsActivity.class);
                this.startActivity(it);
                break;
            case R.id.menu_loading:
                Log.i(TAG, "load config");

                FileChooserDialog dialog = new FileChooserDialog(this);
                dialog.show();
                dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                    public void onFileSelected(Dialog source, File file) {
                        source.hide();

                        Data data = parseConfig(file);
                        if (data != null) {
                            initStruct(data);
                            Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(source.getContext(), "File error : " + file.getName(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    public void onFileSelected(Dialog source, File folder, String name) {
                        source.hide();
                        Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                break;

            default:
                break;
        }

        return true;
    }


    public Data parseConfig(File file) {

        Data data = new Data();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line;
            int num = 0;
            while ((line = bf.readLine()) != null) {
                Log.i(TAG, line);
                if (line.trim().equals("")) {
                    Log.w(TAG, "empty line. ");
                    continue;
                }
                String split[] = line.trim().split(",");
                if (split.length != 2) {
                    Log.w(TAG, "error line. " + line);
                    continue;
                }
                if (num == 0) {
                    data.address = Long.parseLong(split[0], 16);
                    data.name = split[1];
                } else {
                    SwitchData switchData = new SwitchData();
                    switchData.address = Long.parseLong(split[0], 16);
                    switchData.name = split[1];
                    data.sdata[num - 1] = switchData;
                }
                num++;
            }
            data.num = num;

            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void initStruct(Data data) {
        TreeNode root;

        root = TreeNode.root();

        MyHeadHolder.IconTreeItem treeItemArea = new MyHeadHolder.IconTreeItem();
        treeItemArea.text = data.name;
        TreeNode nodeArea = new TreeNode(treeItemArea).setViewHolder(new MyHeadHolder(this));
        root.addChild(nodeArea);


        for (int i = 0; i < data.num; i++) {
            SwitchData switchData = data.sdata[i];
            Level2Holder.IconTreeItem treeItem = new Level2Holder.IconTreeItem();
            treeItem.text = switchData.name;
            TreeNode nodeDevice = new TreeNode(treeItem).setViewHolder(new Level2Holder(this));
            nodeArea.addChild(nodeDevice);
            nodeDevice.setClickListener(this);

        }


        AndroidTreeView tView = new AndroidTreeView(this, root);

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.id_drawer);
        layout.removeAllViewsInLayout();
        layout.addView(tView.getView());

    }


}
