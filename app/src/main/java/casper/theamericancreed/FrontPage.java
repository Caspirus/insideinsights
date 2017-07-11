package casper.theamericancreed;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FrontPage extends ActivityGroup {

    private TabHost tabHost;
    private Button liveChat, signOut;
    private String hasLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        liveChat = (Button) findViewById(R.id.buttonLiveChat);
        signOut = (Button) findViewById(R.id.buttonSignOut);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        hasLogin = getIntent().getExtras().get("isLogin").toString();
        tabHost.setup(this.getLocalActivityManager());

        firebaseAuth = FirebaseAuth.getInstance();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("About Us");
        spec1.setIndicator("", getResources().getDrawable(R.drawable.about_us_icon));
        spec1.setContent(new Intent(FrontPage.this, AboutUs.class));

        TabHost.TabSpec spec = tabHost.newTabSpec("Knowledge Base");
        spec.setIndicator("", getResources().getDrawable(R.drawable.knowledge_base_icon));
        spec.setContent(new Intent(FrontPage.this, KnowledgeBase.class));

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Current Event");
        spec2.setIndicator("", getResources().getDrawable(R.drawable.current_event_logo));
        spec2.setContent(new Intent(FrontPage.this, CurrentEvent.class));

        TabHost.TabSpec spec3 = tabHost.newTabSpec("Ideology");
        spec3.setIndicator("", getResources().getDrawable(R.drawable.ideology_icon));
        spec3.setContent(new Intent(FrontPage.this, Ideology.class));

        tabHost.addTab(spec1);
        tabHost.addTab(spec);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(FrontPage.this, Login.class));
            }
        });

        liveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLogin.equals("0"))
                {
                    noLogin();
                }
                else if (hasLogin.equals("1")) {
                    Intent intent = new Intent(FrontPage.this, ChatSelect.class);
                    intent.putExtra("userEmail", getIntent().getExtras().get("userEmail").toString());
                    startActivity(intent);
                }
            }
        });
    }
    private void noLogin()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("You must be logged in to chat! Proceed to Login?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(FrontPage.this, Login.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
