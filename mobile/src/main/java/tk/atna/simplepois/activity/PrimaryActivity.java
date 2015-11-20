package tk.atna.simplepois.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import butterknife.ButterKnife;
import tk.atna.simplepois.R;
import tk.atna.simplepois.fragment.InteractiveFragment;
import tk.atna.simplepois.fragment.PlaceFragment;
import tk.atna.simplepois.receiver.LocalBroadcaster;
import tk.atna.simplepois.stuff.GPServicesHelper;

public class PrimaryActivity extends BaseActivity
                                implements InteractiveFragment.FragmentActionCallback {

    private static final int PLACE_PICKER_REQUEST_CODE = 0x0000ff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        ButterKnife.inject(this);
        // check google play services availability
        boolean gpServicesAvailable =
                GPServicesHelper.checkGPServices(this, new OnCancelDialogListener());
        // activity just launched
        if(savedInstanceState == null) {
            if(gpServicesAvailable)
                showPlacePicker();
        }

        loadFragment(null);
    }

    @Override
    void initToolbar(Toolbar toolbar) {
        if(toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.setNavigationIcon(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if(data != null) {
                    LocalBroadcaster.sendLocalBroadcast(
                            LocalBroadcaster.ACTION_DATA, PlaceFragment.prepareData(data), this);
                }
            }
        }
    }

    @Override
    public void onAction(int action, Bundle data) {
        switch (action) {
            case InteractiveFragment.ACTION_NEXT:
                showPlacePicker();
                break;
        }
    }

    private void showPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            GPServicesHelper.showGPServicesRecoverDialog(
                    this, e.getConnectionStatusCode(), new OnCancelDialogListener());

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            GPServicesHelper.showGPServicesErrorDialog(this, new OnCancelDialogListener());
        }
    }

    private void loadFragment(Intent placeIntent) {
        FragmentManager fm = getSupportFragmentManager();
        // if fragment created earlier
        PlaceFragment fragment =
                (PlaceFragment) fm.findFragmentByTag(PlaceFragment.TAG);
        // no such fragment found
        if (fragment == null)
            fragment = PlaceFragment.newInstance(
                    PlaceFragment.class, PlaceFragment.prepareData(placeIntent));
        // place result fragment
        fm.beginTransaction()
                .replace(R.id.container, fragment, PlaceFragment.TAG)
                .commitAllowingStateLoss();
    }


    private class OnCancelDialogListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialog) {
            PrimaryActivity.this.finish();
        }
    }

}
