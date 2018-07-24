package me.franciscoigor.habits.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public abstract class ItemDialogFragment extends DialogFragment {

    private static final String ARG_ITEM = "item";
    public static final String EXTRA_MESSAGE = "item";
    DataModel item;


    public ItemDialogFragment() {

    }

    public void setItem(DataModel item) {
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(getDialogLayout(), null);

        bindDialog(item, v);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getDialogTitle(item))
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDialogResult(dialog, item);
                            }
                        })
                .create();


        return dialog;
    }

    public void onDialogResult(DialogInterface dialog, DataModel item) {
        sendResult(Activity.RESULT_OK, item);
    }

    protected abstract String getDialogTitle(DataModel item);

    protected abstract void bindDialog(DataModel item, View v);

    protected abstract int getDialogLayout();

    private void sendResult(int resultCode, DataModel item) {
        if (getTargetFragment() == null) {
            return;
        }
        System.out.println("Sending result");
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, item.getUUID());
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
