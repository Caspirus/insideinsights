package casper.theamericancreed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ChatSelect extends AppCompatActivity {

    private ImageButton addRoom;
    private EditText roomName;
    private ListView allRooms;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private String chatAlias;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users/"+getIntent().getExtras().get("key").toString());

        addRoom = (ImageButton) findViewById(R.id.imageButtonAddRoom);
        roomName = (EditText) findViewById(R.id.editTextAddRoom);
        allRooms = (ListView) findViewById(R.id.listViewRooms);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        allRooms.setAdapter(arrayAdapter);


        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(roomName.getText().toString(),"");
                root.updateChildren(map);
                roomName.setText("");
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> temp = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    temp.add(((DataSnapshot) i.next()).getKey());
                }
                arrayList.clear();
                arrayList.addAll(temp);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext())
                {
                    chatAlias = ((DataSnapshot)i.next()).getValue().toString();
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        allRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (ChatSelect.this, ChatRoom.class);
                intent.putExtra("roomName", ((TextView)view).getText().toString());
                intent.putExtra("alias", chatAlias);
                startActivity(intent);
            }
        });
    }
}
