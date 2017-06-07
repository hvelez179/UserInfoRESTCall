package com.example.android.userinfocall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.userinfocall.Entities.RandomAPI;
import com.example.android.userinfocall.Entities.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
    private static final String BASE_URL = "https://randomuser.me/api";
    private static final String RETROFIT_URL = "https://randomuser.me/";

    private static final String RESPONSE_NAME_EXTRA = "RESPONSE_NAME_EXTRA";
    private static final String RESPONSE_ADDRESS_EXTRA = "RESPONSE_ADDRESS_EXTRA";
    private static final String RESPONSE_EMAIL_EXTRA = "RESPONSE_EMAIL_EXTRA";

    TextView responseNameTv;
    TextView responseAddressTv;
    TextView responseEmailTv;
    Button fetchBT;

    ArrayList<Result> resultList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String responseName = msg.getData().getString(RESPONSE_NAME_EXTRA);
            String responseAddress = msg.getData().getString(RESPONSE_ADDRESS_EXTRA);
            String responseEmail = msg.getData().getString(RESPONSE_EMAIL_EXTRA);
            postResult(responseName, responseAddress, responseEmail);
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseNameTv = (TextView) findViewById(R.id.name_tv);
        responseAddressTv = (TextView) findViewById(R.id.address_tv);
        responseEmailTv = (TextView) findViewById(R.id.email_tv);
        fetchBT = (Button) findViewById(R.id.fetchUserBT);
        fetchBT.setOnClickListener(this);
        resultList = new ArrayList<Result>();
    }

    protected void onResume() {
        super.onResume();
        //doRetrofitNetworkCall();
    }


    private void postResult(String name, String address, String email) {
        String resultName = String.format("Response Name: %1$d", responseNameTv);
        String resultAddress = String.format("Response Address: %1$s", responseAddressTv);
        String resultEmail = String.format("Response Email: %1$s", responseEmailTv);

        responseNameTv.setText(resultName);
        responseAddressTv.setText(resultAddress);
        responseEmailTv.setText(resultEmail);
    }

    private void doRetrofitNetworkCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RETROFIT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<RandomAPI> call = service.getRandomUser();
        call.enqueue(new Callback<RandomAPI>() {

            @Override
            public void onResponse(Call<RandomAPI> call, retrofit2.Response<RandomAPI> response) {
                if (response.isSuccessful()) {
                    RandomAPI randomAPI = response.body();
                    for (Result result : randomAPI.getResults()) {
                        String name = result.getName().getFirst() + " " + result.getName().getLast();
                        String street = result.getLocation().getStreet()+ " " + result.getLocation().getCity();
                        String email = result.getEmail();
                        responseNameTv.setText("Name: " + name);
                        responseAddressTv.setText("Address: " + street);
                        responseEmailTv.setText("Email: " + email);
                        resultList.add(result);
                    }
                    Log.d(TAG, "onResponse: arraylist size = " + resultList.size());
                }
            }

            @Override
            public void onFailure(Call<RandomAPI> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fetchUserBT:
                doRetrofitNetworkCall();
                return;
            default:
                break;
        }

    }

}











