package com.espressolabs.android.TextProto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TextCallback extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String address = extras.getString("address");
		String message = extras.getString("message");

		Log.i("TextCallback", address + ": " + message);
	}

}
