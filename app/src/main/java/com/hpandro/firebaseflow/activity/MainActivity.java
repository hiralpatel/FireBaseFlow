package com.hpandro.firebaseflow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hpandro.firebaseflow.R;
import com.hpandro.firebaseflow.adapter.PersonsAdapter;
import com.hpandro.firebaseflow.pojo.Person;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtDetails, edtSearchName;
    Button btnAdd, btnUpdate, btnCancel, btnSearch;
    private DatabaseReference mDBRef;
    List<Person> persons = new ArrayList<>();
    private RecyclerView recyclerView;
    private PersonsAdapter personsAdapter;
    String id = "", name = "", detail = "";
    private String TAG = "MainActivity";
    private ChildEventListener childEventListener;
    private Query queryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBRef = FirebaseDatabase.getInstance().getReference("persons");

        edtName = (EditText) findViewById(R.id.edtName);
        edtDetails = (EditText) findViewById(R.id.edtDetails);
        edtSearchName = (EditText) findViewById(R.id.edtSearchName);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecorator(MainActivity.this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!id.isEmpty())
                    return;
                id = mDBRef.push().getKey(); //TODO unique id
                name = edtName.getText().toString().trim();
//                id = name;
                detail = edtDetails.getText().toString().trim();

                if (name.isEmpty() && detail.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter values first!", Toast.LENGTH_LONG).show();
                    return;
                }
                mDBRef.child(id).setValue(new Person(id, name, detail));
                clearState();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString().trim();
                detail = edtDetails.getText().toString().trim();

                mDBRef.child(id).setValue(new Person(id, name, detail));
                clearState();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearState();
            }
        });
        btnUpdate.setEnabled(false);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
//                persons.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                Person person = dataSnapshot.getValue(Person.class);
                persons.add(person);
//                }
                personsAdapter = new PersonsAdapter(MainActivity.this, persons);
                recyclerView.setAdapter(personsAdapter);
                personsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Person person = dataSnapshot.getValue(Person.class);
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Person person = dataSnapshot.getValue(Person.class);
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    showAllValues();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edtSearchName.getText().toString().trim();
                if (strName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter keyword!", Toast.LENGTH_LONG).show();
                    return;
                }
                searchByName(strName);
            }
        });
    }

    private void clearState() {
        edtName.requestFocus();
        edtDetails.setText("");
        edtName.setText("");
        id = "";
        detail = "";
        name = "";
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
    }

    private void searchByName(String name) {
        persons.clear();
        personsAdapter = new PersonsAdapter(MainActivity.this, persons);
        recyclerView.setAdapter(personsAdapter);
        personsAdapter.notifyDataSetChanged();

//        queryRef = mDBRef.orderByChild("name").equalTo(name);
        queryRef = mDBRef.orderByChild("name").startAt(name).endAt(name);
        queryRef.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
//        mDBRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                persons.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Person person = postSnapshot.getValue(Person.class);
//                    persons.add(person);
//                }
//                personsAdapter = new PersonsAdapter(MainActivity.this, persons);
//                recyclerView.setAdapter(personsAdapter);
//                personsAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        showAllValues();
    }

    private void showAllValues() {
        persons.clear();
        queryRef = mDBRef.orderByKey();
        queryRef.addChildEventListener(childEventListener);
    }

    public void updateData(String mId, String name, String details) {
        edtDetails.setText(details);
        edtName.setText(name);
        id = mId;
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(true);
    }
}