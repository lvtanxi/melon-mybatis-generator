package com.melon.mybatis.generator.config;

import com.melon.mybatis.generator.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 文件配置
 *
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */
@Getter
@Setter
@Accessors(chain = true)
public class PackageConfig {

    /**
     * 生成文件的输出目录【默认 D 盘根目录】
     */
    private String outputDir = "D://";

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "com.melon";
    /**
     * 父包模块名
     */
    private String moduleName = null;
    /**
     * Entity包名
     */
    private String entity = "entity";
    /**
     * Service包名
     */
    private String service = "service";
    /**
     * Service Impl包名
     */
    private String serviceImpl = "service.impl";
    /**
     * Mapper包名
     */
    private String mapper = "mapper";

    /**
     * param包名
     */
    private String param = "param";

    /**
     * param包名
     */
    private String dto = "dto";

    /**
     * Mapper XML包名
     */
    private String xml = "mapper.xml";
    /**
     * Controller包名
     */
    private String controller = "web.controller";

    /**
     * 父包名
     */
    public String getParent() {
        if (StringUtil.isEmpty(moduleName)) {
            return parent + StringUtil.DOT;
        }
        return parent + StringUtil.DOT + moduleName + StringUtil.DOT;
    }
}
