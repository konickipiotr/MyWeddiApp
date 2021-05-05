package com.myweddi.module.table.model;

public class TableHelper {

    private int position;
    private int placeid;
    private int tableid;
    private Long userid;

    public TableHelper() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPlaceid() {
        return placeid;
    }

    public void setPlaceid(int placeid) {
        this.placeid = placeid;
    }

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "TableHelper{" +
                "position=" + position +
                ", placeid=" + placeid +
                ", tableid=" + tableid +
                ", userid=" + userid +
                '}';
    }
}
