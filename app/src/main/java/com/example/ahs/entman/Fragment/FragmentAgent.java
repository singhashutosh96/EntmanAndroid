package com.example.ahs.entman.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.example.ahs.entman.Adapter.AgentAdapter;
import com.example.ahs.entman.Classes.ActionButton;
import com.example.ahs.entman.Instance.Add_Agent_Instance;
import com.example.ahs.entman.Model.Agent;
import com.example.ahs.entman.R;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

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
    ActionButton agentFab;
    LoadingButton detail_submit;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agent, container, false);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        agentFab = v.findViewById(R.id.agentFab);
        agentList = new ArrayList<>();

        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // getFragmentManager().beginTransaction().replace(R.id.viewPager,FragmentAgent.newInstance()).commit();
                agentList.clear();
                loadAgents();
               // recyclerView.setAdapter(new AgentAdapter(agentList));
                // recyclerView.invalidate();
            }
        });

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

                                            Toast.makeText(getActivity(),"Agent Added Successfully",Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                            agentList.clear();
                                            loadAgents();

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

                            if(adapter==null){
                                adapter = new AgentAdapter(getActivity(),agentList,FragmentAgent.this);
                                recyclerView.setAdapter(adapter);}
                            else{
                                adapter.notifyDataSetChanged();
                            }


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

       swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onDeleteClicked(final String agent_id, final  int position) {



        new FancyAlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setBackgroundColor(Color.parseColor("#BD2031"))  //Don't pass R.color.colorvalue
                .setMessage("Do you really want to Delete?")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#BD2031"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Delete")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_close_cross, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                        final String delete_url = "http://tcetminiproject.000webhostapp.com/agent_delete.php";
                        final String id = agent_id;

                        StringRequest update_info = new StringRequest(Request.Method.POST, delete_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Toast.makeText(getActivity(),"Agent Deleted",Toast.LENGTH_SHORT).show();

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

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getActivity(),"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }


    @Override
    public void onUpdateClicked(final String id, String name, String phone, String email,final int position) {



        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_create_agent,null);
        final EditText agent_name = mView.findViewById(R.id.et_agent_name);
        agent_name.setText(name);
        final EditText agent_phone = mView.findViewById(R.id.et_agent_phone);
        agent_phone.setText(phone);
        final EditText agent_email = mView.findViewById(R.id.et_agent_email);
        agent_email.setText(email);
        detail_submit = mView.findViewById(R.id.btn_submit_details);
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

                                    dialog.dismiss();
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
                    //recreate();

                }
            }});

        mBuilder.setView(mView);
        dialog = mBuilder
                .setNegativeButton("Close", null)
                .setCancelable(false)
                .create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {


                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });

            }
        });

        dialog.show();

        /*

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                //do whatever you want the back key to do
                Toast.makeText(getContext(),"Back",Toast.LENGTH_LONG).show();

                new FancyAlertDialog.Builder(getActivity())
                        .setTitle("Discard the Changes ?")
                        .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                        .setNegativeBtnText("Cancel")
                        .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                        .setPositiveBtnText("Discard")
                        .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                        .setAnimation(Animation.POP)
                        .isCancellable(true)
                        .setIcon(R.drawable.ic_star_border_black_24dp,Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getActivity(),"Discard",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getActivity(),"Cancel",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build();
            }
        });
*/

    }


    public static FragmentAgent newInstance() {

        FragmentAgent f = new FragmentAgent();
        return f;
    }
}
