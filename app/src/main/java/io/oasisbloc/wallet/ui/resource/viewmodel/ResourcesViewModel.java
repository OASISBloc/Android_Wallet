package io.oasisbloc.wallet.ui.resource.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.livedata.SingleLiveData;
import io.oasisbloc.wallet.data.TokenResources;
import io.oasisbloc.wallet.model.WalletModel;

public class ResourcesViewModel extends BaseViewModel {

    private SingleLiveData<TokenResources> resources = new SingleLiveData<>();

    public static ResourcesViewModel get(FragmentActivity activity, Lifecycle lifecycle) {
        ResourcesViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ResourcesViewModel();
            }
        }).get(ResourcesViewModel.class);
        lifecycle.addObserver(vm);
        return vm;
    }

    private ResourcesViewModel() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchResources();
    }

    /*
        getter
     */

    public SingleLiveData<TokenResources> getResources() {
        return resources;
    }

    /*
        action
     */

    private void fetchResources() {
        resources.setLoading(true);
        disposable = WalletModel.getResources().subscribe(
                result -> {
                    resources.setResult(result, null);
                    resources.setLoading(false);
                },
                throwable -> {
                    resources.setResult(null, throwable);
                    resources.setLoading(false);
                }
        );
    }
}
