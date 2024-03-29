package com.xbx123.freedom.pay.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.xbx123.freedom.R;
import com.xbx123.freedom.utils.tool.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by EricYuan on 2016/4/13.
 */
public class Alipay {
    private Activity context;
    private String notifyUrl = "http://notify.msp.hk/notify.htm";
    // 商户PID
    public static final String PARTNER = "2088021381515671";
    // 商户收款账号
    public static final String SELLER = "xbb@xbb.la";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALitmxIbx038zYto"
            + "0LSK/Q3qna1Qhim0BN1WmEUlq2h/YL2cDIc3DlxTl08lvOtI6tdRZpzQaWbV7e/B"
            + "EKD6b/uTnUQ4k82V/kOdpOc/DX/DL1zV1QuPZLfDY2wX0k1qF+IFI7qSmRVzjLJj"
            + "zmFrQ4q/91dMpk/OZ0DWZrEsv3slAgMBAAECgYAcBuzT0LdslIM1NxEFdVp2NDb9"
            + "yIyz44ghdzTguZjL4Rjzba1RD//z7xO1hUqogoZxav8hqVd1rd3QVwKJC7jWuU7l"
            + "OZxsuRGiPoiEsGvyq7mDAboMbY93zbsyD8qgXdHPMtL1MgVqG47SYgEQfFWOcUBf"
            + "6Ox1dzAC0e71OMQbgQJBANo9Ye01gzEplqwZ8CWSIxzK5aeXUs80ebrX43ide1td"
            + "n984P2tXsngOSSYw5OiNWasnWnqVtvnHIIXepuVQGXUCQQDYoai6nwqS/1Ena6/H"
            + "QVFvqqmmDf9jerIjB86OGRHg9Yjp/tYW+QFa6PjXzlOMsoRM5wUTOfmrdOJwgURN"
            + "RPTxAkEAh/Yn1P06n102hj+ekfmKMHzjOFaY+4fIsrOe/ly2JkScvhcvw3MeN5dG"
            + "0Sky4wJ0s6FPyAEPvmrlAyGkPkZ5pQJBALsM+zAI24yJwH0VUrXuBG8zIUEsnPQ8"
            + "oUv2FbhElVd1Kz9At4MmhrEEsLlGgoXeLrZoU82CJb6SMmOKenttq0ECQFeLmVE4"
            + "daUjyyh9QH3EgaQf/IWeYf6fttYkKfUy/UIXbs6JuqayP+CQ0tXCGYK4BHVuk62E"
            + "ryw1U8M9b4XLIIU=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4rZsSG8dN/M2LaNC0iv0N6p2t"
            + "UIYptATdVphFJatof2C9nAyHNw5cU5dPJbzrSOrXUWac0Glm1e3vwRCg+m/7k51E"
            + "OJPNlf5DnaTnPw1/wy9c1dULj2S3w2NsF9JNahfiBSO6kpkVc4yyY85ha0OKv/dX"
            + "TKZPzmdA1maxLL97JQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private AlipayCallBack callBack;

    private String phpPayInfo = "";

    public void setCallBack(AlipayCallBack callBack) {
        this.callBack = callBack;
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    Util.pLog("payResult:" + msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Util.showToast(context, context.getString(R.string.pay_success));
                        if (callBack != null)
                            callBack.onSuccess();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Util.showToast(context, context.getString(R.string.pay_now));
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Util.pLog("resultStatus:" + resultStatus + " resultInfo:" + resultInfo);
                            Util.showToast(context, context.getString(R.string.pay_fail));
                            if (callBack != null)
                                callBack.onFailed();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(context, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
                default:
                    break;
            }
        }
    };

    public Alipay(Activity context, String phpPayInfo) {
        super();
        this.context = context;
        this.phpPayInfo = phpPayInfo;
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        /*String orderInfo = getOrderInfo(payBean);
        *//**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         *//*
        String sign = sign(orderInfo);
        try {
            *//**
         * 仅需对sign 做URL编码
         *//*
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        *//**
         * 完整的符合支付宝参数规范的订单信息
         *//*
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();*/
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo, true);
                Util.pLog("strSign:" + phpPayInfo);
                String result = alipay.pay(phpPayInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(context);
        String version = payTask.getVersion();
        Util.showToast(context, version);
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {

    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public interface AlipayCallBack {
        void onSuccess();

        void onFailed();
    }
}
