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
    private String reminderText;
    private boolean isImportant;
    private String negativeButtonText;
    private String postiveButtonText;

    private CustomDialogListener listener;

    public CustomDialog(String dialogTitle, String reminderText, boolean isImportant, String negativeButtonText, String postiveButtonText) {
        super();
        this.dialogTitle = dialogTitle;
        this.reminderText = reminderText;
        this.isImportant = isImportant;
        this.negativeButtonText = negativeButtonText;
        this.postiveButtonText = postiveButtonText;
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
        reminderTextEditText.setText(reminderText);
        isImportantCheckBox.setChecked(isImportant);
        cancelButton.setText(negativeButtonText);
        commitButton.setText(postiveButtonText);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status;
                if(isImportant) {
                    status = 1;
                } else {
                    status = 0;
                }
                listener.onCommit(reminderText, status);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return builder.create();
    }

    public interface CustomDialogListener {
        void onCommit(String reminderText, int isImportant);
    }
}
