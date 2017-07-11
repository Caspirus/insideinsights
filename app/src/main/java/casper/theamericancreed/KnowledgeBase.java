package casper.theamericancreed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class KnowledgeBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_knowledge_base);
    }
}
