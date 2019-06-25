package com.atoi.touchlock;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atoi.touchlock.Utils.DateConverter;
import com.satsuware.usefulviews.LabelledSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HostFirstFragment extends Fragment {

    private EditText etCheckIn, etCheckOut, etName, etPrice;
    final Calendar myCalendar = Calendar.getInstance();
    private ViewPager viewPager;
    private ImageView nextFab;
    private String hostType = "Full House", adName, currency;
    private String[] checkInString, checkOutString;
    private int adPrice, minDay;
    private String[] minCheckIn, minCheckOut;
    private LabelledSpinner minSpinner;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_first, container, false);

        viewPager = getActivity().findViewById(R.id.pager);

        LabelledSpinner currencySpinner = view.findViewById(R.id.currency_spinner);
        currencySpinner.setItemsArray(R.array.currency_array);
        currencySpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                currency  = (String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        minSpinner = view.findViewById(R.id.min_spinner);

        RadioGroup rb = view.findViewById(R.id.houseGroup);
        rb.check(R.id.rbFull);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFull:
                        hostType = "Full House";
                        break;
                    case R.id.rbSingle:
                        hostType = "Single Room";
                        break;
                }
            }

        });

        nextFab = view.findViewById(R.id.nextButton);
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInString = etCheckIn.getText().toString().split(":");
                checkOutString = etCheckOut.getText().toString().split(":");
                String checkInDateText = checkInString[1].toString().trim();
                String checkOutDateText = checkOutString[1].toString().trim();
                adName = etName.getText().toString().trim();
                String stringAdPrice = etPrice.getText().toString().trim();

                if(TextUtils.isEmpty(checkInDateText) || TextUtils.isEmpty(checkOutDateText) || TextUtils.isEmpty(adName)
                    || TextUtils.isEmpty(stringAdPrice)) {
                    Toast.makeText(getActivity(), "Fill in All Areas!", Toast.LENGTH_LONG).show();
                }
                else {
                    Date checkInDate = DateConverter.toDate(checkInDateText);
                    Date checkOutDate = DateConverter.toDate(checkOutDateText);
                    Log.d("chedsda", DateConverter.fromDate(checkInDate));
                    adPrice = Integer.valueOf(stringAdPrice);
                    ((HostActivity)getActivity()).firstFragmentData(adName, adPrice, hostType, checkInDate, checkOutDate, currency, minDay);
                    viewPager.setCurrentItem(getItem(+1), true);
                }
            }
        });

        etName = view.findViewById(R.id.etName);
        etPrice = view.findViewById(R.id.etPrice);

        etCheckIn = view.findViewById(R.id.etCheckIn);
        etCheckOut = view.findViewById(R.id.etCheckOut);

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

        final DatePickerDialog.OnDateSetListener dateOut = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateCheckOut();
            }

        };

        etCheckIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), dateIn, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateOut, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return view;
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private void updateCheckIn() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etCheckIn.setText("Check-In Date:    " + sdf.format(myCalendar.getTime()));
    }


    private void updateCheckOut() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etCheckOut.setText("Check-Out Date:    " + sdf.format(myCalendar.getTime()));

        minCheckIn = etCheckIn.getText().toString().split(":");
        minCheckOut = etCheckOut.getText().toString().split(":");
        String checkInDateText = minCheckIn[1].toString().trim();
        String checkOutDateText = minCheckOut[1].toString().trim();

        Date checkInDate = DateConverter.toDate(checkInDateText);
        Date checkOutDate = DateConverter.toDate(checkOutDateText);

        int diff = daysBetween(checkInDate, checkOutDate);
        ArrayList<Integer> diffList = new ArrayList<>();

        for(int i=1;i<=diff;i++) {
            diffList.add(i);
        }

        minSpinner.setItemsArray(diffList);
        minSpinner.setEnabled(true);
        minSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                minDay  = (int) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
    }
}
