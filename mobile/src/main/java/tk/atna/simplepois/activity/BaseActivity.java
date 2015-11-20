package tk.atna.simplepois.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import tk.atna.simplepois.R;

public abstract class BaseActivity extends AppCompatActivity {


    public static void display(Context context, Class clazz) {
        display(context, clazz, null);
    }

    public static void display(Context context, Class clazz, Bundle data) {
        if(context != null && clazz != null) {
            Intent intent = new Intent(context, clazz);
            // set additional data if needed
            if(data != null)
                intent.putExtras(data);
            // launch
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // orientation change is available only for tablets
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar(toolbar);
        // shadow under toolbar on new devices
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View shadow = findViewById(R.id.shadow_prelollipop);
            shadow.setVisibility(View.GONE);
            toolbar.setElevation(8);
        }
    }

    @Override
    public void setContentView(int layoutResId) {
        getLayoutInflater().inflate(layoutResId, (ViewGroup) findViewById(R.id.base_container));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    abstract void initToolbar(Toolbar toolbar);

}
