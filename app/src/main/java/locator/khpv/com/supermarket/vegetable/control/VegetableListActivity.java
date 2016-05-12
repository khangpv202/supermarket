package locator.khpv.com.supermarket.vegetable.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.firebase.client.*;
import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.common.TransferObjectHolder;
import locator.khpv.com.supermarket.vegetable.adapter.ShowMenuAdapter;
import locator.khpv.com.supermarket.vegetable.model.Menu;
import locator.khpv.com.supermarket.vegetable.model.Vegetable;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kh on 3/27/2016.
 */
public class VegetableListActivity extends Activity
{
    Firebase myFirebaseRef;
    @Bind(R.id.lvContent)
    ListView lvContent;
    List<Vegetable> vegetableList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vegetable_list_activity);
        myFirebaseRef = new Firebase("https://giaptuyenk.firebaseio.com");
        ButterKnife.bind(this);
        final List<String> keyMenu = new ArrayList<>();
        myFirebaseRef.child("vegetableList").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //list vegetable
                vegetableList = new ArrayList<>();
                String lastKey = "";
                for (final DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    lastKey = snapshot.getKey();
                    //read menu list
                    final Vegetable vegetable = snapshot.getValue(Vegetable.class);
                    vegetableList.add(vegetable);
//                    //cach che bien mon an
                    myFirebaseRef.child("/vegetableList/" + snapshot.getKey() + "/menu").addChildEventListener(new ChildEventListener()
                    {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s)
                        {
                            myFirebaseRef.child("/menuList/" + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    //read data of display name of menu for user.
                                    Log.e("hehe,final data: ", "" + dataSnapshot.getValue(Menu.class));
                                    vegetable.getMenu().add(dataSnapshot.getValue(Menu.class));
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError)
                                {
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s)
                        {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot)
                        {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s)
                        {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError)
                        {

                        }
                    });
                }
                setDataToView();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });
    }

    private void setDataToView()
    {
        lvContent.setAdapter(new ShowMenuAdapter(this, android.R.layout.simple_list_item_1, vegetableList));
    }

    @OnItemClick(R.id.lvContent)
    void clickViewDetail(int position)
    {
        Intent intent = new Intent(this, VegetableDetailActivity.class);
        TransferObjectHolder.getInstance().setTransferData(vegetableList.get(position));
        startActivity(intent);
    }

    @OnClick(R.id.btAddNewVegetable)
    void clickAddItem()
    {
        startActivity(new Intent(this, VegetableAddNewActivity.class));
    }
}
