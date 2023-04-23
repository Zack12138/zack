package org.zack.utils.pushDeer;

import lombok.Data;
import org.zack.constant.RequestMethod;
import org.zack.utils.HttpsUtil;
import org.zack.utils.PropertiesUtil;
import org.zack.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
@Data
public class PushDeer {

    /**
     * 配置文件保存路径
     * 项目根目录起的相对路径
     */
    private static final String path = "config/PushDeer.properties";
    private static final PropertiesUtil prop = new PropertiesUtil(path);
    private static String baseURL ;
    private static String URL_LOGIN ;

    private static String basePUSH;

    private static String instance;

    private static final RequestMethod METHOD = RequestMethod.POST;

    private static final Map<String,PushDeer> instanceMap = new HashMap<>();

    private String pushKey;
    private String url;
    private String push;
    private String idToken;

    private PushDeer(){
    }

    static {
        loadProperties();
        initInstance();
    }

    private static void loadProperties() {
        baseURL = prop.get("pushDeer.base.URL");
        basePUSH = prop.get("pushDeer.base.push");
        instance = prop.get("pushDeer.instance");
    }

    private static void initInstance(){
        if(StringUtil.isBlank(instance))
            return;
        PushDeer pushDeer;
        String[] instances = instance.split(",");
        for (String inst: instances) {
            System.out.println("【PushDeer】正在创建实例"+inst);
            String pushKey = prop.get("pushDeer."+inst+".pushKey");
            if(StringUtil.isBlank(pushKey))
                // 没有pushKey按无效处理
                continue;
            String url = prop.get("pushDeer."+inst+".URL");
            if(StringUtil.isBlank(url))
                url = baseURL;
            String push = prop.get("pushDeer."+inst+".push");
            if(StringUtil.isBlank(push))
                push = basePUSH;

            pushDeer = new PushDeer();
            pushDeer.setUrl(url);
            pushDeer.setPushKey(pushKey);
            pushDeer.setPush(push);
            instanceMap.put(inst,pushDeer);
            System.out.println("【PushDeer】实例创建完成"+inst);
        }
    }

    public static PushDeer getInstance(String instance){
        return instanceMap.get(instance);
    }

    public static String push(String instance,String text,String desp,String type){
        if("ALL".equals(instance))
            return pushAll(text, desp, type);
        PushDeer pushDeer = instanceMap.get(instance);
        return pushDeer.push(text, desp, type);
    }

    public static String pushAll(String text,String desp,String type){
        StringBuilder sb = new StringBuilder();
        for (PushDeer pd: instanceMap.values()) {
            sb.append(",").append(pd.push(text, desp, type));
        }
        return sb.substring(1);
    }

    public String push(String text,String desp,String type){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pushkey",pushKey);
        paramsMap.put("text",text);
        paramsMap.put("type",type);
        paramsMap.put("desp",desp);
        byte[] bytes = HttpsUtil.httpsConnection(url + push, paramsMap, METHOD);
        return new String(bytes);
    }

    public static void main(String[] args) {
        PushDeer pd = getInstance(args[0]);
        System.out.println(args[0]);
        System.out.println(pd.push(args[1],args[2],"text"));

    }
}
