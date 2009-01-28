package com.espressolabs.android.TextProto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

public class TextReceiver extends BroadcastReceiver {
	String intentName = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Run a flight check.
		if(!intent.getAction().equals(intentName)) {
			return;
		}
		// Get all SmsMessages in the intent.
		SmsMessage ss[] = getSmsMessages(intent);

		// Welcome aboard.  Let's run through each SmsMessage.
		for(int i = 0; i < ss.length; ++i) {
			// We want the address of the sender and the message body.
			String address = ss[i].getOriginatingAddress();
			String message = ss[i].getDisplayMessageBody();
			// Act on the message, if it's not empty.
			if(message != null && message.length() > 0) {
				Log.i("TextReceiver",  address + ": " + message);

				// Pass the message along to the TextRouter.
				Intent newIntent = new Intent(context, TextRouter.class);
				newIntent.putExtra("type", "sms");
				newIntent.putExtra("sender", address);
				newIntent.putExtra("message", message);
				context.startService(newIntent);
			}
		}
	}

    private SmsMessage[] getSmsMessages(Intent intent) {
    	SmsMessage msg[] = null;
    	Bundle bundle = intent.getExtras();
    	try {
    		Object ps[] = (Object [])bundle.get("pdus");
    		msg = new SmsMessage[ps.length];
    		for(int i = 0; i < ps.length; ++i) {
    			byte[] data = (byte[])ps[i];
    			msg[i] = SmsMessage.createFromPdu(data);
    		}       

    	} catch(Exception e) {
    		Log.e("TextReceiver.getSmsMessages", "failed", e);
    	}
    	return msg;
    }
}
