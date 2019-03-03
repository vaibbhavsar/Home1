package crdma.genxcoders.com.disasterapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.models.Helpline;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private List<Helpline> contactList;
    private List<Helpline> exampleListFull;
    private Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, year, tvcontact;

        public LinearLayout llCall;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvcontact = (TextView) view.findViewById(R.id.tv_number);
            //    year = (TextView) view.findViewById(R.id.year);
            llCall=view.findViewById(R.id.ll_call);

        }
    }


    public ContactsAdapter(Context mContext,List<Helpline> contactList) {

        this.mContext=mContext;
        this.contactList = contactList;
        exampleListFull = new ArrayList<>(contactList);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_contacts, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Helpline contact = contactList.get(position);
        holder.tvName.setText(""+contact.getHelper());
        holder.tvcontact.setText(""+contact.getContact());
        //   holder.year.setText(movie.getYear());
        holder.llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + contact.getContact()));
                mContext.startActivity(intent1);

            }
        });
    }
/*
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Helpline> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Helpline item : exampleListFull) {
                    if (item.getHelper().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactList.clear();
            contactList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };*/

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}