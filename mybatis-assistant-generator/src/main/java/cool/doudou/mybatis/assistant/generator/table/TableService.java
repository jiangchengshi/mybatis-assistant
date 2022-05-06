package cool.doudou.mybatis.assistant.generator.table;

import cool.doudou.mybatis.assistant.generator.config.DataSourceConfig;
import cool.doudou.mybatis.assistant.generator.config.TableConfig;
import cool.doudou.mybatis.assistant.generator.entity.DbColumn;
import cool.doudou.mybatis.assistant.generator.entity.DbTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TableService
 *
 * @author jiangcs
 * @since 2022/5/6
 */
public class TableService {
    private final DataSourceConfig dataSourceConfig;
    private final TableConfig tableConfig;

    public TableService(DataSourceConfig dataSourceConfig, TableConfig tableConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.tableConfig = tableConfig;
    }

    public DbTable getInfo(String tableName, String driverClassName, String tableSql, String columnSql) {
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
                    String columnName = String.valueOf(resultSet.getObject("COLUMN_NAME"));
                    // 剔除BaseEntity中属性
                    if ("id".equals(columnName) || "create_by".equals(columnName) || "create_time".equals(columnName)
                            || "update_by".equals(columnName) || "update_time".equals(columnName) || "deleted".equals(columnName)) {
                        continue;
                    }

                    dbColumn = new DbColumn();
                    dbColumn.setName(columnName);
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
}
