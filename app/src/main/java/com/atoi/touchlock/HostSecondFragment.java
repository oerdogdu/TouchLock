package com.atoi.touchlock;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atoi.touchlock.Adapters.GalleryAdapter;
import com.atoi.touchlock.Utils.FileUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class HostSecondFragment extends Fragment {

    private FloatingActionButton fabPhoto;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private String city, country, fullAddress, address, adminParts;
    private ImageView nextFab;
    private ViewPager viewPager;
    private EditText etCountry, etCity, etAddress;
    private String apiKey = "AIzaSyCRFI0l-RD3T-eYPcNRPRahLgzs3lwkFBo";
    private ArrayList<Uri> imagesUriArrayList = new ArrayList<>();
    private ArrayList<MultipartBody.Part> images = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_second, container, false);

        viewPager = getActivity().findViewById(R.id.pager);

        Places.initialize(getActivity().getApplicationContext(), apiKey, Locale.US);
        PlacesClient placesClient = Places.createClient(getActivity());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.GEOCODE);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS_COMPONENTS, Place.Field.NAME));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String address="";
                // TODO: Get info about the selected place.
                Log.i("placetry", "Place: " + place.getName() + ", " + place.getAddressComponents().asList());
                ArrayList<AddressComponent> addressComponents = (ArrayList<AddressComponent>) place.getAddressComponents().asList();
                for(int i=0;i<addressComponents.size();i++) {
                    address+=addressComponents.get(i).getName()+",";
                }
                processAddress(address);
                Log.i("address", "Adress: " +address);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("placetry", "An error occurred: " + status);
            }
        });

        recyclerView = view.findViewById(R.id.imageGallery);
        recyclerView.addItemDecoration(new HostSecondFragment.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        etCountry = view.findViewById(R.id.etCounty);
        etCountry.setEnabled(false);
        etCity = view.findViewById(R.id.etCity);
        etCity.setEnabled(false);

        etAddress = view.findViewById(R.id.etAddress);

        fabPhoto = view.findViewById(R.id.fabCamera);
        fabPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });

        nextFab = view.findViewById(R.id.nextButton);
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = etAddress.getText().toString();
                city = etCity.getText().toString();
                country = etCountry.getText().toString();
                if(TextUtils.isEmpty(city) || TextUtils.isEmpty(country) || TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Fill in All Areas!", Toast.LENGTH_LONG).show();
                }
                else {
                    ((HostActivity)getActivity()).secondFragmentData(country, city, address, images);
                    viewPager.setCurrentItem(getItem(+1), true);
                }
            }
        });

        return view;
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                 ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        5);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        5);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            dispatchPickPictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchPickPictureIntent();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void processAddress(String addressString) {
        String[] addressParts = addressString.split(",");

        for(int i=0;i<3;i++) {
            fullAddress+=addressParts[i] + " ";
        }

        fullAddress= fullAddress.replace("null", "");
        etAddress.setText(fullAddress.trim());
        Log.d("address2", fullAddress);
        etCity.setText(addressParts[3]);
        etCountry.setText(addressParts[4]);
    }

    private void dispatchPickPictureIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
                if(resultCode == RESULT_OK){
                    if(imageReturnedIntent.getClipData() != null) {
                        if (imageReturnedIntent.getClipData().getItemCount() > 5) {
                            Snackbar snackbar = Snackbar
                                    .make(fabPhoto, "You can not select more than 5 images", Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent();
                                            intent.setType("image/*");
                                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE);
                                        }
                                    });
                            snackbar.setActionTextColor(Color.BLACK);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.BLACK);
                            snackbar.show();

                        } else {
                            imagesUriArrayList.clear();
                            for (int i = 0; i < imageReturnedIntent.getClipData().getItemCount(); i++) {
                                imagesUriArrayList.add(imageReturnedIntent.getClipData().getItemAt(i).getUri());
                                String filePath = FileUtils.getPath(getContext(), imageReturnedIntent.getClipData().getItemAt(i).getUri());
                                File file = new File(filePath);
                                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                MultipartBody.Part image = MultipartBody.Part.createFormData("uploadfile", file.getName(), fileBody);
                                images.add(image);

                            }
                            Log.e("SIZE", imagesUriArrayList.size() + "");
                            galleryAdapter = new GalleryAdapter(getContext(), imagesUriArrayList);
                            recyclerView.setAdapter(galleryAdapter);
                            galleryAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Uri uri = imageReturnedIntent.getData();
                        imagesUriArrayList.add(uri);
                        galleryAdapter = new GalleryAdapter(getContext(), imagesUriArrayList);
                        recyclerView.setAdapter(galleryAdapter);
                        galleryAdapter.notifyDataSetChanged();
                        String filePath = FileUtils.getPath(getContext(), uri);
                        File file = new File(filePath);
                        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part image = MultipartBody.Part.createFormData("uploadfile", file.getName(), fileBody);
                        images.add(image);
                    }
                }
                }

        }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

   /* @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> images = ImagePicker.getImages(data);
            if (images != null && !images.isEmpty()) {
                photoList.addAll(images);
                galleryAdapter = new GalleryAdapter(getActivity(), photoList);
                recyclerView.setAdapter(galleryAdapter);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;


        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
