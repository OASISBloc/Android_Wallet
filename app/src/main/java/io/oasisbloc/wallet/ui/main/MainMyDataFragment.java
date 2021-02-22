package io.oasisbloc.wallet.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;

public class MainMyDataFragment extends BaseFragment {

    public static MainMyDataFragment newInstance() {
        Bundle args = new Bundle();
        MainMyDataFragment fragment = new MainMyDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_my_data, container, false);
    }


}
