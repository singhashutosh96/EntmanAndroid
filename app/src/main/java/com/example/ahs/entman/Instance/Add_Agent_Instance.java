package com.example.ahs.entman.Instance;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by AHS on 12/27/2017.
 */

public class Add_Agent_Instance {

    private static Add_Agent_Instance mInstance;
    private RequestQueue requestQueue;
    private static Context mctx;

    private Add_Agent_Instance(Context context)
    {
        mctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized Add_Agent_Instance getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance = new Add_Agent_Instance(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());

        }
        return  requestQueue;
    }

    public <T>void  addToRequestQue (Request<T> request)
    {
        requestQueue.add(request);
    }

}
