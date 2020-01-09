package com.developer.tarasenko.testtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.developer.tarasenko.testtask.entity.ReceiveModel;
import com.developer.tarasenko.testtask.fragments.MainFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity.class";

    private List<ReceiveModel> receiveModels;
    private FragmentManager fragmentManager;
    private int currentPosition = 0;
    private int maxPosition;
    @BindView(R.id.next_button)
    Button nextButton;

    @OnClick(R.id.next_button)
    void onNextButtonClicked() {
        if (receiveModels != null) {
            displayNext();
        }
    }

    /**
     * Displays next piece of data, depends on @currentPosition
     */
    private void displayNext() {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(receiveModels.get(currentPosition).getId()));
        fragment.setArguments(bundle);
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_id, fragment, TAG)
                .commit();
        currentPosition = (currentPosition + 1) % maxPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        loadReceiveModel();
    }

    private void loadReceiveModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<ReceiveModel>> callObjArray = apiInterface.getReceiveModels();
        callObjArray.enqueue(new Callback<List<ReceiveModel>>() {
            @Override
            public void onResponse(Call<List<ReceiveModel>> call, Response<List<ReceiveModel>> response) {
                if (response.isSuccessful()) {
                    receiveModels = response.body();
                    maxPosition = response.body().size();
                    displayNext();

                } else {
                    Toast.makeText(MainActivity.this,
                            "Error fetching from base url, code: "
                                    + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReceiveModel>> call, Throwable t) {
                Log.e(getClass().getName(), t.toString());
            }
        });
    }
}
