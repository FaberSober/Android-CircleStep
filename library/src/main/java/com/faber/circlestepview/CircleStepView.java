package com.faber.circlestepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Faber on 2015/6/12.
 * 逐步圆自定义视图。
 */
public class CircleStepView extends View {

    private int DEF_CIRCLE_RADIUS_DP = 20;
    private int DEF_PATH_HEIGHT_DP = 10;
    private int DEF_TEXT_SIZE_SP = 14;
    private int DEF_CURRENT_CIRCLE_DELTA_RADIUS_DP = 2;
    private int DEF_TEXT_BELOW_CIRCLE_DISTANCE_DP = 3;

    /** 圆半径 */
    private int mCircleRadius;
    /** 圆默认时的颜色 */
    private int mCircleColor;
    /** 圆的个数 */
    private int mCircleCount;
    /** 圆内字体大小 */
    private int mCircleTextSize;
    /** 圆内字体颜色 */
    private int mCircleTextColor;
    /** 圆选中时内圈圆和外圈圆的半径差 */
    private int mCurrentCircleDeltaRadius;
    /** 圆选中时内圈圆的颜色 */
    private int mCurrentInnerCircleColor;
    /** 圆选中时外圈圆的颜色 */
    private int mCurrentOuterCircleColor;
    /** 路径的高度 */
    private int mPathHeight;
    /** 路径的颜色 */
    private int mPathColor;
    /** 圆下文字距圆边的距离 */
    private int mTextBelowCircleDistance;
    /** 圆下文字字体大小 */
    private int mTextBelowCircleSize;
    /** 圆下文字字体颜色 */
    private int mTextBelowCircleColor;

    /** 路径的总宽度 */
    private int mPathWidth;
    /** 路径的矩形 */
    private Rect mPathRect;
    /** 两个圆之间圆心的距离 */
    private int mCircleCenterDelta;
    /** 存储圆心坐标的列表 */
    private List<Point> mCircleCenterList = new ArrayList<>();
    /** 圆内字体居中显示偏移高度 */
    private int mCircleTextShiftHeight;
    /** 当前选中圆的序号 */
    private int mCurrentCircleIndex = 0;
    /** 之前选中圆的序号(用作动画) */
    private int mPreCircleIndex = 0;
    /** 圆下文字的列表 */
    private List<String> mTextBelowCircleList;
    /** 圆下文字的baseLine偏移高度 */
    private int mTextBelowCircleShiftHeight;

    private float mCurrentCircleRadiusRatio = 1.0F;
    private float mPreCircleRadiusRatio = 0.0F;

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint mCircleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint mCurrentCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint mTextBelowCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

    public CircleStepView(Context context) {
        this(context, null);
    }

