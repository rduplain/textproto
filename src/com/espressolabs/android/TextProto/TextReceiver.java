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
		if(!intent.getAction().equals(intentName)) {
			return;
		}
		SmsMessage ss[] = getSmsMessages(intent);
        
		for(int i = 0; i < ss.length; ++i) {
			String message = ss[i].getDisplayMessageBody();
			if(message != null && message.length() > 0) {
				Log.i("TextReceiver:",  message);
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
