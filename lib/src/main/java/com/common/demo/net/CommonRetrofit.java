package com.common.demo.net;

import com.common.demo.CommonAppUtils;
import com.common.demo.CommonConfig;
import com.common.demo.net.converter.BaseResponseConverter;
import com.common.demo.net.converter.CharSequenceConverter;
import com.common.demo.net.converter.DoubleConverter;
import com.common.demo.net.converter.FloatConverter;
import com.common.demo.net.converter.IntegerConverter;
import com.common.demo.net.converter.LongConverter;
import com.common.demo.utilcode.util.LogUtils;
import com.common.demo.utilcode.util.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by WhaleFull on 2018/11/29.
 */
public class CommonRetrofit {
  private final static int TIMEOUT = 60;

  private String BASE_URL;
  private String BASE_API;

  private static HttpClientHandler httpClientHandler;

  private final OkHttpClient okHttpClient;
  private final Retrofit retrofit;

  private static class SingleTonHolder {
    private static CommonRetrofit single = new CommonRetrofit();
  }

  public static Retrofit getInstance() {
    return SingleTonHolder.single.retrofit;
  }

  /**
   * 在 getInstance 之前调用才有效
   */
  public static void setHttpClientHandler(HttpClientHandler handler) {
    httpClientHandler = handler;
  }

  private CommonRetrofit() {
    //当RxJava走onError
    RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {

      }
    });
    CommonConfig config = CommonAppUtils.getsConfig();

    BASE_URL =
        config.scheme + config.host + (ObjectUtils.isEmpty(config.port) ? "" : ":" + config.port);
    BASE_API = BASE_URL + "/" + (ObjectUtils.isEmpty(config.api) ? "" : config.api + "/");
    LogUtils.e(BASE_API);

    TrustAllManager trustAllManager = new TrustAllManager();
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .sslSocketFactory(getSSLSocketFactory(trustAllManager), trustAllManager)
        .hostnameVerifier(new TrustAllHostnameVerifier())
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS);

    if (ObjectUtils.isNotEmpty(httpClientHandler)) {
      httpClientHandler.onBuild(builder);
      httpClientHandler = null;
    }

    if (CommonAppUtils.isDebug()){
      builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
    }
    okHttpClient = builder.build();

    Gson gson = new GsonBuilder()
        //.setDateFormat("yyyy-MM-dd HH:mm:ss")
        //.setDateFormat(DateFormatHelper.DATE_FORMAT_ALL)
        .registerTypeAdapter(BaseResponse.class, new BaseResponseConverter())
        .registerTypeAdapter(CharSequence.class, new CharSequenceConverter())
        .registerTypeAdapter(Double.class, new DoubleConverter())
        .registerTypeAdapter(double.class, new DoubleConverter())
        .registerTypeAdapter(Float.class, new FloatConverter())
        .registerTypeAdapter(float.class, new FloatConverter())
        .registerTypeAdapter(Long.class, new LongConverter())
        .registerTypeAdapter(long.class, new LongConverter())
        .registerTypeAdapter(int.class, new IntegerConverter())
        .registerTypeAdapter(Integer.class, new IntegerConverter())
        .create();

    retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_API)
        .build();

  }

  // 忽略 https 证书
  private static SSLSocketFactory getSSLSocketFactory(TrustAllManager trustAllManager) {
    SSLSocketFactory sSLSocketFactory = null;
    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, new TrustManager[] { trustAllManager },
          new SecureRandom());
      sSLSocketFactory = sc.getSocketFactory();
    } catch (Exception e) {
    }
    return sSLSocketFactory;
  }

  /**
   * 证书信任管理器类
   * 默认信任所有的证书
   */
  private static class TrustAllManager implements X509TrustManager {

    // 检查客户端证书
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    // 检查服务器端证书
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    // 返回受信任的X509证书数组
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }
  }

  private static class TrustAllHostnameVerifier implements HostnameVerifier {
    @Override public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  public interface HttpClientHandler {
    void onBuild(OkHttpClient.Builder builder);
  }
}
