package com.monitor;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.frw.monitor.R;
import com.monitor.bean.Data;
import com.monitor.bean.SwitchData;
import com.monitor.dialog.FileChooserDialog;
import com.monitor.net.TcpServer;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityNav extends AppCompatActivity implements TreeNode.TreeNodeClickListener, AdapterView.OnItemClickListener {

    public final static int DATA_REFRESH = 1;
    public final static int DATA_STRUCT_REFRESH = 2;
    public final static int STRUCT_REFRESH = 3;

    public final static int DISCONNECT = 4;
    public final static int CONNECT = 5;

    public static final String TAG = "ActivityNav";
    public static final String DATABASE = "Database";
    private ListView listView;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private ImageView imageView;


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
                case DISCONNECT:
                    imageView.setImageResource(R.drawable.disconnect);
                    break;
                case CONNECT:
                    imageView.setImageResource(R.drawable.connect);
                    break;
                default:
                    break;
            }

        }
    };


    TcpServer tcpServer;

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
        Data area = SampleApplication.getData();

        TextView tvAreaIa = (TextView) this.findViewById(R.id.area_ia);
        TextView tvAreaIb = (TextView) this.findViewById(R.id.area_ib);
        TextView tvAreaIc = (TextView) this.findViewById(R.id.area_ic);
        TextView tvAreaName = (TextView) this.findViewById(R.id.tx_area_title);
        TextView tvAreaLoadDegree = (TextView) this.findViewById(R.id.area_load_degree);
        tvAreaIa.setText("" + area.Ia);
        tvAreaIb.setText("" + area.Ib);
        tvAreaIc.setText("" + area.Ic);
        tvAreaName.setText(area.name);
        tvAreaLoadDegree.setText("" + area.imbalance);
    }

    private void refreshDeviceList() {
        list.clear();
        list.addAll(getData());
        //TODO error congig file error
        deviceAdapter.notifyDataSetChanged();

    }

    List<Map<String, Object>> list = new ArrayList<>();
    DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
        // 获取Editor对象
        editor = sp.edit();
        imageView = (ImageView) findViewById(R.id.img_connect);
        Data data;
        if (sp.getString("data", "").equals("")) {
            Toast toast = Toast.makeText(this, "缺少配置文件", Toast.LENGTH_LONG);
            toast.show();
            data = new Data();

        } else {
            String strData = sp.getString("data", "");
            data = JSON.parseObject(strData, Data.class);
            Log.i(TAG, strData);


        }
        initStruct(data);

        list = getData();
        listView = (ListView) findViewById(R.id.id_lv);
        deviceAdapter = new DeviceAdapter(this, list);
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(this);

    }

    private boolean isClientMode() {
        boolean isClient = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).getBoolean("client_switch", false);

        return isClient;
    }

    @Override
    protected void onResume() {

        tcpServer = new TcpServer(this, this.mHandler);
        tcpServer.start();
        super.onResume();
        Log.i("test", "resume");
    }


    @Override
    protected void onPause() {

        tcpServer.stop();

        super.onPause();
        Log.i("test", "pause");

    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Data data = SampleApplication.getData();

        if (data == null) {
            return list;
        }
        for (int i = 0; i < data.sdata.size(); i++) {
            SwitchData switchData = data.sdata.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", switchData.address);
            map.put("name", switchData.name);
            map.put("ia", switchData.Ia);
            map.put("ib", switchData.Ib);
            map.put("ic", switchData.Ic);
            map.put("actNum", switchData.num);
            map.put("load", switchData.load);

            map.put("state", switchData.switchState);
            map.put("type", switchData.loadType);

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
                            editor.clear();
                            String strData = JSON.toJSONString(data);
                            editor.putString("data", strData);
                            editor.commit();

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

            case R.id.menu_version:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("监控.");
                builder.setTitle("信息");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
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
                long address = Long.parseLong(split[0], 16);
//                if (num == 0) {
//                    data.address = address;
//                    data.name = split[1];
//                } else {
//                    SwitchData switchData = new SwitchData();
//                    switchData.address = address;
//                    switchData.name = split[1];
//                    data.sdata.add(switchData);
//                }
                data.config.put(address, split[1]);
                num++;
            }
            data.num = 0;

            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void initStruct(Data data) {
        TextView txArea = (TextView) this.findViewById(R.id.tx_area_title);
        txArea.setText(data.name);

        TreeNode root;

        root = TreeNode.root();

        MyHeadHolder.IconTreeItem treeItemArea = new MyHeadHolder.IconTreeItem();
        treeItemArea.text = data.name;
        TreeNode nodeArea = new TreeNode(treeItemArea).setViewHolder(new MyHeadHolder(this));
        root.addChild(nodeArea);


        for (int i = 0; i < data.num; i++) {
            SwitchData switchData = data.sdata.get(i);
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

        // 存储
        SampleApplication.initData(data);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(i);
        SwitchData switchData = new SwitchData();
        switchData.address = ((long) item.get("id"));
        switchData.name = item.get("name").toString();
        switchData.Ia = (float) item.get("ia");
        switchData.Ic = (float) item.get("ib");
        switchData.Ib = (float) item.get("ic");
        switchData.switchState = item.get("state").toString();
        switchData.load = (float) item.get("load");
        switchData.num = (int) item.get("actNum");
        switchData.loadType = (String) item.get("type");

        Log.i("onclick", item.get("id").toString());
        dialog(switchData);

    }


    SwitchData cmdSwitchData;

    protected void dialog(final SwitchData switchData) {
        cmdSwitchData = switchData;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = "";
        title += switchData.address;
        if (!("").equals(switchData.name)) {
            title += ":" + switchData.name;
        }
        builder.setTitle(title);
        int checked = -1;
        if ("断开".equals(switchData.switchState)) {
            checked = 0;
        } else if ("A相".equals(switchData.switchState)) {
            checked = 1;
        } else if ("B相".equals(switchData.switchState)) {
            checked = 2;
        } else if ("C相".equals(switchData.switchState)) {
            checked = 3;
        }
        builder.setSingleChoiceItems(new String[]{"断开", "A相", "B相", "C相"}, checked, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("test", "" + which);
                        switch (which) {
                            case 0:
                                cmdSwitchData.switchState = "断开";
                                break;
                            case 1:
                                cmdSwitchData.switchState = "A相";
                                break;
                            case 2:
                                cmdSwitchData.switchState = "B相";
                                break;
                            case 3:
                                cmdSwitchData.switchState = "C相";
                                break;
                        }
                    }
                }
        );
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tcpServer.sendCmd(cmdSwitchData);
                        dialog.dismiss();
                    }

                }

        );
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }

        );
        builder.create().show();
    }
}
