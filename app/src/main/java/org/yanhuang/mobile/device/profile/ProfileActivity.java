package org.yanhuang.mobile.device.profile;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.yanhuang.mobile.device.profile.util.Encodings;

import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {

	private TextView oaidTv;
	private String oaid;
	private TextView debugMsgTv;
	private String idSdkInitMsg;
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

		final CheckBox includeMd5IdCb = findViewById(R.id.includeMd5Id);
		includeMd5IdCb.setOnCheckedChangeListener(this::tapMd5);

		final TextView oaidTypeTv = findViewById(R.id.idTypeOaid);
		oaidTypeTv.setOnClickListener(this::tapOAidLabel);

		//Define and attach click listener
		droid.setOnClickListener(v -> tapDroid());
	}

	private void tapDroid() {
		//device sdk
		DeviceIdHelper idsHelper = new DeviceIdHelper(sup -> {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException ignored) {
			}
			oaid = sup.getOAID();
			setOaidTvText();
			final String newMsg = idSdkInitMsg + "\nsupport oaid: " + sup.isSupported();
			debugMsgTv.setText(newMsg);
		});
		idsHelper.getDeviceIds(getApplicationContext());
		idSdkInitMsg = idsHelper.getDeviceIds(getApplicationContext());
		if (idSdkInitMsg != null && idSdkInitMsg.trim().length() > 0) {
			debugMsgTv.setText(idSdkInitMsg);
		} else {
			debugMsgTv.setText(getString(R.string.hint_debug_message));
		}
	}

	private void tapMd5(CompoundButton button, boolean isChecked){
		checkMd5 = isChecked;
		setOaidTvText();
	}

	private void setOaidTvText() {
		if (oaid == null || oaid.trim().length() == 0) {
			oaidTv.setText(getString(R.string.hint_empty_id));
		}else{
			final String showIds = checkMd5 ? (oaid + "\n" + Encodings.md5(oaid)) : oaid;
			oaidTv.setText(showIds);
		}
	}

	private void tapOAidLabel(View v) {
		final Context context = getApplicationContext();
		ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(oaidTv.getText());
		Toast.makeText(context, "OAID copied to clipboard", Toast.LENGTH_SHORT).show();
	}

}