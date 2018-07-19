package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.repositories.impl.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText user,name,surname,email,pass,passconf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.username_register_etx);
        name  = findViewById(R.id.firstname_register_etx);
        surname = findViewById(R.id.lastname_register_etx);
        email = findViewById(R.id.email_register_etx);
        pass = findViewById(R.id.pass_register_etx);
        passconf = findViewById(R.id.passconfirm_register_etx);

    }

    public void register(View view){

        if(pass.getText().toString().equals(passconf.getText().toString())){
            DBHelper dbHelper = new DBHelper(this);
            if(dbHelper.insertUser(user.getText().toString(),pass.getText().toString(),name.getText().toString(),surname.getText().toString(),email.getText().toString())){
                Intent intent = new Intent(this, CenterManagementListActivity.class);
                startActivity(intent);
            }else{
                //TODO: USUARI JA REGISTRAT
            }
        }else{
            //TODO: PASS NO COINCIDEIXEN
        }
    }
}
