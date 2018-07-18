package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalbefamily.crm.kalbe.kalbefamily.R;

/**
 * Created by Rian Andrivani on 7/26/2017.
 */

public class FragmentRewardCard extends Fragment {
    View v;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_reward_card, container, false);
        context = getActivity().getApplicationContext();

        return v;
    }
}
