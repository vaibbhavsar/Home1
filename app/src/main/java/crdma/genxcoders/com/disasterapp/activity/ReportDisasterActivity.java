package crdma.genxcoders.com.disasterapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import crdma.genxcoders.com.disasterapp.R;

public class ReportDisasterActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cvSelectLocation;
    ImageView ivUploadImg;
    Button btnSubmit;
    EditText etTypeSomething;
    Context mContext= ReportDisasterActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_disaster);
        setTitle("Report Disaster");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initController();

    }

    private void initController() {


        cvSelectLocation= findViewById(R.id.cv_location);
        ivUploadImg = findViewById(R.id.add_img);
        btnSubmit=findViewById(R.id.btn_submit);

        cvSelectLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.cv_location:
                //Intent to MAp Activity
                Intent mapIntent= new Intent(mContext,MapsActivity.class);
                startActivity(mapIntent);
                break;
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
}
