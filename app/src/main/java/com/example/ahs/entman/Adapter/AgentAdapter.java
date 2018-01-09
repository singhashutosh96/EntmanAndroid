package com.example.ahs.entman.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ahs.entman.Classes.ActionButton;
import com.example.ahs.entman.Model.Agent;
import com.example.ahs.entman.R;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by AHS on 12/24/2017.
 */

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {


    private Context mctx;
    private List<Agent> agentList;
    private OnDeleteClikListener OnDeleteClikListener = null;
    private SparseBooleanArray expandState = new SparseBooleanArray();


    public AgentAdapter(Context mctx, List<Agent> agentList, OnDeleteClikListener OnDeleteClikListener) {
        this.mctx = mctx;
        this.OnDeleteClikListener = OnDeleteClikListener;
        this.agentList = agentList;

        for(int i=0;i<agentList.size();i++)
            expandState.append(i,false);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public AgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.layout_recycler,parent,false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AgentViewHolder holder, final int position) {

        final Agent agent = agentList.get(position);
        holder.setIsRecyclable(false);
        holder.textViewInfo.setText(agent.getUser_name());
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

            @Override
            public void onPreOpen() {
                changeRotate(holder.dropdown,0f,180f).start();
                expandState.put(position,true);

            }

            @Override
            public void onPreClose() {

                changeRotate(holder.dropdown,180f,0f).start();
                expandState.put(position,false);

            }

        });

        holder.dropdown.setRotation(expandState.get(position)?180f:0f);
        holder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Expandable Child View

                holder.expandableLayout.toggle();
            }
        });

        //holder.cardView.setBackgroundResource(R.drawable.gradient_cv);
        holder.textViewUserId.setText(String.valueOf(agent.getUser_id()));
        holder.textViewUserName.setText(agent.getUser_name());
        holder.textViewUserPhone.setText(agent.getUser_phone());
        holder.textViewUserEmail.setText(agent.getUser_email());
        holder.imageviewoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                OnDeleteClikListener.onUpdateClicked(id,name,phone,email,position);
                                break;

                            case R.id.menu_item_delete:
                                OnDeleteClikListener.onDeleteClicked(id,position);
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

    private ObjectAnimator changeRotate(RelativeLayout dropdown, float from, float to) {

            ObjectAnimator animator = ObjectAnimator.ofFloat(dropdown,"rotation",from,to);
            animator.setDuration(300);
            animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
            return animator;
    }

    @Override
    public int getItemCount() {

        return agentList.size();
    }

    class AgentViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserId,textViewUserName,textViewUserPhone,textViewUserEmail,textViewInfo;
        ImageView imageviewoptions;
        CardView cardView;
        RelativeLayout dropdown;
        ExpandableLinearLayout expandableLayout;
        ActionButton agentFab;

        public AgentViewHolder(View itemView) {
            super(itemView);

            textViewUserId =itemView.findViewById(R.id.tv_user_id);
            textViewUserName = itemView.findViewById(R.id.tv_user_name);
            textViewUserPhone = itemView.findViewById(R.id.tv_user_phone);
            textViewUserEmail = itemView.findViewById(R.id.tv_user_email);
            imageviewoptions = itemView.findViewById(R.id.iv_options);
            cardView = itemView.findViewById(R.id.cardView);
            textViewInfo =itemView.findViewById(R.id.tvInfo);
            dropdown= itemView.findViewById(R.id.dropdown);
            expandableLayout=itemView.findViewById(R.id.expandableLayout);


        }
    }

    public interface OnDeleteClikListener {
        void onDeleteClicked(String id,int position);
        void onUpdateClicked(String id, String name, String phone, String email,int position);
    }
}
