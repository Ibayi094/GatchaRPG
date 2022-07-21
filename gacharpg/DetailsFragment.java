package com.zybooks.gacharpg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    private String id, name, element, role, level;
    private TextView tvID, tvName, tvElement, tvRole, tvLevel;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View inf = inflater.inflate(R.layout.fragment_details, container, false);

        if(bundle != null) {
            id = getArguments().getString("id");
            name = getArguments().getString("name");
            element = getArguments().getString("element");
            role = getArguments().getString("role");
            level = getArguments().getString("level");

            tvID = inf.findViewById(R.id.detailsFragmentID);
            tvName = inf.findViewById(R.id.detailsFragmentName);
            tvElement = inf.findViewById(R.id.detailsFragmentElement);
            tvRole = inf.findViewById(R.id.detailsFragmentRole);
            tvLevel = inf.findViewById(R.id.detailsFragmentLevel);

            tvID.setText(id);
            tvName.setText(name);
            tvElement.setText(element);
            tvRole.setText(role);
            tvLevel.setText(level);
        }
        // Inflate the layout for this fragment
        return inf;
    }

}
