package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    RecyclerView recyclerView;
    UserAdapter adapter;
    ArrayList<Users> users;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(user == null) {
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
        }

        //toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.mainUserRecyclyerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        DatabaseReference reference = database.getReference().child("user");
        users = new ArrayList<>();
        adapter = new UserAdapter(HomeActivity.this, users);

        //setSupportActionBar(toolbar);
        //require NonNull toolbar otherwise it produces NullPointerException
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("My First Toolbar");
//        toolbar.setSubtitle("Subtitle");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users newuser = dataSnapshot.getValue(Users.class);
                    users.add(newuser);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "It is cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        new MenuInflater(this).inflate(R.menu.option_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.opt_new:
//                Toast.makeText(this, "New To be added", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.opt_save:
//                Toast.makeText(this, "Save To be added", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.opt_open:
//                Toast.makeText(this, "Open To be added", Toast.LENGTH_SHORT).show();
//                break;
//            case android.R.id.home:
//                super.onBackPressed();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}