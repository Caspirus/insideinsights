package casper.theamericancreed;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.app.ActionBar.LayoutParams;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    private ImageButton send;
    private EditText message;
    private String userName, roomName, tempKey, chatUserName, chatMessage, chatTime, chatDate;
    private DatabaseReference root;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        send = (ImageButton) findViewById(R.id.imageButtonSend);
        message = (EditText) findViewById(R.id.editTextSendMessage);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        userName = getIntent().getExtras().get("userEmail").toString();
        roomName = getIntent().getExtras().get("roomName").toString();
        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        setTitle("Room: "+roomName);
        scrollToBottom();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> keyMap = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(keyMap);

                DatabaseReference messageRoot = root.child(tempKey);
                Map<String, Object> nameMsgMap = new HashMap<String, Object>();
                nameMsgMap.put("Name", userName);
                nameMsgMap.put("Message", message.getText().toString());
                nameMsgMap.put("Date", DateFormat.getDateInstance().format(date = new Date()));
                nameMsgMap.put("Time", simpleDateFormat.format(new Date()));
                messageRoot.updateChildren(nameMsgMap);
                scrollToBottom();
                message.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChat(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChat(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void appendChat (DataSnapshot dataSnapshot)
    {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        TextView message = new TextView(ChatRoom.this);
        TextView sender = new TextView(ChatRoom.this);
        TextView time = new TextView(ChatRoom.this);
        TextView date = new TextView(ChatRoom.this);
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            chatDate = ((DataSnapshot)i.next()).getValue().toString();
            chatMessage = ((DataSnapshot)i.next()).getValue().toString();
            chatUserName = ((DataSnapshot)i.next()).getValue().toString();
            chatTime = ((DataSnapshot)i.next()).getValue().toString();

            message.setText(chatMessage);
            sender.setText(chatUserName);
            time.setText(chatTime+" - "+chatDate);
            date.setTextColor(Color.parseColor("#6c6a6d"));
            sender.setTextColor(Color.parseColor("#6c6a6d"));
            time.setTextColor(Color.parseColor("#6c6a6d"));
            message.setTextColor(Color.parseColor("#000000"));
            message.setMaxWidth((int) (width/1.5));
            time.setPadding(0, 0, 0, 60);
            SpannableString spannableString = new SpannableString(time.getText().toString());
            spannableString.setSpan(new UnderlineSpan(), 0, time.getText().length(), 0);
            time.setText(spannableString);
            LinearLayout chat = (LinearLayout) findViewById(R.id.linearViewMessages);

            if (chatUserName.equals(userName))
            {
                message.setBackgroundResource(R.drawable.bubble2);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.RIGHT;
                time.setLayoutParams(layoutParams);
                time.setGravity(Gravity.RIGHT);
                sender.setLayoutParams(layoutParams);
                sender.setGravity(Gravity.CENTER);
                message.setLayoutParams(layoutParams);
                message.setGravity(Gravity.RIGHT);
                chat.addView(sender);
                chat.addView(message);
                chat.addView(time);
            }
            else
            {
                message.setBackgroundResource(R.drawable.bubble1);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.LEFT;
                time.setLayoutParams(layoutParams);
                time.setGravity(Gravity.LEFT);
                sender.setLayoutParams(layoutParams);
                sender.setGravity(Gravity.CENTER);
                message.setLayoutParams(layoutParams);
                message.setGravity(Gravity.LEFT);
                chat.addView(sender);
                chat.addView(message);
                chat.addView(time);
            }
        }
    }

    private void scrollToBottom ()
    {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }
}
