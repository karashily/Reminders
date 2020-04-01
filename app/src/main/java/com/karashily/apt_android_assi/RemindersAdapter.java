package com.karashily.apt_android_assi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RemindersAdapter extends ArrayAdapter<Reminder> {

    public RemindersAdapter(@NonNull Context context, @NonNull List<Reminder> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.reminders_row, parent, false);
        }

        Reminder currentReminder = getItem(position);

        Log.d("SSAS", position+"");
        Log.d("SSAS", currentReminder.getContent().toString());

        TextView reminderTextTextView = (TextView) listItemView.findViewById(R.id.text);
        reminderTextTextView.setText(currentReminder.getContent());

        View isImportantColor = (View) listItemView.findViewById(R.id.color);
        if(currentReminder.getImportant() == 1) {
            isImportantColor.setBackgroundColor(listItemView.getResources().getColor(android.R.color.holo_orange_light));
        } else {
            isImportantColor.setBackgroundColor(listItemView.getResources().getColor(android.R.color.darker_gray));
        }

        // TODO: set onItemClickListener

        return listItemView;
    }
}
