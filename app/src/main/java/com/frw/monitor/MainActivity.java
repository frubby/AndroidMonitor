package com.frw.monitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TreeNode root = TreeNode.root();



        MyHeadHolder.IconTreeItem nodeItem1 = new MyHeadHolder.IconTreeItem();
        nodeItem1.text="台区一";
        TreeNode node1 = new TreeNode(nodeItem1).setViewHolder(new MyHeadHolder(this));
        root.addChild(node1);

        MyHeadHolder.IconTreeItem nodeItem2 = new MyHeadHolder.IconTreeItem();
        nodeItem2.text="台区一";
        TreeNode node2 = new TreeNode(nodeItem2).setViewHolder(new MyHeadHolder(this));

        root.addChild(node2);


        Level2Holder.IconTreeItem node1_child1 = new Level2Holder.IconTreeItem();
        node1_child1.text="开关一";
        TreeNode node1_sw1 = new TreeNode(node1_child1).setViewHolder(new Level2Holder(this));

        Level2Holder.IconTreeItem node1_child2 = new Level2Holder.IconTreeItem();
        node1_child2.text="开关二";
        TreeNode node1_sw2 = new TreeNode(node1_child2).setViewHolder(new Level2Holder(this));


        node1.addChildren(node1_sw1, node1_sw2);






        AndroidTreeView tView = new AndroidTreeView(this, root);

        LinearLayout layout=(LinearLayout)this.findViewById(R.id.main);
        layout.addView(tView.getView());

    }
}
