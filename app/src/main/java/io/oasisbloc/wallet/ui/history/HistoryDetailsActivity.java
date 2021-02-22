package io.oasisbloc.wallet.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import androidx.databinding.DataBindingUtil;

import java.io.Serializable;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.data.Lockup;
import io.oasisbloc.wallet.data.Transaction;
import io.oasisbloc.wallet.databinding.ActivityHistoryDetailsBinding;

public class HistoryDetailsActivity extends BaseActivity {

    private static final String KEY_ITEM = "ITEM";

    private ActivityHistoryDetailsBinding mBinding;

    public static void start(Context context, Transaction item) {
        Intent starter = new Intent(context, HistoryDetailsActivity.class);
        starter.putExtra(KEY_ITEM, item);
        context.startActivity(starter);
    }

    public static void start(Context context, Lockup item) {
        Intent starter = new Intent(context, HistoryDetailsActivity.class);
        starter.putExtra(KEY_ITEM, item);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBarBackButtonEnable();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_details);

        Serializable item = getIntent().getSerializableExtra(KEY_ITEM);
        if (item instanceof Transaction) {
            initTransactionDetailsView((Transaction) item);
        }
        if (item instanceof Lockup) {
            initLockupDetailsView((Lockup) item);
        }


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }

    private void initTransactionDetailsView(Transaction item) {
//        setTitle(R.string.history_details_title_transaction);

        mBinding.noLayout.setVisibility(View.GONE);
        mBinding.beginLayout.setVisibility(View.GONE);
        mBinding.endLayout.setVisibility(View.GONE);
        mBinding.memoLayout.setVisibility(TextUtils.isEmpty(item.getMemo()) ? View.GONE : View.VISIBLE);

        mBinding.logo.setImageResource(R.drawable._temp_obs_ic_1);
        mBinding.balance.setText(item.getBalance(item.getSymbol()));

        mBinding.dateValue.setText(item.getDatetime());
        mBinding.fromValue.setText(item.getFromAccount());
        mBinding.toValue.setText(item.getToAccount());
        mBinding.memoValue.setText(item.getMemo());
        mBinding.nameValue.setText(item.getType());
        if (item.isSend()) {
            mBinding.nameValue.setTextColor(getColor(R.color.text_send));
            mBinding.nameIcon.setImageResource(R.drawable.ic_transaction_send);
        }
        if (item.isReceive()) {
            mBinding.nameValue.setTextColor(getColor(R.color.text_receive));
            mBinding.nameIcon.setImageResource(R.drawable.ic_transaction_receive);
        }
        mBinding.txidValue.setText(item.getId());

        mBinding.claim.setVisibility(View.GONE);
        mBinding.explorer.setOnClickListener(v -> {
            String url = getString(R.string.const_block_explorer_transaction_url_s, item.getId());
            launchWebUrl(url);
        });
    }

    private void initLockupDetailsView(Lockup item) {
        setTitle(R.string.history_details_title_lockup);

        mBinding.dateLayout.setVisibility(View.GONE);
        mBinding.memoLayout.setVisibility(TextUtils.isEmpty(item.getMemo()) ? View.GONE : View.VISIBLE);
        mBinding.txidLayout.setVisibility(View.INVISIBLE);

        mBinding.logo.setImageResource(R.drawable._temp_obs_ic_1);
        mBinding.balance.setText(item.getBalance(item.getSymbol()));

        mBinding.noValue.setText(item.getId());
        mBinding.beginValue.setText(item.getBeginDatetime());
        mBinding.endValue.setText(item.getEndDatetime());
        mBinding.fromValue.setText(item.getFromAccount());
        mBinding.toValue.setText(item.getToAccount());
        mBinding.memoValue.setText(item.getMemo());
        if (item.isSend()) {
            mBinding.nameValue.setTextColor(getColor(R.color.text_send));
            mBinding.nameIcon.setImageResource(R.drawable.ic_transaction_send);
        }
        if (item.isReceive()) {
            mBinding.nameValue.setTextColor(getColor(R.color.text_receive));
            mBinding.nameIcon.setImageResource(R.drawable.ic_transaction_receive);
        }
        mBinding.nameValue.setText(item.getType());

        mBinding.claim.setVisibility(View.GONE);
        mBinding.explorer.setVisibility(View.INVISIBLE);
    }

    public void onClickLeftActionBarBtn(View view){
        if(view.getId() == R.id.actionbar_left){
            super.onBackPressed();
        }
    }
}
