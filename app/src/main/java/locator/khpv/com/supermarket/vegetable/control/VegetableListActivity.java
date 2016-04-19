package locator.khpv.com.supermarket.vegetable.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.common.TransferObjectHolder;
import locator.khpv.com.supermarket.vegetable.adapter.ShowMenuAdapter;
import locator.khpv.com.supermarket.vegetable.model.Menu;
import locator.khpv.com.supermarket.vegetable.model.Vegetable;
import locator.khpv.com.supermarket.vegetable.model.VegetableTransferModel;

/**
 * Created by kh on 3/27/2016.
 */
public class VegetableListActivity extends Activity {

    Firebase myFirebaseRef;
    @Bind(R.id.lvContent)
    ListView lvContent;
    List<String> keyHeaderList;
    Map<String, List<Vegetable>> stringListMap;
    List<Vegetable> vegetableList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vegetable_list_activity);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://giaptuyenk.firebaseio.com");
        ButterKnife.bind(this);
        keyHeaderList = new ArrayList<>();
        stringListMap = new HashMap<>();
        vegetableList = new ArrayList<>();

        myFirebaseRef.child("vegetableList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //list vegetable
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //read menu list
                    final Vegetable vegetable = snapshot.getValue(Vegetable.class);
                    String name = vegetable.getDisplayName();
                    keyHeaderList.add(name);
                    if (stringListMap.get(name) == null)
                        stringListMap.put(name, new ArrayList<Vegetable>());
                    stringListMap.get(name).add(vegetable);
                    vegetableList.add(vegetable);
                    //cach che bien mon an
                    myFirebaseRef.child("/vegetableList/" + snapshot.getKey() + "/menu").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFirebaseRef.child("/menuList/" + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //read data of display name of menu for user.
//                                    Log.e("hehe,final data: ", "" + dataSnapshot.getValue(Menu.class));
                                    vegetable.getMenu().add(dataSnapshot.getValue(Menu.class));
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                setDataToView();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setDataToView() {
        lvContent.setAdapter(new ShowMenuAdapter(this, android.R.layout.simple_list_item_1, vegetableList));
    }

    @OnItemClick(R.id.lvContent)
    void clickViewDetail(int position) {
        Intent intent = new Intent(this, VegetableDetailActivity.class);
        TransferObjectHolder.getInstance().setTransferData(vegetableList.get(position));
        startActivity(intent);
    }
}
