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
    private EditText mReminderTextEditText;
    private CheckBox mIsImportantCheckBox;

    private String mDialogTitle;
    private String mNegativeButtonText;
    private String mPositiveButtonText;
    private String mType;
    private Reminder mReminder;

    private CustomDialogListener mListener;

    public CustomDialog(Reminder reminder) {
        super();
        if (reminder == null) {
            mReminder = new Reminder(-1, "", 0);
            this.mDialogTitle = "New Reminder";
            this.mPositiveButtonText = "ADD";
            this.mType = "New";
        } else {
            mReminder = reminder;
            this.mDialogTitle = "Edit Reminder";
            this.mPositiveButtonText = "Edit";
            this.mType = "Edit";
        }
        this.mNegativeButtonText = "Cancel";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (CustomDialogListener) context;
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

        mReminderTextEditText = view.findViewById(R.id.reminder_text);
        mIsImportantCheckBox = view.findViewById(R.id.is_important);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button commitButton = view.findViewById(R.id.commit_button);
        TextView dialogTitleTextView = view.findViewById(R.id.dialog_title);

        dialogTitleTextView.setText(mDialogTitle);
        mReminderTextEditText.setText(mReminder.getContent());
        if (mReminder.getImportant() == 1) {
            mIsImportantCheckBox.setChecked(true);
        } else {
            mIsImportantCheckBox.setChecked(false);
        }
        cancelButton.setText(mNegativeButtonText);
        commitButton.setText(mPositiveButtonText);


        final AlertDialog dialog = builder.create();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminder.setContent(mReminderTextEditText.getText().toString());
                if (mIsImportantCheckBox.isChecked()) {
                    mReminder.setImportant(1);
                } else {
                    mReminder.setImportant(0);
                }
                mListener.onCommit(mType, mReminder);
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
