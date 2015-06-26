package com.afollestad.silk.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;
import ch.boye.httpclientandroidlib.conn.ClientConnectionManager;
import ch.boye.httpclientandroidlib.conn.scheme.PlainSocketFactory;
import ch.boye.httpclientandroidlib.conn.scheme.Scheme;
import ch.boye.httpclientandroidlib.conn.scheme.SchemeRegistry;
import ch.boye.httpclientandroidlib.conn.ssl.SSLSocketFactory;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.PoolingClientConnectionManager;
import com.afollestad.silk.Silk;
import com.afollestad.silk.utilities.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Aidan Follestad (afollestad)
 */
class SilkHttpBase {

    protected static final int ASYNC_THREAD_COUNT = (Runtime.getRuntime().availableProcessors() * 4);
    protected final List<SilkHttpHeader> mHeaders;
    private final ExecutorService mNetworkExecutorService = newConfiguredThreadPool();
    private final Context mContext;
    private final Handler mHandler;
    private HttpClient mClient;
    private SilkHttpClient.ResponseProcessor mProcessor;

    public SilkHttpBase(Context context, Handler handler) {
        if (handler == null) {
            if (Looper.myLooper() == null)
                throw new RuntimeException("Cannot initialize a SilkHttpClient from a non-UI thread without passing a Handler to SilkHttpClient(Context, Handler).");
            handler = new Handler();
        }
        mHeaders = new ArrayList<SilkHttpHeader>();
        mContext = context;
        mHandler = handler;
        init();
    }

    public SilkHttpBase(Context context) {
        this(context, null);
    }

    private static ExecutorService newConfiguredThreadPool() {
        int corePoolSize = 0;
        int maximumPoolSize = ASYNC_THREAD_COUNT;
        long keepAliveTime = 60L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    protected void log(String message) {
        Log.d("SilkHttpClient", message);
    }

    private void init() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        ClientConnectionManager cm = new PoolingClientConnectionManager(registry);
        mClient = new DefaultHttpClient(cm);
    }

    protected void reset() {
        mHeaders.clear();
    }

    protected Handler getHandler() {
        return mHandler;
    }

    protected void runOnPriorityThread(Runnable runnable) {
        mNetworkExecutorService.execute(runnable);
    }

    public final void setProcessor(SilkHttpClient.ResponseProcessor processor) {
        mProcessor = processor;
    }

    private void processError(SilkHttpException exception) throws Exception {
        if (mProcessor != null) mProcessor.onProcessError(exception);
        else throw exception;
    }

    protected SilkHttpResponse performRequest(final HttpRequestBase request) throws Exception {
        if (mClient == null)
            throw new IllegalStateException("The client has already been shutdown, you must re-initialize it.");
        else if (mContext != null) {
            if (!Silk.hasInternetPermission(mContext))
                throw new IllegalAccessError("Your app does not declare the android.permission.INTERNET permission in its manifest.");
            else if (!Silk.isOnline(mContext))
                throw new IllegalStateException("The device is currently offline.");
        }
        if (mHeaders.size() > 0) {
            for (SilkHttpHeader header : mHeaders)
                request.setHeader(header.getName(), header.getValue());
        }
        log("Making request to " + request.getURI().toString());
        HttpResponse response;
        byte[] content;
        try {
            response = mClient.execute(request);
            content = IOUtils.inputStreamToBytes(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            reset();
            throw new SilkHttpException(e);
        } finally {
            request.releaseConnection();
        }
        int status = response.getStatusLine().getStatusCode();
        if (status != 200 && status != 201) {
            reset();
            String body;
            try {
                body = new String(content, "UTF-8");
            } catch (Exception e) {
                body = null;
            }
            processError(new SilkHttpException(response, body));
        }
        reset();
        return new SilkHttpResponse(response, content);
    }

    public final void shutdown() {
        reset();
        if (mClient != null) {
            mClient.getConnectionManager().shutdown();
            mClient = null;
            log("Client has been shutdown.");
        }
    }
}