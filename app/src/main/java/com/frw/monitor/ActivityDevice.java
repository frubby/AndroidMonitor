package com.frw.monitor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ActivityDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);


        LineChart chart = (LineChart) findViewById(R.id.chart);


        List<Entry> entries = new ArrayList<Entry>();

        Random rd = new Random();
        for (int i = 0; i < 30; i++) {

            // turn your data into Entry objects
            entries.add(new Entry(i, rd.nextInt(10)));

        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(0x0000FF00);
        dataSet.setValueTextColor(0x00FFFF00); // styling, ...

        LineData lineData = new LineData(dataSet);
         chart.setData(lineData);
        chart.invalidate(); // refresh
    }


}


//
//    List<PointValue> values_ia = new ArrayList<PointValue>();
//    Random rd=new Random();
//for(int i=0;i<30;i++) {
//        values_ia.add(new PointValue(i, rd.nextInt(100)));
//        }
//
//        List<PointValue> values_ib = new ArrayList<PointValue>();
//        for(int i=0;i<30;i++) {
//        values_ib.add(new PointValue(i, rd.nextInt(100)));
//        }
//
//        List<PointValue> values_ic = new ArrayList<PointValue>();
//        for(int i=0;i<30;i++) {
//        values_ic.add(new PointValue(i, rd.nextInt(100)));
//        }
//
//        //In most cased you can call data model methods in builder-pattern-like manner.
//        Line line_ia = new Line(values_ia).setColor(Color.RED).setCubic(false);
//        Line line_ib = new Line(values_ib).setColor(Color.GREEN).setCubic(false);
//
//        Line line_ic = new Line(values_ic).setColor(Color.BLUE).setCubic(false);
//
//        List<Line> lines = new ArrayList<Line>();
//        lines.add(line_ia);
//        lines.add(line_ib);
//        lines.add(line_ic);
//
//
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//        data.setAxisXBottom(new Axis());
//        data.setAxisYLeft(new Axis());
//
//
//
//        LineChartView chart = (LineChartView)this.findViewById(R.id.device_chart);
//        chart.setLineChartData(data);
