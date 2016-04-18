package locator.khpv.com.supermarket.showdata;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.showdata.model.Menu;
import locator.khpv.com.supermarket.showdata.model.Vegetable;

/**
 * Created by kh on 3/27/2016.
 */
public class ShowCompany extends Activity {

    Firebase myFirebaseRef;
    @Bind(R.id.exlContent)
    ExpandableListView expContent;
    List<String> keyHeaderList;
    Map<String, List<Vegetable>> stringListMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://giaptuyenk.firebaseio.com");
        setContentView(R.layout.activity_company_name);
        ButterKnife.bind(this);
        keyHeaderList = new ArrayList<>();
        stringListMap = new HashMap<>();

        myFirebaseRef.child("vegetableList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //list vegetable
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //read menu list
                    Vegetable vegetable = snapshot.getValue(Vegetable.class);
                    String name = vegetable.getDisplayName();
                    keyHeaderList.add(name);
                    if (stringListMap.get(name) == null)
                        stringListMap.put(name, new ArrayList<Vegetable>());
                    stringListMap.get(name).add(vegetable);

                    //cach che bien mon an
//                    myFirebaseRef.child("/vegetableList/" + snapshot.getKey() + "/menu").addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            myFirebaseRef.child("/menuList/" + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    //read data of display name of menu for user.
//                                    Log.e("hehe,final data: ", "" + dataSnapshot.getValue());
//                                }
//
//                                @Override
//                                public void onCancelled(FirebaseError firebaseError) {
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(FirebaseError firebaseError) {
//
//                        }
//                    });
                }
                setDataToView();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setDataToView() {
        ShowMenuExpAdapter showMenuExpAdapter = new ShowMenuExpAdapter(this, keyHeaderList, stringListMap);
        expContent.setAdapter(showMenuExpAdapter);
    }
}
