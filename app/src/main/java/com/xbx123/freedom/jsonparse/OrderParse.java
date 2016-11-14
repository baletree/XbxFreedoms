package com.xbx123.freedom.jsonparse;

import com.xbx123.freedom.beans.ComItemTagBean;
import com.xbx123.freedom.beans.CommentTagBean;
import com.xbx123.freedom.beans.InsurerBean;
import com.xbx123.freedom.beans.OrderBean;
import com.xbx123.freedom.beans.OrderDetailBean;
import com.xbx123.freedom.pay.alipay.Alipay;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/6/6.
 */
public class OrderParse {
    public static List<OrderBean> getOrderList(String json) {
        if (Util.isNull(json))
            return null;
        List<OrderBean> orderList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                orderList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    OrderBean orderBean = new OrderBean();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (UtilParse.checkTag(jsonObject, "order_number"))
                        orderBean.setOrderNum(jsonObject.getString("order_number"));
                    if (UtilParse.checkTag(jsonObject, "pay_money"))
                        orderBean.setOrderPay(jsonObject.getString("pay_money"));
                    if (UtilParse.checkTag(jsonObject, "order_time"))
                        orderBean.setOrderTime(jsonObject.getString("order_time"));
                    if (UtilParse.checkTag(jsonObject, "end_addr"))
                        orderBean.setOrderAddress(jsonObject.getString("end_addr"));
                    if (UtilParse.checkTag(jsonObject, "guide_type"))
                        orderBean.setServerType(jsonObject.getInt("guide_type"));
                    if (UtilParse.checkTag(jsonObject, "order_status"))
                        orderBean.setOrderState(jsonObject.getInt("order_status"));
                    if (UtilParse.checkTag(jsonObject, "server_start_date"))
                        orderBean.setOrderStartTime(jsonObject.getString("server_start_date"));
                    orderList.add(orderBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    /**
     * 订单详情
     *
     * @param json
     * @return
     */
    public static OrderDetailBean getOrderDetail(String json) {
        if (Util.isNull(json))
            return null;
        OrderDetailBean orderDetailBean = new OrderDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "guid"))//该服务者的id
                orderDetailBean.setServerId(jsonObject.getString("guid"));
            if (UtilParse.checkTag(jsonObject, "order_number"))//订单号
                orderDetailBean.setOrderNum(jsonObject.getString("order_number"));
            if (UtilParse.checkTag(jsonObject, "realname"))//真实姓名
                orderDetailBean.setServerName(jsonObject.getString("realname"));
            if (UtilParse.checkTag(jsonObject, "phone"))//电话
                orderDetailBean.setServerPhone(jsonObject.getString("phone"));
            if (UtilParse.checkTag(jsonObject, "head_image"))//头像地址
                orderDetailBean.setServerHeadImg(jsonObject.getString("head_image"));
            if (UtilParse.checkTag(jsonObject, "stars"))//星星数量
                orderDetailBean.setServerStar(jsonObject.getString("stars"));
            if (UtilParse.checkTag(jsonObject, "idcard"))//身份证号码
                orderDetailBean.setServerIdCard(jsonObject.getString("idcard"));
            if (UtilParse.checkTag(jsonObject, "order_time"))//下单时间
                orderDetailBean.setOrderDownTime(jsonObject.getString("order_time"));
            if (UtilParse.checkTag(jsonObject, "cancel_time"))//取消订单时间
                orderDetailBean.setOrderCancelTime(jsonObject.getString("cancel_time"));
            if (UtilParse.checkTag(jsonObject, "order_money"))//最原始订单需要支付的费用
                orderDetailBean.setOrderActualPay(jsonObject.getDouble("order_money"));
            if (UtilParse.checkTag(jsonObject, "rebate_money"))//优惠金额
                orderDetailBean.setCouponMoney(jsonObject.getDouble("rebate_money"));
            if (UtilParse.checkTag(jsonObject, "tip_money"))//打赏费用
                orderDetailBean.setRewardMoney(jsonObject.getDouble("tip_money"));
            if (UtilParse.checkTag(jsonObject, "pay_money"))//实际支付的金额
                orderDetailBean.setOrderPay(jsonObject.getDouble("pay_money"));
            if (UtilParse.checkTag(jsonObject, "derate_money"))//减免金额
                orderDetailBean.setBreaksMoney(jsonObject.getDouble("derate_money"));
            if (UtilParse.checkTag(jsonObject, "pay_type"))//支付的方式
                orderDetailBean.setOrderPayType(jsonObject.getInt("pay_type"));
            if (UtilParse.checkTag(jsonObject, "server_start_time"))//订单开始时间
                orderDetailBean.setOrderStartTime(jsonObject.getString("server_start_time"));
            if (UtilParse.checkTag(jsonObject, "server_end_time"))//订单结束时间
                orderDetailBean.setOrderEndTime(jsonObject.getString("server_end_time"));
            if (UtilParse.checkTag(jsonObject, "server_time_long"))//服务时长
                orderDetailBean.setServiceDuration(jsonObject.getString("server_time_long"));
            if (UtilParse.checkTag(jsonObject, "guide_type"))//服务者的类型
                orderDetailBean.setServerType(jsonObject.getInt("guide_type"));
            if (UtilParse.checkTag(jsonObject, "server_price"))//服务者的计费单价
                orderDetailBean.setServerUnitPrice(jsonObject.getDouble("server_price"));
            if (UtilParse.checkTag(jsonObject, "end_addr"))//目的地位置
                orderDetailBean.setUserAddress(jsonObject.getString("end_addr"));
            if (UtilParse.checkTag(jsonObject, "content"))//评论内容
                orderDetailBean.setServerContent(jsonObject.getString("content"));
            if (UtilParse.checkTag(jsonObject, "star"))//评论的星星
                orderDetailBean.setCommentStar(jsonObject.getString("star"));
            if (UtilParse.checkTag(jsonObject, "order_status"))//订单状态
                orderDetailBean.setOrderState(jsonObject.getInt("order_status"));
            if (UtilParse.checkTag(jsonObject, "msg"))//
                orderDetailBean.setOrderCloseReason(jsonObject.getString("msg"));
            if (UtilParse.checkTag(jsonObject, "contactname"))//
                orderDetailBean.setContactsName(jsonObject.getString("contactname"));
            if (UtilParse.checkTag(jsonObject, "contactphone"))//
                orderDetailBean.setContactsPhone(jsonObject.getString("contactphone"));
            if (UtilParse.checkTag(jsonObject, "refund_money"))//
                orderDetailBean.setRefundMoney(jsonObject.getDouble("refund_money"));
            if (UtilParse.checkTag(jsonObject, "service_now_time"))//
                orderDetailBean.setServerNowTime(jsonObject.getLong("service_now_time"));
            if (UtilParse.checkTag(jsonObject, "memberInfo")) {
                List<InsurerBean> insurerList = null;
                JSONArray jsonArray = jsonObject.getJSONArray("memberInfo");
                if (jsonArray.length() > 0) {
                    insurerList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        InsurerBean insurerBean = new InsurerBean();
                        JSONObject job = (JSONObject) jsonArray.get(i);
                        if (UtilParse.checkTag(job, "name"))//
                            insurerBean.setInsurerName(job.getString("name"));
                        if (UtilParse.checkTag(job, "phone"))//
                            insurerBean.setInsurerPhone(job.getString("phone"));
                        if (UtilParse.checkTag(job, "idcard"))//
                            insurerBean.setInsurerIdcard(job.getString("idcard"));
                        insurerList.add(insurerBean);
                    }
                }
                orderDetailBean.setInsurerList(insurerList);
            }
            if (UtilParse.checkTag(jsonObject, "tag")) {
                JSONArray jsonArray = jsonObject.getJSONArray("tag");
                if (jsonArray.length() > 0) {
                    List<String> tagList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        tagList.add(jsonArray.get(i).toString());
                    }
                    orderDetailBean.setServerTagList(tagList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderDetailBean;
    }

    /**
     * 标签解析
     *
     * @param json
     * @return
     */
    public static CommentTagBean getCommentTag(String json) {
        CommentTagBean tagBean = null;
        if (Util.isNull(json))
            return tagBean;
        try {
            JSONObject jsonObject = new JSONObject(json);
            tagBean = new CommentTagBean();
            if (UtilParse.checkTag(jsonObject, "good")) {
                JSONArray jsonArray = jsonObject.getJSONArray("good");
                List<ComItemTagBean> goodComList = null;
                if (jsonArray.length() > 0) {
                    goodComList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json1 = (JSONObject) jsonArray.get(i);
                        ComItemTagBean itemTagBean = new ComItemTagBean();
                        if (UtilParse.checkTag(json1, "key"))
                            itemTagBean.setComTagId(json1.getString("key"));
                        if (UtilParse.checkTag(json1, "val"))
                            itemTagBean.setComTagName(json1.getString("val"));
                        goodComList.add(itemTagBean);
                    }
                    tagBean.setGoodComList(goodComList);
                }
            }
            if (UtilParse.checkTag(jsonObject, "bad")) {
                JSONArray jsonArray = jsonObject.getJSONArray("bad");
                List<ComItemTagBean> badComList = null;
                if (jsonArray.length() > 0) {
                    badComList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json1 = (JSONObject) jsonArray.get(i);
                        ComItemTagBean itemTagBean = new ComItemTagBean();
                        if (UtilParse.checkTag(json1, "key"))
                            itemTagBean.setComTagId(json1.getString("key"));
                        if (UtilParse.checkTag(json1, "val"))
                            itemTagBean.setComTagName(json1.getString("val"));
                        badComList.add(itemTagBean);
                    }
                    tagBean.setBadComList(badComList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tagBean;
    }

    public static List<Integer> getRewardMon(String json) {
        if (Util.isNull(json))
            return null;
        List<Integer> tagList = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "conf")) {
                JSONArray jsonArray = jsonObject.getJSONArray("conf");
                if (jsonArray.length() > 0) {
                    tagList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        tagList.add((Integer) jsonArray.get(i));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tagList;
    }

    /**
     * 解析被保人列表
     *
     * @param json
     * @return
     */
    public static List<InsurerBean> getInsurerList(String json) {
        List<InsurerBean> insurerList = null;
        if (Util.isNull(json))
            return insurerList;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                insurerList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    InsurerBean insurerBean = new InsurerBean();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (UtilParse.checkTag(jsonObject, "id"))
                        insurerBean.setInsurerId(jsonObject.getString("id"));
                    if (UtilParse.checkTag(jsonObject, "realname"))
                        insurerBean.setInsurerName(jsonObject.getString("realname"));
                    if (UtilParse.checkTag(jsonObject, "idcard"))
                        insurerBean.setInsurerIdcard(jsonObject.getString("idcard"));
                    if (UtilParse.checkTag(jsonObject, "phone"))
                        insurerBean.setInsurerPhone(jsonObject.getString("phone"));
                    insurerList.add(insurerBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return insurerList;
    }

    /**
     * 解析被保人详情
     *
     * @param json
     * @return
     */
    public static InsurerBean getInsurerInfo(String json) {
        InsurerBean insurerBean = null;
        if (Util.isNull(json))
            return insurerBean;
        insurerBean = new InsurerBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "id"))
                insurerBean.setInsurerId(jsonObject.getString("id"));
            if (UtilParse.checkTag(jsonObject, "realname"))
                insurerBean.setInsurerName(jsonObject.getString("realname"));
            if (UtilParse.checkTag(jsonObject, "idcard"))
                insurerBean.setInsurerIdcard(jsonObject.getString("idcard"));
            if (UtilParse.checkTag(jsonObject, "phone"))
                insurerBean.setInsurerPhone(jsonObject.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return insurerBean;
    }

    /**
     * 获取支付宝签名
     *
     * @param json
     * @return
     */
    public static String getSign(String json) {
        String strSign = "";
        if (Util.isNull(json))
            return strSign;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "sign"))
                strSign = jsonObject.getString("sign");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strSign;
    }

    /**
     * 修改打赏或者优惠券后返回信息
     *
     * @param orderBean
     * @param json
     * @return
     */
    public static OrderDetailBean getUpdatePayInfo(OrderDetailBean orderBean, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "tip_money"))
                orderBean.setRewardMoney(jsonObject.getDouble("tip_money"));
            if (UtilParse.checkTag(jsonObject, "pay_money"))
                orderBean.setOrderPay(jsonObject.getDouble("pay_money"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderBean;
    }
}
