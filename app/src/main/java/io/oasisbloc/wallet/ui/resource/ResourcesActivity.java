package io.oasisbloc.wallet.ui.resource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.databinding.ActivityResourcesBinding;
import io.oasisbloc.wallet.ui.resource.viewmodel.ResourcesViewModel;

public class ResourcesActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ResourcesActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBarBackButtonEnable();

        ActivityResourcesBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_resources);
        binding.pagerView.setAdapter(new MyViewPagerAdapter());
        binding.pagerView.setVisibility(View.GONE);
        binding.tabLayout.setupWithViewPager(binding.pagerView);

        ResourcesViewModel viewModel = ResourcesViewModel.get(this, getLifecycle());
        viewModel.getResources().observe(
                this,
                () -> {
                    binding.pagerView.setVisibility(View.VISIBLE);
                    binding.messageView.setText("");
                },
                error -> {
                    binding.pagerView.setVisibility(View.GONE);
                    binding.messageView.setText(ExceptionUtils.getMessage(error));
                },
                loading -> {
                    if (loading) {
                        binding.progressView.setVisibility(View.VISIBLE);
                    } else {
                        binding.progressView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }



    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mFragments = new Fragment[]{
                ResourcesCpuNetFragment.newInstance(),
                ResourcesRamFragment.newInstance()
        };

        private MyViewPagerAdapter() {
            super(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return getString(R.string.resource_tab_cpu_net);
            else if (position == 1) return getString(R.string.resource_tab_ram);
            else return super.getPageTitle(position);
        }
    }

    public void onClickLeftActionBarBtn(View view){
        if(view.getId() == R.id.actionbar_left){
            super.onBackPressed();
        }
    }
}
