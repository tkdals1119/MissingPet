package com.jbnu.capstone.missingpet.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.jbnu.capstone.missingpet.R;
import com.jbnu.capstone.missingpet.service.DB_Manger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WriteActivity extends AppCompatActivity {

    final int REQ_CODE_SELECT_IMAGE=100;
    private static final String TAG = "@@@@@@@@@@@@@";
    @BindView(R.id.taken_image) ImageView taken_image;

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ButterKnife.bind(this); // 꼭 써야함

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }
    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addfile:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "파일 추가 버튼", Toast.LENGTH_LONG).show();
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(WriteActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    0);
                        }
                        Log.d(TAG, "@@@@@@@@@@@@@@@@@@@startActivityForResult");

                        startActivityForResult(intent, 0);
                    }
                };
                DialogInterface.OnClickListener albumlistener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    }
                };DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            };
                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("취소", cancelListener)
                        .setNegativeButton("앨범선택", albumlistener)
                        .show();
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "전송 버튼", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try
                {
                    //Uri에서 이미지 이름을 얻어온다.
                    // String name_Str = getImageNameToUri(data.getData());
                    // 이미지 데이터를 비트맵으로 받아온다.
                     Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                     ImageView image = (ImageView)findViewById(R.id.taken_image);
                     //배치해놓은 ImageView에 set image.setImageBitmap(image_bitmap);

                    // Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
                     } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                             e.printStackTrace();
                      } catch (IOException e) {
                            // TODO Auto-generated catch block
                             e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                }
            }
        }
    }

