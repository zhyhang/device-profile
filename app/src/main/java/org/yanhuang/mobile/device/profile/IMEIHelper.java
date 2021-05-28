package org.yanhuang.mobile.device.profile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class IMEIHelper {

	private static final int PERM_REQUEST_CODE = 101;

	public static String getIMEI(AppCompatActivity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			// not allow to acquire the device id since android 10 i.e. Q
			return "";
		}
		TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		// IMEI read permission request
		if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERM_REQUEST_CODE);
			return "";
		}
		try {
			return manager.getDeviceId();
		} catch (Exception ignored) {
			return "";
		}
	}

	public static String getAndroidId(AppCompatActivity activity) {
		return Settings.System.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

}