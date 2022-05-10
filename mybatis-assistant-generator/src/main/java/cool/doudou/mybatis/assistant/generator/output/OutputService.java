package cool.doudou.mybatis.assistant.generator.output;

import cool.doudou.mybatis.assistant.expansion.util.ComUtil;
import cool.doudou.mybatis.assistant.generator.Constant;
import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
import cool.doudou.mybatis.assistant.generator.entity.DbColumn;
import cool.doudou.mybatis.assistant.generator.entity.DbProperty;
import cool.doudou.mybatis.assistant.generator.entity.DbTable;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OutputService
 *
 * @author jiangcs
 * @since 2022/5/6
 */
public class OutputService {
    private final GlobalConfig globalConfig;
    private final PackageConfig packageConfig;

    public OutputService(GlobalConfig globalConfig, PackageConfig packageConfig) {
        this.globalConfig = globalConfig;
        this.packageConfig = packageConfig;
    }

    public void execute(DbTable dbTable) {
        Velocity.setProperty("input.encoding", "UTF-8");
        Velocity.setProperty("output.encoding", "UTF-8");

        // 环境参数
        Map<String, Object> contextMap = contextMap(dbTable);
        // 模版
        Map<String, String> templateMap = templateMap((Map<String, Object>) contextMap.get("instance"));
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            String templateName = entry.getKey();
            String fileName = entry.getValue();

            String parentPath, packagePath;
            if (templateName.endsWith("java.vm")) {
                parentPath = this.globalConfig.getOutputDir() + File.separator + "src" + File.separator + "main" + File.separator + "java";
                packagePath = this.packageConfig.getParent().replaceAll("\\.", File.separator) + File.separator + this.packageConfig.getPath(fileName);
            } else {
                parentPath = this.globalConfig.getOutputDir() + File.separator + "src" + File.separator + "main" + File.separator + "resources";
                packagePath = "mapper";
            }
            File parentFile = new File(parentPath + File.separator + packagePath);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            try {
                File file = new File(parentFile, fileName);
                if (!file.exists() || (file.exists() && this.globalConfig.isCover())) {
                    FileWriter writer = new FileWriter(file);
                    Template template = Velocity.getTemplate(templateName, StandardCharsets.UTF_8.name());
                    template.merge(new VelocityContext(contextMap), writer);
                    writer.flush();
                    writer.close();
                } else {
                    System.err.println("file[" + file.getAbsolutePath() + "] exists.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (this.globalConfig.isOpenDir()) {
            openDir(this.globalConfig.getOutputDir());
        }
    }

    /**
     * 打开目录
     *
     * @param directory
     */
    private void openDir(String directory) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
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
        contextMap.put("instance", instance(dbTable.getName(), dbTable.getColumnList()));
        contextMap.put("xmlMap", xmlMap(dbTable.getColumnList()));
        return contextMap;
    }

    /**
     * 根据 tableName 生成 instance相关
     *
     * @param tableName
     * @param columnList
     * @return
     */
    private Map<String, Object> instance(String tableName, List<DbColumn> columnList) {
        Map<String, Object> instanceMap = new HashMap<>();
        int firstIndex = tableName.indexOf("_");
        String name = ComUtil.underline2Hump(tableName.substring(firstIndex + 1));
        String className = ComUtil.upperFirst(name);
        instanceMap.put("controller", name + Constant.CONTROLLER);
        instanceMap.put("controllerClass", className + Constant.CONTROLLER);
        instanceMap.put("service", name + Constant.SERVICE);
        instanceMap.put("serviceClass", className + Constant.SERVICE);
        instanceMap.put("mapper", name + Constant.MAPPER);
        instanceMap.put("mapperClass", className + Constant.MAPPER);
        instanceMap.put("entity", name);
        instanceMap.put("entityClass", className);

        List<DbProperty> dbPropertyList = new ArrayList<>();
        Set<String> importPackageSet = new HashSet<>();
        columnList.forEach((column -> {
            String columnName = column.getName();

            // 剔除BaseEntity中属性
            if (!"id".equals(columnName) && !"create_by".equals(columnName) && !"create_time".equals(columnName)
                    && !"update_by".equals(columnName) && !"update_time".equals(columnName) && !"deleted".equals(columnName)) {
                DbProperty dbProperty = new DbProperty();
                dbProperty.setName(ComUtil.underline2Hump(columnName));
                dbProperty.setJavaType(ComUtil.convert2JavaType(column.getDataType()));
                dbProperty.setComment(column.getComment());
                dbPropertyList.add(dbProperty);

                // 引入数据类型包
                switch (dbProperty.getJavaType()) {
                    case "LocalDate":
                        importPackageSet.add("java.time.LocalDate");
                        break;
                    case "LocalTime":
                        importPackageSet.add("java.time.LocalTime");
                        break;
                    case "LocalDateTime":
                        importPackageSet.add("java.time.LocalDateTime");
                        break;
                    case "BigDecimal":
                        importPackageSet.add("java.math.BigDecimal");
                        break;
                    case "Blob":
                        importPackageSet.add("java.sql.Blob");
                }
            }
        }));
        instanceMap.put("propertyList", dbPropertyList);
        instanceMap.put("importPackageSet", importPackageSet);
        return instanceMap;
    }

    /**
     * 根据 columnList 生成 xml相关
     *
     * @param columnList
     * @return
     */
    private Map<String, Object> xmlMap(List<DbColumn> columnList) {
        Map<String, Object> xmlMap = new HashMap<>(3);
        xmlMap.put("columnList", columnList);

        // 字段
        List<DbProperty> dbPropertyList = new ArrayList<>();
        columnList.forEach(column -> {
            DbProperty dbProperty = new DbProperty();
            dbProperty.setName(ComUtil.underline2Hump(column.getName()));
            dbProperty.setJavaType(ComUtil.convert2JavaType(column.getDataType()));
            dbProperty.setComment(column.getComment());
            dbPropertyList.add(dbProperty);
        });
        xmlMap.put("propertyList", dbPropertyList);

        return xmlMap;
    }

    /**
     * 模版
     *
     * @param instanceMap
     * @return
     */
    private Map<String, String> templateMap(Map<String, Object> instanceMap) {
        String entityClass = String.valueOf(instanceMap.get("entityClass"));
        Map<String, String> templateMap = new HashMap<>(5);
        templateMap.put("template/controller.java.vm", entityClass + "Controller.java");
        templateMap.put("template/service.java.vm", entityClass + "Service.java");
        templateMap.put("template/mapper.java.vm", entityClass + "Mapper.java");
        templateMap.put("template/entity.java.vm", entityClass + ".java");
        templateMap.put("template/mapper.xml.vm", entityClass + "Mapper.xml");
        return templateMap;
    }
}
