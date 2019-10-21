package com.geniuskid.haveitback.utils.permissions;

import android.Manifest;

/**
 * Created by geniuS on 7/10/2019.
 */
public class Permissions {
    public static final String appName = "FAI";

    public static final String READ_CONTACTS_STRING = appName + " needs to use contacts permission to access contacts, allow permission in settings.";
    public static final String CAMERA_STRING = appName + " needs to use camera and storage permission to take pictures and pick images, allow permission in settings.";

    public static final String CANNOT_READ_CONTACTS = "Sorry, cannot open contacts without permission!";
    public static final String CANNOT_OPEN_CAMERA = "Sorry, cannot open camera without permission!";

    public static String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String CAMERA = Manifest.permission.CAMERA;
    public static String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;

    public static String[] getReadContactsPermission() {
        return new String[]{READ_CONTACTS};
    }

    public static String[] getCamAndStoragePermission() {
        return new String[]{READ_STORAGE, WRITE_STORAGE, CAMERA};
    }

}
