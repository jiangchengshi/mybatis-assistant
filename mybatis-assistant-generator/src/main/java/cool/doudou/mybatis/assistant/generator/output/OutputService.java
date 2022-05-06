package cool.doudou.mybatis.assistant.generator.output;

import cool.doudou.mybatis.assistant.expansion.util.ComUtil;
import cool.doudou.mybatis.assistant.generator.Constant;
import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
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
        Map<String, String> templateMap = templateMap((ClassInstance) contextMap.get("instance"));
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            String templateName = entry.getKey();
            String fileName = entry.getValue();
            File parentPath = new File(this.globalConfig.getOutputDir() + File.separator + this.packageConfig.getPath(fileName));
            if (!parentPath.exists()) {
                parentPath.mkdirs();
            }

            try {
                String directory = parentPath.getAbsolutePath();
                FileWriter writer = new FileWriter(directory + File.separator + fileName);
                Template template = Velocity.getTemplate(templateName, StandardCharsets.UTF_8.name());
                template.merge(new VelocityContext(contextMap), writer);
                writer.flush();
                writer.close();

                openDir(directory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开目录
     *
     * @param directory
     * @throws Exception
     */
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
