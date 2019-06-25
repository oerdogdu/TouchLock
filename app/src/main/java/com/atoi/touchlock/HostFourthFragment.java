package com.atoi.touchlock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class HostFourthFragment extends Fragment {

    private EditText etRoom, etBathroom, etBed, etDescription;
    private ImageView doneFab;
    private String desc;
    private int nOfRoom, nOfBathroom, nOfBed;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_fourth, container, false);

        etRoom = view.findViewById(R.id.etRoom);
        etBathroom = view.findViewById(R.id.etBathroom);
        etBed = view.findViewById(R.id.etBed);
        etDescription = view.findViewById(R.id.etDescription);

        doneFab = view.findViewById(R.id.doneButton);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc = etDescription.getText().toString().trim();
                String roomString = etRoom.getText().toString();
                String bathroomString = etBathroom.getText().toString();
                String bedString = etBed.getText().toString();

                if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(roomString) || TextUtils.isEmpty(bathroomString)
                        || TextUtils.isEmpty(bedString)) {
                    Toast.makeText(getActivity(), "Fill in All Areas!", Toast.LENGTH_LONG).show();
                } else {
                    ((HostActivity) getActivity()).fourthFragmentData(Integer.valueOf(roomString), Integer.valueOf(bathroomString),
                            Integer.valueOf(bedString), desc);

                    AlertDialog builder =
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(R.string.confirm_title)
                                    .setMessage(R.string.confirm_message)
                                    .setPositiveButton(R.string.button_confirm,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog, int button) {

                                                    ((HostActivity) getActivity()).publishAdvertisement();

                                                }
                                            }
                                    ).setNegativeButton(R.string.button_cancel, null)
                                    .create();
                    builder.show();
                }
            }
        });

        return view;
    }
}
