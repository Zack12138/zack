package org.zack.reptile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zack.constant.ReptileException;
import org.zack.utils.*;
import org.zack.utils.threadPool.ThreadPool;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQNewDownload extends Reptile{

    public static void main(String[] args) {
        // http://service-pobdo00c-1259817455.gz.apigw.tencentcs.com/wz2.php?d=163757675390393&_wv=67&vid=17&&alert(1)&csn=tbafls5a
        String url = SysClipboardUtil.getSysClipboardText();

 //       String url = "http://service-pobdo00c-1259817455.gz.apigw.tencentcs.com/wz2.php?d=163757675390393&_wv=67&vid=17&&alert(1)&csn=tbafls5a";

        //https://manhuabika.com/pcomicview/?cid=63eba1f8b63512632687d85b
        //https://manhuabika.com/pchapter/?cid=63eba1f8b63512632687d85b&chapter=1&chapterPage=1&maxchapter=1


        //https://manhuabika.com/pcomicview/?cid=627fe215b48f103d9e552cba
        //https://manhuabika.com/pchapter/?cid=627fe215b48f103d9e552cba&chapter=1&chapterPage=1&maxchapter=24

        String target = "D:\\VOL\\1\\迅雷下载\\和谐物\\图片\\2303";

        try {
            new QQNewDownload().download(url,target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void download(String url, String target) throws Exception {


        String[] split = url.split("[?]");

        String param = split[1];
        String[] paramArr = param.split("&");
        for (String par: paramArr) {
            if(par.startsWith("d="))
                param = par.replaceAll("d=","");
        }
        url = split[0];
        url = url.substring(0,url.lastIndexOf("/"))+"/api.php?act=getContent";
        System.out.println(url);

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("code",param);
        paramMap.put("pwd","123");


        String res = HttpUtil.httpPostWithJSON(url, paramMap);


        JSONObject jsonObject = JSON.parseObject(res);

        String title = StringUtil.unicodeToString((String)jsonObject.get("title"));



        String content = (String)jsonObject.get("content");

        System.out.println(content);
        String pattern = "<img.*?src=\"(http.*?)\".*?>";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        ExecutorService instance = ThreadPool.getInstance();
        int index = 1;
        while(m.find()){
            String fileName = NumberUtil.getNumberString(index++, 3) + ".jpg";
            String savePath = target + "\\" + title + "\\" + fileName;
            System.out.println(savePath);
            String imgUrl = m.group(1);
            instance.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes;
                        if(imgUrl.startsWith("https"))
                            bytes = HttpsUtil.httpsConnection(imgUrl);
                        else
                            bytes = HttpUtil.httpGet(imgUrl,null);
                        FileUtil.saveByte(savePath, bytes);
                    } catch (ReptileException e) {
                        if (e.getCause() instanceof java.net.SocketTimeoutException) {
                            System.out.println("读取超时" + imgUrl);
                            for (int index = 0; index < timeoutTimes; index++) {
                                try {
                                    byte[] bytes = HttpsUtil.httpsConnection(imgUrl);
                                    FileUtil.saveByte(savePath, bytes);
                                    break;
                                } catch (Exception ex) {
                                    System.out.println("读取超时" + imgUrl);
                                }
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        instance.shutdown();
    }


}
