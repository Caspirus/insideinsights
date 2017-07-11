package casper.theamericancreed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    private TextView missionStatement, howTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about_us);

        missionStatement = (TextView) findViewById(R.id.textMissionStatement);
        howTo = (TextView) findViewById(R.id.textHowToUse);

        String text = missionStatement.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        missionStatement.setText(spannableString);

        String howToText = howTo.getText().toString();
        SpannableString spannableString2 = new SpannableString(howToText);
        spannableString2.setSpan(new UnderlineSpan(), 0, howToText.length(), 0);
        howTo.setText(spannableString2);
    }
}
