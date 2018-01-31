package com.js.sci.database;

/**
 * Created by jskim on 2017. 9. 11..
 */

public interface SCIDatabaseConstants {
    String DATABASE_NAME = "sci.sqlite";
    int DATABASE_VERSION = 1;

    String TABLE_CONCERT_DETAIL = "CONCERT_DETAIL";

    String CULTCODE = "CULTCODE";     //문화행사코드
    String SUBJCODE = "SUBJCODE";     //장르분류코드
    String CODENAME = "CODENAME";     //장르명
    String TITLE = "TITLE";           //제목
    String STRTDATE = "STRTDATE";     //시작일자
    String END_DATE = "END_DATE";     //종료일자
    String TIME = "TIME";             //시간
    String PLACE = "PLACE";           //장소
    String ORG_LINK = "ORG_LINK";     //원문링크주소
    String MAIN_IMG = "MAIN_IMG";     //대표이미지
    String HOMEPAGE = "HOMEPAGE";     //홈페이지
    String USE_TRGT = "USE_TRGT";     //이용대상
    String USE_FEE = "USE_FEE";       //이용요금
    String SPONSOR = "SPONSOR";       //주최
    String INQUIRY = "INQUIRY";       //문의
    String SUPPORT = "SUPPORT";       //주관및후원
    String ETC_DESC = "ETC_DESC";     //기타내용
    String AGELIMIT = "AGELIMIT";     //연령
    String IS_FREE = "IS_FREE";       //무료구분
    String TICKET = "TICKET";         //할인, 티켓, 예매정보
    String PROGRAM = "PROGRAM";       //프로그램소개
    String PLAYER = "PLAYER";         //출연자정보
    String CONTENTS = "CONTENTS";     //본문
    String GCODE = "GCODE";           //자치구
    String BOOKMARK = "BOOKMARK";     //북마크

    String CREATE_TABLE_CONCERT_DETAIL = "CREATE TABLE IF NOT EXISTS " +  TABLE_CONCERT_DETAIL + " (" +
            CULTCODE            + " TEXT PRIMARY KEY NOT NULL, "+
            SUBJCODE    + " TEXT, "+
            CODENAME    + " TEXT, "+
            TITLE       + " TEXT, "+
            STRTDATE    + " TEXT, "+
            END_DATE    + " TEXT, "+
            TIME        + " TEXT, "+
            PLACE       + " TEXT, "+
            ORG_LINK    + " TEXT, "+
            MAIN_IMG    + " TEXT, "+
            HOMEPAGE    + " TEXT, "+
            USE_TRGT    + " TEXT, "+
            USE_FEE     + " TEXT, "+
            SPONSOR     + " TEXT, "+
            INQUIRY     + " TEXT, "+
            SUPPORT     + " TEXT, "+
            ETC_DESC    + " TEXT, "+
            AGELIMIT    + " TEXT, "+
            IS_FREE     + " TEXT, "+
            TICKET      + " TEXT, "+
            PROGRAM     + " TEXT, "+
            PLAYER      + " TEXT, "+
            CONTENTS    + " TEXT, "+
            GCODE       + " TEXT, "+
            BOOKMARK    + " TEXT);";

    String DROP_TABLE_CONCERT_DETAIL = "DROP TABLE IF EXISTS " + TABLE_CONCERT_DETAIL;


    enum SELECT_TYPE {
        LIST, BOOKMARK, BOOKMARKS, SEARCH, DATA;
    };

    String AND = "AND";
    String OR = "OR";
    String EQ  = "=?";      // equal to
    String GE = ">=?";      // greater than or equal to
    String LE = "<=?";      // less than or equal to

    String LIKE = "LIKE";
    String ASC = "ASC";
    String DESC = "DESC";
    String OFFSET = "OFFSET";
}
