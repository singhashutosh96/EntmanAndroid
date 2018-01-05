package com.example.ahs.entman.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.example.ahs.entman.Adapter.AgentAdapter;
import com.example.ahs.entman.Instance.Add_Agent_Instance;
import com.example.ahs.entman.Model.Agent;
import com.example.ahs.entman.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashu on 05-01-2018.
 */

public class FragmentAgent extends Fragment implements AgentAdapter.OnDeleteClikListener {

    RecyclerView recyclerView;
    AgentAdapter adapter;
    List<Agent> agentList;
    AlertDialog dialog;
    private static final String Agent_URL = "http://tcetminiproject.000webhostapp.com/agent_list_retrive.php";
    FloatingActionButton agentFab;
    LoadingButton detail_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agent, container, false);


        agentFab =(FloatingActionButton)v.findViewById(R.id.agentFab);
        agentList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadAgents();

        agentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAgent();

            }
        });

        return v;





    }

    private void createAgent() {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_create_agent,null);
                final EditText agent_name = (EditText) mView.findViewById(R.id.et_agent_name);
                final EditText agent_phone = (EditText) mView.findViewById(R.id.et_agent_phone);
                final EditText agent_email = (EditText) mView.findViewById(R.id.et_agent_email);
                detail_submit = (LoadingButton) mView.findViewById(R.id.btn_submit_details);
                final String server_url = "http://tcetminiproject.000webhostapp.com/agent_create.php";


                detail_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detail_submit.startLoading();

                        if(agent_name.getText().toString().isEmpty()){

                            Toast.makeText(getActivity(),"Name Field Is Empty",Toast.LENGTH_LONG).show();
                        }
                        else if(agent_phone.getText().toString().isEmpty()){

                            Toast.makeText(getActivity(),"Phone Field Is Empty",Toast.LENGTH_LONG).show();
                        }
                        else {

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    },

                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Toast.makeText(getActivity(),"Error...",Toast.LENGTH_SHORT).show();

                                        }
                                    }){

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String,String> params = new HashMap<String,String>();
                                    params.put("name",agent_name.getText().toString());
                                    params.put("phone",agent_phone.getText().toString());
                                    params.put("email",agent_email.getText().toString());
                                    return params;
                                }
                            };

                            Add_Agent_Instance.getInstance(getActivity()).addToRequestQue(stringRequest);
                            Toast.makeText(getActivity(),"Agent Added Successfully",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            //  recreate();

                        }
                    }});

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
    }

    private void loadAgents() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Agent_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray agents = new JSONArray(response);

                            for(int i=0;i<agents.length();i++){

                                JSONObject agentobject = agents.getJSONObject(i);

                                int id = agentobject.getInt("id");
                                String name = agentobject.getString("name");
                                String phone = agentobject.getString("phone");
                                String email = agentobject.getString("email");

                                Agent agent = new Agent(id,name,phone,email);
                                agentList.add(agent);
                            }

                            adapter = new AgentAdapter(getActivity(),agentList,FragmentAgent.this);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    @Override
    public void onDeleteClicked(String agent_id) {
        Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
        final String delete_url = "http://tcetminiproject.000webhostapp.com/agent_delete.php";
        final String id = agent_id;

        StringRequest update_info = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getActivity(),"Delete Successful",Toast.LENGTH_SHORT).show();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),"Error...",Toast.LENGTH_LONG).show();
                        error.printStackTrace();


                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };

        Add_Agent_Instance.getInstance(getActivity()).addToRequestQue(update_info);
   //     recreate();



    }

    @Override
    public void onUpdateClicked(final String id, String name, String phone, String email) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_create_agent,null);
        final EditText agent_name = mView.findViewById(R.id.et_agent_name);
        agent_name.setText(name);
        final EditText agent_phone = mView.findViewById(R.id.et_agent_phone);
        agent_phone.setText(phone);
        final EditText agent_email = mView.findViewById(R.id.et_agent_email);
        agent_email.setText(email);
        Button detail_submit = (Button) mView.findViewById(R.id.btn_submit_details);
        final String server_url = "http://tcetminiproject.000webhostapp.com/agent_update.php";


        detail_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(agent_name.getText().toString().isEmpty()){

                    Toast.makeText(getContext(),"Name Field Is Empty",Toast.LENGTH_LONG).show();
                }
                else if(agent_phone.getText().toString().isEmpty()){

                    Toast.makeText(getContext(),"Phone Field Is Empty",Toast.LENGTH_LONG).show();
                }
                else {

                    StringRequest update_Request = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(getContext(),"Update Successful",Toast.LENGTH_LONG).show();

                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(getContext(),"Error...",Toast.LENGTH_SHORT).show();

                                }
                            }){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> params = new HashMap<String,String>();
                            params.put("id",id);
                            params.put("name",agent_name.getText().toString());
                            params.put("phone",agent_phone.getText().toString());
                            params.put("email",agent_email.getText().toString());
                            return params;
                        }
                    };

                    Add_Agent_Instance.getInstance(getContext()).addToRequestQue(update_Request);
                    dialog.dismiss();
                    //recreate();

                }
            }});

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();



    }

    public static FragmentAgent newInstance() {

        FragmentAgent f = new FragmentAgent();
        return f;
    }
}
