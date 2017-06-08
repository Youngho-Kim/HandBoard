package com.kwave.android.handboard;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity{

    FrameLayout layout;
    SeekBar seekBar;                    // 두께 조절 옵션
    RadioGroup radioGroup;              // 색상 조절 옵션
    //    RadioButton btnRed, btnGreen, btnBlue, btnEraser;
    ImageView imageView;                // 캡쳐한 이미지를 썸네일로 화면에 표시
    Button btnBack, btnReturn, btnSave;
    Board board;                        // 그림판

    int opt_brush_color = Color.BLACK;      //  브러쉬 색상 기본값
    float opt_brush_width = 10f;            // 브러쉬 두께 기본 값
    int mode = Brush.MODE_DRAW;

    // 브러쉬는 값을 조절할 때마다 그림판에 새로 생성됨

    // 캡쳐한 이미지를 저장하는 변수
    Bitmap captured = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 그림판에 들어가는 프레임 레이아웃
        layout = (FrameLayout) findViewById(R.id.frameLayout);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnSave = (Button) findViewById(R.id.btnSave);
        // 색상선택
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkColorListener);

        // 두께 선택
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(thickChangeListener);
//        btnRed.findViewById(R.id.seekBar).setOnClickListener(this);
//        btnGreen.findViewById(R.id.btnGreen).setOnClickListener(this);
//        btnBlue.findViewById(R.id.btnBlue).setOnClickListener(this);
//        btnEraser.findViewById(R.id.btnEraser).setOnClickListener(this);

        // 썸네일 이미지뷰
        imageView = (ImageView) findViewById(R.id.imageView);
        btnSave.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
                  captureBoard();
                 }
           });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = board.brushes.size();
                board.brushes.remove(count-1);
            }
        });

        // 1. 그림판을 새로 생성한다.
        board = new Board(getBaseContext());
        // 2. 생성된 보드를 화면에 세팅한다.
        layout.addView(board);
        // 3. 기본 브러쉬 세팅
        setBrush(opt_brush_color, opt_brush_width, mode);
    }

    private void captureBoard() {
        // 드로잉 캐쉬를 지워주고
        layout.destroyDrawingCache();
        // 다시 만들고
        layout.buildDrawingCache();
        // 레이아웃의 그려진 내용을 Bitmap 형태로 가져온다.
        captured = layout.getDrawingCache();
        // 캡쳐한 이미지를 썸네일에 보여준다.
        imageView.setImageBitmap(captured);
    }

// 컬러와 두께는 조절할 때마다 새로운 브러쉬를 생성하여 그림판에 담는다.
// 사용하지 않은 브러쉬는 그냥 버려진다.

    // 컬러 옵션값 조절
    private void setBrushColor(int colorType){
        opt_brush_color = colorType;
        setBrush(opt_brush_color, opt_brush_width, mode);
    }

    // 두께 옵션값 조절
    private void setBrushStroke(float width){

        opt_brush_width = width;
        setBrush(opt_brush_color, opt_brush_width, mode);
    }

    // 현재 설정된 옵션값을 사용하여 브러쉬를 새로 생성하고 그림판에 담는다.
    private void setBrush(int color, float width, int mode) {
        Brush brush = Brush.newInstance(color, width, mode);
        board.setBrush(brush);
    }


    // 컬러 리스너
    RadioGroup.OnCheckedChangeListener checkColorListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.btnRed :
                    mode = Brush.MODE_DRAW;
                    setBrushColor(Color.RED);
                    break;
                case R.id.btnGreen :
                    mode = Brush.MODE_DRAW;
                    setBrushColor(Color.GREEN);
                    break;
                case R.id.btnBlue :
                    mode = Brush.MODE_DRAW;
                    setBrushColor(Color.BLUE);
                    break;
                case R.id.btnEraser :
                    mode = Brush.MODE_ERASE;
                    setBrushColor(Color.WHITE);
                    break;

            }
        }
    };

    // 선굵기 리스너
    SeekBar.OnSeekBarChangeListener thickChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            opt_brush_width = progress + 1;     // seekbar가 0부터 시작하므로 1을 더해준다.
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 터치가 종료되었을 때만 값을 세팅해준다.
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            setBrushStroke(opt_brush_width);
        }
    };
}
