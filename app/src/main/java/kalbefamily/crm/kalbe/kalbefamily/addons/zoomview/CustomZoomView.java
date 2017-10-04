package kalbefamily.crm.kalbe.kalbefamily.addons.zoomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ASUS ZE on 07/09/2016.
 */
public class CustomZoomView extends View implements View.OnTouchListener, View.OnLongClickListener{

    private Paint mPaint;

    Vibrator v;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int MOVE = 3;

    private int mode = NONE;

    Rect src;
    Rect mTempDst = new Rect();
    Rect dst = new Rect();
    Rect tsd = new Rect();

    Bitmap mBitmap;

    private int mBitmapWidth = -1;
    private int mBitmapHeight = -1;

    private PointF mStartPoint = new PointF();
    private PointF mMiddlePoint = new PointF();
    private PointF mStartDragPoint = new PointF();
    private PointF mMovePoint = new PointF();

    private float oldDist = 1f;
    private float scale;
    private float oldEventX = 0;
    private float oldEventY = 0;
    private float oldStartPointX = 0;
    private float oldStartPointY = 0;
    private int mViewWidth = -1;
    private int mViewHeight = -1;

    private boolean mDraggable = false;

    private boolean firstTouch = false;

    public CustomZoomView(Context context) {
        this(context, null, 0);
    }

    public CustomZoomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnTouchListener(this);
        this.setOnLongClickListener(this);
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mPaint = new Paint();
        mPaint.setColorFilter(new PorterDuffColorFilter(Color.argb(100,255,255,255), PorterDuff.Mode.SRC_IN));
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {

            src = new Rect();
            src.left = 0;
            src.top = 0;
            src.right = bitmap.getWidth();
            src.bottom = bitmap.getHeight();
            mBitmap = bitmap;

            mBitmapWidth = bitmap.getWidth() * 1;
            mBitmapHeight = bitmap.getHeight() * 1;

            dst = new Rect();
//            dst.left = (mViewWidth / 2) - (mBitmapWidth / 2);
//            dst.top = (mViewHeight / 2) - (mBitmapHeight / 2);
//            dst.right = (mViewWidth / 2) + (mBitmapWidth / 2);
//            dst.bottom = (mViewHeight / 2) + (mBitmapHeight / 2);
            dst = new Rect();
            dst.left = 0;
            dst.top = 0;
            dst.right = bitmap.getWidth();
            dst.bottom = bitmap.getHeight();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mode == DRAG) {
            if ((mStartPoint.x > dst.left && mStartPoint.x < dst.right) && (mStartPoint.y < dst.bottom && mStartPoint.y > dst.top)
                    && (mMovePoint.x > dst.left && mMovePoint.x < dst.right) && (mMovePoint.y < dst.bottom && mMovePoint.y > dst.top)) {
//                mode = MOVE;
//                v.vibrate(500);
            }
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mStartPoint.set(event.getX(), event.getY());
                mStartDragPoint.set(event.getX(), event.getY());
                mTempDst.set(dst.left, dst.top, dst.right, dst.bottom);
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mMiddlePoint, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (mode == ZOOM) {
                    mBitmapWidth = dst.right - dst.left;
                    mBitmapHeight = dst.bottom - dst.top;
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    mMovePoint.x = event.getX();
                    mMovePoint.y = event.getY();
                    drag(event);
                } else if (mode == ZOOM) {
                    zoom(event);
                } else if (mode == MOVE) {
                    move(event);
                }
                break;
        }
        return false;
    }

    public void move(MotionEvent event) {

        int xChange = (int) (event.getX() - mStartPoint.x);
        int yChange = (int) (event.getY() - mStartPoint.y);

        dst.left = mTempDst.left + (xChange);
        dst.top = mTempDst.top + (yChange);

        dst.right = mTempDst.right + (xChange);
        dst.bottom = mTempDst.bottom + (yChange);

        invalidate();
    }

    public void drag(MotionEvent event) {

        float eventX = event.getX();
        float eventY = event.getY();
        float spacingX = eventX - mStartDragPoint.x;
        float spacingY = eventY - mStartDragPoint.y;
        float newPositionLeft = (dst.left < 0 ? spacingX : spacingX * -1) + dst.left;
        float newPositionRight = (spacingX) + dst.right;
        float newPositionTop = (dst.top < 0 ? spacingY : spacingY * -1) + dst.top;
        float newPositionBottom = (spacingY) + dst.bottom;
        boolean x = true;
        boolean y = true;

        if (newPositionRight < 0.0f || newPositionLeft > 0.0f) {
            if (newPositionRight < 0.0f && newPositionLeft > 0.0f) {
                x = false;
            } else {
                eventX = oldEventX;
                mStartDragPoint.x = oldStartPointX;
            }
        }
        if (newPositionBottom < 0.0f || newPositionTop > 0.0f) {
            if (newPositionBottom < 0.0f && newPositionTop > 0.0f) {
                y = false;
            } else {
                eventY = oldEventY;
                mStartDragPoint.y = oldStartPointY;
            }
        }

        if (mDraggable) {
            if (x) oldEventX = eventX;
            if (y) oldEventY = eventY;
            if (x) oldStartPointX = mStartDragPoint.x;
            if (y) oldStartPointY = mStartDragPoint.y;
        }

    }

    public void zoom(MotionEvent event) {
        float newDist = spacing(event);
        boolean in = newDist > oldDist;

        if (!in && scale < .01f) {
            return;
        }

        scale = newDist / oldDist;

        int xChange = (int) ((mBitmapWidth * scale) / 2);
        int yChange = (int) ((mBitmapHeight * scale) / 2);

        if (xChange > 10 && yChange > 10) { //ADDED THIS TO KEEP IT FROM GOING INVERSE

            int xMidPoint = ((dst.right - dst.left) / 2) + dst.left;
            int yMidPoint = ((dst.bottom - dst.top) / 2) + dst.top;

            dst.left = (int) (float) (xMidPoint - xChange);
            dst.top = (int) (float) (yMidPoint - yChange);

            dst.right = (int) (float) (xMidPoint + xChange);
            dst.bottom = (int) (float) (yMidPoint + yChange);

        }

        invalidate();

    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mode == DRAG) {
            if ((mStartPoint.x > dst.left && mStartPoint.x < dst.right) && (mStartPoint.y < dst.bottom && mStartPoint.y > dst.top)
                    && (mMovePoint.x > dst.left && mMovePoint.x < dst.right) && (mMovePoint.y < dst.bottom && mMovePoint.y > dst.top)) {
                mode = MOVE;
//                v.vibrate(500);
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mode == MOVE) {
            canvas.drawBitmap(mBitmap, src, dst, null);
            canvas.drawBitmap(mBitmap, src, dst, mPaint);
        } else {
            canvas.drawBitmap(mBitmap, src, dst, null);
//            canvas.drawBitmap(mBitmap, (canvas.getWidth()/2-(mBitmap.getWidth()/2)), (float) (canvas.getHeight() * .25), null);
//            canvas.drawRect(
//                    getLeft()+(getRight()-getLeft())/3,
//                    getTop()+(getBottom()-getTop())/3,
//                    getRight()-(getRight()-getLeft())/3,
//                    getBottom()-(getBottom()-getTop())/3,null);
        }

    }

}
