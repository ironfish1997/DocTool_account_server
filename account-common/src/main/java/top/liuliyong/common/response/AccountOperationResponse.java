package top.liuliyong.common.response;

import lombok.Data;

import java.io.Serializable;

/***
 * 用户账户操作返回信息Bean
 */
@Data
public class AccountOperationResponse implements Serializable {
    //返回状态
    private int rtn;
    //返回信息
    private String msg;
    //返回数据体
    private Object data;

    public AccountOperationResponse(int rtn, String msg, Object data) {
        this.rtn = rtn;
        this.msg = msg;
        this.data = data;
    }

    public AccountOperationResponse(StatusEnum se){
        this.rtn = se.getCode();
        this.msg = se.getMsg();
        this.data = null;
    }


}
