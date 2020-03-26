package com.htetznaing.mmcovid19.UI.News;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.htetznaing.mmcovid19.R;

public class NewsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        root.findViewById(R.id.whoWebsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeb("https://www.who.int/");
            }
        });

        root.findViewById(R.id.mohsWebsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeb("http://mohs.gov.mm/Main/content/publication/2019-ncov");
            }
        });

        root.findViewById(R.id.mohsFb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFb("199295433433103");
            }
        });

        root.findViewById(R.id.whoFb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFb("154163327962392");
            }
        });


        return root;
    }

    private void openWeb(String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }

    private void openFb(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("fb://page/"+id));
            intent.setPackage("com.facebook.katana");
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://facebook.com/"+id));
            startActivity(Intent.createChooser(intent,"Open with.."));
        }
    }
}
