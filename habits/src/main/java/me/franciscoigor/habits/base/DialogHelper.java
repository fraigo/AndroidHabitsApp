package me.franciscoigor.habits.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import me.franciscoigor.habits.R;

/**
 * DialogHelper
 *
 * Basic helper to create specific dialogs for the application
 */
public class DialogHelper {


    public static void confirmDialog(Activity activity, String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(activity)
                .setTitle(R.string.confirmation)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.no, null).show();
    }

    protected void snackbarMessage(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


}
