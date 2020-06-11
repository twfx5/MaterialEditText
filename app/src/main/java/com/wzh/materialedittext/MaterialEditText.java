package com.wzh.materialedittext;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;


/**
 * 仿写材质EditText
 */
public class MaterialEditText extends androidx.appcompat.widget.AppCompatEditText {

    private static final int TEXT_SIZE = (int) Utils.dp2px(14);
    private static final int TOP_MARGIN = (int) Utils.dp2px(8);
    private static final int HORIZONTAL_OFFSET = (int) Utils.dp2px(5);
    private static final int VERTICAL_OFFSET = (int) Utils.dp2px(15);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 是否显示悬空FloatingLabel
    private boolean isShowFloatingLabel;

    // FloatingLabel的不透明度
    private float floatingLabelFraction;

    // FloatingLabel的位置
    private float floatingLabelY = Utils.dp2px(8);

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        setPadding(getPaddingLeft(), getPaddingTop() + TEXT_SIZE + TOP_MARGIN, getPaddingRight(), getPaddingBottom());
        paint.setTextSize(TEXT_SIZE);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isShowFloatingLabel && TextUtils.isEmpty(charSequence)) {
                    // FloatingLabel 消失动画
                    // 透明度
                    ObjectAnimator fractionAnim = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelFraction", 0);
                    fractionAnim.start();
                    // 向下移动
                    ObjectAnimator translateAnim = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelY", getFloatingLabelY() + Utils.dp2px(8));
                    translateAnim.start();
                    isShowFloatingLabel = false;
                } else if (!isShowFloatingLabel && !TextUtils.isEmpty(charSequence)) {
                    // FloatingLabel 出现动画
                    // 透明度
                    ObjectAnimator fractionAnim = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelFraction", 1);
                    fractionAnim.start();

                    ObjectAnimator translateAnim = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelY", getFloatingLabelY() - Utils.dp2px(8));
                    translateAnim.start();
                    isShowFloatingLabel = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public float getFloatingLabelFraction() {
        return floatingLabelFraction;
    }

    public void setFloatingLabelFraction(float floatingLabelFraction) {
        this.floatingLabelFraction = floatingLabelFraction;
        invalidate();
    }

    public float getFloatingLabelY() {
        return floatingLabelY;
    }

    public void setFloatingLabelY(float floatingLabelY) {
        this.floatingLabelY = floatingLabelY;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha((int) (0xff * floatingLabelFraction));
        canvas.save();
        canvas.translate(0, floatingLabelY);
        canvas.drawText(getHint().toString(), HORIZONTAL_OFFSET, VERTICAL_OFFSET, paint);
        canvas.restore();
    }
}
