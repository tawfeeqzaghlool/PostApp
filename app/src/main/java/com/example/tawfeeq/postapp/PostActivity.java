package com.example.tawfeeq.postapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1, CAMERA_REQUEST = 2;;
    private Uri uri = null;
    private ImageButton imageButton;
    private EditText editName, editDesc;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editName = findViewById(R.id.edit_name);
        editDesc = findViewById(R.id.edit_desc);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("PicBox");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
    }


    public void imageButtonClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = findViewById(R.id.imagebutton);
            imageButton.setImageURI(uri);
        }
    }

    
    public void submitButtonClicked(View view){

        final String titleValue = editName.getText().toString().trim();
        final String descValue = editDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue)){
            StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloaduri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostActivity.this, "Upload complete", Toast.LENGTH_LONG).show();
                    final DatabaseReference newPost = databaseReference.push();


                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(titleValue);
                            newPost.child("desc").setValue(descValue);
                            newPost.child("image").setValue(downloaduri.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent mainActivityIntent = new Intent(PostActivity.this, MainActivity.class);
                                        startActivity(mainActivityIntent);
                                    }

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
        }
    }
}
