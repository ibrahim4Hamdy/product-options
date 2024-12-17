package com.bemo.product.option;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerView extends HorizontalScrollView {

    private LinearLayout containerLayout;
    private final List<FrameLayout> colorCircles = new ArrayList<>();
    private int indicatorColor = Color.BLACK;
    private int defaultSelectedIndex = -1;
    private int backgroundColor = Color.WHITE;
    private boolean scrollbarVisibility = true;
    private float radius = 0;

    private float circleSize = 48;
    private float indicatorSize = 56;
    private OnColorSelectedListener colorSelectedListener;

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Reade From XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPickerView, 0, 0);
        try {
            indicatorColor = a.getColor(R.styleable.ColorPickerView_indicatorColor, indicatorColor);
            defaultSelectedIndex = a.getInteger(R.styleable.ColorPickerView_defaultSelectedIndex, defaultSelectedIndex);
            backgroundColor = a.getColor(R.styleable.ColorPickerView_backgroundColor, backgroundColor);
            scrollbarVisibility = a.getBoolean(R.styleable.ColorPickerView_scrollbarVisibility, scrollbarVisibility);
            radius = a.getDimension(R.styleable.ColorPickerView_radius, radius);
            circleSize = a.getDimension(R.styleable.ColorPickerView_circleSize, circleSize);
            indicatorSize = a.getDimension(R.styleable.ColorPickerView_indicatorSize, indicatorSize);
        } finally {
            a.recycle();
        }

        //
        setHorizontalScrollBarEnabled(scrollbarVisibility);
        setBackground(createRoundedBackground(backgroundColor, radius));

        containerLayout = new LinearLayout(getContext());
        containerLayout.setMinimumHeight(50);
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(containerLayout);

        // preview in  XML
        if (isInEditMode()) {
            setColors(new int[]{
                    0xFFFF0000, // Red
                    0xFF00FF00, // Green
                    0xFF0000FF, // Blue
                    0xFFFFFF00  // Yellow
            });
        }
//        if (defaultSelectedIndex != -1) {
//            selectColor(defaultSelectedIndex);
//        }
    }
    private Drawable createRoundedBackground(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }
    public void setColors(int[] colors) {
        containerLayout.removeAllViews();
        colorCircles.clear();

        for (int i = 0; i < colors.length; i++) {
            FrameLayout colorCircle = createColorCircle(colors[i], i);
            containerLayout.addView(colorCircle);
            colorCircles.add(colorCircle);
        }

        // Default selected color
        if (defaultSelectedIndex >= 0 && defaultSelectedIndex < colors.length) {
            selectColor(defaultSelectedIndex,colors[defaultSelectedIndex]);
        }
    }

    private FrameLayout createColorCircle(int color, int index) {
        FrameLayout frameLayout = new FrameLayout(getContext());

        //  `circleSize`
        int size = (int) (getResources().getDisplayMetrics().density * circleSize);
        int indicatorPadding = (int) (getResources().getDisplayMetrics().density * (indicatorSize));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorPadding, indicatorPadding);
        //Distances between circles
        params.setMargins(10, 10, 10, 10);
        frameLayout.setLayoutParams(params);

        // main circle
        View circle = new View(getContext());
        FrameLayout.LayoutParams circleParams = new FrameLayout.LayoutParams(size, size);
        circleParams.gravity = Gravity.CENTER;
        circle.setLayoutParams(circleParams);
        circle.setBackground(createColorDrawable(color, false));
        frameLayout.addView(circle);

        //  `indicatorSize`
        View indicator = new View(getContext());
        int indicatorActualSize = (int) (getResources().getDisplayMetrics().density * indicatorSize);
        FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(indicatorActualSize, indicatorActualSize);
        indicatorParams.gravity = Gravity.CENTER;
        indicator.setLayoutParams(indicatorParams);
        indicator.setBackground(createIndicatorDrawable(color));
        indicator.setVisibility(View.INVISIBLE);
        frameLayout.addView(indicator);

        frameLayout.setTag(color);
        frameLayout.setOnClickListener(v -> selectColor(index,color));

        return frameLayout;
    }

    private void selectColor(int index,int color) {
        if (defaultSelectedIndex != -1) {
            // Remove the cursor from the previous color
            FrameLayout previousCircle = colorCircles.get(defaultSelectedIndex);
            View previousIndicator = previousCircle.getChildAt(1); // المؤشر هو الطفل الثاني
            previousIndicator.setVisibility(View.INVISIBLE);
        }

        defaultSelectedIndex = index;

        // Show the cursor on the current color
        FrameLayout selectedCircle = colorCircles.get(defaultSelectedIndex);
        View selectedIndicator = selectedCircle.getChildAt(1);
        selectedIndicator.setVisibility(View.VISIBLE);

        if (colorSelectedListener != null) {
            colorSelectedListener.onColorSelected(defaultSelectedIndex,color);
        }
    }

    private GradientDrawable createColorDrawable(int color, boolean isSelected) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color);

        if (isSelected) {
            // The selected color is the same as the circle color
            drawable.setStroke(8, color);
        }
        return drawable;
    }

    private GradientDrawable createIndicatorDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setStroke(10, color);
        drawable.setColor(0x00000000);
        return drawable;
    }

    public void setIndicatorColor(int color) {
        this.indicatorColor = color;
        invalidate();
    }

//    public void setDefaultSelectedIndex(int index) {
//        this.defaultSelectedIndex = index;
//
//    }

    public void setScrollbarVisibility(boolean visible) {
        setHorizontalScrollBarEnabled(visible);
    }


    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int selectedColorIndex,int color);
    }
}
