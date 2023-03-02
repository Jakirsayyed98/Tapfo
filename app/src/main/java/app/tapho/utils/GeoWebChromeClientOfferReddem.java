package app.tapho.utils;

import static app.tapho.utils.ConstantsKt.REQUEST_FINE_LOCATION;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import app.tapho.ui.WebViewActivity;
import app.tapho.ui.home.OfferRedeemWebViewActivity;

public class GeoWebChromeClientOfferReddem extends WebChromeClient {
    private  OfferRedeemWebViewActivity activity;


    public GeoWebChromeClientOfferReddem(OfferRedeemWebViewActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        // Geolocation permissions coming from this app's Manifest will only be valid for devices with
        // API_VERSION < 23. On API 23 and above, we must check for permissions, and possibly
        // ask for them.
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

