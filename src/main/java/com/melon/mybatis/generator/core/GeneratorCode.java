package com.melon.mybatis.generator.core;

import com.melon.mybatis.generator.common.PipeConsumer;
import com.melon.mybatis.generator.common.PipeLine;
import com.melon.mybatis.generator.common.PipeProducer;
import com.melon.mybatis.generator.config.*;
import com.melon.mybatis.generator.entity.*;
import com.melon.mybatis.generator.enums.ConstVal;
import com.melon.mybatis.generator.enums.InjectionType;
import com.melon.mybatis.generator.enums.MybatisType;
import com.melon.mybatis.generator.jdbc.ConnectionDB;
import com.melon.mybatis.generator.util.StringUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * 执行生成的主要类
 *
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */
public class GeneratorCode {

    /**
     * 数据源配置
     */
    @Setter
    @Accessors(chain = true)
    private DataSourceConfig dataSourceConfig;
    /**
     * 生成 相关配置
     */
    @Setter
    @Accessors(chain = true)
    private GeneratorConfig generatorConfig;
    /**
     * 包 相关配置
     */
    @Setter
    @Accessors(chain = true)
    private PackageConfig packageConfig;
    /**
     * 包 相关配置
     */
    @Setter
    @Accessors(chain = true)
    private CabinConfig cabinConfig;

    /**
     * 分页 相关配置
     */
    @Setter
    @Accessors(chain = true)
    private PagedConfig pagedConfig;

    private Connection connection;
    private ResultSet tablesResultSet;
    private String catalog;
    private DatabaseMetaData metaData;
    private PackageName packageName;
    private Pattern pattern;
    private Set<String> hasAdded = new HashSet<>();

    private List<Ftls> ftls = new ArrayList<>();

