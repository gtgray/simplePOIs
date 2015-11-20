package tk.atna.simplepois.stuff;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import tk.atna.simplepois.R;

public class GPServicesHelper {


    public static boolean checkGPServices(Activity context,
                                          OnCancelListener listener) {
        if (context != null) {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (status != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                    // Show error dialog/notification to allow
                    // user to download or enable gsm services
                    showGPServicesRecoverDialog(context, status, listener);
                } else {
                    // just show unable to use app dialog
                    showGPServicesErrorDialog(context, listener);
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean showGPServicesRecoverDialog(Activity context, int status,
                                                     OnCancelListener listener) {
        return GooglePlayServicesUtil.showErrorDialogFragment(status, context, null, 0, listener);
    }

    public static void showGPServicesErrorDialog(Activity context,
                                                 OnCancelListener listener) {
        Dialog dialog = getGPServicesErrorDialog(context, listener);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static Dialog getGPServicesErrorDialog(Activity context,
                                                  OnCancelListener listener) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.gpservices_error_title)
                .setMessage(R.string.gpservices_error_desc)
                .setCancelable(true)
                .setOnCancelListener(listener)
                .setNegativeButton(R.string.close, null)
                .create();
    }

}
