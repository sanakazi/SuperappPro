package com.superapp.webservice;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohit on 5/6/2015.
 */
public class MultiPartReq extends Request<String> {
    private Response.Listener<String> mListener = null;
    private Response.ErrorListener mEListener;
    //
    private final ArrayList<File> mFilePart;
    private final ArrayList<String> mStringPart;
    private Map<String, String> parameters;
    private Map<String, String> header;
    MultipartEntity entity = new MultipartEntity();
//    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
//        return header;

        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }
//        headers.put(HTTP.CONTENT_TYPE, "text/html; charset=us-ascii");
//        headers.put("charset", "utf-8");
        return headers;

    }

    public MultiPartReq(String url,
                        Response.ErrorListener eListener, Response.Listener<String> rListener,
                        ArrayList<File> files, ArrayList<String> stringPart,
                        Map<String, String> param, Map<String, String> head) {
        super(Method.POST, url, eListener);
        mListener = rListener;
        mEListener = eListener;
        mFilePart = files;
        mStringPart = stringPart;
        parameters = param;
        header = head;
        buildMultipartEntity();
    }

   /* @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
            String entityContentAsString = new String(bos.toByteArray());
            Log.e("volley", entityContentAsString);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(String arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // TODO Auto-generated method stub
        return null;
    }*/


    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            System.out.println("Network Response " + new String(response.data, "UTF-8"));
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    private void buildMultipartEntity() {
        try {
            if (mFilePart != null && mFilePart.size() > 0)
                for (int i = 0; i < mFilePart.size(); i++) {
//                for (File file : mFilePart) {
//                    entity.addPart(mStringPart, new FileBody(file));
                    if (mFilePart.get(i) != null) {
                        entity.addPart(mStringPart != null ? mStringPart.get(i) : "image", new FileBody(mFilePart.get(i)));
                    }
                }

            for (String key : parameters.keySet()) {
                entity.addPart(key, new StringBody(parameters.get(key), Charset.forName("UTF-8")));
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }
}