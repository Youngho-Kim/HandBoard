package com.kwave.android.handboard;

import android.graphics.Path;

/**
 * Created by kwave on 2017-06-09.
 */

// 브러쉬
class Brush {
    // 그리기 모드 설정
    public final static int MODE_DRAW = 100;
    public final static int MODE_ERASE = 200;

    Path path ;         // 브러쉬로 그리는 경로를 같이 담아준다.
    int color;
    float stroke;

    // 지우개 설정값 추가
    boolean erase = false;

    public static Brush newInstance(int color, float width, int mode){
        Brush brush = new Brush();
        brush.color = color;
        brush.stroke = width;
        switch (mode){
            case MODE_DRAW :
                brush.erase = false;
                break;
            case MODE_ERASE :
                brush.erase = true;
                break;
        }
        return brush;
    }

    public void addPath(Path path){
        this.path = path;
    }

}
