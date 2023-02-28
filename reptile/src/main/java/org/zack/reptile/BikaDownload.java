package org.zack.reptile;

import org.zack.constant.RequestMethod;
import org.zack.utils.HttpsUtil;

import java.util.Date;
import java.util.HashMap;

public class BikaDownload extends Reptile{

    public static void main(String[] args) {
        String url = "https://api.manhuabika.com/comics/63e3c27cc0d70f4660709b9c/recommendation";


        byte[] bytes = HttpsUtil.httpsConnection(url, null, RequestMethod.POST, null);

        System.out.println(new String(bytes));

    }


    @Override
    public void download(String url, String target) throws Exception {

    }
}
