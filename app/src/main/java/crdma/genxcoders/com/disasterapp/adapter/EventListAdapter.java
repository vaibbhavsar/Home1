package crdma.genxcoders.com.disasterapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.models.Event;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

        private List<Event> eventList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvDisaster, year, tvStatus;

            public LinearLayout llCall;

            public MyViewHolder(View view) {
                super(view);
                tvDisaster = (TextView) view.findViewById(R.id.tv_disaster);
                tvStatus = (TextView) view.findViewById(R.id.tv_status);


            //    year = (TextView) view.findViewById(R.id.year);
            }
        }


        public EventListAdapter(List<Event> eventList) {
            this.eventList = eventList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_events, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Event event = eventList.get(position);
            holder.tvDisaster.setText("Disaster");
            holder.tvStatus.setText("Status");
         //   holder.year.setText(movie.getYear());


        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }