package edu.salleurl.lscatalunya.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.fragment.RecyclerFragment;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;
import edu.salleurl.lscatalunya.repositories.json.JsonException;

public class CenterManagementListActivity extends AppCompatActivity implements RefreshActivity, AsyncCenterRepo.Callback, ListActivity, View.OnClickListener {

    private Toolbar toolbar;
    private CenterWebService centerWebService;
    private CenterManager centerManager;
    private RecyclerFragment recyclerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_management_list);
        toolbar = findViewById(R.id.centerManagementToolbar);
        toolbar.inflateMenu(R.menu.menu);
        FloatingActionButton fab = findViewById(R.id.floating_button_add);
        fab.setOnClickListener(this);

        centerManager = CenterManager.getInstance();
        centerWebService = CenterWebService.getInstance(this, this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        recyclerFragment = RecyclerFragment.newInstance(centerManager.getCenters());
        transaction.add(R.id.fragment_management_centers, recyclerFragment); // newInstance() is a static factory method.
        transaction.commitNow();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                //TODO: SORT LIST
                return true;
            case R.id.logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean refreshActivity() {
        if (centerWebService.isWorking()) {
            return false;
        } else {
            centerWebService.newContext(this);
            CenterWebService.getInstance(this,this).getCenters();
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (centerWebService.isWorking() && centerWebService.isPaused()) {
            centerWebService.startRequest();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        centerWebService.stopRequest();
        recyclerFragment.endRefreshing();
    }

    @Override
    public void onAddCenterResponse(String msg, int success) {
        //No afegim
    }

    @Override
    public void showCenterContent(Center center) {
        if (!centerWebService.isWorking()) {
            Intent intent = new Intent(this, CenterActivity.class);
            intent.putExtra(CenterActivity.CENTER_EXTRA, center);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.wait_refresh), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onGetCentersResponse(Center center, int errorCode, boolean endInformation) {
        //Manage error codes
        switch (errorCode) {
            case CenterWebService.OK:           //Update centers data
                centerManager.addCenter(center);
                recyclerFragment.notifyDataSetChanged();
                if (endInformation) {
                    Toast.makeText(this, getString(R.string.load_complete), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case CenterWebService.HTTP_ERROR:   //Connection error
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT)
                        .show();
                break;
            case JsonException.FORMAT_ERROR:    //Error from web service
                Toast.makeText(this, getString(R.string.format_error), Toast.LENGTH_SHORT)
                        .show();
                break;
            case JsonException.READ_ERROR:      //Error reading data
                Toast.makeText(this, getString(R.string.read_error), Toast.LENGTH_SHORT)
                        .show();
                break;
        }
        if (endInformation) {
            recyclerFragment.endRefreshing();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floating_button_add:
                Intent intent = new Intent(this, CreateCenterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
