package com.bemo.product.option;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SizePickerView extends HorizontalScrollView {

    private LinearLayout containerLayout;
    private final List<TextView> sizeTexts = new ArrayList<>();
    private int selectedTextColor = Color.BLACK; // لون النص المحدد
    private int defaultTextColor = Color.GRAY;  // لون النص الافتراضي
    private int indicatorColor = Color.BLUE;   // لون المؤشر (الإطار)
    private int defaultSelectedIndex = -1;     // الفهرس الافتراضي
    private float textSize = 16;               // حجم النص الافتراضي
    private float indicatorPadding = 8;        // المسافة بين النصوص والإطار
    private int boxWidth =40;
    private int boxHeight =40;
    private int boxRadius =10;

    private OnSizeSelectedListener sizeSelectedListener;

    public SizePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // قراءة السمات من XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SizePickerView, 0, 0);
        TypedArray aa = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPickerView, 0, 0);

        try {
            selectedTextColor = a.getColor(R.styleable.SizePickerView_selectedTextColor, selectedTextColor);
            defaultTextColor = a.getColor(R.styleable.SizePickerView_defaultTextColor, defaultTextColor);
            indicatorColor = aa.getColor(R.styleable.ColorPickerView_indicatorColor, indicatorColor);
            defaultSelectedIndex = aa.getInteger(R.styleable.ColorPickerView_defaultSelectedIndex, defaultSelectedIndex);
            textSize = a.getDimension(R.styleable.SizePickerView_textSize, textSize);
            indicatorPadding = a.getDimension(R.styleable.SizePickerView_indicatorPadding, indicatorPadding);
            boxHeight    =a.getInteger(R.styleable.SizePickerView_boxHeight,boxHeight);
            boxWidth    =a.getInteger(R.styleable.SizePickerView_boxWidth,boxWidth);
            boxRadius = a.getInteger(R.styleable.SizePickerView_boxRadius, boxRadius);

        } finally {
            a.recycle();
        }

        // إعداد التخطيط
        setHorizontalScrollBarEnabled(false);
        containerLayout = new LinearLayout(getContext());
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(containerLayout);

        // معاينة النصوص عند عرض XML
        if (isInEditMode()) {
            setSizes(new String[]{"S", "M", "L", "XL","2XL"});
        }

        if (defaultSelectedIndex != -1) {
          //  selectSize(defaultSelectedIndex);
        }
    }

    public void setSizes(String[] sizes) {
        containerLayout.removeAllViews();
        sizeTexts.clear();

        for (int i = 0; i < sizes.length; i++) {
            TextView sizeTextView = createSizeTextView(sizes[i], i);
            containerLayout.addView(sizeTextView);
            sizeTexts.add(sizeTextView);
        }

        // تحديد العنصر الافتراضي
        if (defaultSelectedIndex >= 0 && defaultSelectedIndex < sizes.length) {
            selectSize(defaultSelectedIndex);
        }
    }

    private TextView createSizeTextView(String size, int index) {
        TextView textView = new TextView(getContext());

        // إعداد النص
        textView.setText(size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize); // حجم النص كبير
        textView.setMaxLines(1); // تحديد سطر واحد للنص
        textView.setEllipsize(TextUtils.TruncateAt.END); // إذا كان النص كبيرًا، تقليصه بنقاط
        textView.setGravity(Gravity.CENTER); // توسيط النص
        textView.setTextColor(defaultTextColor);

        // إعداد التخطيط بحجم ثابت
        int width = dpToPx(boxWidth); // تحويل 80dp إلى px
        int height = dpToPx(boxHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(16, 16, 16, 16); // مسافة بين المربعات
        textView.setLayoutParams(params);

        // إعداد الخلفية الافتراضية
        textView.setBackground(createDefaultBackground());

        // التعامل مع النقر
        textView.setOnClickListener(v -> selectSize(index));
        return textView;
    }

    private Drawable createDefaultBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE); // لون الخلفية الافتراضي
        drawable.setCornerRadius(dpToPx(boxRadius)); // الزوايا المستديرة
        drawable.setStroke(3, Color.LTGRAY); // الإطار بلون رمادي
        return drawable;
    }

    private Drawable createSelectedBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(indicatorColor); // لون الخلفية المحددة (برتقالي)
        drawable.setCornerRadius(dpToPx(boxRadius)); // الزوايا المستديرة
        return drawable;
    }



    public void selectSize(int index) {
        if (defaultSelectedIndex != -1 && defaultSelectedIndex < sizeTexts.size()) {
            // إعادة النصوص السابقة للحالة الافتراضية
            TextView previousText = sizeTexts.get(defaultSelectedIndex);
            previousText.setTextColor(defaultTextColor);
            previousText.setBackground(createDefaultBackground());
        }

        defaultSelectedIndex = index;

        // تمييز النص المحدد
        TextView selectedText = sizeTexts.get(defaultSelectedIndex);
        selectedText.setTextColor(Color.WHITE); // النص المحدد بلون أبيض
        selectedText.setBackground(createSelectedBackground());

        if (sizeSelectedListener != null) {
            sizeSelectedListener.onSizeSelected(sizeTexts.get(defaultSelectedIndex).getText().toString());
        }
    }



    public void setOnSizeSelectedListener(OnSizeSelectedListener listener) {
        this.sizeSelectedListener = listener;
    }
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public interface OnSizeSelectedListener {
        void onSizeSelected(String size);
    }
}