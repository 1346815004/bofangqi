package com.example.bofangqi;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    int[] dizhi = {R.raw.cat96_1,R.raw.cat96_2,R.raw.cat96_3,R.raw.cat96_4,R.raw.cat96_5};
    String[] geming = {"96猫 - からくりピエロ (双声道版)","96猫 - がランド","96猫 - サイレントエレジー","96猫 - ネトゲ廃人シュプレヒコール","96猫 - 深海のリトルクライ"};
    TextView tv_1,tv_2,tv_3;
    Button btn_1,btn_2,btn_3,btn_4,btn_5;
    static int num = 0;
    MediaPlayer mediaPlayer;
    SeekBar sb;
    boolean isplay;
    int p = 0;
    Spinner sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();

        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        sb = findViewById(R.id.sb);
        sp1 = findViewById(R.id.sp1);


        test(num);
        initview();

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                }
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    mediaPlayer.stop();
//                    test(num);
                Toast.makeText(MainActivity.this, mediaPlayer.isPlaying()+"", Toast.LENGTH_SHORT).show();
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0)
                    num = 4;
                else
                    num--;
                test(num);
                int zong = mediaPlayer.getDuration() / 1000;
                int dangqian = mediaPlayer.getCurrentPosition() / 1000;
                tv_2.setText(calculateTime(dangqian));
                tv_3.setText(calculateTime(zong));
                mediaPlayer.start();
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 4)
                    num = 0;
                else
                    num++;
                test(num);
                int zong = mediaPlayer.getDuration() / 1000;
                int dangqian = mediaPlayer.getCurrentPosition() / 1000;
                tv_2.setText(calculateTime(dangqian));
                tv_3.setText(calculateTime(zong));
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int zong = mediaPlayer.getDuration() / 1000;
                int dangqian = mediaPlayer.getCurrentPosition();
                while(true){
                    zong = mediaPlayer.getDuration() / 1000;
                    dangqian = mediaPlayer.getCurrentPosition() / 10;
                    tv_2.setText(calculateTime(dangqian/100));
                    sb.setProgress(dangqian/zong);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }



    public void initview(){
        int zong = mediaPlayer.getDuration() / 1000;
        int dangqian = mediaPlayer.getCurrentPosition() / 1000;
        tv_2.setText(calculateTime(dangqian));
        tv_3.setText(calculateTime(zong));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int zong = mediaPlayer.getDuration() / 1000;//获取音乐总时长
                int dangqian = mediaPlayer.getCurrentPosition() / 1000;//获取当前播放的位置
                tv_2.setText(calculateTime(dangqian));//开始时间
                tv_3.setText(calculateTime(zong));//总时长
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isplay = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isplay = false;
                mediaPlayer.seekTo(mediaPlayer.getDuration()*seekBar.getProgress()/100);//在当前位置播放
                tv_2.setText(calculateTime(mediaPlayer.getCurrentPosition() / 1000));
            }
        });
    }

    //计算播放时间
    public String calculateTime(int time){
        int minute;
        int second;
        if(time > 60){
            minute = time / 60;
            second = time % 60;
                //判断秒
                if(second >= 0 && second < 10){
                    return "0"+minute+":"+"0"+second;
                }else {
                    return "0"+minute+":"+second;
                }
        }else if(time < 60){
            second = time;
            if(second >= 0 && second < 10){
                return "00:"+"0"+second;
            }else {
                return "00:"+ second;
            }
        }else{
            return "01:00";
        }
    }




    public void test(int i){
        tv_1 = findViewById(R.id.tv_1);
        tv_1.setText(geming[i]);
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, dizhi[i]);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                jieshu();
            }
        });
    }


    public void jieshu(){
        Toast.makeText(MainActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
        String bf = sp1.getSelectedItem().toString();
        switch (bf){
            case "单曲循环":
                mediaPlayer.start();
                break;
            case "顺序播放":
                if (num == 4)
                    num = 0;
                else
                    num++;
                test(num);
                mediaPlayer.start();
                break;
            case "随机播放":
                num = (int) (Math.random()*4);
                Log.d("num:", num+"");
                test(num);
                mediaPlayer.start();
                break;
        }
    }
}