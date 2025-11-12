package br.com.pdasolucoes.standardconfig.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {

    public static final int READ_PHONE_STATE_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;
    public static final int READ_EXTERNAL_STORAGE_REQUEST = 104;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 102;
    public static final int ACCESS_FINE_LOCATIONE_REQUEST = 103;
    private static final int RECORD_AUDIO_REQUEST = 105;


    private static boolean checkPermission(String permission, int requestId, Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{permission}, requestId);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkReadPhoneStatusPermission(Activity activity) {
        return checkPermission(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_REQUEST, activity);
    }

    public static boolean checkCameraPermission(Activity activity) {
        return checkPermission(Manifest.permission.CAMERA, CAMERA_REQUEST, activity);
    }

    public static boolean checkWriteExternalStoragePermission(Activity activity) {
        return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST, activity);
    }

    public static boolean checkReadExternalStoragePermission(Activity activity) {
        return checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_REQUEST, activity);
    }

    public static boolean checkAccessFineLocationPermission(Activity activity) {
        return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATIONE_REQUEST, activity);
    }

    public static boolean checkAccessRecordAudioPermission(Activity activity) {
        return checkPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_REQUEST, activity);
    }

}
