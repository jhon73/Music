package com.musika.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.musika.R;
import com.musika.interfaces.OnDateDialogListener;
import com.musika.widget.CustomTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.onurciner.toastox.ToastOX;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;


/**
 * Created by Yudiz on 17/11/16.
 */
public class Utility {


    private static DialogFragment dateDialogFragment;
    public static long doubleTap = 0;

    public final static int CLICK_INTERVAL = 2000;
    public static int MAX_SELECTION;
    public static long doubleTapTime;
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static void navigationIntent(Context context, Class<?> classname) {
        Intent intent = new Intent(context, classname);
        context.startActivity(intent);
    }

    public static void navigationIntentWithFinishAll(Context context, Class<?> classname) {
        Intent intent = new Intent(context, classname);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void navigationIntentWithTransitionEffect(Context context, Class<?> classname, View... view) {
        Intent intent = new Intent(context, classname);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> pair[] = new Pair[view.length];
            for (int i = 0; i < view.length; i++) {
                pair[i] = new Pair<>(view[i], view[i].getTransitionName());
            }
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, pair);
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void loadImage(String url, ImageView imageView) {
        if (url == null || url.equals("")) {
            imageView.setImageResource(R.drawable.profile_placeholder);
            return;
        }
        if (imageView == null)
            return;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_placeholder)
                .showImageForEmptyUri(R.drawable.profile_placeholder)
                .showImageOnFail(R.drawable.profile_placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(url, imageView, options);
    }
    public static void loadImage(String url, ImageView imageView,int placeHolder) {
        if (url == null || url.equals("")) {
            imageView.setImageResource(placeHolder);
            return;
        }
        if (imageView == null)
            return;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeHolder)
                .showImageForEmptyUri(placeHolder)
                .showImageOnFail(placeHolder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(url, imageView, options);
    }
    public static void loadImage(String url,SimpleImageLoadingListener simpleImageLoadingListener) {
        ImageLoader.getInstance().loadImage(url, simpleImageLoadingListener);
    }
    public static Drawable createBlurredImageFromBitmap(Bitmap bitmap, Context context, int inSampleSize) {

        RenderScript rs = RenderScript.create(context);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        Bitmap blurTemplate = BitmapFactory.decodeStream(bis, null, options);

        final Allocation input = Allocation.createFromBitmap(rs, blurTemplate);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurTemplate);

        return new BitmapDrawable(context.getResources(), blurTemplate);
    }
    public static Calendar getFormatedDate(String dateString, String dateFormat) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            calendar.setTime(sdf.parse(dateString));
            return calendar;

        } catch (Exception e) {
//            e.printStackTrace();
            return calendar;
        }
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(Utility.DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public static void hideKeyboared(Activity activity) {
        if (activity == null)
            return;

        InputMethodManager imm = (InputMethodManager) activity.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(),
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static void log(String msg) {
        Log.i("SMUBU App", msg);
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showDateDialog(FragmentManager fm, int minDay, int minMonth, int minYear, int maxDay, int maxMonth, int maxYear, final OnDateDialogListener listener, int selectedYear, int selectedMonth, int selectedDay) {
        if (dateDialogFragment == null || !dateDialogFragment.isVisible()) {
            DatePickerDialog.Builder builder = new DatePickerDialog.Builder(R.style.AppThemePrimary, minDay, minMonth, minYear, maxDay, maxMonth, maxYear, selectedDay, selectedMonth - 1, selectedYear) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                    listener.onPositiveActionClicked(dialog);
                    super.onPositiveActionClicked(fragment);
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                }
            };
            builder.positiveAction("OK").negativeAction("CANCEL");
            dateDialogFragment = DialogFragment.newInstance(builder);
            dateDialogFragment.show(fm, "datePicker");
        }
    }

    /**
     * Checks if is GPS enable.
     *
     * @param context the context
     * @return true, if is GPS enable
     */
    public static boolean isGPSEnable(Context context) {
        @SuppressWarnings("static-access")
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return false;
        else
            return true;
    }


//    public static void GpsEnableDialog(final Context context) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                context);
//        alertDialogBuilder.setTitle(context.getString(R.string.app_alert_text));
//        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
//
//        alertDialogBuilder
//                .setMessage(context.getString(R.string.enable_gps))
//                .setCancelable(false)
//                .setPositiveButton(context.getString(R.string.yes_text),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                context.startActivity(new Intent(action));
//                                dialog.dismiss();
//                            }
//                        })
//                .setNegativeButton(context.getString(R.string.no_text),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                            }
//                        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

    @SuppressLint("LongLogTag")
    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                    Log.w("My Current loction address", "" + strReturnedAddress.toString());
                } else {
                    Log.w("My Current loction address", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("My Current loction address", "Canont get Address!");
            }

        } catch (Exception ae) {
            ae.printStackTrace();
        }
        return strAdd;

    }


    /**
     * Gets the location.
     *
     * @param context the context
     * @return the location
     */
    public static Location getLocation(Context context) {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled)
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
        gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null) {
            finalLoc = gps_loc;
        } else if (net_loc != null) {
            finalLoc = net_loc;
        }

        return finalLoc;
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public static void showRedSnackBar(Activity activity, String text) {
        try {
            if (activity != null) {


                View view = activity.getLayoutInflater().inflate(R.layout.row_error_message, null);
                CustomTextView customTextView = (CustomTextView) view.findViewById(R.id.row_tv_sucess_message);
                customTextView.setText(text);
                Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
                Crouton.make(activity, view).setConfiguration(croutonConfiguration).show();
            }
            }
        catch ( Exception ae){
            ae.printStackTrace();
        }

    }



    public static void showErrorSnackBar(View view, String text) {
        try {
            Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.btn_red));
            sb.show();
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }




    public static Calendar getProperCalendar(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Calendar calendarDate = Calendar.getInstance();
        if (calendarDate != null) {
            try {
                calendarDate.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendarDate;
    }
}