    /**
     * 生成代码
     */
    public void execute() {
        try {
            initPk();
            getConnection();
            queryTables();
            startCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化信息
     */
    private void initPk() {
        String parent = packageConfig.getParent();
        String entity = parent + packageConfig.getEntity();
        String mapper = parent + packageConfig.getMapper();
        String xml = parent + packageConfig.getXml();
        String service = parent + packageConfig.getService();
        String serviceImpl = parent + packageConfig.getServiceImpl();
        String controller = parent + packageConfig.getController();
        String dto = parent + packageConfig.getDto();

        String param = parent + packageConfig.getParam();
        packageName = new PackageName()
                .setControllerPackage(controller)
                .setServiceImplPackage(serviceImpl)
                .setServicePackage(service)
                .setMapperPackage(mapper)
                .setParamPackage(param)
                .setDtoPackage(dto)
                .setEntityPackage(entity);

        ftls.add(new Ftls(entity, generatorConfig.getSuffix(), "entityTemplate.ftl"));
        ftls.add(new Ftls(mapper, ConstVal.MAPPER, "mapperTemplate.ftl"));
        ftls.add(new Ftls(service, ConstVal.SERVICE, "serviceTemplate.ftl"));
        ftls.add(new Ftls(serviceImpl, ConstVal.SERVICE_IMPL, "serviceImplTemplate.ftl"));
        ftls.add(new Ftls(controller, ConstVal.CONTROLLER, "controllerTemplate.ftl"));
        ftls.add(new Ftls(xml, ConstVal.MAPPER, "xmlTemplate.ftl"));
        ftls.add(new Ftls(param, ConstVal.QUERY, "paramTemplate.ftl"));
        ftls.add(new Ftls(param, ConstVal.SAVE, "paramTemplate.ftl"));
        ftls.add(new Ftls(dto, ConstVal.DTO, "dtoTemplate.ftl"));

        if (cabinConfig != null && !StringUtil.isEmpty(cabinConfig.getCabinPackage())) {
            String cabin = parent + cabinConfig.getCabinPackage();
            ftls.add(new Ftls(cabin, generatorConfig.getSuffix(), "jqTemplate.ftl"));
            ftls.add(new Ftls(cabin, generatorConfig.getSuffix(), "tplTemplate.ftl"));
            ftls.add(new Ftls(cabin, generatorConfig.getSuffix(), "jsTemplate.ftl"));
        }
        pattern = Pattern.compile(generatorConfig.getShardingPattern());
    }

    /**
     * 获取数据库连接
     */
    private void getConnection() {
        connection = ConnectionDB.getConnection(dataSourceConfig);
        if (connection == null)
            throw new RuntimeException("获取数据库连接失败");
    }

    /**
     * 查询表
     */
    private void queryTables() throws Exception {
        catalog = connection.getCatalog();
        metaData = connection.getMetaData();
        tablesResultSet = metaData.getTables(catalog, "%", "%", new String[]{"TABLE"});
    }

    /**
     * 启动多线程创建
     */
    private void startCreate() {
        int threadCount = generatorConfig.getThreadCount();
        PipeLine<TableData> listPipeLine = new PipeLine<>(threadCount);
        listPipeLine.registProducer(new TestProducer("0"));
        IntStream.range(0, threadCount)
                .forEach(value -> listPipeLine.registConsumer(new TestConsumer(String.valueOf(value))));
        listPipeLine.start();
    }

    /**
     * 生产者组装TableData
     */
    private class TestProducer extends PipeProducer<TableData> {
        TestProducer(String id) {
            super(id);
        }

        @Override
        public TableData produce(int idx) throws Exception {
            while (tablesResultSet.next()) {
                val tableData = new TableData();
                String tableName = tablesResultSet.getString("TABLE_NAME");  //表名
                tableName = shardingPattern(tableData, tableName);
                if (notCreate(tableName) || hasAdded.contains(tableName)) {
                    continue;
                }
                hasAdded.add(tableName);
                String tableRemark = tablesResultSet.getString("REMARKS");  //备注
                tableData.setCabin(cabinConfig)
                        .setAuthor(generatorConfig.getAuthor())
                        .setRequestMapping(StringUtil.line(tableName));
                bindSuperClass(tableData);
                bindEntityName(tableData, tableName);
                return tableData.setUseLombok(generatorConfig.isEntityLombokModel())
                        .setPackages(packageName)
                        .setTableName(tableName)
                        .setTableRemark(tableRemark);
            }
            return null;
        }
    }

    private String shardingPattern(TableData tableData, String tableName) {
        Matcher matcher = pattern.matcher(tableName);
        if (matcher.find()) {
            String sharding = matcher.group();
            tableData.setSharding(sharding);
            return tableName.replace(sharding, "");
        }
        return tableName;
    }

    /**
     * 绑定父类相关信息
     */
    private void bindSuperClass(TableData tableData) {
        tableData.setPlus(MybatisType.PLUS.equals(generatorConfig.getMybatisType()));
        tableData.setAuto(InjectionType.AUTOWIRED.equals(generatorConfig.getInjection()));
        tableData.setInjection(generatorConfig.getInjection().getType());
        tableData.setSuperEntityClass(getNames(tableData, generatorConfig.getSuperEntityClass(), 0));
        tableData.setSuperDtoClass(getNames(tableData, generatorConfig.getSuperDtoClass(), 1));
        tableData.setDtoUtils(getNames(tableData, generatorConfig.getDtoUtils(), 2));
        tableData.setSuperDtoColumns(generatorConfig.getSuperDtoColumns());
        PagedName pagedName = new PagedName();
        if (pagedConfig != null) {
            pagedName.setPaged(true)
                    .setPagedAnnotationName(getNames(tableData, pagedConfig.getPagedAnnotation(), 5))
                    .setPagedInfoName(getNames(tableData, pagedConfig.getPagedInfo(), 6));
            tableData.setPagedName(pagedName);
        }
    }

    private String getNames(TableData tableData, String className, int type) {
        if (StringUtil.isEmpty(className)) {
            tableData.addPackageImport("import java.io.Serializable;");
            return className;
        }
        if (!className.contains(".")) {
            return className;
        }
        int lastIndexOf = className.lastIndexOf(".");
        String target = className.substring(lastIndexOf + 1);
        if (type == 0) {
            tableData.addPackageImport("import " + className + ";");
        } else if (type == 1) {
            packageName.setSuperDtoPackage(className);
        } else if (type == 2) {
            packageName.setSuperDtoUtilsPackage(className);
        } else if (type == 3) {
            packageName.setSuperQueryParamPackage(className);
        } else if (type == 4) {
            packageName.setSuperSaveParamPackage(className);
        } else if (type == 5) {
            packageName.setPagedAnnotationPackage(className);
        } else if (type == 6) {
            packageName.setPagedInfoPackage(className);
        }
        return target;
    }


    /**
     * 消费者执行创建
     */
    private class TestConsumer extends PipeConsumer<TableData> {

        TestConsumer(String id) {
            super(id);
        }

        @Override
        protected void consume(TableData tableData) {
            if (tableData == null) {
                return;
            }
            readTableInfo(tableData);
            if (StringUtil.isEmpty(tableData.getDbKey())) {
                System.out.println("table " + tableData.getTableName() + " there are no auto_increment primary key");
                return;
            }
            for (Ftls ftl : ftls) {
                generatedCode(ftl, tableData);
            }
        }
    }

    /**
     * 生成代码
     */
    private void generatedCode(Ftls ftl, TableData tableData) {
        try {
            getInsertEles(tableData);
            if (StringUtil.isEmpty(tableData.getKeyParam())) {
                System.out.println("table " + tableData.getTableName() + " there are no auto_increment primary key");
                return;
            }
            String fileType = getFileType(ftl.getTpl());
            String fileName = createFile(ftl.getPkg(), tableData.getEntityName(), fileType, ftl.getSuffix());
            Configuration config = new Configuration();
            config.setObjectWrapper(new DefaultObjectWrapper());
            Template formBeanTemplate = config.getTemplate(ConstVal.FILE_ROOT + ftl.getTpl(), "UTF-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            formBeanTemplate.process(tableData, out);
            out.flush();
            out.close();
            System.out.println(fileName + " create success");
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private String getFileType(String template) {
        if (template.startsWith("xml")) {
            return ".xml";
        }
        if (template.startsWith("jq")) {
            return ".jq";
        }
        if (template.startsWith("tpl")) {
            return ".tpl";
        }
        if (template.startsWith("js")) {
            return ".js";
        }
        return ".java";
    }

    private boolean isCabin(String fileType) {
        return ".jq".equals(fileType) || ".tpl".equals(fileType) || ".js".equals(fileType);
    }

    /**
     * 创建文件
     */
    private String createFile(String fileDir, String entityName, String fileType, String suffix) throws IOException {
        if (cabinConfig != null && !StringUtil.isEmpty(cabinConfig.getCabinPackage()) && isCabin(fileType)) {
            fileDir = fileDir + "/" + StringUtil.lowerName(entityName);
        }
        String targetDir = fileDir.replaceAll("\\.", "\\/");
        File file = new File(packageConfig.getOutputDir() + File.separator + targetDir);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("create " + fileDir + " fileDir " + mkdirs);
        }
        String fileName = file.getAbsolutePath() + File.separator + getFileName(fileType, entityName) + suffix + fileType;
        file = new File(fileName);
        if (file.exists()) {
            boolean delete = file.delete();
            System.out.println("delete " + fileName + " file " + delete);
        }
        boolean mkdirs = file.createNewFile();
        System.out.println("create " + fileName + " file " + mkdirs);
        return fileName;
    }

    private String getFileName(String fileType, String entityName) {
        if (".jq".equals(fileType) || ".tpl".equals(fileType)) {
            return StringUtil.lowerName(entityName);
        }
        return entityName;
    }


    /**
     * 读取表格信息
     */
    private void readTableInfo(TableData tableData) {
        String tableName = tableData.getTableName() + tableData.getSharding();
        ResultSet columnResult = null;
        ResultSet pkRSet = null;
        try {
            ColumnData columnData;
            // 获取当前表的列
            columnResult = metaData.getColumns(catalog, "%", tableName, "%");
            //获取主键
            pkRSet = metaData.getPrimaryKeys(catalog, "%", tableName);
            Set<String> pkColumns = new HashSet<>();
            while (pkRSet.next()) {
                pkColumns.add(pkRSet.getObject(4) + "");
            }
            while (columnResult.next()) {
                String columnName = columnResult.getString("COLUMN_NAME");
                boolean pk = pkColumns.contains(columnName);
                String columnType = columnResult.getString("TYPE_NAME");
                String filedName = StringUtil.convertToJava(false, columnName);
                String[] fieldType = getFieldType(columnType);
                if (pk) {
                    tableData.setDbKeyType(fieldType[0])
                            .setDbKey(filedName);
                }

                boolean logicDelete = columnName.equals(generatorConfig.getLogicDelete().getLogicDeleteFieldName());
                if (logicDelete) {
                    tableData.setLogicDelete(generatorConfig.getLogicDelete());
                }
                String columnRemark = columnResult.getString("REMARKS");
                columnData = new ColumnData()
                        .setColumnName(columnName)
                        .setColumnType(getColumnType(columnType))
                        .setColumnRemark(columnRemark)
                        .setFiledName(filedName)
                        .setFiledType(fieldType[0])
                        .setPkColumn(pk)
                        .setLogicDelete(logicDelete);

                if (!StringUtil.isEmpty(generatorConfig.getSuperEntityClass()) &&
                        generatorConfig.getSuperEntityColumns() != null && generatorConfig.getSuperEntityColumns().contains(columnName)) {
                    tableData.addCommonColumn(columnData);
                    tableData.addParamPackageImport(fieldType[1]);
                } else {
                    tableData.addPackageImport(fieldType[1]);
                    tableData.addColumn(columnData);
                    //判断是否添加mybatis-plus的相关注解
                    if (tableData.isPlus()) {
                        if (tableData.isLogogram()) {
                            tableData.addPackageImport(ConstVal.TABLE_NAME_ANNOTATION);
                        }
                        if (logicDelete) {
                            tableData.addPackageImport(ConstVal.TABLE_LOGIC_ANNOTATION);
                        }
                        if (pk) {
                            tableData.addPackageImport(ConstVal.TABLE_ID_ANNOTATION);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeAll(null, columnResult, pkRSet);
        }
    }

    /**
     * 这里是获取mysql与java的类型映射和导入包
     */
    private String[] getFieldType(String columnType) {
        String fieldType = getType(columnType);
        if (fieldType.contains("/")) {
            return fieldType.split("/");
        }
        return new String[]{fieldType, ""};
    }

    /**
     * 这里是获取mysql与java的类型映射
     */
    private String getType(String columnType) {
        String type = columnType.toLowerCase();
        String fieldType = MappingConfig.getJavaMapping(type);
        if (StringUtil.isEmpty(fieldType))
            throw new RuntimeException(columnType + " 没有找到对应的类型,请在mysql-java-mapping.properties中配置");
        return fieldType;
    }

    private String getColumnType(String columnType) {
        String lowerColumnType = columnType.toLowerCase();
        if (lowerColumnType.startsWith("datetime"))
            return "TIMESTAMP";
        if (lowerColumnType.startsWith("int"))
            return "INTEGER";
        if (lowerColumnType.contains("text"))
            return "LONGVARCHAR";
        return columnType;
    }

    /**
     * 表是否需要创建
     */
    private boolean notCreate(String tableName) {
        String[] tableNames = generatorConfig.getWishList();
        if (tableNames == null || tableNames.length == 0) {
            return false;
        }
        for (String name : tableNames) {
            if (name.equals(tableName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拼接创建的相关的sql
     */
    private void getInsertEles(TableData tableData) {
        StringBuilder builder = new StringBuilder();
        StringBuilder vals = new StringBuilder();
        append(tableData, builder, vals);
        setKeyParam(tableData);
        if (!tableData.getColumnDatas().isEmpty()) {
            builder.setLength(builder.length() - 2);
            vals.setLength(vals.length() - ConstVal.APPEND_STR.length());
        }
        tableData.setInsertVals(vals.toString());
        tableData.setInsertEles(builder.toString());
    }

    private void setKeyParam(TableData tableData) {
        ColumnData result = null;
        for (ColumnData columnData : tableData.getCommonDatas()) {
            if (columnData.isPkColumn()) {
                result = columnData;
            }
        }
        if (result == null) {
            for (ColumnData columnData : tableData.getColumnDatas()) {
                if (columnData.isPkColumn()) {
                    result = columnData;
                }
            }
        }
        if (result != null) {
            tableData.setKeyParam(String.format("%s = #{param.%s,jdbcType=%s}", result.getColumnName(), result.getFiledName(), result.getColumnType()));
        }
    }

    private void append(TableData tableData, StringBuilder builder, StringBuilder vals) {
        tableData.getColumnDatas().forEach(columnData -> {
            builder.append(columnData.getColumnName())
                    .append(", ");
            vals.append("#{")//  #{attention,jdbcType=INTEGER},
                    .append(columnData.getFiledName())
                    .append(",jdbcType=")
                    .append(columnData.getColumnType())
                    .append("}")
                    .append(ConstVal.APPEND_STR);
        });

    }

    /**
     * 设置实体类的名字和判断实体类的名字是不是简写
     */
    private void bindEntityName(TableData tableData, String tableName) {
        String entityName = StringUtil.convertToJava(true, tableName);
        String target = entityName.toLowerCase();
        if (!StringUtil.isEmpty(generatorConfig.getPrefix()) && target.startsWith(generatorConfig.getPrefix())) {
            boolean equals = generatorConfig.getPrefix().equals(target);
            tableData.setEntityName(equals ? entityName : entityName.substring(generatorConfig.getPrefix().length()));
            tableData.setLogogram(!equals);
        } else {
            tableData.setEntityName(entityName);
        }
        if (!tableData.isLogogram()) {
            tableData.setLogogram(!StringUtil.isEmpty(generatorConfig.getSuffix()));
        }
        tableData.setEntityNameWithSuffix(tableData.getEntityName() + generatorConfig.getSuffix());
        String query = getNames(tableData, generatorConfig.getSuperQueryParam(), 3);
        String save = getNames(tableData, generatorConfig.getSuperSaveParam(), 4);

        tableData.addParamNames(tableData.getEntityName() + ConstVal.QUERY, query, packageName.getSuperQueryParamPackage());

        tableData.addParamNames(tableData.getEntityName() + ConstVal.SAVE, save, packageName.getSuperSaveParamPackage());

    }


}
