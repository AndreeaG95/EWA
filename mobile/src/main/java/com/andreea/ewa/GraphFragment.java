package com.andreea.ewa;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    private LineChart mChart;
    private int mFillColor = Color.argb(150, 51, 181, 229);

    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mChart = (LineChart) view.findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setGridBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);

        mChart.setDrawBorders(true);
        mChart.canScrollVertically(30);

        // description text
        mChart.getDescription().setEnabled(true);
        Description des = new Description();
        des.setText("Temperature");
        mChart.setDescription(des);

        // if disabled, scaling can be done on x- and y-axis separately
        //mChart.setPinchZoom(false);


        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setEnabled(false);
        xAxis.setValueFormatter(new DateValueFormatter());
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(true);



        YAxis leftAxis = mChart.getAxisLeft();

        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(20f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        //leftAxis.setDrawGridLines(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.setVisibleXRange(1000.0f, 2000.0f);
        // add data
        if(MainActivity.getTemperatures().isEmpty()){
            Toast.makeText(view.getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }else {
            setData(MainActivity.getTemperatures().size());
        }
        mChart.invalidate();
    }

    public void setData(int count){
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        Log.d("SETDATA", String.valueOf(count));
        long firstTS = 0;

        for (int i = 0; i < count; i++) {
            if (i + 10 < count)
                continue;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Temperature t = MainActivity.getTemperatures().get(i);
            long timestamp = 0;
            try {
                Date date = df.parse(t.getDate());
                timestamp = date.getTime();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if (i == 0)
                firstTS = timestamp;
            float val = (float) t.getValue();
            System.out.println(timestamp);
            System.out.println((float)((timestamp - firstTS))/(1000.0f));
            yVals1.add(new Entry(timestamp / 1000, val));
        }


        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {

            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(R.color.primaryColor);
            set1.setDrawCircles(false);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
           // set1.setFillAlpha(255);
            //set1.setDrawFilled(true);
            //set1.setFillColor(Color.WHITE);

            set1.setHighLightColor(R.color.primaryColor);
            set1.setDrawCircleHole(true);
            set1.setFillFormatter(new IFillFormatter() {

                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return mChart.getAxisLeft().getAxisMinimum();

                }

            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            // set data
            mChart.setData(data);

        }
    }



}
