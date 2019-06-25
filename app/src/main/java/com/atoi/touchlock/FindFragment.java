package com.atoi.touchlock;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atoi.touchlock.Data.Remote.AdvertisementApiCall;
import com.atoi.touchlock.Data.Remote.AdvertisementService;
import com.atoi.touchlock.POJO.Advertisement;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFragment extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    private TextView checkInDateTv;
    private String city, country, type, checkInDateText;
    private ArrayList<Advertisement> advertisementList = new ArrayList<Advertisement>();
    private Date checkInDate;
    private String apiKey = "AIzaSyCRFI0l-RD3T-eYPcNRPRahLgzs3lwkFBo";
    private EditText etCountry;
    private Context contextThemeWrapper;

    public static FindFragment newInstance()
    {
        FindFragment findFragment = new FindFragment();
        return findFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.HostTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.find_fragment, container, false);
        checkInDateTv = view.findViewById(R.id.checkInText);

        etCountry = view.findViewById(R.id.etCounty);

        final RadioGroup houseGroup = view.findViewById(R.id.houseGroup);
        AppCompatButton findButton = view.findViewById(R.id.searchButton);

        Places.initialize(getActivity().getApplicationContext(), apiKey, Locale.US);
        PlacesClient placesClient = Places.createClient(getActivity());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("placetry", "Place: " + place.getName());
                city = place.getName();
                ArrayList<AddressComponent> addressComponents = (ArrayList<AddressComponent>) place.getAddressComponents().asList();
                country = addressComponents.get(2).getName();
                etCountry.setText(country);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        checkInDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(contextThemeWrapper, dateIn, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        type = "Full House";

        houseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFull:
                        type = "Full House";
                        break;
                    case R.id.rbSingle:
                        type = "Single Room";
                        break;
                }
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInDateText = checkInDateTv.getText().toString().trim();
                if((TextUtils.isEmpty(checkInDateText) || checkInDateText.equalsIgnoreCase("Tap to Select"))
                        || TextUtils.isEmpty(type) || TextUtils.isEmpty(city) || TextUtils.isEmpty(country)) {
                    Toast.makeText(getActivity(), "Fill in All Areas!", Toast.LENGTH_LONG).show();

                }
                else {

                    AdvertisementService advertisementService = AdvertisementApiCall.prepareCall();
                    Call<List<Advertisement>> findCall = advertisementService.findAd(country, city, checkInDateText, type);
                    findCall.enqueue(new Callback<List<Advertisement>>() {
                        @Override
                        public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
                            if(response.body() == null) {
                                Toast.makeText(getActivity(), "Could not find any ad!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                advertisementList.clear();
                                advertisementList.addAll(response.body());
                                Log.d("adSearch", advertisementList.size()+"");
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(getActivity(), SearchResult.class);
                                intent.putExtra("list", advertisementList);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Advertisement>> call, Throwable throwable) {
                            Toast.makeText(getActivity(),throwable.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });

        return view;
    }

    final DatePickerDialog.OnDateSetListener dateIn = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateCheckIn();
        }

    };

    private void updateCheckIn() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        checkInDateTv.setText(sdf.format(myCalendar.getTime()));
    }

}
