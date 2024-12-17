package com.bemo.product.options;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.bemo.product.option.ColorPickerView;
import com.bemo.product.option.ImagePickerView;
import com.bemo.product.option.SizePickerView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPickerView colorPickerView = findViewById(R.id.colorPickerView);
        int[] colors = {
                Color.BLUE,
                Color.LTGRAY,Color.DKGRAY
        };
        colorPickerView.setColors(colors);
        colorPickerView.setOnColorSelectedListener((selectedColorIndex,color) -> {
            Toast.makeText(MainActivity.this, "Selected color index: " + color,
                    Toast.LENGTH_SHORT
            ).show();
        });



        SizePickerView sizePickerView = findViewById(R.id.sizePicker);
        sizePickerView.setSizes(new String[]{"S", "M", "L", "XL","2XL"});
        sizePickerView.setOnSizeSelectedListener(size -> {
            Toast.makeText(this, "Selected size: " + size, Toast.LENGTH_SHORT).show();
        });



        ImagePickerView imagePickerView = findViewById(R.id.imagePickerView);
        List<String> images = Arrays.asList(
                "shoes_1",
                "shoes_2",
                "shoes_3" // صورة من الموارد المحلية
        );
        imagePickerView.setImages(images);
        imagePickerView.setImageSelectedListener(selectedImageIndex -> {
            Toast.makeText(this, "Selected image index: " + selectedImageIndex, Toast.LENGTH_SHORT).show();

        });


    }
}