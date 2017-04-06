package citi.com.moschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 2017/3/10.
 */

public class Score implements Parcelable {
    //学科
    private String subject;
    //课程类别
    private String category;
    //平时成绩
    private String peacetimeScore;
    //期末成绩
    private String finalScore;
    //总评成绩
    private String totalScore;
    //学分
    private String credit;
    //绩点
//    private String gradePoint;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPeacetimeScore() {
        return peacetimeScore;
    }

    public void setPeacetimeScore(String peacetimeScore) {
        this.peacetimeScore = peacetimeScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
//
//    public String getGradePoint() {
//        return gradePoint;
//    }
//
//    public void setGradePoint(String gradePoint) {
//        this.gradePoint = gradePoint;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(category);
        dest.writeString(peacetimeScore);
        dest.writeString(finalScore);
        dest.writeString(totalScore);
        dest.writeString(credit);
//        dest.writeString(gradePoint);

    }
    public static final Parcelable.Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel source) {
            Score score = new Score();
            score.subject  = source.readString();
            score.category = source.readString();
            score.peacetimeScore = source.readString();
            score.finalScore = source.readString();
            score.totalScore = source.readString();
            score.credit = source.readString();
//            score.gradePoint  =source.readString();
            return score;
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
