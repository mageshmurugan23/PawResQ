package com.shrinjal.care4paws.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtil {

    public static String getPath(Context context, Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex =
                        cursor.getColumnIndexOrThrow(
                                MediaStore.Images.Media.DATA
                        );
                return cursor.getString(columnIndex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }
}
