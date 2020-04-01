package com.karashily.apt_android_assi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    ListView remindersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder(0, "Get Milk", 0));
        reminders.add(new Reminder(1, "Study Arch", 1));
        reminders.add(new Reminder(2, "Make the Assignment", 1));


        remindersList = (ListView) findViewById(R.id.reminders_list);
        RemindersAdapter remindersAdapter = new RemindersAdapter(this, reminders);
        remindersList.setAdapter(remindersAdapter);
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
                openDialog();
                return true;
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openDialog() {
        CustomDialog newReminderDialog = new CustomDialog("New Reminder", "",
                false, "Cancel", "Add");
        newReminderDialog.show(getSupportFragmentManager(), "New Reminder");
    }

    @Override
    public void onCommit(String reminderText, boolean isImportant) {
        // TODO: Add the new reminder
    }

    @Override
    public void onDelete() {
    }
}
