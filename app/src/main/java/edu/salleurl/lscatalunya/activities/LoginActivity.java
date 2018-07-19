package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.repositories.impl.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.user_name_etx);
        pass = findViewById(R.id.password_etx);
    }

    public void login(View view) {

        DBHelper dbHelper = new DBHelper(this);

        if (dbHelper.userRegistered(user.getText().toString(), pass.getText().toString())) {
            Intent intent = new Intent(this, CenterManagementListActivity.class);
            startActivity(intent);
        } else {
            TextInputLayout tilUser = findViewById(R.id.user_til_login);
            tilUser.setError(" ");
            TextInputLayout tilPass = findViewById(R.id.pass_til_login);
            tilPass.setError("Invalid user or password");
            tilUser.setErrorEnabled(true);
            tilPass.setErrorEnabled(true);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginFB(View view) {
    }

    public void loginTWT(View view) {
    }

    public void loginGOG(View view) {
    }

}
