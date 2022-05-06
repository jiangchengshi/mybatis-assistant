package cool.doudou.mybatis.assistant.generator;

import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
import cool.doudou.mybatis.assistant.generator.config.TableConfig;
import org.junit.jupiter.api.Test;

/**
 * CodeGeneratorTest
 *
 * @author jiangcs
 * @since 2022/4/18
 */
public class CodeGeneratorTest {
    @Test
    public void execute() {
        CodeGenerator.create("192.168.13.213", 3336, "root", "1234.abcd")
                .globalConfig(new GlobalConfig().author("jiangcs"))
                .packageConfig(new PackageConfig().parent("cool.doudou.celery.upms"))
                .tableConfig(new TableConfig().schema("celery-upms")
                        .nameList("sys_role"))
                .execute();
    }
}
