package crdma.genxcoders.com.disasterapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.adapter.ContactsAdapter;
import crdma.genxcoders.com.disasterapp.models.Helpline;
import crdma.genxcoders.com.disasterapp.network.WLAPIcalls;
import crdma.genxcoders.com.disasterapp.utils.AppCommonMethods;
import crdma.genxcoders.com.disasterapp.utils.AppPrefs;

public class EmergencyContactActivity extends AppCompatActivity implements WLAPIcalls.OnAPICallCompleteListener {
    private List<Helpline> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;


    private Context mContext=EmergencyContactActivity.this;
    private static final String HELPLINE_CONTACTS = "HelplineContacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        setTitle("Emergency Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.rv_contacts);
        mAdapter = new ContactsAdapter(mContext,contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getHelplineContacts();


        if(AppPrefs.getStringPref("helpline",mContext)!=null)

        {



               // contactList.add(helplineContact);


            }

        else {


        }

        mAdapter.notifyDataSetChanged();





    }

    private void getHelplineContacts() {
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            WLAPIcalls mAPIcall = new WLAPIcalls(mContext, HELPLINE_CONTACTS, this);

            mAPIcall.helplineContactRequest();


        } else {
            Toast.makeText(mContext, "Internet Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAPICallCompleteListner(Object item, String flag, String result) throws JSONException {
        if (flag.equals(HELPLINE_CONTACTS)) {
            Log.e("Response", result);
            JSONObject jsonObject = new JSONObject(result);
            String message = jsonObject.getString("helpline");
            Log.e( "HELPLINE:", String.valueOf(message));



                JSONArray cast =jsonObject.getJSONArray("helpline");
                for (int i = 0; i < cast.length(); i++) {
                    JSONObject contactObjJson = cast.getJSONObject(i);
                    String helper = contactObjJson.getString("helper");
                    String contact = contactObjJson.getString("contact");

                    Log.e("Helper", helper);
                    Log.e("Contact", contact);

                    Helpline helplineContact = new Gson()
                            .fromJson(contactObjJson.toString(), Helpline.class);

                    contactList.add(helplineContact);

                    AppPrefs.putStringPref("helpline",jsonObject.toString(), mContext);

                }

                mAdapter.notifyDataSetChanged();







//                Log.d("Colleague", " " + employee.getEmp_id());
//                employee_id = employee.getEmp_id();
//                emp_name = employee.getEmp_name();
//                emp_desig = employee.getEmp_designation();
//                emp_dept = employee.getDepartment_name();





        }

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
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/
}