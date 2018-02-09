package com.ge.grahamelliott.sharkfeed.common.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

/**
 * Store images to external memory, if possible. Register with {@link SaveFileListener} weak reference to get callback.
 *
 * @author graham.elliott
 */
public class FileCachingManager {

    public static final String TAG = FileCachingManager.class.getName();

    private static final String PICTURES_DIRECTORY = "shark_feed";

    private static final String SHARK_FILE_PREFIX = "shark-";

    private static final String SHARK_FILE_SUFFIX = ".jpg";

    private Context context;

    @Inject
    public FileCachingManager(Context context) {
        this.context = context;
    }

    public void saveBitmapToExternalStorage(String filename, Bitmap bitmap,
                                            WeakReference<SaveFileListener> listener) {
        if (!isExternalStorageWritable()) {
            if (listener.get() != null) {
                listener.get().onImageSaveFailure();
            }
            return;
        }

        File rootFile = getAlbumStorageDirectory(PICTURES_DIRECTORY);
        File imgFile = new File(rootFile, SHARK_FILE_PREFIX + filename + SHARK_FILE_SUFFIX);
        if (imgFile.exists()) {
            imgFile.delete();
        }
        Log.d(TAG, String.format("Saving file %s to %s", filename, imgFile.toString()));
        try {
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            if (listener.get() != null) {
                listener.get().onFileSaved();
            }
        } catch (IOException e) {
            if (listener.get() != null) {
                listener.get().onImageSaveFailure();
            }
            Log.e(TAG, e.getLocalizedMessage());
        }

        scanNewFile(imgFile.toString());
    }

    private void scanNewFile(String filePath) {
        MediaScannerConnection.scanFile(context,
                                        new String[]{filePath},
                                        null,
                                        new MediaScannerConnection.OnScanCompletedListener() {
                                            @Override
                                            public void onScanCompleted(String path, Uri uri) {
                                                Log.i(TAG, "External storage scanned");
                                            }
                                        });
    }

    private File getAlbumStorageDirectory(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        file.mkdirs();
        return file;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public interface SaveFileListener {
        void onFileSaved();
        void onImageSaveFailure();
    }

}
