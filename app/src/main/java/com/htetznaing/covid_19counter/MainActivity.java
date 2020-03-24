package com.htetznaing.covid_19counter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.htetznaing.covid_19counter.Utils.API.Covid19API;
import com.htetznaing.covid_19counter.Utils.API.Model.AllModel;
import com.htetznaing.covid_19counter.Utils.API.Model.CountryModel;
import com.htetznaing.covid_19counter.ui.country.CountryViewModel;
import com.htetznaing.covid_19counter.ui.note.NoteViewModel;
import com.htetznaing.covid_19counter.ui.total.TotalViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
    public static TotalViewModel totalViewModel;
    public static CountryViewModel countryViewModel;
    public static MainActivity instance;
    Dialog splashDialog;
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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_total, R.id.navigation_country, R.id.navigation_note)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Get the ViewModel.
        totalViewModel = new ViewModelProvider(this).get(TotalViewModel.class);
        countryViewModel = new ViewModelProvider(this).get(CountryViewModel.class);


        Covid19API covid19API = new Covid19API();
        covid19API.onLoadedListerner(new Covid19API.OnLoaded() {
            @Override
            public void allLoaded(AllModel all) {
                dismissDialog();
                totalViewModel.getCurrent().setValue(all);
            }

            @Override
            public void countriesLoaded(List<CountryModel> countries[]) {
                countryViewModel.getCountries().setValue(countries);
            }
        });
        covid19API.load();
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
