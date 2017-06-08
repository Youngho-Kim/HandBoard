package com.kwave.android.handboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwave on 2017-06-09.
 */

// 그림판
class Board extends View {

    Paint paint;
    Paint erase;
    List<Brush> brushes;
    Brush current_brush;
    Path current_path;

    // 브러쉬의 속성값 변경 여부를 판단하기 위한 플래그 > 브러쉬의 속성값이 바뀌면
    // Path를 다시 생성한다.
    boolean newBrush = true;

    public Board(Context context) {
        super(context);
        setPaint();
        setErase();
        brushes = new ArrayList<>();
    }

    // 처음 한번만 기본 페인트 속성을 설정해둔다.
    private void setPaint(){
            // Paint의 기본 속상만 적용해두고 color와 Brush에서 가져다가 그린다.
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(null);
    }

    // 지워지는 효과를 위해서 XferMode를 사용한다.
    // new PorterDuffXfermode(PorterDuff.Mode.CLEAR)


    // 지우개 설정
    private void setErase(){
        erase = new Paint();
        erase.setColor(Color.TRANSPARENT);
        erase.setStyle(Paint.Style.STROKE);
        erase.setAntiAlias(true);
        erase.setStrokeJoin(Paint.Join.ROUND);
        erase.setStrokeCap(Paint.Cap.ROUND);
        erase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    // 브러쉬를 새로 생성한다.
    public void setBrush(Brush brush){
        current_brush = brush;
        newBrush = true;
    }

    // path를 새로 생성한다.
    private void createPath(){
        if(newBrush){       // 브러쉬가 변경 되었을 때만 Path를 생성해준다.
            current_path = new Path();
            current_brush.addPath(current_path);
            brushes.add(current_brush);
            newBrush = false;           // Path가 생성되면 브러쉬에 path가 적용되었다고 알려준다.
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // xferMode에서 투명값을 적용하기 위해 LayoutType을 설정해준다.
        setLayerType(LAYER_TYPE_HARDWARE, null);

        for(Brush brush : brushes){
            // 브러쉬에서 속성값을 꺼내서 Paint에 반영한다.
            // 지우개 설정
            if(brush.erase){
                erase.setStrokeWidth(brush.stroke);
                canvas.drawPath(brush.path, erase);
            }
            // 그리기 설정
            else{
                // setLayerType(LAYER_TYPE_NONE, paint);
                paint.setStrokeWidth(brush.stroke);
                paint.setColor(brush.color);
                canvas.drawPath(brush.path, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 내가 터치한 좌표를 꺼낸다.
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            // 터치가 시작되면 Path를 생성하고  현재 지정된 브러쉬와 함께 저장소에 담아둔다.
            case MotionEvent.ACTION_DOWN :
                createPath();
                current_path.moveTo(x,y);   // 이전 점과 현재 점 사이를 그리지 않고 이동한다.
                break;
            case MotionEvent.ACTION_MOVE :
                current_path.lineTo(x,y);   // 바로 이전 점과 현재 점 사이에 줄을 그어준다..
                break;
            case MotionEvent.ACTION_UP :
                // none
                break;
        }

        // 화면 갱신해서 위에서 그린 Path를 반영해준다.
        invalidate();

        // 리턴 false일 경우 touch 이벤트를 연속해서 발생 시키지 않는다.
        // 즉, 드래그 시 onTouchEvent가 호출되지 않는다.
        return true;

    }
}
