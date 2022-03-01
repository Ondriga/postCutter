package postCutter.fe.postcutter.functions;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageAction {
    private ImageAction() {
        throw new AssertionError();
    }

    public static String saveAsNew(Activity activity, Bitmap image) {
        String fileName = String.format("picture_%d", System.currentTimeMillis());
        String mediaStorePath = MediaStore.Images.Media.insertImage(activity.getContentResolver(), image, fileName, "");
        return ImageAction.getRealPathFromURI(activity, Uri.parse(mediaStorePath));
    }

    public static String save(Activity activity, Bitmap image, String imgPath) {
        File outFile = new File(imgPath);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream out = new FileOutputStream(outFile, false);
            out.write(bitmapdata);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgPath;
    }

    public static void delete(Activity activity, String imgPath) {
        Uri uri = getUriFromRealPath(activity, imgPath);
        activity.getContentResolver().delete(uri, null, null);
    }

    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);

        cursor.close();
        return path;
    }

    public static Uri getUriFromRealPath(Context context, String realPath) {
        String[] retCol = {MediaStore.Images.Media._ID};
        Cursor cur = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                retCol,
                MediaStore.MediaColumns.DATA + "='" + realPath + "'", null, null
        );
        if (cur.getCount() == 0) {
            return null;
        }
        cur.moveToFirst();
        int id = cur.getInt(cur.getColumnIndex(MediaStore.MediaColumns._ID));
        cur.close();

        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
    }

    public static Bitmap getImageOrientedCorrect(String path) {
        File file = new File(path);
        Bitmap srcBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotation = exif2degrees(exifRotation);
            Matrix matrix = new Matrix();
            if (rotation != 0){
                matrix.preRotate(rotation);
            }
            return Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int exif2degrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}