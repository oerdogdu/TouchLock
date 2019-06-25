package com.atoi.touchlock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HostThirdFragment extends Fragment {

    private boolean wifi, airConditioner, petAllowed, fireExt, basicNeeds, tvAvailable;
    private ImageView doneFab;
    private ViewPager viewPager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_third, container, false);
        CheckBox cbWifi, cbFire, cbAir, cbBasic, cbPet, cbTv;

        viewPager = getActivity().findViewById(R.id.pager);

        cbWifi = view.findViewById(R.id.cbWifi);
        cbWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    wifi = true;
                }
            }
        });

        cbAir = view.findViewById(R.id.cbAir);
        cbAir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    airConditioner = true;
                }
            }
        });

        cbFire = view.findViewById(R.id.cbFire);
        cbFire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    fireExt = true;
                }
            }
        });

        cbBasic = view.findViewById(R.id.cbBasic);
        cbBasic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    basicNeeds = true;
                }
            }
        });

        cbTv = view.findViewById(R.id.cbTv);
        cbTv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvAvailable = true;
            }
        });

        cbPet = view.findViewById(R.id.cbPet);
        cbPet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    petAllowed = true;
                }
            }
        });

        doneFab = view.findViewById(R.id.nextButton);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HostActivity)getActivity()).thirdFragmentData(wifi, tvAvailable, airConditioner, petAllowed,
                                                                fireExt, basicNeeds);

                viewPager.setCurrentItem(getItem(+1), true);

            }
        });

        return view;
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

}
