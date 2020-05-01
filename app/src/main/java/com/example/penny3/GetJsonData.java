package com.example.penny3;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class GetJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetJsonData";

    private List<Photo> mPhotosList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetJsonData(OnDataAvailable callback, String baseURL, String language, boolean matchAll) {
        Log.d(TAG, "GetJsonData: JsonData called ");
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallback = callback;
    }
    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if(mCallback != null) {
            mCallback.onDataAvailable(mPhotosList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotosList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll) {
        Log.d(TAG, "createUri: starts");

        return Uri.parse(mBaseURL).buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: Status = " + status);

        if(status == DownloadStatus.OK) {
            mPhotosList = new ArrayList<>();
            try {
                JSONArray itemsArray = new JSONArray(data);

                for(int i=0; i<itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    if(jsonPhoto.has("colour")) {
                        String sender = jsonPhoto.getString("sender");
                        String message = null;
                        String colour = jsonPhoto.getString("colour");
                        String room = jsonPhoto.getString("room");

                        Photo photoObject = new Photo(sender, message, colour, room);
                        mPhotosList.add(photoObject);

                        Log.d(TAG, "onDownloadComplete " + photoObject.toString());
                    } else if (jsonPhoto.has("message")) {
                        String sender = jsonPhoto.getString("sender");
                        String message = jsonPhoto.getString("message");
                        String colour = null;
                        String room = jsonPhoto.getString("room");

                        Photo photoObject = new Photo(sender, message, colour, room);
                        mPhotosList.add(photoObject);

                        Log.d(TAG, "onDownloadComplete " + photoObject.toString());
                        }
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if(runningOnSameThread && mCallback != null) {
            mCallback.onDataAvailable(mPhotosList, status);
        }

        Log.d(TAG, "onDownloadComplete ends");
    }

}
