package locator.khpv.com.supermarket.vegetable.control;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.*;
import com.google.gson.internal.LinkedTreeMap;
import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.vegetable.model.Menu;
import locator.khpv.com.supermarket.vegetable.model.Vegetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 4/20/2016.
 */
public class VegetableAddNewActivity extends Activity
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

    Map<String, String> data;
    List<String> checkedMenu;
    String displayMenu;

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
                        checkedMenu = new ArrayList<String>();
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
}
