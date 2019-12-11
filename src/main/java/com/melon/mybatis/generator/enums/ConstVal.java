package com.melon.mybatis.generator.enums;

/**
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */
public interface ConstVal {
     String FILE_ROOT="src/main/resources/";

    String SERVICE = "Service";
    String SERVICE_IMPL = "ServiceImpl";
    String MAPPER = "Mapper";
    String QUERY = "QueryParam";
    String SAVE = "SaveParam";
    String DTO = "DTO";
    String CONTROLLER = "Controller";
    String APPEND_STR = ",\n                ";

    String TABLE_NAME_ANNOTATION = "import com.baomidou.mybatisplus.annotation.TableName;";
    String TABLE_ID_ANNOTATION = "import com.baomidou.mybatisplus.annotation.TableId;";
    String TABLE_LOGIC_ANNOTATION = "import com.baomidou.mybatisplus.annotation.TableLogic;";
}
