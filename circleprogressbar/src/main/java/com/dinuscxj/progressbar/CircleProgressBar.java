package com.dinuscxj.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class CircleProgressBar extends View {
    public static final int LINE = 0;
    public static final int SOLID = 1;
    public static final int SOLID_LINE = 2;

    public static final int LINEAR = 0;
    public static final int RADIAL = 1;
    public static final int SWEEP = 2;

    private static final int DEFAULT_MAX = 100;
    private static final float MAX_DEGREE = 360.0f;
    private static final float LINEAR_START_DEGREE = 90.0f;

    private static final int DEFAULT_START_DEGREE = -90;

    private static final int DEFAULT_LINE_COUNT = 45;

    private static final float DEFAULT_LINE_WIDTH = 4.0f;
    private static final float DEFAULT_PROGRESS_TEXT_SIZE = 11.0f;
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 1.0f;

    private static final String COLOR_FFF2A670 = "#fff2a670";
    private static final String COLOR_FFD3D3D5 = "#ffe3e3e5";

    private final RectF mProgressRectF = new RectF();
    private final RectF mBoundsRectF = new RectF();
    private final Rect mProgressTextRect = new Rect();

    private final Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint mProgressTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private float mRadius;
    private float mCenterX;
    private float mCenterY;

    private int mProgress;
    private int mMax = DEFAULT_MAX;

    //Only work well in the Line Style, represents the line count of the rings included
    private int mLineCount;
    //Only work well in the Line Style, Height of the line of the progress bar
    private float mLineWidth;

    //Stroke width of the progress of the progress bar
    private float mProgressStrokeWidth;

    //Text size of the progress of the progress bar
    private float mProgressTextSize;

    //Start color of the progress of the progress bar
    private int mProgressStartColor;
    //End color of the progress of the progress bar
    private int mProgressEndColor;
    //Color of the progress value of the progress bar
    private int mProgressTextColor;
    //Background color of the progress of the progress bar
    private int mProgressBackgroundColor;

    //the rotate degree of the canvas, default is -90.
    private int mStartDegree;

    // whether draw the background only outside the progress area or not
    private boolean mDrawBackgroundOutsideProgress;

    // Format the current progress value to the specified format
    private ProgressFormatter mProgressFormatter = new DefaultProgressFormatter();
    // The style of the progress color
    @Style
    private int mStyle;
    // The Shader of mProgressPaint
    @ShaderMode
    private int mShader;
    // The Stroke Cap of mProgressPaint and mProgressBackgroundPaint
    private Paint.Cap mCap;

    // The blur radius of mProgressPaint
    private int mBlurRadius;
    // The blur style of mProgressPaint
    private BlurMaskFilter.Blur mBlurStyle;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        initPaint();
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Basic data initialization
     */
    @SuppressWarnings("ResourceType")
    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        mLineCount = a.getInt(R.styleable.CircleProgressBar_line_count, DEFAULT_LINE_COUNT);

        mStyle = a.getInt(R.styleable.CircleProgressBar_progress_style, LINE);
        mShader = a.getInt(R.styleable.CircleProgressBar_progress_shader, LINEAR);
        mCap = a.hasValue(R.styleable.CircleProgressBar_progress_stroke_cap) ?
                Paint.Cap.values()[a.getInt(R.styleable.CircleProgressBar_progress_stroke_cap, 0)] : Paint.Cap.BUTT;

        mLineWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_line_width, dip2px(getContext(), DEFAULT_LINE_WIDTH));
        mProgressTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_text_size, dip2px(getContext(), DEFAULT_PROGRESS_TEXT_SIZE));
        mProgressStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_stroke_width, dip2px(getContext(), DEFAULT_PROGRESS_STROKE_WIDTH));

        mProgressStartColor = a.getColor(R.styleable.CircleProgressBar_progress_start_color, Color.parseColor(COLOR_FFF2A670));
        mProgressEndColor = a.getColor(R.styleable.CircleProgressBar_progress_end_color, Color.parseColor(COLOR_FFF2A670));
        mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_progress_text_color, Color.parseColor(COLOR_FFF2A670));
        mProgressBackgroundColor = a.getColor(R.styleable.CircleProgressBar_progress_background_color, Color.parseColor(COLOR_FFD3D3D5));

        mStartDegree = a.getInt(R.styleable.CircleProgressBar_progress_start_degree, DEFAULT_START_DEGREE);
        mDrawBackgroundOutsideProgress = a.getBoolean(R.styleable.CircleProgressBar_drawBackgroundOutsideProgress, false);

        mBlurRadius = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_blur_radius, 0);
        int blurStyle = a.getInt(R.styleable.CircleProgressBar_progress_blur_style, 0);
        switch (blurStyle) {
            case 1:
                mBlurStyle = BlurMaskFilter.Blur.SOLID;
                break;
            case 2:
                mBlurStyle = BlurMaskFilter.Blur.OUTER;
                break;
            case 3:
                mBlurStyle = BlurMaskFilter.Blur.INNER;
                break;
            default:
                mBlurStyle = BlurMaskFilter.Blur.NORMAL;
                break;
        }

        a.recycle();
    }

    /**
     * Paint initialization
     */
    private void initPaint() {
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mProgressPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaint.setColor(mProgressStartColor);
        mProgressPaint.setStrokeCap(mCap);
        updateMaskBlurFilter();

        mProgressBackgroundPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressBackgroundPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);
        mProgressBackgroundPaint.setStrokeCap(mCap);
    }


    private void updateMaskBlurFilter() {
        if (mBlurStyle != null && mBlurRadius > 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, mProgressPaint);
            mProgressPaint.setMaskFilter(new BlurMaskFilter(mBlurRadius, mBlurStyle));
        } else {
            mProgressPaint.setMaskFilter(null);
        }
    }

    /**
     * The progress bar color gradient,
     * need to be invoked in the {@link #onSizeChanged(int, int, int, int)}
     */
    private void updateProgressShader() {
        if (mProgressStartColor != mProgressEndColor) {
            Shader shader = null;
            switch (mShader) {
                case LINEAR: {
                    shader = new LinearGradient(mProgressRectF.left, mProgressRectF.top,
                            mProgressRectF.left, mProgressRectF.bottom,
                            mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(LINEAR_START_DEGREE, mCenterX, mCenterY);
                    shader.setLocalMatrix(matrix);
                    break;
                }
                case RADIAL: {
                    shader = new RadialGradient(mCenterX, mCenterY, mRadius,
                            mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP);
                    break;
                }
                case SWEEP: {
                    //arc = radian * radius
                    float radian = (float) (mProgressStrokeWidth / Math.PI * 2.0f / mRadius);
                    float rotateDegrees = (float) (
                            -(mCap == Paint.Cap.BUTT && mStyle == SOLID_LINE ? 0 : Math.toDegrees(radian)));

                    shader = new SweepGradient(mCenterX, mCenterY, new int[]{mProgressStartColor, mProgressEndColor},
                            new float[]{0.0f, 1.0f});
                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotateDegrees, mCenterX, mCenterY);
                    shader.setLocalMatrix(matrix);
                    break;
                }
                default:
                    break;
            }

            mProgressPaint.setShader(shader);
        } else {
            mProgressPaint.setShader(null);
            mProgressPaint.setColor(mProgressStartColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(mStartDegree, mCenterX, mCenterY);
        drawProgress(canvas);
        canvas.restore();

        drawProgressText(canvas);
    }

    private void drawProgressText(Canvas canvas) {
        if (mProgressFormatter == null) {
            return;
        }

        CharSequence progressText = mProgressFormatter.format(mProgress, mMax);

        if (TextUtils.isEmpty(progressText)) {
            return;
        }

        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setColor(mProgressTextColor);

        mProgressTextPaint.getTextBounds(String.valueOf(progressText), 0, progressText.length(), mProgressTextRect);
        canvas.drawText(progressText, 0, progressText.length(), mCenterX, mCenterY + mProgressTextRect.height() / 2, mProgressTextPaint);
    }

    private void drawProgress(Canvas canvas) {
        switch (mStyle) {
            case SOLID:
                drawSolidProgress(canvas);
                break;
            case SOLID_LINE:
                drawSolidLineProgress(canvas);
                break;
            case LINE:
            default:
                drawLineProgress(canvas);
                break;
        }
    }

    /**
     * In the center of the drawing area as a reference point , rotate the canvas
     */
    private void drawLineProgress(Canvas canvas) {
        float unitDegrees = (float) (2.0f * Math.PI / mLineCount);
        float outerCircleRadius = mRadius;
        float interCircleRadius = mRadius - mLineWidth;

        int progressLineCount = (int) ((float) mProgress / (float) mMax * mLineCount);

        for (int i = 0; i < mLineCount; i++) {
            float rotateDegrees = i * -unitDegrees;

            float startX = mCenterX + (float) Math.cos(rotateDegrees) * interCircleRadius;
            float startY = mCenterY - (float) Math.sin(rotateDegrees) * interCircleRadius;

            float stopX = mCenterX + (float) Math.cos(rotateDegrees) * outerCircleRadius;
            float stopY = mCenterY - (float) Math.sin(rotateDegrees) * outerCircleRadius;

            if (mDrawBackgroundOutsideProgress) {
                if (i >= progressLineCount) {
                    canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
                }
            } else {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
            }

            if (i < progressLineCount) {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressPaint);
            }
        }
    }

    /**
     * Just draw arc
     */
    private void drawSolidProgress(Canvas canvas) {
        if (mDrawBackgroundOutsideProgress) {
            float startAngle = MAX_DEGREE * mProgress / mMax;
            float sweepAngle = MAX_DEGREE - startAngle;
            canvas.drawArc(mProgressRectF, startAngle, sweepAngle, true, mProgressBackgroundPaint);
        } else {
            canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE, true, mProgressBackgroundPaint);
        }
        canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE * mProgress / mMax, true, mProgressPaint);
    }

    /**
     * Just draw arc
     */
    private void drawSolidLineProgress(Canvas canvas) {
        if (mDrawBackgroundOutsideProgress) {
            float startAngle = MAX_DEGREE * mProgress / mMax;
            float sweepAngle = MAX_DEGREE - startAngle;
            canvas.drawArc(mProgressRectF, startAngle, sweepAngle, false, mProgressBackgroundPaint);
        } else {
            canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE, false, mProgressBackgroundPaint);
        }
        canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE * mProgress / mMax, false, mProgressPaint);
    }

    /**
     * When the size of CircleProgressBar changed, need to re-adjust the drawing area
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBoundsRectF.left = getPaddingLeft();
        mBoundsRectF.top = getPaddingTop();
        mBoundsRectF.right = w - getPaddingRight();
        mBoundsRectF.bottom = h - getPaddingBottom();

        mCenterX = mBoundsRectF.centerX();
        mCenterY = mBoundsRectF.centerY();

        mRadius = Math.min(mBoundsRectF.width(), mBoundsRectF.height()) / 2;

        mProgressRectF.set(mBoundsRectF);

        updateProgressShader();

        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.progress = mProgress;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setProgress(ss.progress);
    }


    public void setProgressFormatter(ProgressFormatter progressFormatter) {
        mProgressFormatter = progressFormatter;
        invalidate();
    }

    public void setProgressStrokeWidth(float progressStrokeWidth) {
        mProgressStrokeWidth = progressStrokeWidth;
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressBackgroundPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressRectF.set(mBoundsRectF);
        updateProgressShader();
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
        invalidate();
    }

    public void setProgressTextSize(float progressTextSize) {
        mProgressTextSize = progressTextSize;
        invalidate();
    }

    public void setProgressStartColor(int progressStartColor) {
        mProgressStartColor = progressStartColor;
        updateProgressShader();
        invalidate();
    }

    public void setProgressEndColor(int progressEndColor) {
        mProgressEndColor = progressEndColor;
        updateProgressShader();
        invalidate();
    }

    public void setProgressTextColor(int progressTextColor) {
        mProgressTextColor = progressTextColor;
        invalidate();
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        mProgressBackgroundColor = progressBackgroundColor;
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);
        invalidate();
    }

    public void setLineCount(int lineCount) {
        mLineCount = lineCount;
        invalidate();
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public void setStyle(@Style int style) {
        mStyle = style;
        mProgressPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressBackgroundPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        invalidate();
    }

    public void setBlurRadius(int blurRadius) {
        mBlurRadius = blurRadius;
        updateMaskBlurFilter();
        invalidate();
    }

    public void setBlurStyle(BlurMaskFilter.Blur blurStyle) {
        mBlurStyle = blurStyle;
        updateMaskBlurFilter();
        invalidate();
    }

    public void setShader(@ShaderMode int shader) {
        mShader = shader;
        updateProgressShader();
        invalidate();
    }

    public void setCap(Paint.Cap cap) {
        mCap = cap;
        mProgressPaint.setStrokeCap(cap);
        mProgressBackgroundPaint.setStrokeCap(cap);
        invalidate();
    }

    public void setStartDegree(int startDegree) {
        mStartDegree = startDegree;
        invalidate();
    }

    public void setDrawBackgroundOutsideProgress(boolean drawBackgroundOutsideProgress) {
        mDrawBackgroundOutsideProgress = drawBackgroundOutsideProgress;
        invalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
        invalidate();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINE, SOLID, SOLID_LINE})
    private @interface Style {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINEAR, RADIAL, SWEEP})
    private @interface ShaderMode {
    }

    public interface ProgressFormatter {
        CharSequence format(int progress, int max);
    }

    private static final class DefaultProgressFormatter implements ProgressFormatter {
        private static final String DEFAULT_PATTERN = "%d%%";

        @Override
        public CharSequence format(int progress, int max) {
            return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
        }
    }

    private static final class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int progress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }
    }
}
