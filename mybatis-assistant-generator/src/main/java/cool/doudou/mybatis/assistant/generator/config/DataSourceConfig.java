package cool.doudou.mybatis.assistant.generator.config;

/**
 * 数据源 配置
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public class DataSourceConfig {
    private final String url;
    private final String user;
    private final String password;

    public DataSourceConfig(String ip, int port, String user, String password) {
        this.url = "jdbc:mysql://" + ip + ":" + port + "/information_schema";
        this.user = user;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
