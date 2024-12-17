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
    private int indicatorColor = Color.BLACK; // لون المؤشر الافتراضي
    private int defaultSelectedIndex = -1;    // لا يتم تحديد أي عنصر افتراضيًا
    private int backgroundColor = Color.WHITE; // لون الخلفية الافتراضي
    private boolean scrollbarVisibility = true; // إظهار شريط التمرير افتراضيًا
    private float radius = 0; // زاوية الزوايا الدائرية

    private float circleSize = 48; // حجم الدائرة الافتراضي (بالـ dp)
    private float indicatorSize = 56; // حجم المؤشر الافتراضي (بالـ dp)
    private OnColorSelectedListener colorSelectedListener;

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // قراءة السمات من XML
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

        // تطبيق الإعدادات
        setHorizontalScrollBarEnabled(scrollbarVisibility);
        setBackground(createRoundedBackground(backgroundColor, radius));

        containerLayout = new LinearLayout(getContext());
        containerLayout.setMinimumHeight(50);
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(containerLayout);

        // إعداد اللون الافتراضي عند معاينة XML
        if (isInEditMode()) {
            setColors(new int[]{
                    0xFFFF0000, // Red
                    0xFF00FF00, // Green
                    0xFF0000FF, // Blue
                    0xFFFFFF00  // Yellow
            });
        }
        if (defaultSelectedIndex != -1) {
        //    selectColor(defaultSelectedIndex);
        }
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

        // تطبيق اللون الافتراضي
        if (defaultSelectedIndex >= 0 && defaultSelectedIndex < colors.length) {
            selectColor(defaultSelectedIndex,colors[defaultSelectedIndex]);
        }
    }

    private FrameLayout createColorCircle(int color, int index) {
        FrameLayout frameLayout = new FrameLayout(getContext());

        // حجم الدائرة بناءً على `circleSize`
        int size = (int) (getResources().getDisplayMetrics().density * circleSize);
        int indicatorPadding = (int) (getResources().getDisplayMetrics().density * (indicatorSize)); // مساحة إضافية للمؤشر

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorPadding, indicatorPadding);
        params.setMargins(10, 10, 10, 10); // المسافات بين الدوائر
        frameLayout.setLayoutParams(params);

        // الدائرة الأساسية
        View circle = new View(getContext());
        FrameLayout.LayoutParams circleParams = new FrameLayout.LayoutParams(size, size);
        circleParams.gravity = Gravity.CENTER;
        circle.setLayoutParams(circleParams);
        circle.setBackground(createColorDrawable(color, false));
        frameLayout.addView(circle);

        // المؤشر بناءً على `indicatorSize`
        View indicator = new View(getContext());
        int indicatorActualSize = (int) (getResources().getDisplayMetrics().density * indicatorSize);
        FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(indicatorActualSize, indicatorActualSize);
        indicatorParams.gravity = Gravity.CENTER;
        indicator.setLayoutParams(indicatorParams);
        indicator.setBackground(createIndicatorDrawable(color));
        indicator.setVisibility(View.INVISIBLE); // مخفي بشكل افتراضي
        frameLayout.addView(indicator);

        frameLayout.setTag(color); // حفظ اللون في الـ Tag
        frameLayout.setOnClickListener(v -> selectColor(index,color));

        return frameLayout;
    }

    private void selectColor(int index,int color) {
        if (defaultSelectedIndex != -1) {
            // إزالة المؤشر من اللون السابق
            FrameLayout previousCircle = colorCircles.get(defaultSelectedIndex);
            View previousIndicator = previousCircle.getChildAt(1); // المؤشر هو الطفل الثاني
            previousIndicator.setVisibility(View.INVISIBLE);
        }

        defaultSelectedIndex = index;

        // إظهار المؤشر على اللون الحالي
        FrameLayout selectedCircle = colorCircles.get(defaultSelectedIndex);
        View selectedIndicator = selectedCircle.getChildAt(1); // المؤشر هو الطفل الثاني
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
            drawable.setStroke(8, color); // اللون المحدد نفس لون الدائرة
        }
        return drawable;
    }

    private GradientDrawable createIndicatorDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setStroke(10, color); // جعل الحدود أكثر وضوحًا
        drawable.setColor(0x00000000); // شفاف من الداخل
        return drawable;
    }

    public void setIndicatorColor(int color) {
        this.indicatorColor = color;
        invalidate(); // إعادة رسم العرض
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
