package com.android.mobile.listviewsqlite1;

/**
 * Created by 5N1P3R on 21/3/2560.
 */

public class MemberData {
    private int id;
    private String t_name_b;
    private String s_name_b;
    private String detail_b;
    private String bs_b;
    private byte[] image;
    public MemberData(int id, String t_name, String s_name, String detail_b,String bs_b,byte[] image) {
        this.id = id;
        this.t_name_b = t_name;
        this.s_name_b = s_name;
        this.detail_b = detail_b;
        this.bs_b=bs_b;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public String getT_name() {
        return t_name_b;
    }

    public String getS_name() {
        return s_name_b;
    }

    public String getDetail_b() {
        return detail_b;
    }

    public String getBs_b() {
        return bs_b;
    }

    public byte[] getImage() {
        return image;
    }
}
