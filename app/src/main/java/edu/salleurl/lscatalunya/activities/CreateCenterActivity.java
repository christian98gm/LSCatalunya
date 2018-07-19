package edu.salleurl.lscatalunya.activities;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;

public class CreateCenterActivity extends AppCompatActivity implements View.OnClickListener, AsyncCenterRepo.Callback{

    private Center center;
    private boolean[] disabledButtons;
    private int[] buttonsBackgroundColor;
    private final static String CREATE_CENTER = "createCenterExtra";
    public final static String SPINNER_INFO = "spinnerInfo";
    public final static String BACKGROUND_INFO = "backgroundInfo";
    public final static String DIALOG_INFO = "dialogInfo";
    private int dialogActive = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_center);

        Button children = findViewById(R.id.button_children);
        Button primary = findViewById(R.id.button_primary);
        Button secondary = findViewById(R.id.button_secondary);
        Button highschool= findViewById(R.id.button_high_school);
        Button vocTraining = findViewById(R.id.button_vocational_training);
        Button university = findViewById(R.id.button_university);
        TextView createCenterName = findViewById(R.id.create_center_name);
        TextView createCenterAddress = findViewById(R.id.create_center_address);
        Spinner createCenterProvince = findViewById(R.id.create_center_province);
        TextView createCenterDescription = findViewById(R.id.create_center_description);

        buttonsBackgroundColor = new int[6];
        if(savedInstanceState == null) {
            center = new Center();
            buttonsBackgroundColor[0] = ((ColorDrawable)children.getBackground()).getColor();
            buttonsBackgroundColor[1] = ((ColorDrawable)primary.getBackground()).getColor();
            buttonsBackgroundColor[2] = ((ColorDrawable)secondary.getBackground()).getColor();
            buttonsBackgroundColor[3] = ((ColorDrawable)highschool.getBackground()).getColor();
            buttonsBackgroundColor[4] = ((ColorDrawable)vocTraining.getBackground()).getColor();
            buttonsBackgroundColor[5] = ((ColorDrawable)university.getBackground()).getColor();
        }else{

            center = savedInstanceState.getParcelable(CREATE_CENTER);
            if(center.hasChildren()){
                disabledAppearance(children);
            }
            if(center.hasPrimary())disabledAppearance(primary);
            if(center.hasSecondary())disabledAppearance(secondary);
            if(center.hasHighSchool())disabledAppearance(highschool);
            if(center.hasVocationalTraining())disabledAppearance(vocTraining);
            if(center.hasUniversity())disabledAppearance(university);
            createCenterName.setText(center.getName());
            createCenterAddress.setText(center.getAddress());
            createCenterProvince.setSelection(savedInstanceState.getInt(SPINNER_INFO));
            createCenterDescription.setText(center.getDescription());
            buttonsBackgroundColor = savedInstanceState.getIntArray(BACKGROUND_INFO);
            dialogActive = savedInstanceState.getInt(DIALOG_INFO);
            if(dialogActive != -1){
                dialogInfo(dialogActive);
            }
        }




        children.setOnClickListener(this);
        primary.setOnClickListener(this);
        secondary.setOnClickListener(this);
        highschool.setOnClickListener(this);
        vocTraining.setOnClickListener(this);
        university.setOnClickListener(this);
        Button createCenter = findViewById(R.id.create_center_button);
        createCenter.setOnClickListener(this);

    }

    //Rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CREATE_CENTER, center);
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_INFO, ((Spinner)findViewById(R.id.create_center_province)).getSelectedItemPosition());
        outState.putIntArray(BACKGROUND_INFO, buttonsBackgroundColor);
        outState.putInt(DIALOG_INFO, dialogActive);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_children:
                if(!center.hasChildren()){
                    disabledAppearance((Button)findViewById(R.id.button_children));
                    center.setChildren(true);
                }else{
                    buttonDefaultState((Button)findViewById(R.id.button_children),0);
                    center.setChildren(false);
                }
                break;
            case R.id.button_primary:
                if(!center.hasPrimary()){
                    disabledAppearance((Button)findViewById(R.id.button_primary));
                    center.setPrimary(true);
                }else{
                    buttonDefaultState((Button)findViewById(R.id.button_primary),1);
                    center.setPrimary(false);
                }
                break;
            case R.id.button_secondary:
                if(!center.hasSecondary()){
                    disabledAppearance((Button)findViewById(R.id.button_secondary));
                    center.setSecondary(true);
                }else{
                    buttonDefaultState((Button)findViewById(R.id.button_secondary),2);
                    center.setSecondary(false);
                }
                break;
            case R.id.button_high_school:
                if(!center.hasHighSchool()){
                    disabledAppearance((Button)findViewById(R.id.button_high_school));
                    center.setHighSchool(true);
                }else{
                    buttonDefaultState((Button)findViewById(R.id.button_high_school),3);
                    center.setHighSchool(false);
                }
                break;
            case R.id.button_vocational_training:
                if(!center.hasVocationalTraining()){
                    disabledAppearance((Button)findViewById(R.id.button_vocational_training));
                    center.setVocationalTraining(true);
                }else{
                    buttonDefaultState((Button)findViewById(R.id.button_vocational_training),4);
                    center.setVocationalTraining(false);
                }
                break;
            case R.id.button_university:
                if(!center.hasUniversity()){
                    disabledAppearance((Button)findViewById(R.id.button_university));
                    center.setUniversity(true);
                }else{
                    buttonDefaultState((Button)findViewById(R.id.button_university),5);
                    center.setUniversity(false);
                }
                break;
            case R.id.create_center_button:
                saveInfo();
                CenterWebService.getInstance(this,this).addCenter(center);
                break;
        }
    }

    private void saveInfo(){
        TextView createCenterName = findViewById(R.id.create_center_name);
        TextView createCenterAddress = findViewById(R.id.create_center_address);
        Spinner createCenterProvince = findViewById(R.id.create_center_province);
        TextView createCenterDescription = findViewById(R.id.create_center_description);
        center.setName(createCenterName.getText().toString());
        center.setAddress(createCenterAddress.getText().toString());
        center.setProvince(createCenterProvince.getSelectedItem().toString());
        center.setDescription(createCenterDescription.getText().toString());
    }
    private void disabledAppearance (Button b){
        b.setBackgroundColor(Color.LTGRAY);
        b.setAlpha(0.15f);
    }
    private void buttonDefaultState(Button b, int type){
        b.setBackgroundColor(buttonsBackgroundColor[type]);
        b.setAlpha(1.0f);

    }
    @Override
    public void onAddCenterResponse(String msg, int typeResponse) {
        dialogActive = typeResponse;
        dialogInfo(typeResponse);
    }
    private void dialogInfo(int typeResponse){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.alert));
        switch (typeResponse){
            case 0:
                alertDialog.setMessage(getString(R.string.format_error));
                break;
            case 1:
                alertDialog.setMessage(getString(R.string.successful_introduction));
                break;
            case 2:
                alertDialog.setMessage(getString(R.string.read_error));
                break;
            case 3:
                alertDialog.setMessage(getString( R.string.connection_error));
                break;
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish(); //Tornem a activitar anterior.
                    }
                });
        alertDialog.show();
    }
    @Override
    public void onGetCentersResponse(Center center, int errorCode, boolean endInformation) {
        //Res
    }
}
