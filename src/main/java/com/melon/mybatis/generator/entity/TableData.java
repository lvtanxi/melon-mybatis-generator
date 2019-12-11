package com.melon.mybatis.generator.entity;

import com.melon.mybatis.generator.config.CabinConfig;
import com.melon.mybatis.generator.util.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Accessors(chain = true)
public class TableData {
    /**
     * 包
     */
    private PackageName packages;
    /**
     * 包
     */
    private String author;

    /**
     * 包
     */
    private CabinConfig cabin;
    /**
     * 超类
     */
    private String superEntityClass;
    /**
     * 超类
     */
    private String superDtoClass;

    /**
     * 超类
     */
    private String dtoUtils;

    /**
     * 表名字
     */
    private String tableName;

    /**
     * 实体
     */
    private String entityName;

    /**
     * 实体
     */
    private String entityNameWithSuffix;

    /**
     * 表备注
     */
    private String tableRemark;
    /**
     * 表列
     */
    @Setter(AccessLevel.NONE)
    private List<ColumnData> columnDatas = new ArrayList<>();

    /**
     * 公共列
     */
    @Setter(AccessLevel.NONE)
    private List<ColumnData> commonDatas = new ArrayList<>();

    /**
     * 实体类需要引用的类
     */
    @Setter(AccessLevel.NONE)
    private Set<String> packageImports = new HashSet<>();

    /**
     * 实体类需要引用的类
     */
    @Setter(AccessLevel.NONE)
    private Set<String> paramImports = new HashSet<>();
    /**
     * 实体类需要引用的类
     */
    private Set<String> superDtoColumns = new HashSet<>();

    /**
     * 是否使用Lombok
     */
    private boolean useLombok;

    /**
     * 主键名称
     */
    private String dbKey;

    /**
     * 主键Type
     */
    private String dbKeyType;

    /**
     * 插入sql
     */
    private String insertVals;
    /**
     * 插入sql
     */
    private String insertEles;

    /**
     * 主键句子
     */
    private String keyParam;

    /**
     * 主键句子
     */
    private String sharding = "";


    /**
     * 表名和实体类的名字不一样
     */
    private boolean logogram;

    /**
     * MybatisPlus
     */
    private boolean plus;

    /**
     * InjectionType
     */
    private boolean auto;

    /**
     * InjectionType
     */
    private String injection;

    /**
     * 自定义继承的Mapper全路径
     */
    private String superMapperFile;

    /**
     * Controller的请求路径
     */
    private String requestMapping;

    /**
     * sql常量
     */
    private String conditionSql = "${spwb.conditionSql}";
    /**
     * 列常量
     */
    private String columns = "${spwb.columns}";

    /**
     * 更新
     */
    private String updateSql = "${spwb.updateSql}";

    private String aggregationSql = "${spwb.aggregationSql}";

    /**
     * 逻辑删除
     */
    private LogicDelete logicDelete;

    private PagedName pagedName;

    @Setter(AccessLevel.NONE)
    private List<ParamName> paramNames = new ArrayList<>();

    private AtomicInteger atomic = new AtomicInteger(0);


    public void addColumn(ColumnData columnData) {
        columnDatas.add(columnData);
    }

    public void addCommonColumn(ColumnData columnData) {
        commonDatas.add(columnData);
    }

    public void addPackageImport(String packageImport) {
        if (!StringUtil.isEmpty(packageImport)) {
            packageImports.add(packageImport);
            addParamPackageImport(packageImport);
        }
    }

    public void addParamPackageImport(String packageImport) {
        if (!StringUtil.isEmpty(packageImport) && !packageImport.startsWith("import java.io.Serializable")) {
            paramImports.add(packageImport);
        }
    }

    public void addParamNames(String name, String superName, String pkg) {
        paramNames.add(new ParamName(name, superName, pkg));
    }


    public List<ColumnData> getAllColumnData() {
        List<ColumnData> result = new ArrayList<>(commonDatas.size() + columnDatas.size());
        result.addAll(commonDatas);
        result.addAll(columnDatas);
        return result;
    }

    public List<ColumnData> getDtoColumnData() {
        List<ColumnData> result = new ArrayList<>();
        for (ColumnData commonData : commonDatas) {
            if (superDtoColumns == null || !superDtoColumns.contains(commonData.getColumnName())) {
                result.add(commonData);
            }
        }
        for (ColumnData commonData : columnDatas) {
            if (superDtoColumns == null || !superDtoColumns.contains(commonData.getColumnName())) {
                result.add(commonData);
            }
        }
        return result;
    }

    public String getLogicDeleteFiled() {
        for (ColumnData data : commonDatas) {
            if (data.isLogicDelete()) {
                return data.getFiledName();
            }
        }
        for (ColumnData data : columnDatas) {
            if (data.isLogicDelete()) {
                return data.getFiledName();
            }
        }
        return "";
    }

    public ParamName getParamName() {
        System.out.println(paramNames.get(atomic.get()));
        return paramNames.get(atomic.getAndIncrement());
    }
}
