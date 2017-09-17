package com.recipe.r.ui.activity.order;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.recipe.r.R;
import com.recipe.r.events.Invoice;
import com.recipe.r.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hht on 2016/10/26.
 * 发票界面
 */

public class InvoiceActivity extends BaseActivity implements View.OnClickListener {
    private TextView invoice_selfTv, invoice_unitTv, invocie_sureTv;
    private EditText invoice_Et;
    private boolean selfSelected = false;
    private boolean unitSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        invoice_selfTv = (TextView) findViewById(R.id.invoice_selfTv);
        invoice_unitTv = (TextView) findViewById(R.id.invoice_unitTv);
        invocie_sureTv = (TextView) findViewById(R.id.invocie_sureTv);
        invoice_Et = (EditText) findViewById(R.id.invoice_Et);
        invoice_selfTv.setOnClickListener(this);
        invoice_unitTv.setOnClickListener(this);
        invocie_sureTv.setOnClickListener(this);
        invocie_sureTv.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invocie_sureTv:
                if (selfSelected) {
                    EventBus.getDefault().post(new Invoice("个人"));
                    this.finish();
                } else if (unitSelected) {
                    if (invoice_Et.getText() == null || "".equals(invoice_Et.getText().toString())) {
                        Toast.makeText(this, "请输入单位信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EventBus.getDefault().post(new Invoice(invoice_Et.getText().toString()));
                    this.finish();
                } else {
                    EventBus.getDefault().post(new Invoice(""));
                }
                break;
            case R.id.invoice_selfTv:
                if (selfSelected) {
                    selfSelected = false;
                    invocie_sureTv.setClickable(false);
                    invoice_selfTv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                    invoice_unitTv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                } else {
                    selfSelected = true;
                    unitSelected = false;
                    invocie_sureTv.setClickable(true);
                    invoice_selfTv.setTextColor(ContextCompat.getColor(this, R.color.main_color));
                    invoice_unitTv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                }
                invoice_Et.setVisibility(View.GONE);
                break;
            case R.id.invoice_unitTv:
                if (unitSelected) {
                    unitSelected = false;
                    invocie_sureTv.setClickable(false);
                    invoice_unitTv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                    invoice_selfTv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                    invoice_Et.setVisibility(View.GONE);

                } else {
                    unitSelected = true;
                    selfSelected = false;
                    invocie_sureTv.setClickable(true);
                    invoice_unitTv.setTextColor(ContextCompat.getColor(this, R.color.main_color));
                    invoice_selfTv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                    invoice_Et.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
