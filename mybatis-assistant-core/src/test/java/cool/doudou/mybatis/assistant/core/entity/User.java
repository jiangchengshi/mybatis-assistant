package cool.doudou.mybatis.assistant.core.entity;

import cool.doudou.mybatis.assistant.annotation.Desensitize;

/**
 * User
 *
 * @author jiangcs
 * @since 2022/3/31
 */
public class User extends BaseEntity {
    @Desensitize(strategy = "userName")
    private String name;
    private String py;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", py='" + py + '\'' +
                '}';
    }
}
