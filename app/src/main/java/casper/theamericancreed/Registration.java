package casper.theamericancreed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private Button register;
    private EditText enterEmail, enterPassword, confirmPassword, enterFirstName, enterLastName, enterUsername;
    private String tempKey;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        register = (Button) findViewById(R.id.buttonRegisterNewUser);
        enterFirstName = (EditText) findViewById(R.id.editTextEnterFirstName);
        enterLastName = (EditText) findViewById(R.id.editTextEnterLastName);
        enterUsername = (EditText) findViewById(R.id.editTextEnterAlias);
        enterEmail = (EditText) findViewById(R.id.editTextEnterEmail);
        enterPassword = (EditText) findViewById(R.id.editTextPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextVerifyPassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser()
    {
        String email = enterEmail.getText().toString();
        String password = enterPassword.getText().toString();
        String verifyPassword = confirmPassword.getText().toString();

        //Handle all possible exceptions
        //Handle duplicate usernames
        //Handle blank fields
        if (email.isEmpty())
        {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
            enterPassword.setText("");
            confirmPassword.setText("");
            return;
        }
        else if (password.isEmpty() || verifyPassword.isEmpty())
        {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_LONG).show();
            enterEmail.setText("");
            enterPassword.setText("");
            confirmPassword.setText("");
            return;
        }
        else if (password.length() < 6)
        {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
            enterEmail.setText("");
            enterPassword.setText("");
            confirmPassword.setText("");
            return;
        }
        else if (!password.equals(verifyPassword))
        {
            Toast.makeText(this, "Passwords did not match! Try Again", Toast.LENGTH_LONG).show();
            enterEmail.setText("");
            enterPassword.setText("");
            confirmPassword.setText("");
            return;
        }
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            String[] split = enterEmail.getText().toString().split("@");
                            databaseReference.child(split[0]).setValue(new User(enterEmail.getText().toString(),
                                    enterFirstName.getText().toString(), enterLastName.getText().toString(), enterUsername.getText().toString(),
                                    "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png"));
                            Toast.makeText(Registration.this, "You have been registered!", Toast.LENGTH_LONG).show();
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            update (currentUser);
                            //Start profile activity
                        }
                        else
                        {
                            Toast.makeText(Registration.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void update (FirebaseUser user)
    {
        if (user!=null)
        {
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            Intent intent = new Intent(Registration.this, FrontPage.class);
            intent.putExtra("isLogin", "1");
            String[] split = enterEmail.getText().toString().split("@");
            intent.putExtra("key", split[0]);
            startActivity(intent);
        }
    }
}
