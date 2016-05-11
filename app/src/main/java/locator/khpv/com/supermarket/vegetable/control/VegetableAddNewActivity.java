package locator.khpv.com.supermarket.vegetable.control;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.internal.LinkedTreeMap;
import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.common.MyBaseActivity;
import locator.khpv.com.supermarket.vegetable.model.Menu;
import locator.khpv.com.supermarket.vegetable.model.Vegetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 4/20/2016.
 */
public class VegetableAddNewActivity extends MyBaseActivity
{
    Firebase myFirebaseRef;
    @Bind(R.id.etNameOfVegetable)
    EditText etNameOfVegetable;
    @Bind(R.id.etPrice)
    EditText etPrice;
    @Bind(R.id.tvChooseMenu)
    TextView tvChooseMenu;
    @Bind(R.id.etCalo)
    EditText etCalo;
    @Bind(R.id.btFinish)
    TextView tvFinish;
    @Bind(R.id.btCancel)
    TextView tvCancel;
    @Bind(R.id.llNameOfMenuList)
    LinearLayout llNameOfMenuList;
    @Bind(R.id.ivTakePhoto)
    ImageView ivTakePhoto;
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;

    Map<String, String> data;
    List<String> checkedMenu;
    String displayMenu;
    private static final String TAG = "VegetableDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vegetable_add_new_activity);
        myFirebaseRef = new Firebase("https://giaptuyenk.firebaseio.com");
        ButterKnife.bind(this);
        data = new LinkedTreeMap<>();
        displayMenu = "";
        myFirebaseRef.child("menuList").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Menu menu = snapshot.getValue(Menu.class);
                    data.put(menu.getDisplayName(), snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });
    }

    @OnClick(R.id.ivTakePhoto)
    void onClickAddAvatar()
    {

        ContentValues values = new ContentValues();
        mainImageId = UUID.randomUUID().toString();
        values.put(MediaStore.Images.Media.TITLE, mainImageId);

        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,
                REQUEST_CODE_CAPTURE_IMAGE);
    }

    @OnClick(R.id.tvChooseMenu)
    void clickChooseMenu()
    {
        new MaterialDialog.Builder(this)
                .title("Chọn Cách Chế Biến")
                .items(data.keySet())
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice()
                {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text)
                    {
                        checkedMenu = new ArrayList<>();
                        for (int i = 0; i < which.length; i++)
                        {
                            View childView = View.inflate(VegetableAddNewActivity.this, R.layout.add_vegetable_menu_item, null);
                            TextView nameItem = (TextView) childView.findViewById(R.id.tvNameItem);
                            nameItem.setText(text[i]);
                            displayMenu = displayMenu + text[i] + ",";
                            llNameOfMenuList.addView(childView);
                            checkedMenu.add(data.get(text[i]));
                        }
                        return true;
                    }
                }).cancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                llNameOfMenuList.removeAllViews();
                checkedMenu.clear();
            }
        })
                .positiveText("Confirm")
                .negativeText("Cancel")
                .show();
    }

    @OnClick(R.id.btFinish)
    void onclickFinish()
    {
        Firebase child = myFirebaseRef.child("vegetableList").push();
        Vegetable vegetable = new Vegetable();
        vegetable.setCost(etPrice.getText().toString());
        vegetable.setDisplayName(etNameOfVegetable.getText().toString());
        vegetable.setCalo(etCalo.getText().toString());
        vegetable.setMainImageID(getMainImageSharedID());
        vegetable.setDisplayMenu(displayMenu);
        child.setValue(vegetable);
        Firebase menu = child.child("menu");
        for (String keyMenu : checkedMenu)
        {
            menu.child(keyMenu).setValue("true");
        }
        finish();
    }

    @OnClick(R.id.btCancel)
    void clickCancel()
    {
        finish();
    }

    @Override
    public ImageView getImageView()
    {
        return ivAvatar;
    }
}
