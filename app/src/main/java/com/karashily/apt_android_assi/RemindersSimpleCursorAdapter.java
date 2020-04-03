package com.karashily.apt_android_assi;

import android.content.Context;
import android.database.Cursor;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

/**
 * Created by engMa_000 on 2017-04-03.
 */

public class RemindersSimpleCursorAdapter extends SimpleCursorAdapter {

    public RemindersSimpleCursorAdapter(Context context, int layout, Cursor c, String[]
            from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        View v = view.findViewById(R.id.color);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(cursor.getString(1));
        if (cursor.getInt(2) == 1) {
            v.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
        } else {
            v.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }
    }


}
