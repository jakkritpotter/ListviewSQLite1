package com.android.mobile.listviewsqlite1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //ตัวแปรของ View
    private Button btnAdd;
    private ListView listMember;
    //list ในกำรเก็บข้อมูลของ MemberData
    private ArrayList<MemberData> listData = new ArrayList<MemberData>();
    //ตัวจัดกำรฐำนข้อมูล
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        listMember = (ListView)findViewById(R.id.listMember);
        //สร้ำง Event ให้ปุ่ มเพิ่มข้อมูล
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //addMember();
                Intent intent = new Intent(MainActivity.this, AddBotanyActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        //สร้ำงตัวจัดกำรฐำนข้อมูล
        dbHelper = new DatabaseHelper(this);
        //น ำตัวจัดกำรฐำนข้อมูลมำใช้งำน
        database = dbHelper.getWritableDatabase();
        //แสดงรำยกำรสมำชิก
        showList();
    }
    //Method แก้ไขข้อมูลใน SQLite
    public void editMember(int id,String t_name,String s_name,String detail_b,String bs_b,byte[] image){
        //เตรียมค่ำต่ำงๆ เพื่อท ำกำรแก้ไข
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("t_name_b", t_name);
        values.put("s_name_b", s_name);
        values.put("detail_b", detail_b);
        values.put("bs_b", bs_b);
        values.put("image", image);
        //ให้ Database ท ำกำรแก้ไขข้อมูลที่ id ที่ก ำหนด
        database.update("botany", values, "id = ?", new String[] { ""+id });
        //แสดงข้อมูลล่ำสุด
        showList();
    }
    //Method ลบข้อมูลใน SQLite
    public void deleteMember(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete botay");
        builder.setMessage("Are you sure detele this botany?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                database.delete("botany", "id = " + id, null);
                Toast.makeText(MainActivity.this, "Delete Data Id " + id + " Complete",
                        Toast.LENGTH_SHORT).show();
                showList();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    //Method ดึงข้อมูลจำก SQLite
    private void getMember() {
        //ท ำกำร Query ข้อมูลจำกตำรำง botany ใส่ใน Cursor
        Cursor mCursor = database.query(true, "botany", new String[] {
                        "id", "t_name_b", "s_name_b", "detail_b", "bs_b","image" }, null,
                null, null, null, null, null);
        //หรือใช้ Cursor mCursor = database.rawQuery("SELECT * FROM botany", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            listData.clear();
            //ถ้ำมีข้อมูลจะท ำกำรเก็บข้อมูลใส่ List เพื่อน ำไปแสดง
            if(mCursor.getCount() > 0){
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex("id"));
                    String t_name = mCursor.getString(mCursor.getColumnIndex("t_name_b"));
                    String s_name = mCursor.getString(mCursor.getColumnIndex("s_name_b"));
                    String detail_b = mCursor.getString(mCursor.getColumnIndex("detail_b"));
                    String bs_b = mCursor.getString(mCursor.getColumnIndex("bs_b"));
                    byte[] image = mCursor.getBlob(mCursor.getColumnIndex("image"));
                    listData.add(new MemberData(id, t_name, s_name, detail_b,bs_b,image));
                }while (mCursor.moveToNext());
            }
        }
    }

    private void showList() {
        //ดึงข้อมูลสมำชิกจำก SQLite Database
        getMember();
        //แสดงสมำชิกใน ListView
        listMember.setAdapter(new AdapterListViewData(this,listData));
    }
    public void showEdit(int id,String t_name_b,String s_name_b,String detail_b,String bs_b,byte[] image){
        Intent i = new Intent(this,EditActivity.class);
        //ท ำกำรส่งค่ำต่ำงๆ ให้ EditActivity ไปท ำกำรแก้ไข
        i.putExtra("keyId", id);
        i.putExtra("keyT_name_b", t_name_b);
        i.putExtra("keyS_name_b", s_name_b);
        i.putExtra("keyDetail_b", detail_b);
        i.putExtra("keyBs_b", bs_b);
        i.putExtra("keyImage", image);
        //***** ในกำรส่งค่ำและรับค่ำ ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//
        //ท ำกำรเรียก EditActivity โดยให้ Request Code เป็น 1
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //ถ้ำกลับมำหน้ำ MainActivity แล้วผลลัพธ์กำรท ำงำนสมบูรณ์
        if(requestCode == 1 && resultCode == RESULT_OK){
            //เก็บค่ำที่ส่งกลับมำใส่ตัวแปร
            int id = intent.getExtras().getInt("keyId");
            String t_name_b = intent.getExtras().getString("keyT_name_b");
            String s_name_b = intent.getExtras().getString("keyS_name_b");
            String detail_b = intent.getExtras().getString("keyDetail_b");
            String bs_b = intent.getExtras().getString("keyBs_b");
            //Bundle extras = getIntent().getExtras();
            //byte[] byteArray = extras.getByteArray("keyImage");
            byte[] byteArray = intent.getByteArrayExtra("keyImage");
            //***** ในกำรส่งค่ำและรับค่ำ ส่งเป็นตัวแปรชนิดไหน ต้องรับเป็นตัวแปรชนิดนั้น *****//
            //ให้แก้ไขข้อมูลสมำชิก
            editMember(id, t_name_b, s_name_b, detail_b,bs_b,byteArray);
        }
    }
}
