package com.htetznaing.mmcovid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        if (getIntent().hasExtra("url")){
            PhotoView imageView = findViewById(R.id.imageView);
            Glide.with(this).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.blank)).load(getIntent().getStringExtra("url")).into(imageView);
        }else {
            finish();
        }
        this.setFinishOnTouchOutside(true);
    }
}
