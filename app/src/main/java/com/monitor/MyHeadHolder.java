package com.monitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.frw.monitor.R;
import com.unnamed.b.atv.model.TreeNode;



public class MyHeadHolder extends TreeNode.BaseNodeViewHolder<MyHeadHolder.IconTreeItem> {

    public MyHeadHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_header_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        return view;
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
    }
}
