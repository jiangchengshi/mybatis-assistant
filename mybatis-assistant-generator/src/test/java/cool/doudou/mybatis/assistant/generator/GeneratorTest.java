package cool.doudou.mybatis.assistant.generator;

import cool.doudou.mybatis.assistant.generator.config.GlobalConfig;
import cool.doudou.mybatis.assistant.generator.config.PackageConfig;
import cool.doudou.mybatis.assistant.generator.config.TableConfig;
import cool.doudou.mybatis.assistant.generator.gen.Generator;
import org.junit.jupiter.api.Test;

/**
 * GeneratorTest
 *
 * @author jiangcs
 * @since 2022/4/18
 */
public class GeneratorTest {
    @Test
    public void execute() {
        new Generator("192.168.13.213", 3336, "root", "1234.abcd")
                .globalConfig(new GlobalConfig().author("jiangcs"))
                .packageConfig(new PackageConfig().parent("cool.doudou.celery.upms"))
                .tableConfig(new TableConfig().schema("celery-upms")
                        .nameList("sys_user"))
                .execute();
    }
}
