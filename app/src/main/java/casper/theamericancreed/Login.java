package casper.theamericancreed;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button register, login, noLogin;
    private EditText userEmail, userPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button) findViewById(R.id.buttonRegister);
        login = (Button) findViewById(R.id.buttonLogin);
        userEmail = (EditText) findViewById(R.id.editTextEnterUser);
        userPassword = (EditText) findViewById(R.id.editTextEnterPassword);
        noLogin = (Button) findViewById(R.id.buttonNoLogin);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        noLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FrontPage.class);
                intent.putExtra("isLogin", "0");
                startActivity(intent);
            }
        });
    }

    private void userLogin ()
    {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty())
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.isEmpty())
        {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            finish();
                            Intent intent = new Intent(getApplicationContext(), FrontPage.class);
                            intent.putExtra("isLogin", "1");
                            String[] split = userEmail.getText().toString().split("@");
                            intent.putExtra("key", split[0]);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Incorrect login. Try Again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
}
