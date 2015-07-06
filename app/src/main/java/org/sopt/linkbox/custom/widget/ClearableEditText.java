package org.sopt.linkbox.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.sopt.linkbox.R;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class ClearableEditText extends EditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {

    private Drawable clearIcon = null;
    private float clearPadding = 0.0f;

    public ClearableEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public Drawable getClearIcon() {
        return clearIcon;
    }

    public float getClearPadding() {
        return clearPadding;
    }

    public void setClearIcon(Drawable clearIcon) {
        this.clearIcon = clearIcon;
    }

    public void setClearPadding(float clearPadding) {
        this.clearPadding = clearPadding;
    }

    private OnTouchListener t = null;
    private OnFocusChangeListener f = null;
    private TextWatcher w = null;

    @Override
    public void setOnTouchListener(OnTouchListener t) {
        this.t = t;
    }

    @Override
    public  void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    @Override
    public void addTextChangedListener(TextWatcher w) {
        this.w = w;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = motionEvent.getX() > (getWidth() - getPaddingRight() - clearIcon.getIntrinsicWidth());
            if (tappedX) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                }
                return true;
            }
        }
        if (t != null) {
            return t.onTouch(v, motionEvent);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty());
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (w != null) {
            w.afterTextChanged(s);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (w != null) {
            w.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setClearIconVisible(isNotEmpty());
        if (w != null) {
            w.onTextChanged(s, start, before, count);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        boolean wasVisible = (getCompoundDrawablesRelative()[2] != null);
        if (visible != wasVisible) {
            Drawable drawable[] = getCompoundDrawablesRelative();
            Drawable clear = visible ? clearIcon : null;
            setCompoundDrawablesRelative(drawable[0], drawable[1], clear, drawable[3]);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.ClearableEditText, defStyle, 0);

        if (typedArray.hasValue(R.styleable.ClearableEditText_clearIcon)) {
            clearIcon = typedArray.getDrawable(R.styleable.ClearableEditText_clearIcon);
        }
        else {
            clearIcon = getResources().getDrawable(R.drawable.abc_ic_clear_mtrl_alpha);
        }
        clearPadding = typedArray.getFloat(R.styleable.ClearableEditText_clearPadding, 0.0f);
        clearIcon.setBounds(0, 0, clearIcon.getIntrinsicWidth(), clearIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        super.addTextChangedListener(this);
    }

    private boolean isNotEmpty() {
        return !getText().toString().isEmpty();
    }
}
