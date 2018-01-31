package com.js.sci.dto;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jskim on 2016. 9. 17..
 * 서울시 문화행사 정보
 *
 */
public class SCIData implements Parcelable {
    private String CULTCODE;    //문화행사코드
    private String SUBJCODE;    //장르분류코드
    private String CODENAME;    //장르명
    private String TITLE;       //제목
    private String STRTDATE;    //시작일자
    private String END_DATE;    //종료일자
    private String TIME;        //시간
    private String PLACE;       //장소
    private String ORG_LINK;    //원문링크주소
    private String MAIN_IMG;    //대표이미지
    private String HOMEPAGE;    //홈페이지
    private String USE_TRGT;    //이용대상
    private String USE_FEE;     //이용요금
    private String SPONSOR;     //주최
    private String INQUIRY;     //문의
    private String SUPPORT;     //주관및후원
    private String ETC_DESC;    //기타내용
    private String AGELIMIT;    //연령
    private String IS_FREE;     //무료구분
    private String TICKET;      //할인, 티켓, 예매정보
    private String PROGRAM;     //프로그램소개
    private String PLAYER;      //출연자정보
    private String CONTENTS;    //본문
    private String GCODE;       //자치구
    private String BOOKMARK;    //즐겨찾기

    public String getCULTCODE() {
        return CULTCODE;
    }

    public String getSUBJCODE() {
        return SUBJCODE;
    }

