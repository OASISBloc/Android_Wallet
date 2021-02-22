package io.oasisbloc.wallet.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.ui.wallet.TokenSendActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQrActivity extends BaseActivity {

    private IntentIntegrator qrScan;

    private static final String KEY_SYMBOL = "SYMBOL";

    public static void start(Context context, String symbol) {
        Intent starter = new Intent(context, ScanQrActivity.class);
        starter.putExtra(KEY_SYMBOL, symbol);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("Scan the other person's QRcode.");
        qrScan.setBeepEnabled(false);
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                finish();
            } else {

                String symbol = getIntent().getStringExtra(KEY_SYMBOL);
                TokenSendActivity.start(this, symbol, result.getContents());

                finish();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
