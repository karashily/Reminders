package com.karashily.apt_android_assi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomDialog extends AppCompatDialogFragment {
    private EditText reminderTextEditText;
    private CheckBox isImportantCheckBox;
    private Button cancelButton;
    private Button commitButton;
    private TextView dialogTitleTextView;

    private String dialogTitle;
    private String negativeButtonText;
    private String postiveButtonText;
    private String type;
    private Reminder mReminder;

    private CustomDialogListener listener;

    public CustomDialog(Reminder reminder) {
        super();
        if (reminder == null) {
            mReminder = new Reminder(-1, "", 0);
            this.dialogTitle = "New Reminder";
            this.postiveButtonText = "ADD";
            this.type = "New";
        } else {
            mReminder = reminder;
            this.dialogTitle = "Edit Reminder";
            this.postiveButtonText = "Edit";
            this.type = "Edit";
        }
        this.negativeButtonText = "Cancel";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CustomDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom, null);
        builder.setView(view);

        reminderTextEditText = view.findViewById(R.id.reminder_text);
        isImportantCheckBox = view.findViewById(R.id.is_important);
        cancelButton = view.findViewById(R.id.cancel_button);
        commitButton = view.findViewById(R.id.commit_button);
        dialogTitleTextView = view.findViewById(R.id.dialog_title);

        dialogTitleTextView.setText(dialogTitle);
        reminderTextEditText.setText(mReminder.getContent());
        if (mReminder.getImportant() == 1) {
            isImportantCheckBox.setChecked(true);
        } else {
            isImportantCheckBox.setChecked(false);
        }
        cancelButton.setText(negativeButtonText);
        commitButton.setText(postiveButtonText);


        final AlertDialog dialog = builder.create();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminder.setContent(reminderTextEditText.getText().toString());
                if (isImportantCheckBox.isChecked()) {
                    mReminder.setImportant(1);
                } else {
                    mReminder.setImportant(0);
                }
                listener.onCommit(type, mReminder);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface CustomDialogListener {
        void onCommit(String type, Reminder reminder);
    }
}
