package locator.khpv.com.supermarket.showdata;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import locator.khpv.com.supermarket.R;

/**
 * Created by kh on 3/27/2016.
 */
public class ShowCompany extends Activity {

    Firebase myFirebaseRef;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://giaptuyenk.firebaseio.com/");
        setContentView(R.layout.activity_company_name);
        tvContent = (TextView) findViewById(R.id.tvValue);
        myFirebaseRef.child("vegtable").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                tvContent.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }
}
