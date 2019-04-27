package top.liuliyong.account.common.exception;

/**
 * @Author liyong.liu
 * @Date 2019/3/30
 **/

import lombok.Data;
import top.liuliyong.account.common.response.StatusEnum;

/**
 * 抛出自定义异常
 */
@Data
public class AccountOperationException extends RuntimeException {
    private Integer rtn;

    public AccountOperationException(Integer rtn, String msg) {
        super(msg);
        this.rtn = rtn;
    }

    public AccountOperationException(StatusEnum statusEnum) {
        super(statusEnum.getMsg());
        this.rtn = statusEnum.getCode();
    }
}
