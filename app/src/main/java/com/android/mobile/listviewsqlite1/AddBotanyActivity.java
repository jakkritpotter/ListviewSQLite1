package com.android.mobile.listviewsqlite1;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

public class AddBotanyActivity extends AppCompatActivity {

    private EditText txtt_name,txts_name,txtdetail_b,txtbs_b;
    private Button btnAdd,btnChoos;
    ImageView imgPic;


    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_botany);
        //เชื่อม View
        txtt_name = (EditText)findViewById(R.id.txtt_Name_b);
        txts_name = (EditText)findViewById(R.id.txts_Name_b);
        txtdetail_b = (EditText)findViewById(R.id.txtdetail_b);
        txtbs_b = (EditText)findViewById(R.id.txtbs_b);
        imgPic = (ImageView)findViewById(R.id.imageView);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnChoos = (Button) findViewById(R.id.btnChoosImage);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //addMember();
                try{
                    insertData(
                            txtt_name.getText().toString().trim(),
                            txts_name.getText().toString().trim(),
                            txtdetail_b.getText().toString().trim(),
                            txtbs_b.getText().toString().trim(),
                            imageViewToByte(imgPic)
                    );
                    Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                    txtt_name.setText("");
                    txts_name.setText("");
                    txtdetail_b.setText("");
                    txtbs_b.setText("");
                    imgPic.setImageResource(R.mipmap.ic_launcher);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(AddBotanyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        //สร้ำงตัวจัดกำรฐำนข้อมูล
        dbHelper = new DatabaseHelper(this);
        //น ำตัวจัดกำรฐำนข้อมูลมำใช้งำน
        database = dbHelper.getWritableDatabase();
        //แสดงรำยกำรสมำชิก

        //choose image
        btnChoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AddBotanyActivity.this,
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
    //Method เพิ่มข้อมูลใน SQLite
    private void addMember() {
        // TODO Auto-generated method stub
        if(txtt_name.length() > 0 && txts_name.length() > 0 && txtdetail_b.length() > 0){
            //เตรียมข้อมูลส ำหรับใส่ลงไปในตำรำง
            ContentValues values = new ContentValues();
            values.put("t_name_b", txtt_name.getText().toString());
            values.put("s_name_b", txts_name.getText().toString());
            values.put("detail_b", txtdetail_b.getText().toString());
            values.put("bs_b", txtbs_b.getText().toString());
            //ท ำกำรเพิ่มข้อมูลลงไปในตำรำง member
            database.insert("botany", null, values);
            Toast.makeText(this, "Add Data Complete", Toast.LENGTH_SHORT).show();
            //ล้ำงข้อมูล From
            txtt_name.setText("");
            txts_name.setText("");
            txtdetail_b.setText("");
            //showList();
        }else{
            Toast.makeText(this, "Please Input Data", Toast.LENGTH_SHORT).show();
        }
    }
    public void insertData(String t_name, String s_name,String detail_b,String bs_b, byte[] image){
        //SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO botany VALUES (NULL, ?, ?, ?, ?, ?)";

        if(txtt_name.length() > 0 && txts_name.length() > 0 && txtdetail_b.length() > 0&&txtbs_b.length()>0&&imgPic!=null){
            SQLiteStatement statement = database.compileStatement(sql);
            statement.clearBindings();

            statement.bindString(1, t_name);
            statement.bindString(2, s_name);
            statement.bindString(3, detail_b);
            statement.bindString(4, bs_b);
            statement.bindBlob(5, image);

            statement.executeInsert();
        }else{
            Toast.makeText(this, "Please Input Data", Toast.LENGTH_SHORT).show();
        }
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
