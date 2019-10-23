package com.geniuskid.haveitback.activities.reportNew;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.pojos.LostItems;
import com.geniuskid.haveitback.utils.DataStorage;
import com.geniuskid.haveitback.utils.IntentHelper;
import com.geniuskid.haveitback.utils.Keys;
import com.geniuskid.haveitback.utils.imageHelpers.ImageHelpers;
import com.geniuskid.haveitback.utils.permissions.Permissions;
import com.geniuskid.haveitback.utils.permissions.PermissionsHelper;
import com.geniuskid.haveitback.views.MaterialRippleLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import butterknife.BindView;

public class CreateNewReportActivity extends AppCompatActivity {

    AppCompatEditText itemNameEd;
    AppCompatEditText placeEd;
    AppCompatEditText descEd;
    AppCompatEditText dateEd;
    ImageView itemIv;
    ImageView backIv;
    AppCompatButton addImgBtn;
    AppCompatButton uploadBtn;

    private String name = "", date = "", place = "", desc = "", imagePath = "";
    private Uri imageUri, uploadedPosterUri;

    public DatabaseReference postDbRef;
    public StorageReference storageReference;
    public FirebaseStorage firebaseStorage;
    private PermissionsHelper permissionsHelper;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_report);

        initFirebase();

        itemNameEd = findViewById(R.id.nameEd);
        placeEd = findViewById(R.id.placeEd);
        descEd = findViewById(R.id.descEd);
        dateEd = findViewById(R.id.dateEd);
        itemIv = findViewById(R.id.itemIv);
        backIv = findViewById(R.id.backIv);
        addImgBtn = findViewById(R.id.addImgBtn);
        uploadBtn = findViewById(R.id.uploadBtn);

        permissionsHelper = new PermissionsHelper(this);
        progressDialog = new ProgressDialog(this);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenCamera();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllOk()) {
                    tryToUpload();
                }
            }
        });
    }

    private void checkPermissionAndOpenCamera() {
        if(permissionsHelper.isCameraAndStoragePermissionsAvailable()) {
            showPickerDialog();
            return;
        }
        permissionsHelper.initCameraAndStoragePermissions();
        permissionsHelper.requestPermission(new PermissionsHelper.PermissionCallback() {
            @Override
            public void onGranted() {
                showPickerDialog();
            }

            @Override
            public void onSinglePermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onDenied() {

            }

            @Override
            public void onDeniedCompletely() {
                permissionsHelper.askAndOpenSettings(Permissions.CAMERA_STRING, Permissions.CANNOT_OPEN_CAMERA);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionsHelper.onRequestPermissionsResult(requestCode, Permissions.getCamAndStoragePermission(), grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentHelper.OPEN_CAMERA_INTENT:
                    imageUri = IntentHelper.getImageUri();
                    if (imageUri != null) onCameraImageReceived(imageUri);
                    else showToast("Taken image invalid, take another image!");
                    break;
                case IntentHelper.OPEN_GALLERY_INTENT:
                    if (data != null && data.getData() != null) onGalleryImageReceived(data);
                    else showToast("Chosen image invalid, choose another image!");
                    break;
            }
        }
    }

    private void onCameraImageReceived(Uri imageUri) {
        this.imageUri = imageUri;
        Bitmap thumbnail = ImageHelpers.getBitmapFromUri(this, imageUri);
        ImageHelpers.insertImage(null, thumbnail, "image", "image description");
        imagePath = ImageHelpers.getRealPathFromURI(this, imageUri);
        itemIv.setImageBitmap(thumbnail);
    }

    private void onGalleryImageReceived(Intent data) {
        imageUri = data.getData();
        Bitmap thumbnail = ImageHelpers.getBitmapFromUri(this, imageUri);
        ImageHelpers.insertImage(null, thumbnail, "image", "image description");
        imagePath = ImageHelpers.getRealPathFromURI(this, imageUri);
        itemIv.setImageBitmap(thumbnail);
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void showPickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_image_picker);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        if (!dialog.isShowing() && !isFinishing()) dialog.show();

        MaterialRippleLayout takePhoto = dialog.findViewById(R.id.takePhoto);
        MaterialRippleLayout chooseGallery = dialog.findViewById(R.id.chooseGallery);
        MaterialRippleLayout cancel = dialog.findViewById(R.id.cancel);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CreateNewReportActivity.this.startActivityForResult(IntentHelper.getCameraIntent(CreateNewReportActivity.this),
                        IntentHelper.OPEN_CAMERA_INTENT);
            }
        });
        chooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CreateNewReportActivity.this.startActivityForResult(Intent.createChooser(IntentHelper.getGalleryUriIntent(), "Choose..."),
                        IntentHelper.OPEN_GALLERY_INTENT);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        postDbRef = FirebaseDatabase.getInstance().getReference().child("LostItemsPost");
    }

    private void tryToUpload() {
        String postName = name + "_image";

        showProgress("Please wait, Uploading your report...");

        final StorageReference storageRef = storageReference.child("Posts/" + postName);
        UploadTask uploadTask = storageRef.putFile(imageUri);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(CreateNewReportActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }
                cancelProgress();

                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    uploadedPosterUri = task.getResult();
                    cancelProgress();

                    if (uploadedPosterUri != null) {
                        String photoStringLink = uploadedPosterUri.toString();
                        onImageUploaded(uploadedPosterUri);
                    } else {
                        cancelProgress();
                    }
                } else {
                    cancelProgress();
                    Toast.makeText(CreateNewReportActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onImageUploaded(Uri uploadedPosterUri) {
        DataStorage dataStorage = new DataStorage(this);
        showProgress(getString(R.string.updating_post));

        LostItems lostItems = new LostItems();
        lostItems.setName(name);
        lostItems.setDesc(desc);
        lostItems.setImage(String.valueOf(uploadedPosterUri));
        lostItems.setDate(date);
        lostItems.setPlace(place);
        lostItems.setPostedName(dataStorage.getString("username"));
        lostItems.setPostedNum(dataStorage.getString("email"));

        String id = postDbRef.push().getKey();
        lostItems.setId(id);
        lostItems.setIsClaimed("1");

        postDbRef.child(id).setValue(lostItems, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                cancelProgress();

                if (databaseError == null) {
                    Toast.makeText(CreateNewReportActivity.this, "Report creation done successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateNewReportActivity.this, "Report creation failed, try again later!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isAllOk() {
        name = Objects.requireNonNull(itemNameEd.getText()).toString().trim();
        place = Objects.requireNonNull(placeEd.getText()).toString().trim();
        date = Objects.requireNonNull(dateEd.getText()).toString().trim();
        desc = Objects.requireNonNull(descEd.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(place)) {
            Toast.makeText(this, "Enter place!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Enter description!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Enter date and time!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(imagePath)) {
            Toast.makeText(this, "Upload an image!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showProgress(String msg) {
        if (msg != null) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void cancelProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
