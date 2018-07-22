package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.repositories.db.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText user, name, surname, email, pass, passConf;
    private TextInputLayout tilUser;
    private TextInputLayout tilPass;
    private TextInputLayout tilName;
    private TextInputLayout tilSur;
    private TextInputLayout tilEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.username_register_etx);
        name = findViewById(R.id.firstname_register_etx);
        surname = findViewById(R.id.lastname_register_etx);
        email = findViewById(R.id.email_register_etx);
        pass = findViewById(R.id.pass_register_etx);
        passConf = findViewById(R.id.passconfirm_register_etx);

    }

    public void register(View view) {

        if (pass.getText().toString().equals(passConf.getText().toString())) {
            DBHelper dbHelper = new DBHelper(this);
            if (dbHelper.insertUser(user.getText().toString(), pass.getText().toString(), name.getText().toString(), surname.getText().toString(), email.getText().toString())) {
                Intent intent = new Intent(this, CenterManagementListActivity.class);
                user.setText("");
                name.setText("");
                surname.setText("");
                email.setText("");
                pass.setText("");
                passConf.setText("");
                tilUser.setErrorEnabled(false);
                tilPass.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilSur.setErrorEnabled(false);
                tilEmail.setErrorEnabled(false);
                startActivity(intent);
            } else {
                tilUser = findViewById(R.id.user_til_register);
                tilPass = findViewById(R.id.pass_til_register);
                tilName = findViewById(R.id.name_til_register);
                tilSur = findViewById(R.id.surname_til_register);
                tilEmail = findViewById(R.id.email_til_register);
                tilUser.setError(getResources().getString(R.string.invalid_param));
                tilPass.setError(" ");
                tilName.setError(" ");
                tilSur.setError(" ");
                tilEmail.setError(" ");
                tilUser.setErrorEnabled(true);
                tilPass.setErrorEnabled(true);
                tilName.setErrorEnabled(true);
                tilSur.setErrorEnabled(true);
                tilEmail.setErrorEnabled(true);
            }
        } else {
            Toast.makeText(this, getString(R.string.pass_not_match), Toast.LENGTH_SHORT).show();
        }
    }

}