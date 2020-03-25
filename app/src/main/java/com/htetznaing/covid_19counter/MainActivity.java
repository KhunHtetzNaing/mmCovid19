package com.htetznaing.covid_19counter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.htetznaing.covid_19counter.UI.Emergency.EmergencyViewModel;
import com.htetznaing.covid_19counter.Utils.API.Covid19API;
import com.htetznaing.covid_19counter.Utils.API.Model.AllModel;
import com.htetznaing.covid_19counter.Utils.API.Model.CountryModel;
import com.htetznaing.covid_19counter.UI.Country.CountryViewModel;
import com.htetznaing.covid_19counter.UI.Main.MainViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
    public static MainViewModel totalViewModel;
    public static CountryViewModel countryViewModel;
    public static EmergencyViewModel emergencyViewModel;
    public static MainActivity instance;
    private Dialog splashDialog;
    private WebView mmWebView;
    private Handler mHandler = new Handler();
    private boolean stopMMHandler = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        splashDialog = new Dialog(this, R.style.SplashTheme);
        splashDialog.setContentView(R.layout.activity_splash);
        splashDialog.setCancelable(false);
        splashDialog.show();
        setDialogTimeout(5000, splashDialog);

        loadMmData();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_main, R.id.navigation_country,R.id.navigation_news, R.id.navigation_note,R.id.navigation_emergency).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Get the ViewModel.
        totalViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        countryViewModel = new ViewModelProvider(this).get(CountryViewModel.class);
        emergencyViewModel = new ViewModelProvider(this).get(EmergencyViewModel.class);

        Covid19API covid19API = new Covid19API();
        covid19API.onLoadedListerner(new Covid19API.OnLoaded() {
            @Override
            public void allLoaded(AllModel all) {
                totalViewModel.getCurrent().setValue(all);
                dismissDialog();
            }

            @Override
            public void countriesLoaded(List<CountryModel> countries) {
                countryViewModel.getCountries().setValue(countries);
            }
        });
        covid19API.load();
        ConstantAPI.load();
    }

    private void loadMmData() {
        mmWebView = findViewById(R.id.mmWebView);
        mmWebView.getSettings().setJavaScriptEnabled(true);
        mmWebView.addJavascriptInterface(new MMJavascriptInterface(),"HtetzNaing");
        mmWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        });
        mmWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findMM();
            }
        });
        mmWebView.loadUrl("https://doph.maps.arcgis.com/apps/opsdashboard/index.html#/16ed43418d2045fbbf94cb89cbc9eec4");
    }

    private void findMM() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                mmWebView.loadUrl("javascript: (function() {"+Constants.getJsFunction()+"})()");
                if (!stopMMHandler) {
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        mHandler.post(runnable);
    }

    class MMJavascriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void fuck(String html) {
            new Handler(Looper.getMainLooper()).post(() -> openMM(html));
        }
    }

    private void openMM(String html) {
        if (!stopMMHandler) {
            stopMMHandler = true;
            totalViewModel.getMm_data().setValue(html);
        }
    }

    public void dismissDialog() {
        if (splashDialog != null) {
            if (splashDialog.isShowing()) {
                splashDialog.dismiss();
            }
        }
    }

    public void setDialogTimeout(long time, final Dialog d) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (d != null && d.isShowing()) {
                    d.dismiss();
                }
            }
        }, time);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
