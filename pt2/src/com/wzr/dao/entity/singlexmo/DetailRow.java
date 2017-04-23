package com.wzr.dao.entity.singlexmo;

/**
 * Created by wzr on 2017/4/15.
 * 生产明细表的一行有效数据
 */
public class DetailRow {
    private String barCode;
    private String styleCode;
    private String pdName;
    private String length;
    private String size;
    private String cusCode;
//    private String billNo;
    private String billNo;
    private String remark;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String brand) {
        this.billNo = brand;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
