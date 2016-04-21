package locator.khpv.com.supermarket.vegetable.control;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.common.TransferObjectHolder;
import locator.khpv.com.supermarket.vegetable.model.Menu;
import locator.khpv.com.supermarket.vegetable.model.Vegetable;
import locator.khpv.com.supermarket.vegetable.model.VegetableTransferModel;

/**
 * Created by Administrator on 4/19/2016.
 */
public class VegetableDetailActivity extends Activity {

    Firebase myFirebaseRef;
    @Bind(R.id.llWholeMenu)
    LinearLayout llWholeMenu;
    @Bind(R.id.nameOfVegetable)
    TextView tvNameOfVegetable;
    @Bind(R.id.tvPrice)
    TextView tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vegetable_detail_activity);
        myFirebaseRef = new Firebase("https://giaptuyenk.firebaseio.com");
        ButterKnife.bind(this);
        Vegetable vegetable = (Vegetable) TransferObjectHolder.getInstance().getTransferData();

        tvPrice.setText(vegetable.getCost());
        tvNameOfVegetable.setText(vegetable.getDisplayName());
        for (Menu menu : vegetable.getMenu()) {
            View view = View.inflate(this, R.layout.vegetable_detail_activity_item, null);
            TextView tvNameOfFood = (TextView) view.findViewById(R.id.nameOfFood);
            TextView tvHowToMakeFood = (TextView) view.findViewById(R.id.tvHowToMakeFood);
            tvNameOfFood.setText(menu.getDisplayName());
            tvHowToMakeFood.setText(menu.getHowTo());
            llWholeMenu.addView(view);
        }
    }
}
