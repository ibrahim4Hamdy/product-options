package com.bemo.product.option;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImagePickerView extends HorizontalScrollView {

    private Context context;
    private LinearLayout container;
    private int selectedColor;
    private int defaultColor;
    private int boxWidth;
    private int boxHeight;
    private int boxRadius;
    private boolean scrollbarVisibility;
    private OnImageSelectedListener imageSelectedListener;


    private int selectedIndex = -1;

    public ImagePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImagePickerView);
        TypedArray aa = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);

        selectedColor = typedArray.getColor(R.styleable.ImagePickerView_selectedColor, Color.parseColor("#FF6E40"));
        defaultColor = typedArray.getColor(R.styleable.ImagePickerView_defaultColor, Color.WHITE);
        boxWidth = typedArray.getDimensionPixelSize(R.styleable.ImagePickerView_imageWidth, dpToPx(70));
        boxHeight = typedArray.getDimensionPixelSize(R.styleable.ImagePickerView_imageHeight, dpToPx(70));
        boxRadius = typedArray.getDimensionPixelSize(R.styleable.ImagePickerView_imageRadius, dpToPx(10));
        scrollbarVisibility = aa.getBoolean(R.styleable.ColorPickerView_scrollbarVisibility, false);
        typedArray.recycle();

        setHorizontalScrollBarEnabled(scrollbarVisibility);

        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(container);
    }


    public void setImages(List<String> imageUrls) {
        container.removeAllViews();
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = createImageView(imageUrls.get(i), i);
            container.addView(imageView);
        }
    }

    private ImageView createImageView(String imageUrl, int index) {
        ImageView imageView = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(boxWidth, boxHeight);
        params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        imageView.setLayoutParams(params);
        imageView.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        imageView.setBackground(createDefaultBackground());

        if (imageUrl.startsWith("http")) {
            Glide.with(context).load(imageUrl).into(imageView);
        } else {
            int resId = getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
            imageView.setImageResource(resId);
        }

        imageView.setOnClickListener(v -> selectImage(index));
        return imageView;
    }


    private void selectImage(int index) {
        if (selectedIndex != -1) {
            ImageView previousImageView = (ImageView) container.getChildAt(selectedIndex);
            previousImageView.setBackground(createDefaultBackground());
        }

        ImageView currentImageView = (ImageView) container.getChildAt(index);
        currentImageView.setBackground(createSelectedBorder());
        selectedIndex = index;
        if (imageSelectedListener != null)
            imageSelectedListener.onImageSelected(index);

    }


    private Drawable createDefaultBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.TRANSPARENT);
        drawable.setCornerRadius(boxRadius);
        drawable.setStroke(dpToPx(2), Color.LTGRAY);
        return drawable;
    }

    private Drawable createSelectedBorder() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.TRANSPARENT);
        drawable.setCornerRadius(boxRadius);
        drawable.setStroke(dpToPx(3), selectedColor);
        return drawable;
    }


    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public void setImageSelectedListener(OnImageSelectedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
    }

    public interface OnImageSelectedListener {
        void onImageSelected(int selectedImageIndex);
    }

}

