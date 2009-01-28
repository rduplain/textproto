package com.espressolabs.android.TextProto;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.util.Log;

public class TextSender extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Bundle extras = intent.getExtras();
		String address = extras.getString("address");
		String message = extras.getString("message");

		// Act on the message, if it's not empty.
		if(message != null && message.length() > 0) {
			Log.i("TextSender",  address + ": " + message);

			// Test with a loopback, based on target.
			// Replace xxxyyyzzzz with your spare SMS-capable phone.
			// Note: you'll have to pay SMS charges.
			if(address.endsWith("xxxyyyzzzz")) {
				Log.i("TextSender",  "echo message to " + address);

				SmsManager manager = SmsManager.getDefault();
				// Use sendMultipartTextMessage instead of sendTextMessage.
				// This supports messages of arbitrary length.
				ArrayList<String> parts = manager.divideMessage(message);
				// Give SmsManager a useful intent to send upon delivery.
				// ... one for each part of the message.
				ArrayList<PendingIntent> pendingIntents = 
					new ArrayList<PendingIntent>();
				for(int i = 0; i < parts.size(); ++i) {
					Intent newIntent = new Intent(this, TextCallback.class);
					newIntent.putExtra("address", address);
					newIntent.putExtra("message", parts.get(i));
					pendingIntents.add(PendingIntent.getBroadcast(this, 0,
							newIntent, 0));
				}
				// sentIntents, deliveryIntents, or both?
				manager.sendMultipartTextMessage(address, null, parts,
						pendingIntents, null);
			}
		}
		super.onStart(intent, startId);
	}
}
