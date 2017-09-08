package com.android.mobile.listviewsqlite1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {

    //ตัวแปรของ View
    private EditText txtt_name,txts_name,txtdetail_b,txtbs_b;
    private ImageView imgPic;
    private Button btnEdit,btnChoos;
    //ตัวแปรไว้เก็บว่ำข้อมูลที่จะแก้ไข id เป็นอะไร
    private int id;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //เชื่อม View
        txtt_name = (EditText)findViewById(R.id.txtt_Name_b);
        txts_name = (EditText)findViewById(R.id.txts_Name_b);
        txtdetail_b = (EditText)findViewById(R.id.txtdetail_b);
        txtbs_b = (EditText)findViewById(R.id.txtbs_b);
        imgPic = (ImageView) findViewById(R.id.imageView);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnChoos = (Button) findViewById(R.id.btnChoosImage);

        //รับค่ำจำก MainActivity มำแสดงข้อมูลเพื่อท ำกำรแก้ไข
        this.id = getIntent().getExtras().getInt("keyId");
        txtt_name.setText(getIntent().getExtras().getString("keyT_name_b"));
        txts_name.setText(getIntent().getExtras().getString("keyS_name_b"));
        txtdetail_b.setText(getIntent().getExtras().getString("keyDetail_b"));
        txtbs_b.setText(getIntent().getExtras().getString("keyBs_b"));
        //Bundle extras = getIntent().getExtras();
        //byte[] byteArray = extras.getByteArray("keyImage");
        byte[] byteArray = getIntent().getExtras().getByteArray("keyImage");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgPic.setImageBitmap(bmp);
        //***** ในกำรส่งค่ำและรับค่ำ ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//
        //สร้ำง Event ให้ปุ่ มแก้ไขข้อมูล
        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                //ตั้งค่ำผลลัพธ์กำรท ำงำนว่ำ RESULT_OK
                setResult(RESULT_OK,i);
                //ส่งข้อมูลกลับไปให้ MainActivity ท ำกำรแก้ไขข้อมูลให้
                i.putExtra("keyId", id);
                i.putExtra("keyT_name_b", txtt_name.getText().toString());
                i.putExtra("keyS_name_b", txts_name.getText().toString());
                i.putExtra("keyDetail_b", txtdetail_b.getText().toString());
                i.putExtra("keyBs_b", txtbs_b.getText().toString());
                i.putExtra("keyImage", imageViewToByte(imgPic));
                //i.putExtra("keyImage", byteArray);
                //***** ในกำรส่งค่ำและรับค่ำ ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//
                finish();
                //startActivity(i);
            }
        });
        //choose image
        btnChoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
    }
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPic.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
