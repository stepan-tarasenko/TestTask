package com.developer.tarasenko.testtask.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.developer.tarasenko.testtask.ApiInterface;
import com.developer.tarasenko.testtask.R;
import com.developer.tarasenko.testtask.entity.DisplayModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {

    private TextView textView;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View target = inflater.inflate(R.layout.base_fragment, container, false);
        findAndConfigureViews(target);
        if (getArguments() != null) {
            if (getArguments().containsKey("id")) {
                int id = getArguments().getInt("id");
                /**
                 * Load than particular id, what we passed to fragment
                 */
                loadDisplayModelById(id);
            }
        }

        return target;
    }

    /**
     * Loads DisplayModel by id and display it into fragment
     * @param id
     */
    private void loadDisplayModelById(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<DisplayModel> displayModelCall = apiInterface.getReceiveModelById(id);
        displayModelCall.enqueue(new Callback<DisplayModel>() {
            @Override
            public void onResponse(Call<DisplayModel> call, Response<DisplayModel> response) {
                if (response.isSuccessful()) {
                    renderDisplayModel(response.body());
                }
            }

            @Override
            public void onFailure(Call<DisplayModel> call, Throwable t) {
                Log.e(getTag(), t.toString());
            }
        });
    }

    /**
     * Display into WebView/TextView depends on type
     * @param displayModel model what we get by id, from response
     */
    private void renderDisplayModel(DisplayModel displayModel){
        if (displayModel.getType().equals("text")) {
            webView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(displayModel.getContents());
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(displayModel.getUrl());
        }
    }

    /**
     * Finds views, make them invisible,
     * enables js in webview, makes wvclient
     * @param target view, what will be displayed in fragment
     */
    private void findAndConfigureViews(View target){
        textView = target.findViewById(R.id.text_view);
        webView = target.findViewById(R.id.web_view);
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        textView.setVisibility(View.INVISIBLE);
    }
}
