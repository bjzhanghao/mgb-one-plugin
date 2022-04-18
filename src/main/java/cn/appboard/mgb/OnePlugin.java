package cn.appboard.mgb;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * Implementing MBG plugins:
 * https://mybatis.org/generator/reference/pluggingIn.html
 */
public class OnePlugin extends PluginAdapter {

    final String ANNOTATION_TEXT = "@Generated(value=\"cn.appboard.mgb.OnePlugin\")";

    @Override
    public boolean validate(List<String> warnings) {
        for (Object key : properties.keySet()) {
            String theKey = (String) key;
            if ((theKey).split(",").length != 2) {
                warnings.add("OnePlugin: Wrong property key format: " + key);
                return false;
            }
            String theValue = properties.getProperty(theKey);
            if ((theValue).split(",").length != 2) {
                warnings.add("OnePlugin: Wrong property value format: " + theValue);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        for (Object key : properties.keySet()) { //e.g. t_stock_item,c_parent_id
            String tableName = ((String) key).split(",")[0];
            String columnName = ((String) key).split(",")[1];
            String typeName = properties.getProperty((String) key).split(",")[0]; //e.g. StockItem
            String fieldName = properties.getProperty((String) key).split(",")[1]; //e.g. parent
            if (introspectedTable.getFullyQualifiedTableNameAtRuntime().equals(tableName) && introspectedColumn.getActualColumnName().equals(columnName)) {
                String qualifiedTypeName = context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + typeName; //e.g. cn.appboard.fain.model.StockItem
                FullyQualifiedJavaType type = new FullyQualifiedJavaType(qualifiedTypeName);
                Field newField = new Field(fieldName, type);
                newField.setVisibility(JavaVisibility.PRIVATE);
                topLevelClass.addField(newField);

                newField.addAnnotation(ANNOTATION_TEXT);
            }
        }
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        for (Object key : properties.keySet()) { //e.g. t_stock_item,c_parent_id
            String tableName = ((String) key).split(",")[0];
            String columnName = ((String) key).split(",")[1];
            String typeName = properties.getProperty((String) key).split(",")[0]; //e.g. StockItem
            String fieldName = properties.getProperty((String) key).split(",")[1]; //e.g. parent
            if (introspectedTable.getFullyQualifiedTableNameAtRuntime().equals(tableName) && introspectedColumn.getActualColumnName().equals(columnName)) {
                String qualifiedTypeName = context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + typeName; //e.g. cn.appboard.fain.model.StockItem
                FullyQualifiedJavaType type = new FullyQualifiedJavaType(qualifiedTypeName);
                String newMethodName = "get" + typeName;//e.g. getParent
                Method newMethod = new Method(newMethodName);
                newMethod.setVisibility(JavaVisibility.PUBLIC);
                newMethod.setReturnType(type);
                newMethod.addBodyLine("return this." + fieldName + ";");
                topLevelClass.addMethod(newMethod);

                newMethod.addAnnotation(ANNOTATION_TEXT);
            }
        }
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        for (Object key : properties.keySet()) { //e.g. t_stock_item,c_parent_id
            String tableName = ((String) key).split(",")[0];
            String columnName = ((String) key).split(",")[1];
            String typeName = properties.getProperty((String) key).split(",")[0]; //e.g. StockItem
            String fieldName = properties.getProperty((String) key).split(",")[1]; //e.g. parent
            if (introspectedTable.getFullyQualifiedTableNameAtRuntime().equals(tableName) && introspectedColumn.getActualColumnName().equals(columnName)) {
                String qualifiedTypeName = context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + typeName; //e.g. cn.appboard.fain.model.StockItem
                FullyQualifiedJavaType type = new FullyQualifiedJavaType(qualifiedTypeName);
                String newMethodName = "set" + typeName;//e.g. setParent
                Method newMethod = new Method(newMethodName);
                newMethod.setVisibility(JavaVisibility.PUBLIC);
                newMethod.addParameter(new Parameter(type, fieldName));
                newMethod.addBodyLine("this." + fieldName + " = " + fieldName + ";");
                topLevelClass.addMethod(newMethod);

                newMethod.addAnnotation(ANNOTATION_TEXT);
            }
        }
        return true;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // method.addAnnotation(ANNOTATION_TEXT); //Will cause duplication
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select"));
        method.addAnnotation("@Select(\"select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()
                + " where " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + " = #{id_}\")");
        method.addAnnotation("@ResultMap(\"" + introspectedTable.getMyBatisDynamicSQLTableObjectName() + "Result\")");
        return true;
    }

    /**
     * 原始MGB生成的annotation举例如下:
     * @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: t_stock_record")
     *     @SelectProvider(type=SqlProviderAdapter.class, method="select")
     *     @Results(id="StockRecordResult", value = {
     *         @Result(column="c_id", property="id", jdbcType=JdbcType.BIGINT, id=true),
     *         @Result(column="c_user", property="user", jdbcType=JdbcType.VARCHAR),
     *         @Result(column="c_store_id", property="stockItemId", jdbcType=JdbcType.BIGINT),
     *         @Result(column="c_event_time", property="eventTime", jdbcType=JdbcType.TIMESTAMP)
     *     })
     * 需要在倒数第三行添加store对象的内容：
     * @Result(column="c_store_id", property="store",
     *         one = @One(select = "cn.appboard.fain.dao.StoreDao.selectByPrimaryKey")
     * ),
     *
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientBasicSelectManyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        for (Object key : properties.keySet()) { //e.g. t_stock_item,c_parent_id
            String tableName = ((String) key).split(",")[0];
            String columnName = ((String) key).split(",")[1];
            String typeName = properties.getProperty((String) key).split(",")[0]; //e.g. StockItem
            String fieldName = properties.getProperty((String) key).split(",")[1]; //e.g. parent
            if (introspectedTable.getFullyQualifiedTableNameAtRuntime().equals(tableName)) {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.One"));

                int position = -1;
                for (int i = 0; i < method.getAnnotations().size(); i++) {
                    if (method.getAnnotations().get(i).contains("column=\"" + columnName + "\"")) {
                        position = i;
                        break;
                    }
                }
                if (position <= 0) {
                    System.err.println("@Result annotation position not exists (selectMany)");
                    return true;
                }
                String annotation = "    @Result(column=\"" + columnName + "\", property=\"" + fieldName
                        + "\", one = @One(select = \"" + interfaze.getType().getPackageName()
                        + "." + typeName + "Dao.selectByPrimaryKey\")),";
                method.getAnnotations().add(position, annotation);
            }
        }

        return true;
    }

}
