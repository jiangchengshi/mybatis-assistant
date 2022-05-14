package cool.doudou.mybatis.assistant.generator.output;

import cool.doudou.mybatis.assistant.expansion.util.ComUtil;
import cool.doudou.mybatis.assistant.generator.Constant;
import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
import cool.doudou.mybatis.assistant.generator.entity.Column;
import cool.doudou.mybatis.assistant.generator.entity.Property;
import cool.doudou.mybatis.assistant.generator.entity.TableInfo;
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

    public void execute(TableInfo tableInfo) {
        Velocity.setProperty("input.encoding", "UTF-8");
        Velocity.setProperty("output.encoding", "UTF-8");

        // 环境参数
        Map<String, Object> contextMap = contextMap(tableInfo);
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

            FileWriter writer = null;
            try {
                File file = new File(parentFile, fileName);
                if (!file.exists() || (file.exists() && this.globalConfig.isCover())) {
                    writer = new FileWriter(file);
                    Template template = Velocity.getTemplate(templateName, StandardCharsets.UTF_8.name());
                    template.merge(new VelocityContext(contextMap), writer);
                } else {
                    System.err.println("file[" + file.getAbsolutePath() + "] exists.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert writer != null;
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
     * @param tableInfo
     * @return
     */
    private Map<String, Object> contextMap(TableInfo tableInfo) {
        Map<String, Object> contextMap = new HashMap<>(8);
        contextMap.put("tableName", tableInfo.getName());
        contextMap.put("tableNameAlias", ComUtil.underline2Hump(tableInfo.getName()) + "0");
        contextMap.put("tableComment", tableInfo.getComment());
        contextMap.put("author", this.globalConfig.getAuthor());
        contextMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        contextMap.put("package", this.packageConfig);
        contextMap.put("instance", instance(tableInfo.getName(), tableInfo.getColumnList()));
        contextMap.put("xmlMap", xmlMap(tableInfo.getColumnList()));
        return contextMap;
    }

    /**
     * 根据 tableName 生成 instance相关
     *
     * @param tableName
     * @param columnList
     * @return
     */
    private Map<String, Object> instance(String tableName, List<Column> columnList) {
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

        List<Property> propertyList = new ArrayList<>();
        Set<String> importPackageSet = new HashSet<>();
        columnList.forEach((column -> {
            String columnName = column.getName();

            // 剔除BaseEntity中属性
            if (!"id".equals(columnName) && !"create_by".equals(columnName) && !"create_time".equals(columnName) && !"update_by".equals(columnName) && !"update_time".equals(columnName) && !"deleted".equals(columnName)) {
                Property property = new Property();
                property.setName(ComUtil.underline2Hump(columnName));
                property.setJavaType(ComUtil.convert2JavaType(column.getDataType()));
                property.setComment(column.getComment());
                propertyList.add(property);

                // 引入数据类型包
                switch (property.getJavaType()) {
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
        instanceMap.put("propertyList", propertyList);
        instanceMap.put("importPackageSet", importPackageSet);
        return instanceMap;
    }

    /**
     * 根据 columnList 生成 xml相关
     *
     * @param columnList
     * @return
     */
    private Map<String, Object> xmlMap(List<Column> columnList) {
        Map<String, Object> xmlMap = new HashMap<>(3);

        List<Column> columnInsertList = new ArrayList<>();
        List<Column> columnUpdateList = new ArrayList<>();
        List<Property> propertyList = new ArrayList<>();
        List<Property> propertyInsertList = new ArrayList<>();
        List<Property> propertyUpdateList = new ArrayList<>();
        columnList.forEach(column -> {
            String columnName = column.getName();

            Property property = new Property();
            property.setName(ComUtil.underline2Hump(columnName));
            property.setJavaType(ComUtil.convert2JavaType(column.getDataType()));
            property.setComment(column.getComment());
            propertyList.add(property);

            // insert
            if (!"update_by".equals(columnName) && !"update_time".equals(columnName)) {
                columnInsertList.add(column);
                propertyInsertList.add(property);
            }
            // update
            if (!"id".equals(columnName) && !"create_by".equals(columnName) && !"create_time".equals(columnName) && !"deleted".equals(columnName)) {
                columnUpdateList.add(column);
                propertyUpdateList.add(property);
            }
        });
        xmlMap.put("columnList", columnList);
        xmlMap.put("columnInsertList", columnInsertList);
        xmlMap.put("columnUpdateList", columnUpdateList);
        xmlMap.put("propertyList", propertyList);
        xmlMap.put("propertyInsertList", propertyInsertList);
        xmlMap.put("propertyUpdateList", propertyUpdateList);

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
