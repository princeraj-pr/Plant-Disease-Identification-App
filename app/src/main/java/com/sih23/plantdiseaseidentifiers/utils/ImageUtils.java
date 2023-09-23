package com.sih23.plantdiseaseidentifiers.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;

public class ImageUtils {

    public static Bitmap getCorrectOrientatedBitmap(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            // Rotate the bitmap based on the Exif orientation tag
            return rotateBitmap(bitmap, orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap rotateBitmap(Bitmap source, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return source;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return source;
        }

        try {
            Bitmap rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            source.recycle();
            return rotatedBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}

