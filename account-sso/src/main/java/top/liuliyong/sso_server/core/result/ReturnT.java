package top.liuliyong.sso_server.core.result;

import lombok.Data;
import top.liuliyong.common.response.StatusEnum;

import java.io.Serializable;

/**
 * common return
 *
 * @param <T>
 * @author xuxueli 2015-12-4 16:32:31
 */
@Data
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 42L;

    public static final int SUCCESS_CODE = 0;

    private int code;
    private String msg;
    private T data;

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnT(T data) {
        this.code = SUCCESS_CODE;
        this.data = data;
    }

    public ReturnT(StatusEnum statusEnum) {
        this.code = statusEnum.getCode();
        this.msg = statusEnum.getMsg();
    }

}
