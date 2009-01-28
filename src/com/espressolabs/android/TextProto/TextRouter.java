package com.espressolabs.android.TextProto;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TextRouter extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Bundle extras = intent.getExtras();
		String sender = extras.getString("sender");
		String message = extras.getString("message");

		// Act on the message, if it's not empty.
		if(message != null && message.length() > 0) {
			Log.i("TextRouter",  sender + ": " + message);

			// For now, pass the message along to the TextSender.
			Intent newIntent = new Intent(this, TextSender.class);
			newIntent.putExtra("address", sender);
			newIntent.putExtra("message", message);
			this.startService(newIntent);
		}
		super.onStart(intent, startId);
	}
}
