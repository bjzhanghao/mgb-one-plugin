## Mybatis Generator (MBG)插件
此插件的作用是根据插件配置，在POJO里生成对象类型的成员变量，一般对应外键的情况。

仅限于targetRuntime配置为`MyBatis3DynamicSql`的情况（配置样例：https://mybatis.org/generator/quickstart.html#MyBatis3DynamicSql）。

### 配置

将`mbg-one-plugin-1.0-SNAPSHOT.jar`复制到项目的`/src/main/resources/jar`目录下，然后在项目`pom.xml`里添加依赖，注意是添加到`mybatis-generator-maven-plugin`下：
```
<!-- mybatis generator -->
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.4.1</version>
    <configuration>
        <overwrite>true</overwrite>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.48</version>
        </dependency>
        <dependency>
            <groupId>cn.appboard</groupId>
            <artifactId>mgb</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/src/main/resources/jar/mbg-one-plugin-1.0-SNAPSHOT.jar</systemPath>
        </dependency>
    </dependencies>
</plugin>
```

在`generatorConfig.xml`里配置需要生成的内容：
```
<plugin type="cn.appboard.mgb.OnePlugin">
    <property name="t_product,c_store_id" value="Store,store" />
</plugin>
```
其中`property`里的配置格式如下：
- `name`是用逗号分隔的字符串，[0]表示table名称，[1]表示column名称；
- `value`是用逗号分隔的字符串，[0]是需要在POJO里生成的field类型，[1]是需要在POJO里生成的field名称；注意：如果[1]与MGB默认生成的property冲突，建议在`generatorConfig.xml`里用`columnOverride`更改MGB生成的property名称。

同一个table可以指定多条配置，如果指定的table或column名称不存在，会忽略。

### 使用

使用`mvn mybatis-generator:generate`命令生成代码，生成的代码效果如下：

```
class Product

    // 原有代码仍然保留
    
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator")
    private Long storeId; 

    // 新生成的部分
    
    @Generated(value="cn.appboard.mgb.OnePlugin")
    private Store store;
    
    @Generated(value="cn.appboard.mgb.OnePlugin")
    public Store getStore() {
        return this.store;
    }
    
    @Generated(value="cn.appboard.mgb.OnePlugin")
    public void setStore(Store store) {
        this.store = store;
    }
}
```
