package citi.com.moschool.util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import citi.com.moschool.bean.Score;

/**
 * Created by Zhangyanfu on 2017/3/6.
 * 解析html的工具类
 */

public class JsoupUtil {
    private final String TAG = "JsoupUtil";
    private static JsoupUtil mJsoupUtil;
    private static String[] times;

    private JsoupUtil() {
        times = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    }

    private static JsoupUtil getInstance() {
        if (mJsoupUtil == null) {
            synchronized (JsoupUtil.class) {
                if (mJsoupUtil == null) {
                    return mJsoupUtil = new JsoupUtil();
                }
            }
        }
        return mJsoupUtil;
    }

    private Map<String, String> _getViewStateValue(String html) {
        //LinkedMap 保留插入的顺序
        Map<String, String> viewStateValue = new LinkedHashMap<>();
        if (html != null) {
            Document document = Jsoup.parse(html);
            Element viewstateElement = document.select("input[name=\"__VIEWSTATE\"]").first();
            // Element viewstateGeneratorElement = document.select("input[name=\"__VIEWSTATEGENERATOR\"]").first();
            if (viewstateElement != null) {
                viewStateValue.put(Constants.LOGIN_BODY_NAME_VIEWSTATE, viewstateElement.attr("value"));
            }
        }
        return viewStateValue;
    }

    private Map<String, String> _getNameOrFailedInfo(String html) {
        Map<String, String> returnInfo = new LinkedHashMap<>();
        if (html != null) {
            Document document = Jsoup.parse(html);
            Element nameElement = document.getElementById("xhxm");
            if (nameElement != null) {
                String studentName = nameElement.html();
                Pattern p = Pattern.compile("(.+)[^同学]");
                Matcher m = p.matcher(studentName);
                if (m.find()) {
                    returnInfo.put(Constants.STUDENTNAME, m.group());
                }
            } else {
                Element element = document.select("script[defer]").last();
                if(element!=null)
                {
                    String login_failed_info = element.html();
                    Pattern p = Pattern.compile("([\\u4E00-\\u9FA5]+)");
                    Matcher m = p.matcher(login_failed_info);
                    if(m.find())
                    {
                        returnInfo.put(Constants.FAILEDINFO,m.group());
                    }
                }
            }
        }

        return returnInfo;
    }

    private List<Map<String, String>> _getCourseList(String html) {
        List<Map<String, String>> courseList = new ArrayList<>();
        if (html == null) {
            return courseList;
        }
        Document document = Jsoup.parse(html);
        Element table1 = document.getElementById("Table1");
        Elements tr = table1.getElementsByTag("tr");
        int index = 0;
        for (int i = 0; i < tr.size()-1; i+=2) {
            Elements td = tr.get(i).getElementsByTag("td");
            Elements course = td.select("td[align=\"Center\"]");
            Log.i(TAG, "_getCourseList: coures : size " + course.size());
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < course.size(); j++) {
                String text = course.get(j).text();
                Log.i(TAG, "_getCourseList: text " + text);
                if (!text.equals("")) {
                    map.put(times[index], text);
                }
                index++;
            }
            courseList.add(map);
            index = 0;
        }
        return courseList;

    }

    private List<Score> _getScoreList(String html) {
        List<Score> scoreList = new ArrayList<>();
        if (html == null) {
            Log.i(TAG, "_getScoreList: ----------------------------------------");
            return scoreList;
        }
        Document document = Jsoup.parse(html);
        Elements tables = document.select("table[class=\"datelist\"]");
        Element datas = tables.get(0);
        Elements trs = datas.getElementsByTag("tr");
        boolean first = true;
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).getElementsByTag("td");
            Score score = new Score();
            for (int j = 0; j < tds.size(); j++) {
                String content = tds.get(j).text();
                if (j == 3) {
                    score.setSubject(content);
                } else if (j == 4) {
                    score.setCategory(content);
                } else if (j == 6) {
                    score.setCredit(content);
                } else if (j == 7) {
                    score.setPeacetimeScore(content);
                } else if (j == 9) {
                    score.setFinalScore(content);
                } else if (j == 11) {
                    score.setTotalScore(content);
                }
            }
            scoreList.add(score);

        }
        return scoreList;


    }

    /******************************对外提供的方法****************************************/
    public static Map<String, String> getViewStateValue(String html) {
        return getInstance()._getViewStateValue(html);
    }

    public static Map<String, String> getNameOrFailedInfo(String html) {
        return getInstance()._getNameOrFailedInfo(html);
    }

    public static List<Map<String, String>> getCourseList(String html) {
        return getInstance()._getCourseList(html);
    }

    public static List<Score> getScoreList(String html) {
        return getInstance()._getScoreList(html);
    }
}