    public String getCODENAME() {
        return CODENAME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getSTRTDATE() {
        return STRTDATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public String getTIME() {
        return TIME;
    }

    public String getPLACE() {
        return PLACE;
    }

    public String getORG_LINK() {
        return ORG_LINK;
    }

    public String getMAIN_IMG() {
        return MAIN_IMG;
    }

    public String getHOMEPAGE() {
        return HOMEPAGE;
    }

    public String getUSE_TRGT() {
        return USE_TRGT;
    }

    public String getUSE_FEE() {
        return USE_FEE;
    }

    public String getSPONSOR() {
        return SPONSOR;
    }

    public String getINQUIRY() {
        return INQUIRY;
    }

    public String getSUPPORT() {
        return SUPPORT;
    }

    public String getETC_DESC() {
        return ETC_DESC;
    }

    public String getAGELIMIT() {
        return AGELIMIT;
    }

    public String getIS_FREE() {
        return IS_FREE;
    }

    public String getTICKET() {
        return TICKET;
    }

    public String getPROGRAM() {
        return PROGRAM;
    }

    public String getPLAYER() {
        return PLAYER;
    }

    public String getCONTENTS() {
        return CONTENTS;
    }

    public String getGCODE() {
        return GCODE;
    }

    public String getBOOKMARK() {
        return BOOKMARK;
    }

    public void setCULTCODE(String CULTCODE) {
        this.CULTCODE = CULTCODE;
    }

    public void setSUBJCODE(String SUBJCODE) {
        this.SUBJCODE = SUBJCODE;
    }

    public void setCODENAME(String CODENAME) {
        this.CODENAME = CODENAME;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public void setSTRTDATE(String STRTDATE) {
        this.STRTDATE = STRTDATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public void setPLACE(String PLACE) {
        this.PLACE = PLACE;
    }

    public void setORG_LINK(String ORG_LINK) {
        this.ORG_LINK = ORG_LINK;
    }

    public void setMAIN_IMG(String MAIN_IMG) {
        this.MAIN_IMG = MAIN_IMG;
    }

    public void setHOMEPAGE(String HOMEPAGE) {
        this.HOMEPAGE = HOMEPAGE;
    }

    public void setUSE_TRGT(String USE_TRGT) {
        this.USE_TRGT = USE_TRGT;
    }

    public void setUSE_FEE(String USE_FEE) {
        this.USE_FEE = USE_FEE;
    }

    public void setSPONSOR(String SPONSOR) {
        this.SPONSOR = SPONSOR;
    }

    public void setINQUIRY(String INQUIRY) {
        this.INQUIRY = INQUIRY;
    }

    public void setSUPPORT(String SUPPORT) {
        this.SUPPORT = SUPPORT;
    }

    public void setETC_DESC(String ETC_DESC) {
        this.ETC_DESC = ETC_DESC;
    }

    public void setAGELIMIT(String AGELIMIT) {
        this.AGELIMIT = AGELIMIT;
    }

    public void setIS_FREE(String IS_FREE) {
        this.IS_FREE = IS_FREE;
    }

    public void setTICKET(String TICKET) {
        this.TICKET = TICKET;
    }

    public void setPROGRAM(String PROGRAM) {
        this.PROGRAM = PROGRAM;
    }

    public void setPLAYER(String PLAYER) {
        this.PLAYER = PLAYER;
    }

    public void setCONTENTS(String CONTENTS) {
        this.CONTENTS = CONTENTS;
    }

    public void setGCODE(String GCODE) {
        this.GCODE = GCODE;
    }

    public void setBOOKMARK(String BOOKMARK) {
        this.BOOKMARK = BOOKMARK;
    }

    public SCIData() {
    }

    public SCIData(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CULTCODE);
        dest.writeString(SUBJCODE);
        dest.writeString(CODENAME);
        dest.writeString(TITLE);
        dest.writeString(STRTDATE);
        dest.writeString(END_DATE);
        dest.writeString(TIME);
        dest.writeString(PLACE);
        dest.writeString(ORG_LINK);
        dest.writeString(MAIN_IMG);
        dest.writeString(HOMEPAGE);
        dest.writeString(USE_TRGT);
        dest.writeString(USE_FEE);
        dest.writeString(SPONSOR);
        dest.writeString(INQUIRY);
        dest.writeString(SUPPORT);
        dest.writeString(ETC_DESC);
        dest.writeString(AGELIMIT);
        dest.writeString(IS_FREE);
        dest.writeString(TICKET);
        dest.writeString(PROGRAM);
        dest.writeString(PLAYER);
        dest.writeString(CONTENTS);
        dest.writeString(GCODE);
        dest.writeString(BOOKMARK);
    }

    private void readFromParcel(Parcel in) {
        CULTCODE = in.readString();
        SUBJCODE = in.readString();
        CODENAME = in.readString();
        TITLE = in.readString();
        STRTDATE = in.readString();
        END_DATE = in.readString();
        TIME = in.readString();
        PLACE = in.readString();
        ORG_LINK = in.readString();
        MAIN_IMG = in.readString();
        HOMEPAGE = in.readString();
        USE_TRGT = in.readString();
        USE_FEE = in.readString();
        SPONSOR = in.readString();
        INQUIRY = in.readString();
        SUPPORT = in.readString();
        ETC_DESC = in.readString();
        AGELIMIT = in.readString();
        IS_FREE = in.readString();
        TICKET = in.readString();
        PROGRAM = in.readString();
        PLAYER = in.readString();
        CONTENTS = in.readString();
        GCODE = in.readString();
        BOOKMARK = in.readString();
    }

    public static final Creator CREATOR = new Creator<SCIData>() {
        @Override
        public SCIData createFromParcel(Parcel source) {
            return new SCIData(source);
        }

        @Override
        public SCIData[] newArray(int size) {
            return new SCIData[size];
        }
    };

    @Override
    public String toString() {
        return "SearchConcertDetailServiceData{ " +
                "CULTCODE='" + CULTCODE + '\'' +
                ", SUBJCODE='" + SUBJCODE + '\'' +
                ", CODENAME='" + CODENAME + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", STRTDATE='" + STRTDATE + '\'' +
                ", END_DATE='" + END_DATE + '\'' +
                ", TIME='" + TIME + '\'' +
                ", PLACE='" + PLACE + '\'' +
                ", ORG_LINK='" + ORG_LINK + '\'' +
                ", MAIN_IMG='" + MAIN_IMG + '\'' +
                ", HOMEPAGE='" + HOMEPAGE + '\'' +
                ", USE_TRGT='" + USE_TRGT + '\'' +
                ", USE_FEE='" + USE_FEE + '\'' +
                ", SPONSOR='" + SPONSOR + '\'' +
                ", INQUIRY='" + INQUIRY + '\'' +
                ", SUPPORT='" + SUPPORT + '\'' +
                ", ETC_DESC='" + ETC_DESC + '\'' +
                ", AGELIMIT='" + AGELIMIT + '\'' +
                ", IS_FREE='" + IS_FREE + '\'' +
                ", TICKET='" + TICKET + '\'' +
                ", PROGRAM='" + PROGRAM + '\'' +
                ", PLAYER='" + PLAYER + '\'' +
                ", CONTENTS='" + CONTENTS + '\'' +
                ", GCODE='" + GCODE + '\'' +
                ", BOOKMARK='" + BOOKMARK + '\'' +
                '}';
    }
}
