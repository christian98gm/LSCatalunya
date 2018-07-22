package edu.salleurl.lscatalunya.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.fragments.RecyclerFragmentCenterManager;
import edu.salleurl.lscatalunya.holders.CenterHolder;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;
import edu.salleurl.lscatalunya.adapters.RecyclerItemTouchHelper;
import edu.salleurl.lscatalunya.repositories.json.JsonException;

public class CenterManagementListActivity extends AppCompatActivity implements RefreshActivity,
        AsyncCenterRepo.Callback, ListActivity, View.OnClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerFragmentCenterManager recyclerFragmentCenterManager;
    private CenterWebService centerWebService;
    private CenterManager centerManager;
    private Center deletedItem;

    private AlertDialog exitDialog;
    private int descending = 1;
    private int deletedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_management_list);
        Toolbar toolbar = findViewById(R.id.centerManagementToolbar);
        toolbar.inflateMenu(R.menu.menu);
        FloatingActionButton fab = findViewById(R.id.floating_button_add);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        centerManager = CenterManager.getInstance();
        centerWebService = CenterWebService.getInstance(this, this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        recyclerFragmentCenterManager = RecyclerFragmentCenterManager.newInstance(centerManager.getCenters());
        transaction.add(R.id.fragment_management_centers, recyclerFragmentCenterManager); // newInstance() is a static factory method.
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
                centerManager.orderCenters(descending);
                descending = descending == 1 ? 0 : 1;
                recyclerFragmentCenterManager.notifyDataSetChanged();
                return true;
            case R.id.logout:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerFragmentCenterManager.notifyDataSetChanged();
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
        recyclerFragmentCenterManager.endRefreshing();
        if(exitDialog != null)  exitDialog.cancel();
    }

    @Override
    public void onAddCenterResponse(String msg, int type) {}

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
                recyclerFragmentCenterManager.notifyDataSetChanged();
                if (endInformation) {
                    Toast.makeText(this, getString(R.string.load_complete), Toast.LENGTH_SHORT).show();
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
            recyclerFragmentCenterManager.endRefreshing();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof CenterHolder) {
            // backup of removed item for undo purpose
            deletedItem = CenterManager.getInstance().getCenters().get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            CenterManager.getInstance().getCenters().remove(deletedItem);
            final RecyclerView recyclerView = findViewById(R.id.centerListItems);
            recyclerView.getAdapter().notifyItemRemoved(deletedIndex);
            CenterManager.getInstance().deleteCenterAux(deletedItem);
            createConfirmationDeleteDialog(recyclerView);
        }
    }

    private void createConfirmationDeleteDialog(final RecyclerView recyclerView){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.delete_item_list));
        builder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialogNO(dialog, recyclerView);

            }
        });
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogYES(dialog);

            }
        });
        exitDialog = builder.show();
    }

    private void dialogNO(DialogInterface dialog, RecyclerView recyclerView){
        CenterManager.getInstance().getCenters().add(deletedIndex,deletedItem);
        recyclerView.getAdapter().notifyItemInserted(deletedIndex);
        CenterManager.getInstance().addCenterAux(deletedItem);
        dialog.dismiss();
    }

    private void dialogYES(DialogInterface dialog){
        CenterWebService.getInstance(this,this).newContext(this);
        CenterWebService.getInstance(this, this).deleteCenter(deletedItem);
        dialog.dismiss();
    }

    @Override
    public void onDeleteCenterResponse(String msg, int type) {
        switch (type){
            case 0:
                Toast.makeText(this,getString(R.string.format_error),Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this,getString(R.string.successful_deletion),Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,getString(R.string.read_error),Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,getString( R.string.connection_error),Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
