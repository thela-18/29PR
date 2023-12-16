package com.example.a29pr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    final int TYPE_VIDEO = 2;
    final int REQUEST_CODE_VIDEO = 2;


    final int CAMERA_REQUEST = 1;
    final int PIC_CROP = 2;
    private Uri picUri;
    Button btnWeb, btnMap, btnCall, btnPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle("Съёмка и Кадрирование");
        btnWeb = findViewById(R.id.btnWeb);
        btnMap = findViewById(R.id.btnMap);
        btnCall = findViewById(R.id.btnCall);
        btnPhoto = findViewById(R.id.btnPhoto);


        btnWeb.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {

        Intent intent;
        switch(v.getId()) {
            case R.id.btnPhoto:
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(captureIntent, CAMERA_REQUEST);
                break;
            case R.id.btnVideo:
                Intent intent2 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, (TYPE_VIDEO));
                startActivityForResult(intent2, REQUEST_CODE_VIDEO);

                break;
            case R.id.btnWeb:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com"));
                startActivity(intent);
                break;
            case R.id.btnMap:
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:55.043581,82.917402"));
                startActivity(intent);
                break;
            case R.id.btnCall:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"));
                startActivity(intent);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Вернулись от приложения Камера
            if (requestCode == CAMERA_REQUEST) {
                // Получим Uri снимка
                picUri = data.getData();
                // кадрируем его
                performCrop();
            }
            // Вернулись из операции кадрирования
            else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                // Получим кадрированное изображение
                Bitmap thePic = extras.getParcelable("data");
                // передаём его в ImageView
                ImageView picView = findViewById(R.id.ivPhoto);
                picView.setImageBitmap(thePic);
            }
        }
    }

    private void performCrop(){
        try {
            // Намерение для кадрирования. Не все устройства поддерживают его
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            String errorMessage = "Извините, но ваше устройство не поддерживает кадрирование";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }



    }

}