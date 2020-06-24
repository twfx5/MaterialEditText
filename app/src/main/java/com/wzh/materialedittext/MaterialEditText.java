package com.wzh.materialedittext;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
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
    private static final int VERTICAL_OFFSET = (int) Utils.dp2px(22);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 是否显示悬空FloatingLabel
    private boolean isShowFloatingLabel;

    // FloatingLabel的不透明度
    private float floatingLabelFraction;

    private ObjectAnimator fractionAnim;

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    {
//        构造代码块：类中直接用{}定义，每一次创建对象时执行。
//        执行顺序优先级：静态块,main(),构造块,构造方法。
        /**
         * 父类静态变量--》父类静态代码块--》--》子类静态变量--》子类静态代码块--》
         * 父类非静态变量--》父类非静态代码块--》父类构造方法--》
         * 子类非静态变量--》子类非静态代码块--》子类构造方法
         */
    }

    private void init(Context context, AttributeSet attrs) {
        // attrs是定义在xml里面的属性：id，width、height、hint、weather等
        // 这一步是取我们自定义属性的数组
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
        // 这一步是取自定义属性数组的哪一个，第一个参数是index，第二个参数是默认值
        boolean weather = typedArray.getBoolean(R.styleable.MaterialEditText_weather, true);
        // 用完后回收
        typedArray.recycle();

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
                    getFractionAnimator().start();
                    isShowFloatingLabel = false;
                } else if (!isShowFloatingLabel && !TextUtils.isEmpty(charSequence)) {
                    // FloatingLabel 出现动画
                    getFractionAnimator().reverse();
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

    /**
     * 透明度动画
     * 把动画抽取出来，避免每次调用都创建ObjectAnimator对象
     *
     * 动画如果设置了起始值和终点值，那么可以通过reverse()方法来反转动画
     */
    private ObjectAnimator getFractionAnimator() {
        if (fractionAnim == null)
            fractionAnim = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelFraction", 1, 0);
        return fractionAnim;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha((int) (0xff * floatingLabelFraction));
        float offSetY = Utils.dp2px(16) * (1 - floatingLabelFraction);
        canvas.drawText(getHint().toString(), HORIZONTAL_OFFSET, VERTICAL_OFFSET + offSetY, paint);
    }
}
