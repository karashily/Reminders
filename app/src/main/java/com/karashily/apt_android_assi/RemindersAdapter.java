package com.karashily.apt_android_assi;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RemindersAdapter extends ArrayAdapter<Reminder> {


    public RemindersAdapter(@NonNull Context context, @NonNull List<Reminder> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.reminders_row, parent, false);
        }

        Reminder currentReminder = getItem(position);

        TextView reminderTextTextView = (TextView) listItemView.findViewById(R.id.text);
        reminderTextTextView.setText(currentReminder.getContent());

        View isImportantColor = (View) listItemView.findViewById(R.id.color);
        if(currentReminder.getImportant() == 1) {
            isImportantColor.setBackgroundColor(listItemView.getResources().getColor(android.R.color.holo_orange_light));
        } else {
            isImportantColor.setBackgroundColor(listItemView.getResources().getColor(android.R.color.darker_gray));
        }

        return listItemView;
    }


}
