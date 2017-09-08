package com.android.mobile.listviewsqlite1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 5N1P3R on 21/3/2560.
 */

public class AdapterListViewData extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private MainActivity control;
    //list ในกำรเก็บข้อมูลของ MemberData
    private ArrayList<MemberData> listData = new ArrayList<MemberData>();

    public AdapterListViewData(MainActivity control,ArrayList<MemberData> listData) {
        this.control = control;
        this.context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderListAdapter holderListAdapter; //เก็บส่วนประกอบของ List แต่ละอัน
        if(convertView == null)
        {
            //ใช้ Layout ของ List เรำเรำสร้ำงขึ้นเอง (adapter_listview.xml)
            convertView = mInflater.inflate(R.layout.adapter_listview, null);
            //สร้ำงตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapter = new HolderListAdapter();
            //เชื่อมส่วนประกอบต่ำงๆ ของ List เข้ำกับ View
            holderListAdapter.txtt_name = (TextView) convertView.findViewById(R.id.txtt_name);
            holderListAdapter.txts_name = (TextView) convertView.findViewById(R.id.txts_name);
            //holderListAdapter.txtdetail_b = (TextView) convertView.findViewById(R.id.txtdetail_b);
            //holderListAdapter.txtbs_b = (TextView) convertView.findViewById(R.id.txtbs_b);
            holderListAdapter.imgPic = (ImageView) convertView.findViewById(R.id.imageView);
            holderListAdapter.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
            holderListAdapter.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holderListAdapter);
        }else{
            holderListAdapter = (HolderListAdapter) convertView.getTag();
        }
        //ดึงข้อมูลจำก listData มำแสดงทีละ position
        final int id = listData.get(position).getId();
        final String t_name = listData.get(position).getT_name();
        final String s_name = listData.get(position).getS_name();
        final String detail_b = listData.get(position).getDetail_b();
        final String bs_b = listData.get(position).getBs_b();
        final byte[] image = listData.get(position).getImage();
        holderListAdapter.txtt_name.setText(t_name);
        holderListAdapter.txts_name.setText(s_name);
        final Bitmap imageB = BitmapFactory.decodeByteArray(image, 0, image.length);
        holderListAdapter.imgPic.setImageBitmap(imageB);
        //สร้ำง Event ให้ปุ่ ม Delete
        holderListAdapter.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control.deleteMember(id);
            }
        });
        //สร้ำง Event ให้ปุ่ ม Edit
        holderListAdapter.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control.showEdit(id, t_name, s_name, detail_b,bs_b,image);
            }
        });
        return convertView;
    }
}
