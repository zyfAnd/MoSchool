package citi.com.moschool.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author : Zhangyanfu
 * Date ：2017.3.1
 */

public class OkHttpUtil {
    private final String TAG = "OkHttpUtil";
    private static OkHttpUtil mHttpUtil;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mOkHttpBuilder;
    private Handler mDelivery;

    private OkHttpUtil() {
        mOkHttpBuilder = new OkHttpClient.Builder();
        mOkHttpBuilder.cookieJar(new MyCookieJar());
        mOkHttpClient = mOkHttpBuilder.build();
        mDelivery = new Handler(Looper.myLooper());
    }

    private static OkHttpUtil getInstance() {
        if (mHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (mHttpUtil == null) {
                    mHttpUtil = new OkHttpUtil();
                }
            }
        }
        return mHttpUtil;
    }

    /**
     * 同步的get请求 返回response对象
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Response _getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    /**
     * 同步的get请求 返回一个字符串
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String _getSyncString(String url) throws IOException {
        return _getSync(url).body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url
     */
    private void _getAsync(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    /**
     * 同步的post请求 返回Response对象
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Response _postSync(String url, RequestData[] params, RequestData... headers) throws IOException {
        Request request = buildPostReqeust(url, params, headers);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的 post请求 返回String字符串
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     */
    private String _postSyncString(String url, RequestData[] params, RequestData... headers) throws IOException {
        Response response = _postSync(url, params, headers);
        return response.body().string();
    }

    /**
     * 异步post请求 请求参数是RequestData类型
     *
     * @param url
     * @param callback
     * @param params
     * @param headers
     */
    private void _postAsync(String url, final ResultCallback callback, RequestData[] params, RequestData... headers) {
        Request request = buildPostReqeust(url, params, headers);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求 请求参数是map类型
     *
     * @param url
     * @param callback
     * @param params
     * @param headers
     */
    private void _postAsync(String url, final ResultCallback callback, Map<String, String> params, Map<String, String> headers) {

//        Set<Map.Entry<String, String>> entries = params.entrySet();
//
//        Log.i(TAG, "_postAsync: =============================================== ");
//        Set<String> keys = params.keySet();
//        for (String key:keys)
//        {
//            Log.i(TAG, "_postAsync: params  "+" key: "+key+" value :"+params.get(key));
//        }
//        Log.i(TAG, "_postAsync end : =============================================== ");

//        Set<String> headersKeys = headers.keySet();
//        for (String key: headersKeys)
//        {
//            Log.i(TAG, "_postAsync: headers:"+" key: "+key+" value: " +headers.get(key));
//        }
//        Log.i(TAG, "_postAsync: =============================================== ");
        RequestData[] requestParams = mapToRequestData(params);
        RequestData[] requestHeaders = mapToRequestData(headers);
        Log.i(TAG, "_postAsync: ===============================================================");
//        for (int i=0;i<requestParams.length;i++)
//        {
//            Log.i(TAG, "_postAsync: requestParams "+" key :"+requestParams[i].key+" value:"+requestParams[i].value);
//        }
//        Log.i(TAG, "_postAsync: ===============================================================");
//        for (int i=0;i<requestHeaders.length;i++)
//        {
//            Log.i(TAG, "_postAsync: requestHeaders "+" key"+requestHeaders[i].key+" value:"+requestHeaders[i].value);
//        }
//        Log.i(TAG, "_postAsync: requestHeaders============end");
        Request request = buildPostReqeust(url, requestParams, requestHeaders);
        deliveryResult(callback, request);
    }

    private RequestData[] mapToRequestData(Map<String, String> params) {
        int index = 0;
        if (params == null) {
            return new RequestData[0];
        }
        int size = params.size();
        RequestData[] res = new RequestData[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            Log.i(TAG, "mapToRequestData: ===========================================================================");

            Log.i(TAG, "mapToRequestData: "+"key:"+entry.getKey()+" value:"+entry.getValue());
            res[index++] = new RequestData(entry.getKey(), entry.getValue());
        }
        Log.i(TAG, "mapToRequestData: res length:"+res.length+" ");
        for (int i=0;i<res.length;i++)
        {
            Log.i(TAG, "mapToRequestData: "+"key:"+res[i].key+" value:"+res[i].value);
        }
        return res;
    }

    /**
     * 构建post请求参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Request buildPostReqeust(String url, RequestData[] params, RequestData... headers) {
        if (headers == null) {
            headers = new RequestData[0];
        }
        Headers.Builder headersBuilder = new Headers.Builder();
        for (RequestData header : headers) {
            headersBuilder.add(header.key, header.value);
        }
        Headers requestHeaders = headersBuilder.build();
        if (params == null) {
            params = new RequestData[0];
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Log.i(TAG, "buildPostReqeust: ---------------------------------------------------------------");
        for (RequestData param : params) {
//            Log.i(TAG, "buildPostReqeust: "+" key:"+param.key +" value:"+param.value);
            formBodyBuilder.add(param.key, param.value);

        }
        FormBody requestFormBody = formBodyBuilder.build();
        Log.i(TAG, "buildPostReqeust: url "+url);
        return new Request.Builder().url(url).headers(requestHeaders).post(requestFormBody).build();

    }

    /**
     * 调用call.enqueue 将加入调度队列 执行完成后 在callback中得到结果
     *
     * @param callback
     * @param request
     */

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
                sendFailedStringCall(call, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " +response.toString());
                try {
                    switch (response.code()) {
                        case 200:
                            final byte[] bytes = response.body().bytes();
                            sendSuccessResultCallback(bytes, callback);
                            break;
                        case 500:
                            sendSuccessResultCallback(null, callback);
                            break;
                        default:
                            throw new IOException();
                    }
                } catch (Exception e) {
                    sendFailedStringCall(call, e, callback);
                }
            }
        });
    }

    /**
     * 请求回调失败对应的回调方法 利用 handler.post_article 使得回调方法在UI线程中执行
     *
     * @param call
     * @param e
     * @param callback
     */
    private void sendFailedStringCall(final Call call, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(call, e);
                }
            }
        });
    }

    /**
     * 请求成功对应的回调方法 利用handler.post_article 使得回调方法在UI 线程中执行
     *
     * @param bytes
     * @param callback
     */
    private void sendSuccessResultCallback(final byte[] bytes, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(bytes);
                }
            }
        });
    }
    /**************************************供外部调用的方法***********************************************/
    /**
     * 请求的接口
     */
    public interface ResultCallback {
        void onError(Call call, Exception e);

        void onSuccess(byte[] response);
    }

    /**
     * post请求参封装
     */
    public static class RequestData {
        String key;
        String value;

        public RequestData() {
        }

        public RequestData(String key, String value) {
            this.value = value;
            this.key = key;
        }
    }
    public static Response getSync(String url) throws IOException {
        return getInstance()._getSync(url);
    }
    public static String getSyncString(String url) throws IOException {
        return getInstance()._getSyncString(url);
    }
    public static void getAsync(String url,ResultCallback callback)
    {
         getInstance()._getAsync(url,callback);
    }
    public static Response postync(String url,RequestData[] params,RequestData[] headers) throws IOException {
       return getInstance()._postSync(url,params,headers);
    }
    public static String postyncString(String url,RequestData[] params,RequestData[] headers) throws IOException {
        return getInstance()._postSyncString(url,params,headers);
    }
    public static void postAsync(String url,final ResultCallback callback,final RequestData[] params,RequestData[] headers)
    {
        getInstance()._postAsync(url,callback,params,headers);
    }
    public static void postAsync(String url,final ResultCallback callback,Map<String,String> params,Map<String,String> headers)
    {
        getInstance()._postAsync(url,callback,params,headers);
    }
}
