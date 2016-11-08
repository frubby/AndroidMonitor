package com.frw.monitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.frw.monitor.common.EnumLoadType;
import com.frw.monitor.common.EnumStats;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityNav extends AppCompatActivity implements TreeNode.TreeNodeClickListener {

    private ListView listView;


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
                Intent it=new Intent(this,SettingsActivity.class);
                this.startActivity(it);
                break;


            default:
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        TreeNode root = TreeNode.root();


        MyHeadHolder.IconTreeItem nodeItem1 = new MyHeadHolder.IconTreeItem();
        nodeItem1.text = "台区一";
        TreeNode node1 = new TreeNode(nodeItem1).setViewHolder(new MyHeadHolder(this));
        root.addChild(node1);

        MyHeadHolder.IconTreeItem nodeItem2 = new MyHeadHolder.IconTreeItem();
        nodeItem2.text = "台区二";
        TreeNode node2 = new TreeNode(nodeItem2).setViewHolder(new MyHeadHolder(this));

        root.addChild(node2);


        Level2Holder.IconTreeItem node1_child1 = new Level2Holder.IconTreeItem();
        node1_child1.text = "开关一";
        TreeNode node1_sw1 = new TreeNode(node1_child1).setViewHolder(new Level2Holder(this));

        Level2Holder.IconTreeItem node1_child2 = new Level2Holder.IconTreeItem();
        node1_child2.text = "开关二";
        TreeNode node1_sw2 = new TreeNode(node1_child2).setViewHolder(new Level2Holder(this));


        node1.addChildren(node1_sw1, node1_sw2);

        node1_sw1.setClickListener(this);


        AndroidTreeView tView = new AndroidTreeView(this, root);

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.id_drawer);
        layout.addView(tView.getView());


        listView = (ListView) findViewById(R.id.id_lv);
        List<Map<String, Object>> list = getData();
        listView.setAdapter(new DeviceAdapter(this, list));


    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", "" + (100000 + i));
            map.put("name", "开关Test" + i);
            map.put("ia", "" + i);

            map.put("ib", "" + i);
            map.put("ic", "" + i);

            map.put("state", EnumStats.AXIANG.getText());

            map.put("type", EnumLoadType.LEVEL_IMP.getText());

            list.add(map);
        }
        return list;
    }

    @Override
    public void onClick(TreeNode node, Object value) {
        Intent it = new Intent(this, ActivityDevice.class);
        this.startActivity(it);


    }
}
