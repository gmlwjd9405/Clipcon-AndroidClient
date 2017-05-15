package com.sprout.clipcon.server;

import android.os.AsyncTask;
import android.util.Log;

import com.sprout.clipcon.model.Message;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.websocket.EncodeException;

/**
 * Created by delf on 17-05-06.
 */

public class EndpointInBackGround extends AsyncTask<String, Void, String> {
    private final static int TYPE = 0;
    private final static int GROUP_PK = 1;

    private BackgroundCallback backgroundCallback;

    public interface BackgroundCallback {
        void onSuccess(JSONObject result);
    }

    public EndpointInBackGround() {
    }

    public EndpointInBackGround(BackgroundCallback callback) {
        this.backgroundCallback = callback;
    }

    @Override
    protected String doInBackground(String... msg) {
        switch (msg[TYPE]) {
            case Message.CONNECT:
                Log.d("delf", "[CLIENT] connecting server...");
                Endpoint.getInstance();
                break;

            case Message.REQUEST_CREATE_GROUP:
                // Log.d("delf", "[CLIENT] send group create request to server. group pk is \"" + msg[GROUP_PK] + "\""); // XXX: caution!
                setCallBack();
                sendMessage(
                        new Message().setType(Message.REQUEST_CREATE_GROUP)
                );
                break;

            case Message.REQUEST_JOIN_GROUP:
                setCallBack();
                Log.d("delf", "[CLIENT] send group join request to server. group pk is \"" + msg[GROUP_PK] + "\"");
                sendMessage(
                        new Message().setType(Message.REQUEST_JOIN_GROUP)
                                .add(Message.GROUP_PK, msg[GROUP_PK]) // msg[1]: group key
                );
                break;

            case Message.UPLOAD:
                Log.d("delf", "[CLIENT] send upload request to server");
                Endpoint.getUploader().upload(msg[1]);
                break;

            case Message.DOWNLOAD:
                Log.d("delf", "[CLIENT] send download request to server");
                try {
                    Endpoint.getDownloader().requestDataDownload("1"); // for test
                } catch (MalformedURLException e) {
                    Log.e("delf", "[CLIENT] error at sending download request");
                    e.printStackTrace();
                }
                break;

            case Message.REQUEST_EXIT_GROUP:
                // TODO: 17-05-11 add callback(it's may not needed, but natural logic)
                Log.d("delf", "[CLIENT] send exit request to server");
                sendMessage(
                        new Message().setType(Message.REQUEST_EXIT_GROUP) // TODO: 17-05-11 maybe needed user's name
                );
                break;
            case "test":
                Log.d("delf", "send test request");
                sendMessage(
                    new Message().setType("test: hansung")
                );
                break;

            default:
                Log.d("delf", "do nothing in doInBackground()");
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private void sendMessage(Message message) {
        try {
            Endpoint.getInstance().sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    private void setCallBack() {
        final Endpoint.SecondCallback secondResult = new Endpoint.SecondCallback() {
            @Override
            // method name recommendation: onResponseAtEndpoint() // tmp
            public void onEndpointResponse(JSONObject responseFromServer) {
                backgroundCallback.onSuccess(responseFromServer); // call in MainActivity
            }
        };
        Endpoint.getInstance().setSecondCallback(secondResult);
    }
}
