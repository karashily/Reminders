package com.karashily.apt_android_assi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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
    ListView remindersList;

    RemindersDbAdapter dbAdapter;
    ArrayList<Reminder> reminders = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new RemindersDbAdapter(this);
        setContentView(R.layout.activity_reminders);
        Cursor res = dbAdapter.fetchAllReminders();
        if(res.getCount() == 0) {
            // show message
            showMessage("Error","Nothing found");
        }

//        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
//            buffer.append("_id :"+ res.getInt(0)+"\n");
//            buffer.append("comment :"+ res.getString(1)+"\n");
//            buffer.append("important :"+ res.getInt(2)+"\n");
            reminders.add(new Reminder(res.getInt(0), res.getString(1), res.getInt(2)));
        }

        // Show all data
//        showMessage("Data",buffer.toString());


//
//        reminders.add(new Reminder(0, "Get Milk", 0));
//        reminders.add(new Reminder(1, "Study Arch", 1));
//        reminders.add(new Reminder(2, "Make the Assignment", 1));


        remindersList = (ListView) findViewById(R.id.reminders_list);
        RemindersAdapter remindersAdapter = new RemindersAdapter(this, reminders);
        remindersList.setAdapter(remindersAdapter);

        remindersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(position);
            }
        });


    }

    public void showMessage(String title,String Message){
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
                        .setTitle("Exiting App")
                        .setMessage("Are you sure you want to exit the app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
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
        if(type.equals("Edit")) {
            // TODO: Edit Reminder
            dbAdapter.updateReminder(reminder);


        } else {
            if(!reminder.getContent().equals("")) {
                dbAdapter.createReminder(reminder.getContent(), reminder.getImportant());
                // TODO: Add reminder to reminders
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
        ArrayAdapter<String> dialogListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dialogListOptions);

        ListView dialogList = view.findViewById(R.id.dialog_list);
        dialogList.setAdapter(dialogListAdapter);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    openEditDialog(reminders.get(itemPosition));
                    dialog.dismiss();
                } else if(position == 1) {
                    // TODO: delete the reminder at itemPosition
                    dbAdapter.deleteReminderById(((int) id));
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }
}
