package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE =42;
    public static final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    private String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        etDescription = findViewById( R.id.etDescription);
        btnCaptureImage = findViewById( R.id.btnCaptureImage);
        ivPostImage = findViewById( R.id.ivPostImage);
        btnSubmit = findViewById( R.id.btnSubmit);

        btnCaptureImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }

            private void launchCamera() {
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                photoFile = getPhotoFileUri(photoFileName);
                Uri fileProvider = FileProvider.getUriForFile( MainActivity.this,"com.codepath.fileprovider",photoFile );
                intent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider);

                if (intent.resolveActivity( getPackageManager() )!= null){
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );

                }
            }



            protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
//                super.onActivityResult( requestCode, resultCode, data );
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        Bitmap takenImage = BitmapFactory.decodeFile( photoFile.getAbsolutePath() );
                        ivPostImage.setImageBitmap( takenImage );
                    } else {
                        Toast.makeText( MainActivity.this, "Picture wasn't taken", Toast.LENGTH_SHORT ).show();
                    }
                }

            }

            private File getPhotoFileUri(String photoFileName) {
              File mediaStorageDir = new File(getExternalFilesDir( Environment.DIRECTORY_PICTURES),TAG);
              if(mediaStorageDir.exists()&& mediaStorageDir.mkdir()){
                  Log.d(TAG,"Failed to create directory");
              }
              return new File( mediaStorageDir.getPath()+File.separator+ photoFileName );

            }
        } );

        //queryPosts();
        btnSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText( MainActivity.this,"Description cannot be empty",Toast.LENGTH_SHORT ).show();
                    return;
                }
                if (photoFile ==null || ivPostImage.getDrawable()==null){
                    Toast.makeText( MainActivity.this,"There is no image",Toast.LENGTH_SHORT ).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser,photoFile);
            }

            private void savePost(String description, ParseUser currentUser, File photoFile) {
                Post post = new Post();
                post.setDescription( description );
                post.setImage( new ParseFile( photoFile ) );
                // post.setImage()
                post.setUser( currentUser );
                post.saveInBackground( new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                      if(e !=null) {
                          Log.e( TAG,"Error while saving" ,e);
                          Toast.makeText( MainActivity.this,"Error while saving!",Toast.LENGTH_SHORT ).show();
                      }
                      Log.i(TAG,"Post save was successful!");
                      etDescription.setText( "");
                      ivPostImage.setImageResource( 0 );
                    }
                } );
            }
        } );
    }

    private void queryPosts() {
        ParseQuery<Post>query= ParseQuery.getQuery( Post.class );
        query.include( Post.KEY_USER );
        query.findInBackground( new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e !=null){
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                for(Post post: posts){
                    Log.i(TAG,"Post"+ post.getDescription()+ "username" + post.getUser().getUsername());
                }

            }
        } );
    }
}