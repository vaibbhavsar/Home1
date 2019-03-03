package crdma.genxcoders.com.disasterapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.List;

import crdma.genxcoders.com.disasterapp.models.Event;
import crdma.genxcoders.com.disasterapp.adapter.EventListAdapter;
import crdma.genxcoders.com.disasterapp.R;

public class RecentEventActivity extends AppCompatActivity {
    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_event);
        setTitle("Events/Accidents");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rv_events);

        mAdapter = new EventListAdapter(eventList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareEventData();
    }

    private void prepareEventData() {
        Event event = new Event("Disaster", "Status");
        eventList.add(event);



        mAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}