package org.yanhuang.mobile.device.profile;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.yanhuang.mobile.device.profile.util.Encodings;

import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {

	private static final int PERM_REQUEST_CODE = 101;
	private TextView oaidTv;
	private String oaid;
	private TextView imeiTv;
	private String imei;
	private TextView androidIdTv;
	private String androidId;
	private TextView debugMsgTv;
	private String oaIdSdkInitMsg;
	private boolean checkMd5 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ImageView droid = findViewById(R.id.droidImage);

		debugMsgTv = findViewById(R.id.debugMessage);
		debugMsgTv.setTextIsSelectable(true);

		oaidTv = findViewById(R.id.idOaid);
		oaidTv.setTextIsSelectable(true);
		final TextView oaidTypeTv = findViewById(R.id.idTypeOaid);
		oaidTypeTv.setOnClickListener(this::tapOAidLabel);

		imeiTv = findViewById(R.id.idImei);
		imeiTv.setTextIsSelectable(true);
		final TextView imeiTypeTv = findViewById(R.id.idTypeImei);
		imeiTypeTv.setOnClickListener(this::tapIMeiLabel);

		androidIdTv = findViewById(R.id.idAndroidId);
		androidIdTv.setTextIsSelectable(true);
		final TextView androidIdTypeTv = findViewById(R.id.idTypeAndroidId);
		androidIdTypeTv.setOnClickListener(this::tapAndroidIdLabel);

		final CheckBox includeMd5IdCb = findViewById(R.id.includeMd5Id);
		includeMd5IdCb.setOnCheckedChangeListener(this::tapMd5);

		//Define and attach click listener
		droid.setOnClickListener(v -> tapDroid());
	}

	private void tapDroid() {
		// oaid sdk
		readOaid();
		// imei
		readImei();
		// android id
		readAndroidId();
	}

	private void readAndroidId() {
		androidId = IMEIHelper.getAndroidId(this);
		setAndroidIdTvText();
	}

	private void readOaid() {
		OAIDHelper idsHelper = new OAIDHelper(sup -> {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException ignored) {
			}
			oaid = sup.getOAID();
			setOaidTvText();
			final String newMsg = oaIdSdkInitMsg + "\nsupport oaid: " + sup.isSupported();
			debugMsgTv.setText(newMsg);
		});
		idsHelper.triggerOAIDRead(getApplicationContext());
		oaIdSdkInitMsg = idsHelper.triggerOAIDRead(getApplicationContext());
		if (oaIdSdkInitMsg != null && oaIdSdkInitMsg.trim().length() > 0) {
			debugMsgTv.setText(oaIdSdkInitMsg);
		} else {
			debugMsgTv.setText(getString(R.string.hint_debug_message));
		}
	}

	private void readImei() {
		imei = IMEIHelper.getIMEI(this);
		setImeiTvText();
	}

	private void tapMd5(CompoundButton button, boolean isChecked) {
		checkMd5 = isChecked;
		setOaidTvText();
		setImeiTvText();
		setAndroidIdTvText();
	}

	private void setImeiTvText() {
		if (imei == null || imei.trim().length() == 0) {
			imeiTv.setText(getString(R.string.hint_empty_id));
		} else {
			final String showIds = checkMd5 ? (imei + "\n" + Encodings.md5(imei)) : imei;
			imeiTv.setText(showIds);
		}
	}

	private void setOaidTvText() {
		if (oaid == null || oaid.trim().length() == 0) {
			oaidTv.setText(getString(R.string.hint_empty_id));
		} else {
			final String showIds = checkMd5 ? (oaid + "\n" + Encodings.md5(oaid)) : oaid;
			oaidTv.setText(showIds);
		}
	}

	private void setAndroidIdTvText() {
		if (TextUtils.isEmpty(androidId)) {
			androidIdTv.setText(getString(R.string.hint_empty_id));
		} else {
			final String showIds = checkMd5 ? (androidId + "\n" + Encodings.md5(androidId)) : androidId;
			androidIdTv.setText(showIds);
		}
	}

	private void tapAndroidIdLabel(View v) {
		copyId("Android ID", androidIdTv);
	}

	private void tapOAidLabel(View v) {
		copyId("OAID", oaidTv);
	}

	private void tapIMeiLabel(View view) {
		copyId("IMEI", imeiTv);
	}

	private void copyId(String idType, TextView tv) {
		final Context context = getApplicationContext();
		ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(tv.getText());
		Toast.makeText(context, idType + " copied to clipboard", Toast.LENGTH_SHORT).show();
	}

}