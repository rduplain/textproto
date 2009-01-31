package com.espressolabs.android.TextProto;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Provides a hub for messages.
 * Use an Intent with the <code>startService</code> method.
 * Provide intent extras for 'sender', 'address', and 'message' as Strings.
 * 
 * sender: message origin
 * address: message destination
 * message: message content
 * 
 * If address is empty, sender is used as the address.
 * 
 * This class should be modified to match the needs of its enclosing project.
 * As is, this class provides a demo and an entry point for managing message.
 * Once called, a message is given to TextSender based on a hard-coded rule.
 */
public class TextRouter extends Service {
    private final String tag = "TextRouter"; // used for logs

    @Override
    public IBinder onBind(Intent intent) {
        // Everything is currently in onStart.
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // Get the sender and message from the intent extras.
        Bundle extras = intent.getExtras();
        String sender = extras.getString("sender");
        String address = extras.getString("address");
        String message = extras.getString("message");

        if(address == null || address.length() <= 0) {
            address = sender;
        }

        // Act on non-empty messages.
        if(message != null && message.length() > 0) {
            // HERE Do something interesting with the message.
            // HERE Replace this block of code with respect to project needs.

            // For now, pass the message along to the TextSender.
            // For a demo, simply log the message and send it a test phone.
            Log.i(tag,  address + ": " + message);

            // Send the message out if it matches the hardcoded number below.
            // Replace xxxyyyzzzz with number of a spare SMS-capable phone.
            // Prepend message with some text, especially to verify multipart.
            // i.e. send a max length message from test phone, receive 2 SMSs.
            // NOTE SMS charges will apply.
            if(address.endsWith("xxxyyyzzzz")) {
                Log.i(tag,  "send message to " + address);
                message = "(Test) " + sender + " said: " + message;

                // TextSender requires intent extras 'address' and 'message'.
                Intent newIntent = new Intent(this, TextSender.class);
                newIntent.putExtra("address", address);
                newIntent.putExtra("message", message);
                this.startService(newIntent);
            } else {
                // Log that we're doing nothing, for clarity in the log.
                Log.i(tag, "doing nothing with message from " + address);
            }
        }
        super.onStart(intent, startId);
    }
}
