package com.example.jiangfei.passwordlevelview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.jiangfei.passwordlevelview.R;

/**
 * 显示密码的强度等级的控件。
 * Created by jiangfei on 2016/10/21.
 */
public class PasswordLevelView extends View {

    // 文字尺寸
    private float mTextWidth;
    private float mTextHeight;

    // 文字和图形的间距
    private int mPaddingText = 0;
    // 文字大小
    private float mTextSize = 36F;

    // 当前密级强度
    private Level mCurLevel;

    // 默认情况下的密级颜色
    private int defaultColor = Color.argb(255, 220, 220, 220);

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public enum Level {
        DANGER("风险", Color.RED, 0), LOW("弱", Color.YELLOW, 1), MID("gf˙å", Color.BLUE, 2), STRONG("强", Color.GREEN, 3);
        String mStrLevel;
        int mLevelResColor;
        int mIndex;

        Level(String levelText, int levelResColor, int index) {
            mStrLevel = levelText;
            mLevelResColor = levelResColor;
            mIndex = index;
        }
    }

    public PasswordLevelView(Context context) {
        this(context, null);
    }

    public PasswordLevelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 从xml读取自定义属性
        TypedArray typedArray =
                context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordLevelView, defStyleAttr, 0);
        mPaddingText = typedArray.getDimensionPixelSize(R.styleable.PasswordLevelView_text_padding_level, mPaddingText);
        calculateTextWidth();
    }

    private void calculateTextWidth() {
        // 测量文字宽高，这里最多就2个字:风险
        mPaint.setTextSize(mTextSize);
        Rect rect = new Rect();
        mPaint.getTextBounds(Level.DANGER.mStrLevel, 0, Level.DANGER.mStrLevel.length(), rect);
        mTextWidth = rect.width();
        mTextHeight = rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth;
        int measuredHeight;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measuredWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : getMeasuredWidth();
        measuredHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : getMeasuredHeight();
        // 处理padding设置异常时的控件高度
        if (measuredHeight < getPaddingTop() + getPaddingBottom() + mTextHeight) {
            measuredHeight = (int) (getPaddingTop() + getPaddingBottom() + mTextHeight);
        }
        // 固定套路，保存控件宽高值
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算密级色块区域的宽高
        float levelAreaWidth =
                getWidth() - getPaddingLeft() - getPaddingRight() - mPaddingText - mTextWidth;
        int levelNum = Level.values().length;
        float eachLevelWidth = levelAreaWidth / levelNum;
        float eachLevelHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int startIndexOfDefaultColor = mCurLevel == null ? 0 : mCurLevel.mIndex + 1;
        float startRectLeft = getPaddingLeft();
        // 画密级色块
        for (int i = 0; i < levelNum; i++) {
            if (i >= startIndexOfDefaultColor) {
                mPaint.setColor(defaultColor);
            } else {
                mPaint.setColor(Level.values()[i].mLevelResColor);
            }
            canvas.drawRect(
                    startRectLeft,
                    getPaddingTop(),
                    startRectLeft + eachLevelWidth,
                    getPaddingTop() + eachLevelHeight,
                    mPaint);
            startRectLeft += eachLevelWidth;
        }
        // 画色块后面的字
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTextSize);
        String strText = mCurLevel != null ? mCurLevel.mStrLevel : "";
        // 计算text的baseline
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        // baseline思路：先设为控件的水平中心线，再调整到文本区域的水平中心线上
        // 注意：fontMetrics的top/bottom/ascent/descent属性值，是基于baseline为原点的，上方为负值，下方为正！
        float baseLine =
                getPaddingTop()
                        + eachLevelHeight / 2
                        + ((Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent)) / 2 - Math.abs(fontMetrics.descent));
        canvas.drawText(
                strText,
                startRectLeft + mPaddingText,
                baseLine,
                mPaint);
        // 最后，画一条水平中心线,看看文字的居中效果
        // float centerVerticalY = getPaddingTop() + eachLevelHeight / 2F;
        // canvas.drawLine(0F, centerVerticalY, getWidth(), centerVerticalY, mPaint);
    }

    /**
     * 显示level对应等级的色块
     *
     * @param level 密码密级
     */
    public void showLevel(Level level) {
        mCurLevel = level;
        invalidate();
    }
}
