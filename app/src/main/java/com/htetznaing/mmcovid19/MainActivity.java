package com.htetznaing.mmcovid19;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.htetznaing.mmcovid19.UI.Emergency.EmergencyViewModel;
import com.htetznaing.mmcovid19.UI.Note.NoteViewModel;
import com.htetznaing.mmcovid19.Utils.API.Covid19API;
import com.htetznaing.mmcovid19.Utils.API.Model.AllModel;
import com.htetznaing.mmcovid19.Utils.API.Model.CountryModel;
import com.htetznaing.mmcovid19.UI.Country.CountryViewModel;
import com.htetznaing.mmcovid19.UI.Main.MainViewModel;
import com.htetznaing.mmcovid19.Utils.CheckInternet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    public static NoteViewModel noteViewModel;
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
        TextView textView = splashDialog.findViewById(R.id.desc);
        String[] foo_array = getResources().getStringArray(R.array.splash_messages);
        for (String s:foo_array){
            textView.append(s+"\n");
        }
        splashDialog.setCancelable(false);
        if (!splashDialog.isShowing()){
            splashDialog.show();
        }

        if (new CheckInternet(this).checkInternet()){
            setDialogTimeout(7000, splashDialog);
            initData();
        }else {
            View view = getLayoutInflater().inflate(R.layout.dialog_title,null);
            TextView title = view.findViewById(R.id.title);
            title.setText(getString(R.string.no_internet_title));
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCustomTitle(view)
                    .setMessage(getString(R.string.no_internet_message))
                    .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            splashDialog.cancel();
                            splashDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finishAffinity();
                        }
                    })
                    .setNegativeButton(R.string.no_use, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    });
            builder.show();
        }
    }

    private void initData(){
        AppUpdater.init(this);
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
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

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

    @Override
    protected void onDestroy() {
        Covid19API.stopMMHandler = true;
        ConstantAPI.stopMMHandler = true;
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showAbout();
        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        View view = getLayoutInflater().inflate(R.layout.about_page,null);
        setupClicks(view);
        TextView info = view.findViewById(R.id.info_note);
        String infoNote = "အက်ပလီကေးရှင်းအတွင်းရှိ\n" +
                "အချက်အလက်များအားလုံးကို\n" +
                "ကျန်းမာရေးနှင့်အားကစားဝန်ကြီးဌာနနှင့်\n" +
                "World Meter ဝက်ဘ်ဆိုက်များမှရယူထားပါသည်။";
        info.setText(infoNote);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view);
        builder.show();
    }

    private void setupClicks(View view) {
        view.findViewById(R.id.profile_image).setOnClickListener(view1 -> {
            Toast.makeText(instance, "Clicked Photo", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.fb).setOnClickListener(view12 -> {
            openDevFB();
        });

        view.findViewById(R.id.github).setOnClickListener(view13 -> {
            openLink("https://github.com/KhunHtetzNaing/mmCovid19");
        });

        view.findViewById(R.id.youtube).setOnClickListener(view14 -> {
            openLink("https://www.youtube.com/c/KhunHtetzNaingX");
        });

        view.findViewById(R.id.web).setOnClickListener(view15 -> {
            openLink("https://khunhtetznaing.com");
        });

        view.findViewById(R.id.app_info).setOnClickListener(view16 -> {
            openDevFB();
        });

        view.findViewById(R.id.worldmeter).setOnClickListener(view17 -> {
            openLink("https://www.worldometers.info/coronavirus/");
        });

        view.findViewById(R.id.mohs).setOnClickListener(view18 ->{
            openLink("https://mohs.gov.mm/Main/content/publication/2019-ncov");
        });

        view.findViewById(R.id.feedback).setOnClickListener(view19 ->{
            Constants.showFeedback(this);
        });
    }

    private void openLink(String link){
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)));
    }

    private void openDevFB() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse("fb://profile/100011402500763"));
            startActivity(intent);
        } catch (Exception e) {
            intent.setData(Uri.parse("https://facebook.com/100011402500763"));
            startActivity(intent);
        }
    }
}
