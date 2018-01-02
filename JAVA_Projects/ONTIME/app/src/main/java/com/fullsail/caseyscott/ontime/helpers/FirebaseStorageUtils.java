// Casey Scott
// FINAL PROJECT - 1712
// FirebaseStorageUtils.java

package com.fullsail.caseyscott.ontime.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseStorageUtils {
    public  interface SetImageUrl{
        void setImageUrl(String uri);
    }
    private SetImageUrl setImageUrl;
    private final StorageReference mStorageRef;
    private final Context context;
    private Uri imageUriFromFirebase;

    public FirebaseStorageUtils(Context context){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        this.context = context;
        if(context instanceof SetImageUrl){
            setImageUrl = (SetImageUrl) context;
        }
    }

    public void uploadImageToFirebaseStorage(Uri uri){
        //Get the time to make the path unique
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        StorageReference riversRef = mStorageRef.child("images/"+String.valueOf(date.getTime()));
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        imageUriFromFirebase = taskSnapshot.getDownloadUrl();
                        if (imageUriFromFirebase != null) {
                            setImageUrl.setImageUrl(imageUriFromFirebase.getPath());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(context, "Image failed to save to storage", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    public void downloadImageFromFirebaseStorage(StorageReference reference){
//        File localFile;
//        try {
//            localFile = File.createTempFile("images", "jpg");
//            reference.getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // Successfully downloaded data to local file
//                        // ...
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context, "Logo failed to download from storage", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
