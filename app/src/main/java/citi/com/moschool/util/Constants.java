package citi.com.moschool.util;

import citi.com.moschool.bean.Score;

/**
 * Created by zfy on 2017/3/6.
 */

public class Constants {

  /************************环境上的HOST********************************************/
    public static final String HOST = "http://116.196.113.107";
    /************************本地的HOST********************************************/
//    public static final String HOST = "http://192.168.191.1";

    public static final String LOGIN_SCHOOL =HOST+ "/loginSchool";//
    public static final String LOGIN_WEB =HOST+ "/mobileUser/userLogin";
    public static final String ADD_ARTICLE = HOST+"/article/add";
    public static final String FOUND_ARTICLE = HOST+"/mobile/find";
    public static final String RECOMMEND_ARTICLE = HOST+"/mobile/recommend";
    public static final String RECRUIT_ARTICLE = HOST+"/mobile/recruit";
    public static final String GET_GRADE_HOST= HOST+"/getJsonOfGrade";
    public static final String GET_SCHEDULE_HOST= HOST+"/getJsonTable";
    //http://172.46.136.199/mobile/recruit

}
