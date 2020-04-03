package com.karashily.apt_android_assi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    ListView mRemindersList;
    // create the cursor
    Cursor mCursor;
    // create adapter to connect with the database
    RemindersDbAdapter mDbAdapter;
    // create Cursor adapter to refresh data automatically when Add, Update, Delete.
    RemindersSimpleCursorAdapter mRemindersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the adapter
        mDbAdapter = new RemindersDbAdapter(this);
        // this function will get any writable database to connect on it to be able to write or read from it.
        mDbAdapter.open();
        // will initialize the cursor adapter with
        // 1- the context => this
        // 2- the layout which will write the data on it => reminders_row
        // 3- array contain the columns name where i must read my data respectively.
        //4- array contains things will hold the data from the cursor respectively text, color.
        mRemindersAdapter = new RemindersSimpleCursorAdapter(this,
                R.layout.reminders_row, mCursor, new String[]{RemindersDbAdapter.COL_CONTENT,
                RemindersDbAdapter.COL_IMPORTANT}, new int[]{R.id.text, R.id.color}, 0);
        setContentView(R.layout.activity_reminders);

        updateReminders();

        mRemindersList = (ListView) findViewById(R.id.reminders_list);
        mRemindersList.setAdapter(mRemindersAdapter);
        mRemindersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(position);
            }
        });

    }

    // here we fetch the data from the database and save them back in the cursor
    // change the old cursor with the new one i got from fetchAllReminders
    private void updateReminders() {
        mCursor = mDbAdapter.fetchAllReminders();
        mRemindersAdapter.changeCursor(mCursor);
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminder:
                openNewDialog();
                return true;
            case R.id.exit:
                new AlertDialog.Builder(this)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit the app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openNewDialog() {
        CustomDialog newReminderDialog = new CustomDialog(null);
        newReminderDialog.show(getSupportFragmentManager(), "New Reminder");
    }

    public void openEditDialog(Reminder reminder) {
        CustomDialog newReminderDialog = new CustomDialog(reminder);
        newReminderDialog.show(getSupportFragmentManager(), "Edit Reminder");
    }


    @Override
    public void onCommit(String type, Reminder reminder) {
        if (type.equals("Edit")) {
            // here we can update the database
            mDbAdapter.updateReminder(reminder);
            // then change the cursor to refresh the layout with the new data.
            updateReminders();
        } else {
            if (!reminder.getContent().equals("")) {
                // here we can create a new record in the database
                mDbAdapter.createReminder(reminder.getContent(), reminder.getImportant());
                // then change the cursor to refresh the layout with the new data.
                updateReminders();
            }
        }

    }

    public void onClick(final int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_delete, null);

        ArrayList<String> dialogListOptions = new ArrayList<>();
        dialogListOptions.add("Edit Reminder");
        dialogListOptions.add("Delete Reminder");
        ArrayAdapter<String> dialogListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dialogListOptions);

        ListView dialogList = view.findViewById(R.id.dialog_list);
        dialogList.setAdapter(dialogListAdapter);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the current cursor on the list which point to the first element on the database
                Cursor c = ((RemindersSimpleCursorAdapter) mRemindersList.getAdapter()).getCursor();
                // move it to the position where the user touch to get a life data from the database to be able to
                // update it or delete it.
                c.moveToPosition(itemPosition);
                if (position == 0) {
                    openEditDialog(new Reminder(c.getInt(0), c.getString(1), c.getInt(2)));
                    dialog.dismiss();
                } else if (position == 1) {
                    mDbAdapter.deleteReminderById(c.getInt(0));
                    // then change the cursor to refresh the layout with the new data.
                    updateReminders();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }
}
