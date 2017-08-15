package casper.theamericancreed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

public class Profile extends AppCompatActivity {

    private String firstName, lastName, email, alias, imageUrl;
    private TextView profileName, profileEmail, profileAlias;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users/"+getIntent().getExtras().get("key").toString());
        imageView = (ImageView) findViewById(R.id.imageViewProfilePic);
        profileName = (TextView) findViewById(R.id.textViewProfileName);
        profileAlias = (TextView) findViewById(R.id.textViewAlias);
        profileEmail = (TextView) findViewById(R.id.textViewEmail);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateUI(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(DataSnapshot dataSnapshot)
    {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            alias = ((DataSnapshot)i.next()).getValue().toString();
            email = ((DataSnapshot)i.next()).getValue().toString();
            firstName = ((DataSnapshot)i.next()).getValue().toString();
            lastName = ((DataSnapshot)i.next()).getValue().toString();
            imageUrl = ((DataSnapshot) i.next()).getValue().toString();

        }
        profileName.setText("Name: "+firstName+" "+lastName);
        profileAlias.setText("Username: "+alias);
        profileEmail.setText("Email: "+email);
        Picasso.with(this).load(imageUrl).fit().into(imageView);
    }
}
