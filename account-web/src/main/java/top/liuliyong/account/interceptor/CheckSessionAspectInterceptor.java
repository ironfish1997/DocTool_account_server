package top.liuliyong.account.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.liuliyong.account.common.exception.AccountOperationException;
import top.liuliyong.account.common.response.AccountOperationResponse;
import top.liuliyong.account.common.response.StatusEnum;
import top.liuliyong.account.controller.SSOAppController;
import top.liuliyong.account.dao.model.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author liyong.liu
 * @Date 2019/3/29
 **/
@Aspect
@Order(1)
@Component
@Slf4j
public class CheckSessionAspectInterceptor {

    private final SSOAppController ssoAppController;

    public CheckSessionAspectInterceptor(SSOAppController ssoAppController) {
        this.ssoAppController = ssoAppController;
    }

    @Pointcut("@annotation(top.liuliyong.account.interceptor.annotation.CheckSessionId)")
    public void checkSessionId() {
    }

    @Pointcut("@annotation(top.liuliyong.account.interceptor.annotation.NeedAdminAuth)")
    public void needAdminAuth() {

    }

    @Before("checkSessionId()")
    public void checkSessionId(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 记录下请求内容
        log.info("URL : " + Objects.requireNonNull(request).getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        //判定是否已经登录
        String session_id = request.getHeader("session_id");
        if (session_id == null || session_id.trim().length() == 0) {
            throw new AccountOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        AccountOperationResponse checkResult = (AccountOperationResponse) ssoAppController.logincheck(session_id);
        if (checkResult.getRtn() != 0) {
            throw new AccountOperationException(StatusEnum.SESSION_ID_OUTOFDATA);
        }
    }

    @Before("needAdminAuth()")
    public void checkIsAdmin(JoinPoint joinPoint) throws AccountOperationException {
        // 接收到请求，拿到对应的session_id
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String session_id = Objects.requireNonNull(request).getHeader("session_id");
        if (session_id == null || session_id.trim().length() == 0) {
            throw new AccountOperationException(StatusEnum.LACK_OF_INFORMATION);
        }
        //验证该session_id是否有效并且是否是管理员
        AccountOperationResponse response = (AccountOperationResponse) ssoAppController.logincheck(session_id);
        if (response.getRtn() == 0) {
            Account account = (Account) response.getData();
            List<String> auth = account.getAccount_permission().parallelStream().filter(t -> t.equals("admin_auth")).collect(Collectors.toList());
            if (auth.size() == 0) {
                throw new AccountOperationException(StatusEnum.NO_AUTH);
            }
        } else {
            //该session_id未登录
            throw new AccountOperationException(StatusEnum.NOT_ONLINE);
        }
    }

}
