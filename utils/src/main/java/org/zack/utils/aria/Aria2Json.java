package org.zack.utils.aria;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Package: com.dls.util.aria
 * @ClassName: Aria2Json
 * @Description: Aria2请求的Json对象
 * @date: 2021年6月21日 上午10:27:01
 * 
 * @author Zack
 * @version V2.0
 */
public class Aria2Json {
	/**
	 * 方法名常量
	 */
	public final static String METHOD_TELL_ACTIVE = "aria2.tellActive";
	public final static String METHOD_ADD_URI = "aria2.addUri";
	public final static String METHOD_GET_GLOBAL_STAT = "aria2.getGlobalStat";
	public final static String METHOD_TELL_STOPPED = "aria2.tellStopped";
	public final static String METHOD_TELL_WAITING = "aria2.tellWaiting";
	public final static String METHOD_REMOVE_DOWNLOAD_RESULT = "aria2.removeDownloadResult";
	private final static String[] PARAM_ARRAY_OF_FILED = new String[] { "totalLength", "completedLength", "files",
			"status", "errorCode", "gid" };
	/**
	 * id随机生成，也可以手动设置
	 */
	private String id = UUID.randomUUID().toString();
	private String jsonrpc = "2.0";
	private String method = METHOD_TELL_ACTIVE;
	private String url;
	private List<Object> params = new ArrayList<>();
	// 暂存下载参数

	public Aria2Json(String id) {
		this.id = id;
	}

	public Aria2Json() {
	}

	/**
	 * 添加下载参数
	 * 
	 * @return
	 */
	public Aria2Json addParam(Object obj) {
		params.add(obj);
		return this;
	}

	public static String tellActive() {
		Aria2Json aria2Json = new Aria2Json();
		aria2Json.setMethod(METHOD_TELL_ACTIVE).addParam(PARAM_ARRAY_OF_FILED);
		return aria2Json.send(null);
	}

	public static String tellStopped() {
		Aria2Json aria2Json = new Aria2Json();
		aria2Json.setMethod(METHOD_TELL_STOPPED).addParam(-1).addParam(1000).addParam(PARAM_ARRAY_OF_FILED);
		return aria2Json.send(null);
	}

	public static String tellWaiting() {
		Aria2Json aria2Json = new Aria2Json();
		aria2Json.setMethod(METHOD_TELL_WAITING).addParam(0).addParam(1000).addParam(PARAM_ARRAY_OF_FILED);
		return aria2Json.send(null);
	}

	public static String removeDownloadResult(String gid) {
		Aria2Json aria2Json = new Aria2Json();
		aria2Json.setMethod(METHOD_REMOVE_DOWNLOAD_RESULT).addParam(gid);
		return aria2Json.send(null);
	}

	public String send(String jsonRpcUrl) {
		// rpcurl 默认为本地默认地址
		jsonRpcUrl = StringUtils.isEmpty(jsonRpcUrl) ? "http://localhost:6800/jsonrpc" : jsonRpcUrl;
		// 创建post请求对象
		HttpPost httpPost = new HttpPost(jsonRpcUrl);
		// 设置content type（正文类型） 为json格式
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		// 将 this 对象解析为 json字符串 并用UTF-8编码(重要)将其设置为 entity （正文）
		httpPost.setEntity(new StringEntity(JSONObject.toJSONString(this), StandardCharsets.UTF_8));
		// 发送请求并获取返回对象
		CloseableHttpResponse response;
		try {
			response = HttpClients.createDefault().execute(httpPost);
		} catch (HttpHostConnectException e) {
			// log.debug("Aria2 无法连接");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// 返回的状态码
		int statusCode = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		// 请求结果字符串
		String result = null;
		try {
			// 用UTF-8解码返回字符串
			result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			// 如果状态码为200表示请求成功，返回结果
			if (statusCode == HttpStatus.SC_OK) {
				EntityUtils.consume(entity);
				return result;
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		// 请求失败 打印状态码和提示信息 返回null
		System.out.println("statusCode = " + statusCode);
		System.out.println("result = " + result);
		return null;
	}

	public String getId() {
		return id;
	}

	public Aria2Json setId(String id) {
		this.id = id;
		return this;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public Aria2Json setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Aria2Json setUrl(String url) {
		this.url = url;
		return this;
	}

	public List<Object> getParams() {
		return params;
	}

	public Aria2Json setParams(List<Object> params) {
		this.params = params;
		return this;
	}

	public String getMethod() {
		return method;
	}

	private Aria2Json setMethod(String method) {
		this.method = method;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jsonrpc == null) ? 0 : jsonrpc.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aria2Json other = (Aria2Json) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jsonrpc == null) {
			if (other.jsonrpc != null)
				return false;
		} else if (!jsonrpc.equals(other.jsonrpc))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Aria2Json [id=%s, jsonrpc=%s, method=%s, url=%s, params=%s]", id, jsonrpc, method, url,
				params);
	}
	
	public static void main(String[] args) {
		
	}

}