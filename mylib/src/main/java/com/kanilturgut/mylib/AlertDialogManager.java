package com.kanilturgut.mylib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Author   : kanilturgut
 * Date     : 05/05/14
 * Time     : 10:23
 */
public class AlertDialogManager {

    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     *
     */
    public static void showAlertDialog(Context context, String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



    /**
     * Function to display simple Alert Dialog and exits from application
     *
     * @param context - application context
     *
     */
    public static void noInternetConnection(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.no_internet_title));

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.app_needs_internet_connection));

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                System.exit(0);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
