package com.xbx123.freedom.http;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.jsonparse.UtilParse;
import com.xbx123.freedom.linsener.RequestBackLisener;
import com.xbx123.freedom.linsener.RequestNoErrorBackLisener;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.constant.HttpRequestFlag;
import com.xbx123.freedom.utils.constant.UrlConstant;
import com.xbx123.freedom.utils.tool.Util;

import java.io.File;
import java.util.Map;

/**
 * Created by EricYuan on 2016/5/23.
 */
public class Api extends ApiUtil {

    public Api(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }

    /**
     * 版本更新
     */
    public void checkVersion() {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlCheckVersion);
        IRequest.get(context, getUrl, new RequestNoErrorBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("版本更新：" + json);
                sendEmptyMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 登录页面获取验证码
     *
     * @param phone
     */
    public void getCode(String phone) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlGetCode);
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("获取验证码：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 登录
     *
     * @param phone
     * @param code
     * @param pushId
     */
    public void toLogin(String phone, String code, String pushId) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlToLogin);
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        params.put("password", code);
        params.put("push_id", pushId);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("登录：" + json);
                sendShowMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 首页根据loginToken获取是否有进行中订单或未处理订单
     *
     * @param phone
     * @param token
     * @param pushId
     */
    public void checkHasNoHandlerOrder(String phone, String token, final String pushId) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlToLogin);
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        params.put("password", token);
        params.put("push_id", pushId);
        IRequest.post(context, postUrl, params, new RequestNoErrorBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("登录检查：" + json + "\npushId=" + pushId);
                sendCompleteData(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 周围的服务者获取
     *
     * @param serverLocate
     * @param serverType
     */
    public void getNearServer(String serverLocate, String serverType) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlGetServer).concat("?server_addr_lnglat=" + serverLocate)
                .concat("&guide_type=" + serverType);
        Util.pLog("nearUrl:" + getUrl);
        IRequest.get(context, getUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("周围服务者：" + json);
                sendCompleteData(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 用户创建订单
     *
     * @param uid
     * @param userLocate
     * @param guideType
     * @param realName
     * @param insurerId
     * @param resortsId
     * @param phone
     * @param chargingType
     */
    public void createOrder(String uid, String userLocate, int guideType, String realName, String insurerId, String resortsId,
                            String phone, String chargingType) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlCreateOrder);
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("server_addr_lnglat", userLocate);
        params.put("guide_type", guideType + "");
        params.put("realname", realName);//讲解员传称呼，领路人传真实姓名
        params.put("phone", phone);
        if (guideType == Constant.NarratorType) {
            params.put("tourist_id", resortsId);
            params.put("charging_type", chargingType);//讲解员传服务付费方式
        }
        if (guideType == Constant.NativeType) {
            params.put("linkman_id", insurerId);
            params.put("charging_type", chargingType);//领路人服务付费方式
        }
        Util.pLog("下单Url:" + UrlConstant.urlIp.concat(UrlConstant.urlCreateOrder));
        params.inputParams();
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("创建订单：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 取消呼叫服务者
     *
     * @param orderNum
     */
    public void cancelCallServer(String orderNum) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlCancelCall);
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        IRequest.post(context, postUrl, params, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("取消呼叫服务者：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 用户取消订单
     *
     * @param orderNum
     * @param action
     */
    public void cancelOrder(String orderNum, String action) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlCancelOrder);
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        if (!Util.isNull(action))
            params.put("action", action);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("取消订单是否支付：" + json);
                sendCompleteData(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 用户取消订单、导游订单
     *
     * @param orderNum
     * @param action
     */
    public void cancelOrder(String orderNum, String action, String reasonMsg) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlCancelOrder);
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        if (!Util.isNull(action))
            params.put("action", action);
        if (!Util.isNull(action))
            params.put("msg", reasonMsg);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("取消订单支付：" + json);
                sendCompleteData(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 根据订单好获取我的服务者
     *
     * @param orderNum
     */
    public void findMyServer(String orderNum) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlGetMyServer).concat("?order_number=" + orderNum);
        IRequest.get(context, getUrl, new RequestNoErrorBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("寻找服务者：" + json);
                sendCompleteData(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 根据订单号获取我的服务者
     *
     * @param orderNum
     */
    public void getMyServerInfo(String orderNum) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlGetMyServerInfo).concat("?order_number=" + orderNum);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("我的服务者信息：" + json);
                sendShowMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 上传自己经纬度并获取服务者的经纬度
     *
     * @param lat
     * @param log
     * @param orderNum
     */
    public void getServerLatLng(String uid, String lat, String log, String orderNum) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlGetMyServerLatLng);
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("lng", log);
        params.put("lat", lat);
        params.put("is_getlnglat", "1");
        params.put("order_number", orderNum);
        IRequest.post(context, postUrl, params, new RequestNoErrorBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
//                Util.pLog("获取服务者经纬度：" + json);
                sendEmptyMsgDeploy(HttpRequestFlag.requestPageFour, json);
            }
        });
    }

    /**
     * 用户订单列表
     *
     * @param uid
     * @param pageIndex
     * @param pageNum
     */
    public void getOrderList(String uid, String pageIndex, String pageNum) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlOrderList).concat("?uid=" + uid + "&now_page=" + pageIndex + "&page_number=" + pageNum);
        Util.pLog("getUrl: " + getUrl);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("订单列表：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 订单详情
     *
     * @param orderNum
     */
    public void getOrderDetail(final String orderNum) {
        final String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlOrderDetail).concat("?order_number=" + orderNum);
        Util.pLog("getUrl: " + getUrl);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("订单详情：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 模拟支付
     *
     * @param orderNum
     */
    public void simulationPay(String orderNum) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlSimulationPay);
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("模拟支付：" + json);
                sendShowMsg(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 正常支付服务订单
     *
     * @param orderNum
     */
    public void updatePay(String orderNum, String rewardId, String couponId) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlPay);
//        String postUrl = UrlConstant.urlGetSign;
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        params.put("tip_id", rewardId);
        params.put("coupon_id", couponId);
//        params.inputParams();
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("支付修改(打赏/优惠)：" + json);
                sendShowMsg(HttpRequestFlag.requestPageFour, json);
            }
        });
    }

    /**
     * 订单支付获取sign
     *
     * @param orderNum
     */
    public void toPaySign(String orderNum, String payWay) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlGetSign);
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        params.put("pay_channel", payWay);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("支付获取sign：" + json);
                sendShowMsg(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 打赏的金额
     */
    public void getRewardMoney() {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlRewardMon);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("打赏金额：" + json);
                sendShowMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 获取评论的标签
     */
    public void getCommentTag() {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlTagList);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("评论标签：" + json);
                sendShowMsg(HttpRequestFlag.requestPageFive, json);
            }
        });
    }

    /**
     * 提交评论
     *
     * @param orderNum
     * @param starScore
     * @param comment
     * @param commentTag
     */
    public void submitComment(String orderNum, String starScore, String comment, String commentTag) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlSubmitComment);
        RequestParams params = new RequestParams();
        params.put("order_number", orderNum);
        params.put("content", comment);
        params.put("star", starScore);
        params.put("tag", commentTag);
        params.inputParams();
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("提交评论：" + json);
                sendShowMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 修改个人信息
     *
     * @param userInfo
     * @param headFile
     */
    public void modifyUserInfo(UserInfo userInfo, File headFile) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlModifyInfo);
        RequestParams params = new RequestParams();
        params.put("uid", userInfo.getUid());
        params.put("realname", userInfo.getUserRealName());
        params.put("idcard", userInfo.getUserIdCard());
        params.put("phone", userInfo.getUserPhone());
        params.put("nickname", userInfo.getNickName());
        params.put("sex", userInfo.getUserSex() + "");
        params.put("birthday", userInfo.getUserBirthday());
        params.put("email", userInfo.getUserEmail());
        if (headFile != null)
            params.put("head_image", headFile);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("修改个人信息：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 意见反馈
     *
     * @param uid
     * @param content
     */
    public void submitFeedBack(String uid, String content) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlFeedBack);
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("content", content);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("意见反馈：" + json);
                sendCompleteData(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 获取服务者主页
     *
     * @param serverId
     */
    public void getServerPageInfo(String serverId) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlServerPage).concat("?uid=" + serverId);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("导游主页：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 评论列表
     *
     * @param serverId
     * @param pageIndex
     * @param pageNum
     */
    public void getServerCommentList(String serverId, String pageIndex, String pageNum) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlCommentList).concat("?uid=" + serverId + "&now_page=" + pageIndex + "&page_number=" + pageNum);
        IRequest.get(context, getUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("评论列表：" + json);
                sendShowMsg(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 增加被保人
     *
     * @param uid
     * @param insurerName
     * @param insurerIdCard
     * @param insurerPhone
     */
    public void addInsurer(String uid, String insurerName, String insurerIdCard, String insurerPhone) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlAddInsurer);
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("realname", insurerName);
        params.put("idcard", insurerIdCard);
        params.put("phone", insurerPhone);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("增加被保人：" + json);
                sendCompleteData(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 被保人列表
     *
     * @param uid
     */
    public void getInsurerList(String uid) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlListInsurer).concat("?uid=" + uid);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("被保险人列表：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 被保人详情
     *
     * @param insurerId
     */
    public void getInsurerInfo(String insurerId) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlInsurerInfo).concat("?id=" + insurerId);
        IRequest.get(context, getUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("被保人详情：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 删除被保人
     *
     * @param insurerId
     */
    public void deleteInsurer(String insurerId) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlInsurerDelete);
        RequestParams params = new RequestParams();
        params.put("id", insurerId);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("删除被保人：" + json);
                sendShowMsg(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 修改被保人
     *
     * @param insurerBean
     */
    public void editInsurer(InsurerBean insurerBean) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlInsurerEdit);
        RequestParams params = new RequestParams();
        params.put("id", insurerBean.getInsurerId());
        params.put("realname", insurerBean.getInsurerName());
        params.put("idcard", insurerBean.getInsurerIdcard());
        params.put("phone", insurerBean.getInsurerPhone());
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("修改被保人：" + json);
                sendShowMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 版本更新检查
     */
    public void checkUpdateVersion() {
        String url = UrlConstant.urlIp.concat(UrlConstant.urlCheckVersion);
        IRequest.get(context, url, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("版本更新:" + json);
                sendEmptyMsg(HttpRequestFlag.requestPageThree, json);
            }
        });
    }

    /**
     * 导游列表筛选
     *
     * @param conditionMap
     */
    public void getConditionGuideList(Map<String, String> conditionMap, String sortCondition, String pageIndex) {
        String params = "?bgndate=" + conditionMap.get("TripDate") + "&days=" + conditionMap.get("TripDateNum") + sortCondition;
        if (!Util.isNull(conditionMap.get("TripStarLev")))
            params = params + "&star=" + conditionMap.get("TripStarLev");
        if (!Util.isNull(conditionMap.get("TripSex")))
            params = params + "&sex=" + conditionMap.get("TripSex");
        if (!Util.isNull(conditionMap.get("TripServiceFee"))) {
            String[] priceBetween = conditionMap.get("TripServiceFee").split("-");
            if (!Util.isNull(priceBetween[0]) && !Util.isNull(priceBetween[1])) {
                int price = (Integer.getInteger(priceBetween[0]) + Integer.getInteger(priceBetween[1])) / 2;
                params = params + "&fee=" + price;
            }
        }
        params = params + "&currentpage=" + pageIndex;
        String url = UrlConstant.urlIp + UrlConstant.urlGuideList + params;
        Util.pLog("GuideListUrl:" + url);
        IRequest.get(context, url, "", new RequestBackLisener(context, mHandler) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("导游列表:" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 导游主页数据获取
     *
     * @param guideCardNum
     */
    public void getGuideHomePage(String guideCardNum) {
        String url = UrlConstant.urlIp + UrlConstant.urlGuideHomePage + "?guidecode=" + guideCardNum;
        Util.pLog("GuideHomePage:" + url);
        IRequest.get(context, url, "", new RequestBackLisener(context, mHandler) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("导游主页:" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 导游下单
     *
     * @param uid
     * @param guideCode
     * @param tripDate
     * @param tripDays
     * @param tripPNum
     * @param traverId
     * @param contactsName
     * @param contactsPhone
     * @param memo
     */
    public void orderToGuide(String uid, String guideCode, String tripDate, String tripDays, String tripPNum, String traverId,
                             String contactsName, String contactsPhone, String memo) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlGuideOrdered);
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("guidecode", guideCode);
        params.put("bgndate", tripDate);
        params.put("days", tripDays);
        params.put("numbers", tripPNum);
        params.put("people_id", traverId);
        params.put("name", contactsName);
        params.put("phone", contactsPhone);
        params.put("memo", memo);
        Util.pLog("下单Url " + postUrl);
        Util.pLog("uid = " + uid + " guidecode = " + guideCode + " bgndate = " + tripDate + " days = " + tripDays + " numbers = " + tripPNum + " people_id = " + traverId + " name = " + contactsName + " phone = " + contactsPhone);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("导游下单：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 添加紧急联系人
     *
     * @param uid
     * @param name
     * @param phone
     */
    public void addFrequenContact(String uid, String name, String phone) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlFrequentContactsAdd);
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("realname", name);
        params.put("phone", phone);
        params.put("idcard", phone);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("添加紧急联系人：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 修改紧急联系人
     *
     * @param cid
     * @param name
     * @param phone
     */
    public void editFrequenContact(String cid, String name, String phone) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlFrequentContactsEdit);
        RequestParams params = new RequestParams();
        params.put("id", cid);
        params.put("realname", name);
        params.put("phone", phone);
        params.put("idcard", "");
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("修改紧急联系人：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 删除紧急联系人
     *
     * @param cid
     */
    public void deleteFrequenContact(String cid) {
        String postUrl = UrlConstant.urlIp.concat(UrlConstant.urlFrequentContactsDelete);
        RequestParams params = new RequestParams();
        params.put("id", cid);
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("修改紧急联系人：" + json);
                sendShowMsg(HttpRequestFlag.requestPageTwo, json);
            }
        });
    }

    /**
     * 紧急联系人列表
     *
     * @param uid
     */
    public void listFrequenContact(String uid) {
        String getUrl = UrlConstant.urlIp.concat(UrlConstant.urlFrequentContactsList) + "?uid=" + uid;
        IRequest.get(context, getUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("紧急联系人：" + json);
                sendShowMsg(HttpRequestFlag.requestPageOne, json);
            }
        });
    }

    /**
     * 获取微信支付签名
     */
    public void getWxSign() {
        String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
        IRequest.get(context, url, new RequestNoErrorBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("微信sign:" + json);
                sendCompleteData(HttpRequestFlag.requestPageFour, json);
            }
        });
    }

    /**
     * 获取开通景区的列表
     */
    public void getResortsList() {
        String url = UrlConstant.urlIp.concat(UrlConstant.urlResorts);
        IRequest.get(context, url, "", new RequestNoErrorBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("景区列表:" + json);
                sendShowMsg(HttpRequestFlag.requestPageFive, json);
            }
        });
    }

    /**
     * 获取微信预支付信息
     */
    public void getWXSign() {
        String postUrl = "http://192.168.1.27/tutu_guide/Api/User/test.json";
        RequestParams params = new RequestParams();
        IRequest.post(context, postUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                sendShowMsg(HttpRequestFlag.requestPageFour, json);
            }
        });
    }
}
