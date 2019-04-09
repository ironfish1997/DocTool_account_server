package top.liuliyong.common.exception;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.liuliyong.common.response.AccountOperationResponse;

import javax.servlet.http.HttpServletResponse;


/**
 * 全局错误处理
 *
 * @Author liyong.liu
 * @Date 2019/3/12
 **/
@ControllerAdvice
class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultErrorHandler(HttpServletResponse res, Exception e) {
        logger.error("global catched error", e);
        int rtn = -10000;
        if (e instanceof AccountOperationException) {
            AccountOperationException result = (AccountOperationException) e;
            rtn = result.getRtn();
        }
        AccountOperationResponse error = new AccountOperationResponse(rtn, e.getMessage(), null);
        res.setStatus(500);
        return JSON.toJSONString(error);
    }

}