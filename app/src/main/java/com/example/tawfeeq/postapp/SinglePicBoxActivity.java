package com.example.tawfeeq.postapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglePicBoxActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private TextView singlePostTitle, singlePostDesc;
    private Button deleteBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pic_box);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("PicBox");
        singlePostImage = (ImageView) findViewById(R.id.singleImageView);
        singlePostTitle = (TextView) findViewById(R.id.singleTitle);
        singlePostDesc = (TextView) findViewById(R.id.singleDesc);

        mAuth = FirebaseAuth.getInstance();
        deleteBtn = (Button) findViewById(R.id.single_delete_button);
        deleteBtn.setVisibility(View.INVISIBLE);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singlePostTitle.setText(post_title);
                singlePostDesc.setText(post_desc);
                Picasso.with(SinglePicBoxActivity.this).load(post_image).into(singlePostImage);

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteButtonClicked(View view) {
        mDatabase.child(post_key).removeValue();
        Toast.makeText(SinglePicBoxActivity.this, "Image Deleted..", Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(SinglePicBoxActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
