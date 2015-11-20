package tk.atna.simplepois.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import tk.atna.simplepois.R;
import tk.atna.simplepois.receiver.LocalBroadcaster;

public class PlaceFragment extends InteractiveFragment {

    public static final String TAG = PlaceFragment.class.getName();

    private Intent placeIntent;

    @InjectView(R.id.vg_no_place)
    ViewGroup vgNoPlace;

    @InjectView(R.id.vg_place_data)
    ViewGroup vgPlaceData;

    @InjectView(R.id.tv_place_name)
    TextView tvPlaceName;

    @InjectView(R.id.tv_place_coords)
    TextView tvPlaceCoords;

    @InjectView(R.id.tv_place_rating)
    TextView tvPlaceRating;

    @InjectView(R.id.tv_place_address)
    TextView tvPlaceAddress;

    @InjectView(R.id.tv_place_phone)
    TextView tvPlacePhone;

    @InjectView(R.id.tv_place_web)
    TextView tvPlaceWeb;


    public static Bundle prepareData(Intent placeIntent) {
        Bundle data = new Bundle();
        data.putParcelable(TAG, placeIntent);
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateViews();
    }

    @Override
    protected void processArguments(@NonNull Bundle args) {
        placeIntent = args.getParcelable(TAG);
    }

    @Override
    public void onReceive(int action, Bundle data) {
        switch (action) {
            case LocalBroadcaster.ACTION_DATA:
                processArguments(data != null ? data : prepareData(null));
                updateViews();
                break;
        }
    }

    @OnClick({ R.id.btn_try_again, R.id.btn_pick_other })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_pick_other:
                makeFragmentAction(ACTION_NEXT, null);
                break;
        }
    }

    private void updateViews() {
        if(placeIntent == null)
            showNoPLace();
        else
            showPlaceData();
    }

    private void showNoPLace() {
        vgPlaceData.setVisibility(View.GONE);
        vgNoPlace.setVisibility(View.VISIBLE);
    }

    private void showPlaceData() {
        if(getActivity() != null) {
            Place place = PlacePicker.getPlace(placeIntent, getActivity());
            tvPlaceName.setText(place.getName());
            tvPlaceCoords.setText(getCoords(place.getLatLng()));
            tvPlaceRating.setText(String.format(
                    Locale.getDefault(), getString(R.string.rating) + ": %.2f", place.getRating()));
            tvPlaceAddress.setText(place.getAddress());
            tvPlacePhone.setText(place.getPhoneNumber());
            tvPlaceWeb.setText(
                    place.getWebsiteUri() != null ? place.getWebsiteUri().toString() : null);
            vgNoPlace.setVisibility(View.GONE);
            vgPlaceData.setVisibility(View.VISIBLE);
        }
    }

    private String getCoords(LatLng latLng) {
        if(latLng != null)
            return String.format(Locale.getDefault(),
                    "(%.4f, %.4f)", latLng.latitude, latLng.longitude);

        return null;
    }

}
