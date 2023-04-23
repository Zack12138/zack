package org.zack.reptile;

import org.zack.utils.StringUtil;
import org.zack.utils.SysClipboardUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class M3u8Download extends Reptile{
    static String m3u8url = "http://img.rr.tv/ugc/20221110/o_64e9cc02481c4f4e94832f658099d8d9.html??=eJwdy8EOgjAQhOGnmaOk7W5p90hxOfAAJh4t1XgwgELi61tM5vAfvomefZBT5Naa+0TFiuFsOYvJMmUylktxDwcayvKdX8utgM4IPbRFMkgRykiC6N/QgKTomClQYDL+7wKiO0Q9xAixUA9JED2iU6S2sue+rxuogxvqtubTbHMzzbWv6zi6yw9pSik3";// m3u8链接
    static String fileName = "";// 保存文件名
    static String Dir = "D:\\zack\\Downloads\\Video\\m3u8\\2303-1\\";// 保存路径
    static String KEY = "";// 加密视频的密钥，位数必须为16的倍数
    static int N = 10;// 线程数10
    static int INDEX = 0;// 下标
    public static void main(String[] args) {
        String m3u8urls = SysClipboardUtil.getSysClipboardText();

        String[] m3u8urlArr = m3u8urls.split("\n");
        System.out.println(m3u8urlArr.length);
        M3u8Download m3u8Download = new M3u8Download();
        for(int j =0 ;j<m3u8urlArr.length;j++) {
            try {
                m3u8Download.download(m3u8urlArr[j] ,Dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void download(String url, String target) throws Exception {
        INDEX=0;
        m3u8url = url;
        if (StringUtil.isBlank(m3u8url))
            return;
        String fn = m3u8url.substring(0,m3u8url.lastIndexOf("/"));
        fileName = fn.substring(fn.lastIndexOf("/")+1);
        String headUrl = m3u8url.substring(0, m3u8url.lastIndexOf("/") + 1);// 链接的头部
        String sendGet = sendGet(m3u8url, StandardCharsets.UTF_8.name());// 下载index.m3u8
        String[] split = sendGet.split("\n");
        String keyUrl = "";
        List<String> urls = new ArrayList<String>();
        // 获取ts链接和加密视频的key
        for (String s : split) {
            if (s.contains("EXT-X-KEY")) {
                keyUrl = s.substring(s.indexOf("URI=") + 5).replace("\"", "");
                if(!keyUrl.startsWith("http")){
                    int i = m3u8url.indexOf("com/");
                    keyUrl = m3u8url.substring(0,i+3)+url;
                }
                KEY = sendGet(keyUrl, StandardCharsets.UTF_8.name());
                System.out.println(KEY);// 加密视频的key
            } else if (s.startsWith("http")) {
                if (s.startsWith("http")) {
                    urls.add(s);
                } else {
                    urls.add(headUrl + s);
                }
            }
        }
        File f = new File(Dir + "/test.ts");
        while (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        // 开启多线程下载
        //		CountDownLatch countDownLatch = new CountDownLatch(N);// 实例化一个倒计数器，N指定计数个数
        //		countDownLatch.countDown(); // 计数减一
        //		countDownLatch.await();// 等待，当计数减到0时，所有线程并行执行
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(N);
        for (int i = 0; i < urls.size(); i++) {
            fixedThreadPool.execute(() -> {
                try {
                    int index = getIndex();
                    String ts = sendGet(urls.get(index), StandardCharsets.ISO_8859_1.name());
                    byte[] tbyte = ts.getBytes(StandardCharsets.ISO_8859_1.name());
                    if (!"".equals(KEY)) {
                        tbyte = decryptCBC(tbyte, KEY, KEY);
                    }
                    saveFile(tbyte, "000" + (index + 1) + ".ts");
                    System.out.println("下载000" + (index + 1) + ".ts结束：" + urls.get(index));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            });
        }
        fixedThreadPool.shutdown();
        // 等待子线程结束，再继续执行下面的代码
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("所有ts下载结束，总共：" + urls.size() + "个文件！");

        // 合并ts文件
        mergeTs(urls);
        System.out.println("合并完成：" + Dir + fileName + ".mp4");
        // 删除ts文件
//            deleteTs(urls);
    }

    /**
     * 获取其中一个ts文件
     */
    private synchronized static int getIndex() {
        return INDEX++;
    }

    /**
     * 删除ts文件
     */
    private static void deleteTs(List<String> urls) {
        for (int i = 0; i < urls.size(); i++) {
            new File(Dir + "000" + (i + 1) + ".ts").deleteOnExit();
        }
    }

    /**
     * 合并ts文件
     *
     */
    private static void mergeTs(List<String> urls) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            if ("".equals(fileName)) {
                fileName = "1" + new Random().nextInt(10000);
            }
            File file = new File(Dir + fileName + ".mp4");
            fos = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            int len;
            File f;
            for (int i = 0; i < urls.size(); i++) {
                f = new File(Dir + "000" + (i + 1) + ".ts");
                fis = new FileInputStream(f);
                while ((len = fis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fis.close();
                f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * AES CBC 解密
     * @param key   sSrc ts文件字节数组
     * @param iv    IV，需要和key长度相同
     * @return  解密后数据
     */
    public static byte[] decryptCBC(byte[] src, String key, String iv) {
        try {
            byte[] keyByte = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
            byte[] ivByte = iv.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivSpec = new IvParameterSpec(ivByte);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] content = cipher.doFinal(src);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存ts文件
     */
    private static void saveFile(byte[] ts, String name) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Dir + name);
            fos.write(ts);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 下载文件
     */
    public static String sendGet(String url, String charset) {
        HttpURLConnection con = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            // 打开连接
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 客户端告诉服务器实际发送的数据类型
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
            // 开启连接
            con.connect();
            is = con.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
                baos.flush();
            }
            return baos.toString(charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
