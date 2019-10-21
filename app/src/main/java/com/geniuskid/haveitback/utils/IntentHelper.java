package com.geniuskid.haveitback.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.geniuskid.haveitback.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by geniuS on 9/4/2019.
 */
public class IntentHelper {

    private Context context;

    public static final int OPEN_FOR_RESULT = 247;

    public static final int OPEN_CAMERA_INTENT = 123;
    public static final int OPEN_GALLERY_INTENT = 246;
    public static final int SELECT_CONTACT = 251;
    public static final int PICK_FILE = 252;

    public static final String CIRCULAR_REVEAL_X = "reveal_x";
    public static final String CIRCULAR_REVEAL_Y = "reveal_y";
    public static final String CIRCULAR_TRANSITION = "reveal_transition";

    public static String FILE_FORMAT_TEXT = "text/plain";
    public static String FILE_FORMAT_IMAGE = "image/*";
    public static String FILE_FORMAT_WORD = "application/msword";
    public static String FILE_FORMAT_POWERPOINT = "application/vnd.ms-powerpoint";
    public static String FILE_FORMAT_EXCEL = "application/vnd.ms-excel";
    public static String FILE_FORMAT_PDF = "application/pdf";

    private static Uri imageUri;

    public IntentHelper(Context context) {
        this.context = context;
    }

    //used to open camera and take image
    public static Intent getCameraIntent(Context context) {
        Intent intent;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description!");
        imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //this extra OP function will not deliver result via intent in onActivityResult
        //the image will be stored in the "imageUri" variable and from this it can be used

        return intent;
    }

    public static Uri getImageUri() {
        return imageUri;
    }

    //used to open gallery and pick image
    public static Intent getGalleryUriIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    //used to share text from app to other apps
    public static void shareText(Context context, String messageToShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, messageToShare);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    //returns bitmap image when called in onActivityResult while opening camera
    public static Bitmap getBitmapFromCameraData(Intent data) {
        Bitmap thumbnail = null;
        if (data != null) {
            thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            if (thumbnail != null) {
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            }
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            printLog("Camera intent data null");
        }
        return thumbnail;
    }

    //returns bitmap image when called in onActivityResult while opening gallery
    public static Bitmap getBitmapFromGalleryData(Context context, Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    private static void printLog(String msg) {
        if (BuildConfig.DEBUG)
            System.out.println(msg);
    }

    public static void pickContacts(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(ContactsContract.Contacts.CONTENT_URI, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Contact"), SELECT_CONTACT);
    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        Uri uri = Uri.fromFile(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void pickFile(Context context, String fileFormat) {
        String[] mimeTypes = new String[0];
        if (!fileFormat.equalsIgnoreCase("")) {
            mimeTypes = new String[]{fileFormat};
        }
        //String[] mimeTypes = {"image/*","application/pdf","application/msword","application/vnd.ms-powerpoint","application/vnd.ms-excel","text/plain"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            StringBuilder mimeTypesStr = new StringBuilder();
            for (String mimeType : mimeTypes) {
                mimeTypesStr.append(mimeType).append("|");
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Choose File"), IntentHelper.PICK_FILE);
    }

}
