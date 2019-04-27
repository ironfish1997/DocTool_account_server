package top.liuliyong.account.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import top.liuliyong.account.common.response.AccountOperationResponse;

/**
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/account/logincheck")
    AccountOperationResponse logincheck(@RequestHeader("session_id") String sessionId);

}
