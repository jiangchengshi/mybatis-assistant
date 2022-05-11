package cool.doudou.mybatis.assistant.generator;

import cool.doudou.mybatis.assistant.expansion.dialect.DialectHandlerFactory;
import cool.doudou.mybatis.assistant.expansion.dialect.IDialectHandler;
import cool.doudou.mybatis.assistant.generator.config.DataSourceConfig;
import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
import cool.doudou.mybatis.assistant.generator.config.TableConfig;
import cool.doudou.mybatis.assistant.generator.entity.TableInfo;
import cool.doudou.mybatis.assistant.generator.output.OutputService;
import cool.doudou.mybatis.assistant.generator.table.TableService;
import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * CodeGenerator
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public class CodeGenerator {
    /**
     * 数据源配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * 全局配置
     */
    private GlobalConfig globalConfig;
    /**
     * 类包配置
     */
    private PackageConfig packageConfig;
    /**
     * 数据表配置
     */
    private TableConfig tableConfig;

    public CodeGenerator(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.globalConfig = new GlobalConfig();
        this.packageConfig = new PackageConfig();
        this.tableConfig = new TableConfig();
    }

    public static CodeGenerator create(String ip, int port, String user, String password) {
        return new CodeGenerator(new DataSourceConfig(ip, port, user, password));
    }

    public CodeGenerator globalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public CodeGenerator packageConfig(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
        return this;
    }

    public CodeGenerator tableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
        return this;
    }

    public void execute() {
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);

        IDialectHandler dialectHandler = DialectHandlerFactory.getInstance(this.dataSourceConfig.getUrl());
        if (dialectHandler == null) {
            System.err.println("数据库方言匹配失败");
            return;
        }

        // 驱动类名
        final String driverClassName = dialectHandler.getDriverClassName();
        // 表信息SQL
        final String tableSql = dialectHandler.getTableSql();
        // 字段信息SQL
        final String columnSql = dialectHandler.getColumnSql();

        TableService tableService = new TableService(this.dataSourceConfig, this.tableConfig);
        OutputService outputService = new OutputService(this.globalConfig, this.packageConfig);
        this.tableConfig.getNameList().forEach(tableName -> {
            TableInfo tableInfo = tableService.getInfo(tableName, driverClassName, tableSql, columnSql);
            if (tableInfo != null) {
                outputService.execute(tableInfo);
            }
        });

        System.out.println("code generate ok.");
    }
}
