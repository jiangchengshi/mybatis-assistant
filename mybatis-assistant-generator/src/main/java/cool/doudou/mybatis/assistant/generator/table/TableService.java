package cool.doudou.mybatis.assistant.generator.table;

import cool.doudou.mybatis.assistant.generator.config.DataSourceConfig;
import cool.doudou.mybatis.assistant.generator.config.TableConfig;
import cool.doudou.mybatis.assistant.generator.entity.Column;
import cool.doudou.mybatis.assistant.generator.entity.TableInfo;

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

    public TableInfo getInfo(String tableName, String driverClassName, String tableSql, String columnSql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(this.dataSourceConfig.getUrl(), this.dataSourceConfig.getUser(), this.dataSourceConfig.getPassword());

            TableInfo tableInfo;
            // 表信息
            preparedStatement = connection.prepareStatement(tableSql);
            preparedStatement.setString(1, this.tableConfig.getSchema());
            preparedStatement.setString(2, tableName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tableInfo = new TableInfo();
                tableInfo.setName(tableName);
                tableInfo.setComment(resultSet.getString("TABLE_COMMENT"));

                // 字段信息
                Column column;
                List<Column> columnList = new ArrayList<>();
                preparedStatement = connection.prepareStatement(columnSql);
                preparedStatement.setString(1, this.tableConfig.getSchema());
                preparedStatement.setString(2, tableName);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    column = new Column();
                    column.setName(resultSet.getString("COLUMN_NAME"));
                    column.setDataType(resultSet.getString("DATA_TYPE"));
                    column.setComment(resultSet.getString("COLUMN_COMMENT"));
                    column.setKey(resultSet.getString("COLUMN_KEY"));
                    column.setNullable(resultSet.getString("IS_NULLABLE"));
                    columnList.add(column);
                }
                tableInfo.setColumnList(columnList);

                return tableInfo;
            }
            System.err.println("prompt: table[" + tableName + "] not exists");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert resultSet != null;
                resultSet.close();

                assert preparedStatement != null;
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
