package top.liuliyong.model;

import lombok.Data;

import java.util.Map;

/**
 * xxl sso user
 *
 * @author xuxueli 2018-04-02 19:59:49
 */
@Data
public class XxlSsoUser extends Account {
    private static final long serialVersionUID = 42L;

    // field
    private String userid;
    private String username;
    private Map<String, String> plugininfo;

    private String version;
    private int expireMinite;
    private long expireFreshTime;


}
