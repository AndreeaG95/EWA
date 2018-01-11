package com.andreea.ewa;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrei on 1/11/2018.
 */

public class DateValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
    Date dt = new Date(((long) value) * 1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        return sdfDate.format(dt);

    }

}
