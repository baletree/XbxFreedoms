package com.xbx123.freedom.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/18.
 * 订单详情
 */
public class OrderDetailBean implements Serializable {
    private String serverId;
    private String serverName;
    private String serverPhone;
    private String serverHeadImg;
    private String serverStar;
    private String serverIdCard;
    private String orderNum;//订单编号
    private String orderDownTime;//下单时间
    private String orderCancelTime;
    private double orderActualPay;//最原始支付
    private double rewardMoney;//打赏
    private double couponMoney;//优惠价格
    private double orderPay;//实际支付
    private double breaksMoney;//减免金额
    private double refundMoney;//退款的取消费
    private int orderPayType;
    private String orderStartTime;
    private String orderEndTime;
    private double serverUnitPrice;//服务者的单价
    private int serverType;//服务者的类型
    private int chargingWay;//计费方式
    private String userAddress;
    private String serverContent;
    private String commentStar;
    private List<String> serverTagList;
    private String serviceDuration;//服务时长
    private int orderState; //订单状态
    private String orderCloseReason;//订单关闭原因
    private long serverNowTime;
    /*导游的多余信息*/
    private String contactsName;//紧急联系人姓名
    private String contactsPhone;//紧急联系人电话
    private List<InsurerBean> insurerList;//出行人列表

    public long getServerNowTime() {
        return serverNowTime;
    }

    public void setServerNowTime(long serverNowTime) {
        this.serverNowTime = serverNowTime;
    }

    public double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public List<InsurerBean> getInsurerList() {
        return insurerList;
    }

    public void setInsurerList(List<InsurerBean> insurerList) {
        this.insurerList = insurerList;
    }

    public double getBreaksMoney() {
        return breaksMoney;
    }

    public void setBreaksMoney(double breaksMoney) {
        this.breaksMoney = breaksMoney;
    }

    public String getOrderCloseReason() {
        return orderCloseReason;
    }

    public void setOrderCloseReason(String orderCloseReason) {
        this.orderCloseReason = orderCloseReason;
    }

    public String getServerIdCard() {
        return serverIdCard;
    }

    public void setServerIdCard(String serverIdCard) {
        this.serverIdCard = serverIdCard;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPhone() {
        return serverPhone;
    }

    public void setServerPhone(String serverPhone) {
        this.serverPhone = serverPhone;
    }

    public String getServerHeadImg() {
        return serverHeadImg;
    }

    public void setServerHeadImg(String serverHeadImg) {
        this.serverHeadImg = serverHeadImg;
    }

    public String getServerStar() {
        return serverStar;
    }

    public void setServerStar(String serverStar) {
        this.serverStar = serverStar;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderDownTime() {
        return orderDownTime;
    }

    public void setOrderDownTime(String orderDownTime) {
        this.orderDownTime = orderDownTime;
    }

    public String getOrderCancelTime() {
        return orderCancelTime;
    }

    public void setOrderCancelTime(String orderCancelTime) {
        this.orderCancelTime = orderCancelTime;
    }

    public double getOrderActualPay() {
        return orderActualPay;
    }

    public void setOrderActualPay(double orderActualPay) {
        this.orderActualPay = orderActualPay;
    }

    public double getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(double rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public double getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(double orderPay) {
        this.orderPay = orderPay;
    }

    public int getOrderPayType() {
        return orderPayType;
    }

    public void setOrderPayType(int orderPayType) {
        this.orderPayType = orderPayType;
    }

    public String getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(String orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public String getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(String orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public double getServerUnitPrice() {
        return serverUnitPrice;
    }

    public void setServerUnitPrice(double serverUnitPrice) {
        this.serverUnitPrice = serverUnitPrice;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public int getChargingWay() {
        return chargingWay;
    }

    public void setChargingWay(int chargingWay) {
        this.chargingWay = chargingWay;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getServerContent() {
        return serverContent;
    }

    public void setServerContent(String serverContent) {
        this.serverContent = serverContent;
    }

    public String getCommentStar() {
        return commentStar;
    }

    public void setCommentStar(String commentStar) {
        this.commentStar = commentStar;
    }

    public List<String> getServerTagList() {
        return serverTagList;
    }

    public void setServerTagList(List<String> serverTagList) {
        this.serverTagList = serverTagList;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }
}
