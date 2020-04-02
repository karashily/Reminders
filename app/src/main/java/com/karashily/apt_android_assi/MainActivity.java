package com.karashily.apt_android_assi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
        setContentView(R.layout.activity_reminders);


        reminders.add(new Reminder(0, "Get Milk", 0));
        reminders.add(new Reminder(1, "Study Arch", 1));
        reminders.add(new Reminder(2, "Make the Assignment", 1));


        remindersList = (ListView) findViewById(R.id.reminders_list);
        RemindersAdapter remindersAdapter = new RemindersAdapter(this, reminders);
        remindersList.setAdapter(remindersAdapter);

        remindersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(position);
            }
        });

        dbAdapter = new RemindersDbAdapter(this);
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
                android.os.Process.killProcess(android.os.Process.myPid());
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
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    openEditDialog(reminders.get(itemPosition));
                } else if(position == 1) {
                    // TODO: delete the reminder at itemPosition
                }
            }
        });

        builder.setView(view);
        builder.create().show();

    }
}
