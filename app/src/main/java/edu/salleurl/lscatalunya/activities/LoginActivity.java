package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.repositories.db.DBHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText pass;
    private TextInputLayout tilUser;
    private TextInputLayout tilPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.user_name_etx);
        pass = findViewById(R.id.password_etx);
        tilUser = findViewById(R.id.user_til_login);
        tilPass = findViewById(R.id.pass_til_login);
    }

    public void login(View view) {

        DBHelper dbHelper = new DBHelper(this);

        if(dbHelper.userRegistered(user.getText().toString(), pass.getText().toString())) {
            Intent intent = new Intent(this, CenterManagementListActivity.class);
            user.setText("");
            pass.setText("");
            tilUser.setErrorEnabled(false);
            tilPass.setErrorEnabled(false);
            startActivity(intent);
        } else {
            tilUser.setError(" ");
            tilPass.setError("Invalid user or password");
            tilUser.setErrorEnabled(true);
            tilPass.setErrorEnabled(true);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
