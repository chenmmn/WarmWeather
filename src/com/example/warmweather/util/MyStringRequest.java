package com.example.warmweather.util;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class MyStringRequest extends StringRequest {

	public MyStringRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
	}
	
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		Log.e("TAG", "PARSENETWORKRESPONSE");
		 try {  
	            String dataString = new String(response.data, "UTF-8");  
	            return Response.success(dataString,HttpHeaderParser.parseCacheHeaders(response));  
	        } catch (UnsupportedEncodingException e) {  
	            return Response.error(new ParseError(e));  
	        }   
	}
}
