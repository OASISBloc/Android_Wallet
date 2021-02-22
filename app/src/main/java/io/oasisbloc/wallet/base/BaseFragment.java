package io.oasisbloc.wallet.base;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected BaseActivity getBaseActivity() {
        return ((BaseActivity) requireActivity());
    }
}
