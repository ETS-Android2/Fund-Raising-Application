package com.abdulaleem.appcon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.abdulaleem.appcon.databinding.ActivityFundsDetailsBinding;
import com.abdulaleem.appcon.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amnt = binding.amountPay.getText().toString();
                if(amnt.equals(""))
                {
                    Toast.makeText(PaymentActivity.this, "Please Enter Some Amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(PaymentActivity.this,DonationActivity.class);
                    intent.putExtra("donation",amnt);
                    startActivityForResult(intent,0);
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Get String data from Intent
            String ResponseCode = data.getStringExtra("credit_card_processed");
            System.out.println("AhmadLogs: ResponseCode:" + ResponseCode);
            if(ResponseCode.equals("Y")) {
                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}