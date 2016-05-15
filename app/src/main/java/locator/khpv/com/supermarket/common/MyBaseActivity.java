package locator.khpv.com.supermarket.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.plus.Plus;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import locator.khpv.com.supermarket.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 5/8/2016.
 */
public abstract class MyBaseActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "MyBaseActivity";
    GoogleApiClient mGoogleApiClient;
    public GoogleAccountCredential mCredential;
    public Bitmap mBitmapToSave;
    public String mainImageId;
    private String mainImageSharedID;
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    public static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    public static final String[] SCOPES = {DriveScopes.DRIVE_METADATA_READONLY};
    public Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Connect the client. Once connected, the camera is launched.
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        if (mCredential == null)
        {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SCOPES))
                    .setSelectedAccountName(accountName)
                    .setBackOff(new ExponentialBackOff());
            Log.i(this.getLocalClassName(), "API client connected." + accountName);
        }

        if (mBitmapToSave != null)
        {
            saveFileToDrive();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution())
        {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try
        {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        }
        catch (IntentSender.SendIntentException e)
        {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    public void saveFileToDrive()
    {
        // Start by creating a new contents, and setting a callback.
        Log.e(TAG, "Creating new contents.");
        final Bitmap image = mBitmapToSave;
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>()
                {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result)
                    {

                        if (!result.getStatus().isSuccess())
                        {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }
                        // Otherwise, we can write our data to the new contents.
                        Log.i(TAG, "New contents created.");
                        // Get an output stream for the contents.
                        DriveContents driveContents = result.getDriveContents();
                        OutputStream outputStream = driveContents.getOutputStream();
                        // Write the bitmap data from it.
                        ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 20, bitmapStream);
                        try
                        {
                            outputStream.write(bitmapStream.toByteArray());
                        }
                        catch (IOException e1)
                        {
                            Log.i(TAG, "Unable to write file contents.");
                        }
                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setMimeType("image/jpeg").setTitle(mainImageId).build();
                        // Create an intent for the file chooser, and start it.
                        IntentSender intentSender = Drive.DriveApi
                                .newCreateFileActivityBuilder()
                                .setInitialMetadata(metadataChangeSet)
//                                .setActivityStartFolder(DriveId.decodeFromString("0B8ZkS3FTNs2fdEhBYnlNb0Z3YUU"))
                                .setInitialDriveContents(driveContents)

                                .build(mGoogleApiClient);
                        try
                        {
                            startIntentSenderForResult(
                                    intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
                        }
                        catch (IntentSender.SendIntentException e)
                        {
                            Log.i(TAG, "Failed to launch file chooser.");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_CAPTURE_IMAGE:
                // Called after a photo has been taken.
                if (resultCode == Activity.RESULT_OK)
                {
                    Log.e("onActivityResult", "RgsgeEQUEST_CODE_CAPTURE_IMAGE");

                    String imageId = convertImageUriToFile(imageUri, MyBaseActivity.this);
                    Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageId);
//                    try
//                    {
                    try
                    {
                        mBitmapToSave = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), uri);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inPreferredConfig = Bitmap.Config.RGB_565;
//                        mBitmapToSave = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
//                    }
//                    catch (FileNotFoundException e)
//                    {
//                        e.printStackTrace();
//                    }
                    if (getImageView() != null)
                    {
                        getImageView().setImageBitmap(mBitmapToSave);
                    }

                }
                break;
            case REQUEST_CODE_CREATOR:
                // Called after a file is saved to Drive.
                Log.e("log 1-----------", "REQUEST_CODE_CREATOR----------------------");
                if (resultCode == RESULT_OK)
                {
                    Log.e(TAG, "Image successfully saved.");
                    mBitmapToSave = null;
                    Log.e("deo hieu 1", "hic hic");
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Log.e("deo hieu 2", "hic hic");
                            HttpTransport transport = AndroidHttp.newCompatibleTransport();
                            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                            com.google.api.services.drive.Drive mService = new com.google.api.services.drive.Drive.Builder(
                                    transport, jsonFactory, mCredential)
                                    .setApplicationName("Drive API Android Quickstart")
                                    .build();

                            FileList result = null;
                            List<File> files = null;
                            try
                            {
                                do
                                {
                                    result = mService.files().list()
                                            .setQ("name ='" + mainImageId + "'")
                                            .execute();
                                    files = result.getFiles();
                                } while (files.size() == 0);
                            }
                            catch (UserRecoverableAuthIOException e)
                            {
                                Log.e("deo hieu dsfdf3333333", "hic hic" + e.getMessage());
                                e.printStackTrace();
                            }
                            catch (IOException e)
                            {
                                Log.e("haha, lai error", "message: " + e.getMessage());
                                e.printStackTrace();
                            }
                            Log.e("deo hieu 3", "hic hic");

                            if (files != null)
                            {
                                Log.e("deo hieu 4", "hic hic" + files.size() + " id: ");
                                for (final File file : files)
                                {
                                    mainImageSharedID = file.getId();
                                    Log.e("-----------" + file.getName(), file.getId() + "");
                                }
                            }
                        }
                    }).start();
                }
                break;
        }
    }

    public static String convertImageUriToFile(Uri imageUri, Activity activity)
    {

        Cursor cursor = null;
        int imageID = 0;

        try
        {

            /*********** Which columns values want to get *******/
            String[] proj = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0)
            {


//                imageDetails.setText("No Image");
            }
            else
            {

                int thumbID = 0;
                if (cursor.moveToFirst())
                {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID = cursor.getInt(columnIndex);

                    thumbID = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            + " ImageID :" + imageID + "\n"
                            + " ThumbID :" + thumbID + "\n"
                            + " Path :" + Path + "\n";

                    // Show Captured Image detail on activity
//                    imageDetails.setText( CapturedImageDetails );

                }
            }
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return "" + imageID;
    }

    public abstract ImageView getImageView();

    @Override
    protected void onPause()
    {
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Plus.API)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();
    }

    public String getMainImageSharedID()
    {
        return mainImageSharedID;
    }
}
