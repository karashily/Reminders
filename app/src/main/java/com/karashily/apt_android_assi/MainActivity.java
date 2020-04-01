package com.karashily.apt_android_assi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    ListView remindersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        remindersList = (ListView) findViewById(R.id.reminders_list);

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
