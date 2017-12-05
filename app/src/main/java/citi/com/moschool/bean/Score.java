package citi.com.moschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 2017/3/10.
 */

public class Score  {
    //课程名称
    private String td3;
    //平时成绩
    private String td7;
    //期末成绩
    private String td9;

    public String getTd3() {
        return td3;
    }

    public void setTd3(String td3) {
        this.td3 = td3;
    }

    public String getTd7() {
        return td7;
    }

    public void setTd7(String td7) {
        this.td7 = td7;
    }

    public String getTd9() {
        return td9;
    }

    public void setTd9(String td9) {
        this.td9 = td9;
    }

    public Score(String td3, String td7, String td9) {
        this.td3 = td3;
        this.td7 = td7;
        this.td9 = td9;
    }

    public Score() {
    }

    @Override
    public String toString() {
        return "Score{" +
                "td3='" + td3 + '\'' +
                ", td7='" + td7 + '\'' +
                ", td9='" + td9 + '\'' +
                '}';
    }
}
