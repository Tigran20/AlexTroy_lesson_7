package ru.tinkoff.ru.seminar.ui;

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
import ru.tinkoff.ru.seminar.R;
import ru.tinkoff.ru.seminar.api.model.ApiResponse;

public class MainActivity extends AppCompatActivity {

    private static final String ERROR_TEXT = "Error!";

    private EditText currencyEditText;
    private Button calculateButton;
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
        calculateButton = findViewById(R.id.calculateButton);
        progressBar = findViewById(R.id.progressBar);
        resultTextView = findViewById(R.id.resultTextView);

        currencyEditText.addTextChangedListener(new StubTextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                calculateButton.setEnabled(Currencies.contains(text.toString()));
            }
        });
        calculateButton.setOnClickListener(v -> loadCurrencyRate(currencyEditText.getText().toString()));
    }


    @WorkerThread
    private String loadRate(String currency) throws Exception {
        ApiResponse rates = App.getInstance().getApi().getRates(currency.toUpperCase()).execute().body();
        return rates != null ? String.valueOf(rates.getRates().getRate()) : ERROR_TEXT;
    }

    private void loadCurrencyRate(String currency) {
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

}