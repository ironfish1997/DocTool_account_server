package top.liuliyong.account.common.response;

import lombok.Getter;

@Getter
public enum StatusEnum {
    LACK_OF_INFORMATION(-10030, "lack of information"),
    FROZEN_ACCOUNT(-10031, "invalid status"),
    NOT_ONLINE(-10032, "this account is not online"),
    DELETE_ACCOUNT_FAILED(-10033, "delete account failed"),
    ALREADY_REGISTED(-10034, "already registed"),
    WRONG_ACCOUNT_OR_PASSWORD(-10035, "wrong account_id or password"),
    SESSION_ID_OUTOFDATA(-10036,"session_id is out of date"),
    NO_AUTH(-10037,"you have no permission");

    private Integer code;
    private String msg;

    StatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
