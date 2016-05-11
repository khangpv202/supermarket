package locator.khpv.com.supermarket.common;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
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
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Connect the client. Once connected, the camera is launched.

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        if (mCredential.getSelectedAccountName() == null)
        {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Log.i(this.getLocalClassName(), "API client connected." + accountName);
            mCredential.setSelectedAccountName(accountName);
            return;
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
                        image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
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
                    // Store the image data as a bitmap for writing later.
                    mBitmapToSave = (Bitmap) data.getExtras().get("data");
                    getImageView().setImageBitmap(mBitmapToSave);
                }
                break;
            case REQUEST_CODE_CREATOR:
                // Called after a file is saved to Drive.
                Log.e("log 1-----------", "REQUEST_CODE_CREATOR----------------------");
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
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
                            try
                            {
                                result = mService.files().list()
                                        .setQ("name ='" + mainImageId + "'")
                                        .execute();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Log.e("deo hieu 3", "hic hic");
                            List<File> files = result.getFiles();
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