    public CircleStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.circlestep_CircleStepView, defStyleAttr, 0);

        if (attributes != null) {
            try {
                mCircleRadius = (int) attributes.getDimension(R.styleable.circlestep_CircleStepView_circlestep_circle_radius,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_CIRCLE_RADIUS_DP, getResources().getDisplayMetrics()));
                mCircleColor = attributes.getColor(R.styleable.circlestep_CircleStepView_circlestep_circle_color, Color.GRAY);
                mCircleCount = attributes.getInt(R.styleable.circlestep_CircleStepView_circlestep_circle_count, 3);
                mCircleTextSize = (int) attributes.getDimension(R.styleable.circlestep_CircleStepView_circlestep_circle_text_size,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_TEXT_SIZE_SP, getResources().getDisplayMetrics()));
                mCircleTextColor = attributes.getColor(R.styleable.circlestep_CircleStepView_circlestep_circle_text_color, Color.WHITE);
                mCurrentCircleDeltaRadius = (int) attributes.getDimension(R.styleable.circlestep_CircleStepView_circlestep_current_circle_delta_radius,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_CURRENT_CIRCLE_DELTA_RADIUS_DP, getResources().getDisplayMetrics()));
                mCurrentInnerCircleColor = attributes.getColor(R.styleable.circlestep_CircleStepView_circlestep_current_inner_circle_color, Color.BLUE);
                mCurrentOuterCircleColor = attributes.getColor(R.styleable.circlestep_CircleStepView_circlestep_current_outer_circle_color, Color.GREEN);
                mPathHeight = (int) attributes.getDimension(R.styleable.circlestep_CircleStepView_circlestep_path_height,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_PATH_HEIGHT_DP, getResources().getDisplayMetrics()));
                mPathColor = attributes.getColor(R.styleable.circlestep_CircleStepView_circlestep_path_color, Color.GREEN);
                mTextBelowCircleDistance = (int) attributes.getDimension(R.styleable.circlestep_CircleStepView_circlestep_text_below_circle_distance,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_TEXT_BELOW_CIRCLE_DISTANCE_DP, getResources().getDisplayMetrics()));
                mTextBelowCircleSize = (int) attributes.getDimension(R.styleable.circlestep_CircleStepView_circlestep_text_below_circle_size,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_TEXT_SIZE_SP, getResources().getDisplayMetrics()));
                mTextBelowCircleColor = attributes.getColor(R.styleable.circlestep_CircleStepView_circlestep_text_below_circle_color, Color.BLACK);
            } finally {
                // make sure recycle is always called.
                attributes.recycle();
            }
        }

        // 至少有两个圆
        mCircleCount = mCircleCount < 2 ? 2 : mCircleCount;

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mCircleColor);

        mCircleTextPaint.setTextSize(mCircleTextSize);
        mCircleTextPaint.setColor(mCircleTextColor);
        mCircleTextPaint.setTextAlign(Paint.Align.CENTER);
        mCircleTextShiftHeight = (int) (-mCircleTextPaint.descent() - mCircleTextPaint.ascent()) / 2;

        mPathPaint.setStyle(Paint.Style.FILL);
        mPathPaint.setColor(mPathColor);

        mTextBelowCirclePaint.setTextSize(mTextBelowCircleSize);
        mTextBelowCirclePaint.setColor(mTextBelowCircleColor);
        mTextBelowCirclePaint.setTextAlign(Paint.Align.CENTER);
        mTextBelowCircleShiftHeight = (int) (mTextBelowCirclePaint.descent() - mTextBelowCirclePaint.ascent());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = getDefaultSize(
//                getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
//        int width = getDefaultSize(
//                getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
//
//        setMeasuredDimension(width, height);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;
        } else {
            // Cal the desire width.
            width = getSuggestedMinimumWidth();

            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + 2 * mCircleRadius + mTextBelowCircleDistance + mTextBelowCircleSize + getPaddingBottom();

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);

        // Cal path width
        mPathWidth = w - getPaddingLeft() - getPaddingRight() - 2 * mCircleRadius;
        // 计算 两个圆之间圆心的距离
        mCircleCenterDelta = mPathWidth / (mCircleCount - 1);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        mPathRect = new Rect(
                paddingLeft + mCircleRadius,
                paddingTop + mCircleRadius -  mPathHeight / 2,
                paddingLeft + mCircleRadius + mPathWidth,
                paddingTop + mCircleRadius + mPathHeight / 2
        );

        int py = paddingTop + mCircleRadius;
        for (int i = 0; i < mCircleCount; i++) {
            int px = paddingLeft + mCircleRadius + i * mCircleCenterDelta;
            mCircleCenterList.add(i, new Point(px, py));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw Path
        canvas.save();
        canvas.drawRect(mPathRect, mPathPaint);
        canvas.restore();

        // Draw circles
        for (int i = 0; i < mCircleCount; i++) {
            Point centerPoint = mCircleCenterList.get(i);

            canvas.save();
            canvas.drawCircle(centerPoint.x, centerPoint.y, mCircleRadius, mCirclePaint);
            canvas.restore();

            // Draw circle
            canvas.save();
            // Draw current circle
            if (i == mCurrentCircleIndex) {
                // Draw outer circle
                int currentOuterCircleRadius = (int) (mCurrentCircleRadiusRatio * mCircleRadius);
                mCurrentCirclePaint.setColor(mCurrentOuterCircleColor);
                canvas.drawCircle(centerPoint.x, centerPoint.y, currentOuterCircleRadius, mCurrentCirclePaint);

                // Draw inner circle
                int currentInnerCircleRadius = (int) (mCurrentCircleRadiusRatio * (mCircleRadius - mCurrentCircleDeltaRadius));
                mCurrentCirclePaint.setColor(mCurrentInnerCircleColor);
                canvas.drawCircle(centerPoint.x, centerPoint.y, currentInnerCircleRadius, mCurrentCirclePaint);
            }
            // Draw pre selected circle
            else if (i == mPreCircleIndex) {
                // Draw outer circle
                int preOuterCircleRadius = (int) (mPreCircleRadiusRatio * mCircleRadius);
                mCurrentCirclePaint.setColor(mCurrentOuterCircleColor);
                canvas.drawCircle(centerPoint.x, centerPoint.y, preOuterCircleRadius, mCurrentCirclePaint);

                // Draw inner circle
                int preInnerCircleRadius = (int) (mPreCircleRadiusRatio * (mCircleRadius - mCurrentCircleDeltaRadius));
                mCurrentCirclePaint.setColor(mCurrentInnerCircleColor);
                canvas.drawCircle(centerPoint.x, centerPoint.y, preInnerCircleRadius, mCurrentCirclePaint);
            }
            canvas.restore();

            // Draw text in circle
            canvas.save();
            String index = i + 1 + "";
            canvas.drawText(index, centerPoint.x, centerPoint.y + mCircleTextShiftHeight, mCircleTextPaint);
            canvas.restore();

            // Draw text below the circle
            canvas.save();
            canvas.drawText(getTextBelowCircleByIndex(i),
                    centerPoint.x,
                    centerPoint.y + mCircleRadius + mTextBelowCircleDistance + mTextBelowCircleShiftHeight,
                    mTextBelowCirclePaint);
            canvas.restore();
        }
    }

    /**
     * 设置当前圆的index
     */
    public void setCurrentCircleIndex(int index, boolean isShowAnim) {
        // 记录之前一次选中圆的index
        mPreCircleIndex = mCurrentCircleIndex;

        mCurrentCircleIndex = index;

        // 保证index范围不会超出圆个数范围
        mCurrentCircleIndex = mCurrentCircleIndex < 0 ? 0 : mCurrentCircleIndex;
        mCurrentCircleIndex = mCurrentCircleIndex >= mCircleCount ? mCircleCount - 1 : mCurrentCircleIndex;

        if (mCurrentCircleIndex == mPreCircleIndex)
            return;

        if (!isShowAnim) {
            invalidate();
        } else {
            PropertyValuesHolder pvhHide = PropertyValuesHolder.ofFloat("preCircleRadiusRatio", 1.0F, 0.0F);
            PropertyValuesHolder pvhShow = PropertyValuesHolder.ofFloat("currentCircleRadiusRatio", 0.0F, 1.0F);
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this, pvhHide, pvhShow);
            anim.setDuration(200L);
            anim.start();
        }
    }

    /**
     * 获取当前圆的index
     */
    public int getCurrentCircleIndex() {
        return mCurrentCircleIndex;
    }

    public void setPreCircleRadiusRatio(float preCircleRadiusRatio) {
        mPreCircleRadiusRatio = preCircleRadiusRatio;
        mPreCircleRadiusRatio = mPreCircleRadiusRatio > 1.0F ? 1.0F : mPreCircleRadiusRatio;
        mPreCircleRadiusRatio = mPreCircleRadiusRatio < 0.0F ? 0.0F : mPreCircleRadiusRatio;

        invalidate();
    }

    public void setCurrentCircleRadiusRatio(float currentCircleRadiusRatio) {
        mCurrentCircleRadiusRatio = currentCircleRadiusRatio;
        mCurrentCircleRadiusRatio = mCurrentCircleRadiusRatio > 1.0F ? 1.0F : mCurrentCircleRadiusRatio;
        mCurrentCircleRadiusRatio = mCurrentCircleRadiusRatio < 0.0F ? 0.0F : mCurrentCircleRadiusRatio;

        invalidate();
    }

    /**
     * 根据当前所绘圆的序号index取得圆下的文字，没有返回空字符串
     */
    private String getTextBelowCircleByIndex(int index) {
        String result = "";

        if (mTextBelowCircleList == null || mTextBelowCircleList.isEmpty() || mTextBelowCircleList.size() <= index) {
            return result;
        } else {
            result = mTextBelowCircleList.get(index);
        }

        return result;
    }

    /**
     * 设置圆下文字列表
     */
    public void setTextBelowCircle(String... strings) {
        mTextBelowCircleList = Arrays.asList(strings);

        invalidate();
    }
}
