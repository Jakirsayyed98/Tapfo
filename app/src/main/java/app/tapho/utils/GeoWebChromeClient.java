package app.tapho.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import app.tapho.ui.WebViewActivity;


import static app.tapho.utils.ConstantsKt.REQUEST_FINE_LOCATION;

public class GeoWebChromeClient extends WebChromeClient {
    private  WebViewActivity activity;


    public GeoWebChromeClient(WebViewActivity activity) {
        this.activity = activity;
    }



    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        String perm = Manifest.permission.ACCESS_FINE_LOCATION;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(activity, perm) == PackageManager.PERMISSION_GRANTED) {
            // we're on SDK < 23 OR user has already granted permission
            callback.invoke(origin, true, false);
        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
            // ask the user for permission
            ActivityCompat.requestPermissions(activity, new String[]{perm}, REQUEST_FINE_LOCATION);

            // we will use these when user responds


            activity.mGeolocationOrigin = origin;
            activity.mGeolocationCallback = callback;
//            }
        }

    }


}

