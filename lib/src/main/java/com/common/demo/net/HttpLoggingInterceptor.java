package com.common.demo.net;

import android.util.Log;
import com.common.demo.CommonAppUtils;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 一个记录请求和响应信息的OkHttp拦截器. 可以作为一个应用
 * {@linkplain OkHttpClient#interceptors() application interceptor} 或作为 {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}
 */
public final class HttpLoggingInterceptor implements Interceptor {

  private static final Charset UTF8 = Charset.forName("UTF-8");

  public enum Level {
    /**
     * No logs.
     */
    NONE,
    /**
     * Logs request and response lines.
     */
    BASIC,
    /**
     * Logs request and response lines and their respective headers.
     */
    HEADERS,
    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     */
    BODY
  }

  public interface Logger {

    void log(String message);

    /**
     * A {@link Logger} 默认Log适合当前平台.
     */
    Logger DEFAULT = message -> {
      if (!CommonAppUtils.isDebug()) {
        return;
      }
      Log.e("HttpLoggingInterceptor", "\n" + message);
    };
  }

  public HttpLoggingInterceptor() {
    this(Logger.DEFAULT);
  }

  public HttpLoggingInterceptor(Logger logger) {
    this.logger = logger;
  }

  private final Logger logger;

  private volatile Level level = Level.BASIC;

  /**
   * Change the level at which this interceptor logs.
   */
  public HttpLoggingInterceptor setLevel(Level level) {
    if (level == null) {
      throw new NullPointerException("level == null. Use Level.NONE instead.");
    }
    this.level = level;
    return this;
  }

  public Level getLevel() {
    return level;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Level level = this.level;

    Request request = chain.request();
    if (level == Level.NONE) {
      return chain.proceed(request);
    }

    boolean logBody = level == Level.BODY;
    boolean logHeaders = logBody || level == Level.HEADERS;

    RequestBody requestBody = request.body();
    boolean hasRequestBody = requestBody != null;

    Connection connection = chain.connection();
    String requestStartMessage = "--> "
        + request.method()
        + ' ' + request.url()
        + (connection != null ? " " + connection.protocol() : "");
    if (!logHeaders && hasRequestBody) {
      requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
    }
    logger.log(requestStartMessage);

    if (logHeaders) {
      if (hasRequestBody) {
        // Request body headers are only present when installed as a network interceptor. Force
        // them to be included (when available) so there values are known.
        if (requestBody.contentType() != null) {
          logger.log("Content-Type: " + requestBody.contentType());
        }
        if (requestBody.contentLength() != -1) {
          logger.log("Content-Length: " + requestBody.contentLength());
        }
      }

      Headers headers = request.headers();
      for (int i = 0, count = headers.size(); i < count; i++) {
        String name = headers.name(i);
        // Skip headers from the request body as they are explicitly logged above.
        if (!"Content-Type".equalsIgnoreCase(name) &&
            !"Content-Length".equalsIgnoreCase(name)) {
          logger.log(name + ": " + headers.value(i));
        }
      }

      if (!logBody || !hasRequestBody) {
        logger.log("--> END " + request.method());
      } else if (bodyEncoded(request.headers())) {
        logger.log("--> END " + request.method() + " (encoded body omitted)");
      } else {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        logger.log("");
        if (isPlaintext(buffer)) {
          logger.log(buffer.readString(charset));
          logger.log("--> END " + request.method()
              + " (" + requestBody.contentLength() + "-byte body)");
        } else {
          logger.log("--> END " + request.method() + " (binary "
              + requestBody.contentLength() + "-byte body omitted)");
        }
      }
    }

    long startNs = System.nanoTime();
    Response response;
    try {
      response = chain.proceed(request);
    } catch (Exception e) {
      logger.log("<-- HTTP FAILED: " + e);
      throw e;
    }
    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

    ResponseBody responseBody = response.body();
    long contentLength = responseBody.contentLength();
    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
    logger.log("<-- "
        + response.code()
        + (response.message().isEmpty() ? "" : ' ' + response.message())
        + ' ' + response.request().url()
        + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

    if (logHeaders) {
      Headers headers = response.headers();
      for (int i = 0, count = headers.size(); i < count; i++) {
        logger.log(headers.name(i) + ": " + headers.value(i));
      }

      if (!logBody || !HttpHeaders.hasBody(response)) {
        logger.log("<-- END HTTP");
      } else if (bodyEncoded(response.headers())) {
        logger.log("<-- END HTTP (encoded body omitted)");
      } else {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        if (!isPlaintext(buffer)) {
          logger.log("");
          logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
          return response;
        }

        if (contentLength != 0) {
          logger.log("");
          logger.log(buffer.clone().readString(charset));
        }

        logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
      }
    }

    return response;
  }

  /**
   * 如果有问题的正文可能包含人类可读的文本，则返回true.
   * 使用小的代码点样本来检测二进制文件签名中通常使用的unicode控制字符。
   */
  static boolean isPlaintext(Buffer buffer) {
    try {
      Buffer prefix = new Buffer();
      long byteCount = buffer.size() < 64 ? buffer.size() : 64;
      buffer.copyTo(prefix, 0, byteCount);
      for (int i = 0; i < 16; i++) {
        if (prefix.exhausted()) {
          break;
        }
        int codePoint = prefix.readUtf8CodePoint();
        if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
          return false;
        }
      }
      return true;
    } catch (EOFException e) {
      return false; // Truncated UTF-8 sequence.
    }
  }

  private boolean bodyEncoded(Headers headers) {
    String contentEncoding = headers.get("Content-Encoding");
    return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
  }
}