package com.example.ahs.entman.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ahs.entman.Model.Agent;
import com.example.ahs.entman.R;

import java.util.List;
import java.util.Random;

/**
 * Created by AHS on 12/24/2017.
 */

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {


    private Context mctx;
    private List<Agent> agentList;
    private OnDeleteClikListener OnDeleteClikListener = null;
    private String[] colors = {"#4c99cf","#4873a6","#5b538a","#311a41"} ;


    public AgentAdapter(Context mctx, List<Agent> agentList, OnDeleteClikListener OnDeleteClikListener) {
        this.mctx = mctx;
        this.OnDeleteClikListener = OnDeleteClikListener;
        this.agentList = agentList;
    }

    @Override
    public AgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.recycler_layout,null);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AgentViewHolder holder, final int position) {

        final Agent agent = agentList.get(position);



//        if(position%2==0)
//            holder.cardView.setBackgroundColor(Color.parseColor(colors[0]));
//        else
          holder.cardView.setBackgroundResource(R.drawable.gradient_cv);

        holder.textViewUserId.setText(String.valueOf(agent.getUser_id()));
        holder.textViewUserName.setText(agent.getUser_name());
        holder.textViewUserPhone.setText(agent.getUser_phone());
        holder.textViewUserEmail.setText(agent.getUser_email());
        holder.imageviewoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String delete_url = "http:/192.168.0.20/practice/agent_delete.php";
                final String name = agent.getUser_name();
                final String phone = agent.getUser_phone();
                final String email = agent.getUser_email();
                final String id = String.valueOf(agent.getUser_id());
                PopupMenu popupMenu = new PopupMenu(mctx,holder.imageviewoptions);
                popupMenu.inflate(R.menu.options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.menu_item_update:
                                OnDeleteClikListener.onUpdateClicked(id,name,phone,email);
                                break;

                            case R.id.menu_item_delete:
                                OnDeleteClikListener.onDeleteClicked(id);
                                break;

                            default:
                                break;

                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return agentList.size();
    }

    class AgentViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserId,textViewUserName,textViewUserPhone,textViewUserEmail;
        ImageView imageviewoptions;
        CardView cardView;

        public AgentViewHolder(View itemView) {
            super(itemView);

            textViewUserId = itemView.findViewById(R.id.tv_user_id);
            textViewUserName = itemView.findViewById(R.id.tv_user_name);
            textViewUserPhone = itemView.findViewById(R.id.tv_user_phone);
            textViewUserEmail = itemView.findViewById(R.id.tv_user_email);
            imageviewoptions = itemView.findViewById(R.id.iv_options);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

    public interface OnDeleteClikListener {
        void onDeleteClicked(String id);
        void onUpdateClicked(String id, String name, String phone, String email);
    }
}
