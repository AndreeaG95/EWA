package com.andreea.ewa.medicine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andreea.ewa.AppState;
import com.andreea.ewa.R;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrei on 1/11/2018.
 */

public class MedicineAdapter extends ArrayAdapter<Medicine> {
    private Context context;
    private List<Medicine> medicines;
    private int layoutResID;

    public MedicineAdapter(Context context, int layoutResourceID, List<Medicine> medicines) {
        super(context, layoutResourceID, medicines);
        this.context = context;
        this.medicines = medicines;
        this.layoutResID = layoutResourceID;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tIndex = (TextView) view.findViewById(R.id.tIndex);
            itemHolder.tName = (TextView) view.findViewById(R.id.tName);
            itemHolder.lHeader = (RelativeLayout) view.findViewById(R.id.lHeader);
            itemHolder.tDate = (TextView) view.findViewById(R.id.tDate);
            itemHolder.tTime = (TextView) view.findViewById(R.id.tTime);
            itemHolder.tNext = (TextView) view.findViewById(R.id.tNext);
            itemHolder.tNextTime = (TextView) view.findViewById(R.id.tNextTime);
            itemHolder.imageButton = view.findViewById(R.id.imageButton);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final Medicine hItem = medicines.get(position);

        itemHolder.tIndex.setText(String.valueOf(position + 1));
        itemHolder.tName.setText(hItem.getName());
        //itemHolder.lHeader.setBackgroundColor(PaymentType.getColorFromPaymentType(hItem.getType()));
        itemHolder.lHeader.setBackgroundColor(Color.rgb(50, 150, 50));

        itemHolder.imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("Took medicine at position" + position);
                DatabaseReference databaseReference = AppState.get().getDatabaseReference();
                DatabaseReference newRef = databaseReference.child("Patients").child(AppState.getUserId()).child("Medicine").child(medicines.get(position).getName()).child("last_ts");
                newRef.setValue(new Date().getTime());
            }
        });

        long last = hItem.getLast();
        long next = hItem.getNext();

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date l = new Date(last);


        Date n = new Date(next);
        String lastDate = sdfDate.format(l);
        String nextDate = sdfDate.format(n);


        itemHolder.tDate.setText("Last: " + lastDate.substring(0, 10));
        itemHolder.tTime.setText(lastDate.substring(11));
        itemHolder.tNext.setText("Next: " + nextDate.substring(0, 10));
        itemHolder.tNextTime.setText(nextDate.substring(11));

        return view;
    }

    private static class ItemHolder {
        TextView tIndex;
        TextView tName;
        RelativeLayout lHeader;
        TextView tDate, tTime;
        TextView tNext, tNextTime;
        ImageButton imageButton;
    }


}
