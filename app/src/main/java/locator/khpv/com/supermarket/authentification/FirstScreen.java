package locator.khpv.com.supermarket.authentification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import locator.khpv.com.supermarket.R;

/**
 * Created by kh on 3/27/2016.
 */
public class FirstScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);
    }

    public void actionLogin(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


    public void actionSignUp(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
}
