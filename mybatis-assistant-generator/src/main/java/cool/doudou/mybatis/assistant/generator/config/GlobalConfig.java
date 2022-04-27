package cool.doudou.mybatis.assistant.generator.config;

/**
 * 全局 配置
 *
 * @author jiangcs
 * @since 2022/4/9
 */
public class GlobalConfig {
    /**
     * 作者
     */
    private String author = "doudou";
    /**
     * 输出路径
     */
    private String outputDir = System.getProperty("os.name").toLowerCase().contains("windows") ? "D://" : "./tmp";
    /**
     * 是否 打开目录
     */
    private boolean openDir = false;

    public GlobalConfig author(String author) {
        this.author = author;
        return this;
    }

    public GlobalConfig outputDir(String dir) {
        this.outputDir = dir;
        return this;
    }

    public GlobalConfig openDir(boolean openDir) {
        this.openDir = openDir;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public boolean isOpenDir() {
        return openDir;
    }
}
