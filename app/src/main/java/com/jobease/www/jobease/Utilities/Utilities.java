package com.jobease.www.jobease.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created by LeGenD on 4/10/2015.
 */
public class Utilities extends Activity {


    public static String getDoubleWithTwoDecimals(double d) {
        NumberFormat my = NumberFormat.getInstance(Locale.US);
        my.setMaximumFractionDigits(2);
        my.setMinimumFractionDigits(2);
        return my.format(d);
    }

    public void setStatusBarColor(int color, Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    /*
does this app exists on Phone*/
    public static boolean appInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /*Dial phone Number*/
    public static void dialNumber(Context context, String phoneNumber) {
        String phone = "tel:" + phoneNumber;
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
        context.startActivity(i);
    }



    public static void shareApp(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey am using Mazzad Online" + context.getPackageName() + "\n"
                + "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(intent, "Share MazzadOnline with friends"));
    }

/*
   Share the App
*/



    /*
   copy something to clipBoard
*/

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }


    public static void openMapIntentWithMarker(Activity context, String name, String latitude, String longetude){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longetude
                +"?q="+latitude+","+longetude+"(Label"+name+")"));
        context.startActivity(intent);
    }



    public static AlertDialog createAlertDialogue(Context context, String Title, String Message,
                                                  String negativeBtnText,
                                                  DialogInterface.OnClickListener negativeOnClickListener
            , String positiveBtnText, DialogInterface.OnClickListener positiveOnClickListener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(Title);
        alertDialog.setMessage(Message);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeBtnText, negativeOnClickListener);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveBtnText, positiveOnClickListener);

        alertDialog.show();
        return alertDialog;
    }




}
