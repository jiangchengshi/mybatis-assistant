package cool.doudou.mybatis.assistant.generator;

import cool.doudou.mybatis.assistant.expansion.dialect.DialectHandlerFactory;
import cool.doudou.mybatis.assistant.expansion.dialect.IDialectHandler;
import cool.doudou.mybatis.assistant.expansion.util.ComUtil;
import cool.doudou.mybatis.assistant.generator.config.DataSourceConfig;
import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
import cool.doudou.mybatis.assistant.generator.config.TableConfig;
import cool.doudou.mybatis.assistant.generator.entity.ClassField;
import cool.doudou.mybatis.assistant.generator.entity.ClassInstance;
import cool.doudou.mybatis.assistant.generator.entity.DbColumn;
import cool.doudou.mybatis.assistant.generator.entity.DbTable;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

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

        this.tableConfig.getNameList().forEach(tableName -> {
            DbTable dbTable = getTableInfo(driverClassName, tableName, tableSql, columnSql);
            if (dbTable != null) {
                output(dbTable);
            }
        });
    }

    /**
     * 数据表信息
     *
     * @param driverClassName
     * @param tableName
     * @param tableSql
     * @param columnSql
     * @return
     */
    private DbTable getTableInfo(String driverClassName, String tableName, String tableSql, String columnSql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(this.dataSourceConfig.getUrl(), this.dataSourceConfig.getUser(), this.dataSourceConfig.getPassword());

            DbTable dbTable;
            // 表信息
            preparedStatement = connection.prepareStatement(tableSql);
            preparedStatement.setString(1, this.tableConfig.getSchema());
            preparedStatement.setString(2, tableName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                dbTable = new DbTable();
                dbTable.setName(tableName);
                dbTable.setComment(String.valueOf(resultSet.getObject("TABLE_COMMENT")));

                // 字段信息
                DbColumn dbColumn;
                List<DbColumn> dbColumnList = new ArrayList<>();
                preparedStatement = connection.prepareStatement(columnSql);
                preparedStatement.setString(1, this.tableConfig.getSchema());
                preparedStatement.setString(2, tableName);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    dbColumn = new DbColumn();
                    dbColumn.setName(String.valueOf(resultSet.getObject("COLUMN_NAME")));
                    dbColumn.setDataType(String.valueOf(resultSet.getObject("DATA_TYPE")));
                    dbColumn.setComment(String.valueOf(resultSet.getObject("COLUMN_COMMENT")));
                    dbColumn.setKey(String.valueOf(resultSet.getObject("COLUMN_KEY")));
                    dbColumnList.add(dbColumn);
                }
                dbTable.setColumnList(dbColumnList);

                return dbTable;
            }
            System.err.println("prompt: table[" + tableName + "] not exists");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void output(DbTable dbTable) {
        Velocity.setProperty("input.encoding", "UTF-8");
        Velocity.setProperty("output.encoding", "UTF-8");

        // 环境参数
        Map<String, Object> contextMap = contextMap(dbTable);
        // 模版
        Map<String, String> templateMap = templateMap((ClassInstance) contextMap.get("instance"));
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            String templateName = entry.getKey();
            String fileName = entry.getValue();
            File parentFile = new File(this.globalConfig.getOutputDir());
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            try {
                String directory = parentFile.getAbsolutePath();
                FileWriter writer = new FileWriter(directory + File.separator + fileName);
                Template template = Velocity.getTemplate(templateName, StandardCharsets.UTF_8.name());
                template.merge(new VelocityContext(contextMap), writer);
                writer.flush();
                writer.close();

                // 打开目录
                openDir(directory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openDir(String directory) throws Exception {
        if (this.globalConfig.isOpenDir()) {
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Mac")) {
                    Runtime.getRuntime().exec("open " + directory);
                } else if (osName.contains("Windows")) {
                    Runtime.getRuntime().exec("cmd /c start " + directory);
                } else {
                    System.err.println("操作系统[" + osName + "]匹配失败: 目录 => " + directory);
                }
            } else {
                System.err.println("操作系统获取失败");
            }
        }
    }

    /**
     * 参数
     *
     * @param dbTable
     * @return
     */
    private Map<String, Object> contextMap(DbTable dbTable) {
        Map<String, Object> contextMap = new HashMap<>(8);
        contextMap.put("tableName", dbTable.getName());
        contextMap.put("tableComment", dbTable.getComment());
        contextMap.put("author", this.globalConfig.getAuthor());
        contextMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        contextMap.put("package", this.packageConfig);
        contextMap.put("instance", instance(dbTable.getName()));
        contextMap.put("entityMap", entityMap(dbTable.getColumnList()));
        return contextMap;
    }

    /**
     * 根据 tableName 生成 instance相关
     *
     * @param tableName
     * @return
     */
    private ClassInstance instance(String tableName) {
        int firstIndex = tableName.indexOf("_");
        String underlineName = tableName.substring(firstIndex + 1);
        String name = ComUtil.underline2Hump(underlineName);
        String className = ComUtil.upperFirst(name);
        ClassInstance classInstance = new ClassInstance();
        classInstance.setController(name + Constant.CONTROLLER);
        classInstance.setControllerClass(className + Constant.CONTROLLER);
        classInstance.setService(name + Constant.SERVICE);
        classInstance.setServiceClass(className + Constant.SERVICE);
        classInstance.setMapper(name + Constant.MAPPER);
        classInstance.setMapperClass(className + Constant.MAPPER);
        classInstance.setEntity(name);
        classInstance.setEntityClass(className);
        return classInstance;
    }

    /**
     * 根据 columnList 生成 entity相关
     *
     * @param columnList
     * @return
     */
    private Map<String, Object> entityMap(List<DbColumn> columnList) {
        Map<String, Object> entityMap = new HashMap<>(3);
        entityMap.put("columnList", columnList);

        // 字段
        List<ClassField> fieldList = new ArrayList<>();
        // 字段属性导入包
        Set<String> pkgSet = new HashSet<>();

        columnList.forEach(column -> {
            ClassField classField = new ClassField();
            classField.setName(ComUtil.underline2Hump(column.getName()));
            classField.setJavaType(ComUtil.convert2JavaType(column.getDataType()));
            classField.setComment(column.getComment());
            fieldList.add(classField);

            if ("Date".equals(classField.getJavaType())) {
                pkgSet.add("java.util.Date");
            } else if ("BigDecimal".equals(classField.getJavaType())) {
                pkgSet.add("java.math.BigDecimal");
            } else if ("Blob".equals(classField.getJavaType())) {
                pkgSet.add("java.sql.Blob");
            }
        });
        entityMap.put("fieldList", fieldList);
        entityMap.put("importPackages", pkgSet);

        return entityMap;
    }

    /**
     * 模版
     *
     * @param instance
     * @return
     */
    private Map<String, String> templateMap(ClassInstance instance) {
        String entityClass = instance.getEntityClass();

        Map<String, String> templateMap = new HashMap<>(5);
        templateMap.put("template/controller.java.vm", entityClass + "Controller.java");
        templateMap.put("template/service.java.vm", entityClass + "Service.java");
        templateMap.put("template/mapper.java.vm", entityClass + "Mapper.java");
        templateMap.put("template/entity.java.vm", entityClass + ".java");
        templateMap.put("template/mapper.xml.vm", entityClass + "Mapper.xml");
        return templateMap;
    }
}
