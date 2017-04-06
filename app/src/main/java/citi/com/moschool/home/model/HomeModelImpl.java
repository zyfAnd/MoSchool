package citi.com.moschool.home.model;

/**
 * Created by zhang on 2017/3/10.
 */

public class HomeModelImpl {
    public interface OnLoadHomeDetailListener
    {
        void onSuccess();
        void onFailure(String msg,Exception e);
    }
    public interface OnLoadHomeListListener
    {
        void onSuccess();
        void onFailure(String msg,Exception e);
    }
}
