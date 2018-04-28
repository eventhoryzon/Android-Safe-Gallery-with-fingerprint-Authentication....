package sge.cs.stir.ac.uk.mygallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

/**
 * Map fragment implements onMapReadyCallback to get markers.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng imagemapper = null;

    /**
     * inflating the map with the fragment_map Layout.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    /**
     * After Creation of view we get fragment and sync the map with it.
     * @param view
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment myMAPF = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);
    }

    /**
     * OnMapready() is called after view is created.
     * It sets markers on different locations as per request.
     * @param googleMap
     *
     * @return
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<ImageDetails> imageDetails = ImageDetails.listAll(ImageDetails.class);

        for (int i = 0; i < imageDetails.size(); i++) {
//            Log.i("Lat", imageDetails.get(i).getLatitude().toString());
//            Log.i("Long", imageDetails.get(i).getLongitude().toString());

//        Float value of a compressed bitmap is lost due to compression(bitmap)
            //Thus a null value is saved in the database

//             lat = imageDetails.get(i).getLatitude();/////nullvalue The image has no TAGS
//             lng = imageDetails.get(i).getLongitude();/////nullvalue The image has no TAGS


            imagemapper = new LatLng(22.0,22.0);


            mMap.addMarker(new MarkerOptions().position(imagemapper).title(imageDetails.get(i).Gettitle()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(imagemapper));
        }
    }
}
