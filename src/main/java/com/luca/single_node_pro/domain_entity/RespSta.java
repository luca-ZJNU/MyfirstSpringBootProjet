package com.luca.single_node_pro.domain_entity;

/**
 * @author luca
 * @create 2020-09-22 14:20
 */

public class RespSta {
    /**
     * code：状态码，给自己看的，比如 200=成功，500/400=失败
     * msg：消息
     * data: 数据，我们可以将一些json数据写到data里
     * 这样的一套规则，我们可以称之为一个json报文
     *
     * */
    private int code;
    private String msg;
    private String data;

    public RespSta() {
    }

    public RespSta(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
