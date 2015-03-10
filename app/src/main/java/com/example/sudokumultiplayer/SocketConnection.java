package com.example.sudokumultiplayer;

/**
 * Created by Gc on 2015-03-03.
 */

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketConnection extends Application{

    private static SocketConnection instance;
    private Activity activity;
    private String currentUsername;

    private static Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://104.131.185.217:4000");
//              mSocket = IO.socket("http://104.131.185.217:3000");

        } catch (URISyntaxException e) {}
    }

    public Socket getmSocket() {
        return mSocket;
    }

    protected SocketConnection() {
        // Exists only to defeat instantiation.
    }

    public static SocketConnection getInstance() {
        if(instance == null) {
            instance = new SocketConnection();
            mSocket.connect();
        }
        return instance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Emitter.Listener receiveTest = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    JSONObject data = (JSONObject) args[0];
                    JSONObject data = (JSONObject) args[0];
                    String result;
                    try {
                        result = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }
                    Log.d("socket", data.toString());
                }
            });
        }
    };

    public void sendData(String packetName, String value) {
        mSocket.emit(packetName, value);
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }
}
