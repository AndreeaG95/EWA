package com.andreea.ewa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentsFragment extends Fragment {

    private CalendarView calendarView;
    private Spinner timeSpinner;

    public AppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        calendarView = view.findViewById(R.id.simpleCalendarView);
        timeSpinner = view.findViewById(R.id.spinnerTime);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                List<String> timeslots = new ArrayList<String>();

                // TODO: need to check availible timeslots.
                timeslots.add("8:00");
                timeslots.add("9:00");
                timeslots.add("10:00");
                timeslots.add("11:00");
                timeslots.add("12:00");
                timeslots.add("13:00");

                final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, timeslots);
                sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                timeSpinner.setAdapter(sAdapter);
            }
        });
    }
}
