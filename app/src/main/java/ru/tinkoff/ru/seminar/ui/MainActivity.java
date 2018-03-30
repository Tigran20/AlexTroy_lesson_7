package ru.tinkoff.ru.seminar.ui;

import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.ru.seminar.R;
import ru.tinkoff.ru.seminar.api.model.ApiResponse;
import ru.tinkoff.ru.seminar.api.model.RateObject;

public class MainActivity extends AppCompatActivity {

    private static final String ERROR_TEXT = "Error!";

    private EditText currencyEditText;
    private Button firstWayButton;
    private Button secondWayButton;
    private ProgressBar progressBar;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        currencyEditText = findViewById(R.id.currencyEditText);
        firstWayButton = findViewById(R.id.firstWayButton);
        secondWayButton = findViewById(R.id.secondWayButton);
        progressBar = findViewById(R.id.progressBar);
        resultTextView = findViewById(R.id.resultTextView);

        currencyEditText.addTextChangedListener(new StubTextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                firstWayButton.setEnabled(Currencies.contains(text.toString()));
                secondWayButton.setEnabled(Currencies.contains(text.toString()));
            }
        });
        firstWayButton.setOnClickListener(v -> loadCurrencyRateWay1(currencyEditText.getText().toString()));
        secondWayButton.setOnClickListener(v -> loadCurrencyRateWay2(currencyEditText.getText().toString()));
    }

    private void loadCurrencyRateWay1(String currency) {
        Single.fromCallable(() -> loadRate(currency))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    progressBar.setVisibility(View.VISIBLE);
                    resultTextView.setText("");
                })
                .doFinally(() -> progressBar.setVisibility(View.INVISIBLE))
                .subscribe(
                        rate -> resultTextView.setText(rate),
                        throwable -> resultTextView.setText(ERROR_TEXT)
                );
    }

    private Loader<Currencies> loadCurrencyRateWay2(String currency) {

        App.getInstance().getApi().getRates(currency.toUpperCase()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    String s = apiResponse.getRates().getRate() + "";
                    resultTextView.setText(s);
                } else {
                    resultTextView.setText(ERROR_TEXT);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                resultTextView.setText(ERROR_TEXT);
            }
        });
        return null;
    }

    @WorkerThread
    private String loadRate(String currency) throws Exception {
        ApiResponse rates = App.getInstance().getApi().getRates(currency.toUpperCase()).execute().body();
        return rates != null ? String.valueOf(rates.getRates().getRate()) : ERROR_TEXT;
    }


}