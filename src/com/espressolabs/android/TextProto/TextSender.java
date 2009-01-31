package com.espressolabs.android.TextProto;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.util.Log;

/**
 * Sends SMS messages. Use an Intent with the <code>startService</code> method.
 * Provide intent extras for 'address' and 'message' as Strings.
 * The given message can be any length. Messages exceeding the max SMS length
 * are sent in multipart.
 * 
 * Tells SmsManager to call TextCallback after handling the SMS message.
 * 
 * Service will fail if no address or message is given.
 */
public class TextSender extends Service {
    private final String tag = "TextSender"; // used for logs

    @Override
    public IBinder onBind(Intent intent) {
        // Everything is currently in onStart.
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // Get the address and message from the intent extras.
        Bundle extras = intent.getExtras();
        String address = extras.getString("address");
        String message = extras.getString("message");

        // Act on non-empty messages.
        if(message != null && message.length() > 0) {
            Log.i(tag,  address + ": " + message);

            // Get the default SmsManager from the system.
            SmsManager manager = SmsManager.getDefault();
            // Use sendMultipartTextMessage instead of sendTextMessage.
            // This supports messages of arbitrary length.
            // Before using sendMultipartTextMessage, use divideMessage.
            ArrayList<String> parts = manager.divideMessage(message);
            // Give SmsManager a useful intent to use after completion.
            // ... one for each part of the message.
            ArrayList<PendingIntent> pendingIntents = 
                new ArrayList<PendingIntent>();
            for(int i = 0; i < parts.size(); ++i) {
                // Use the TextCallback class for the PendingIntents.
                Intent newIntent = new Intent(this, TextCallback.class);
                newIntent.putExtra("address", address);
                newIntent.putExtra("message", parts.get(i));
                pendingIntents.add(PendingIntent.getBroadcast(this, 0,
                        newIntent, 0));
            }
            // sendMultipartTextMessage throws IllegalArgumentException
            try {
                // HERE Provide sentIntents, deliveryIntents, or both?
                manager.sendMultipartTextMessage(address, null, parts,
                        pendingIntents, null);
            } catch(Exception e) {
                Log.e(tag, "failed to send message", e);
            }
        }
        super.onStart(intent, startId);
    }
}
