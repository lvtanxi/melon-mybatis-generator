<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packages.mapperPackage}.${entityName}Mapper">

    <resultMap id="BaseResultMap" type="${packages.entityPackage}.${entityNameWithSuffix}">
        <#list allColumnData as columnData>
        <#if columnData.pkColumn>
        <id column="${columnData.columnName}" jdbcType="${columnData.columnType}" property="${columnData.filedName}"/>
        <#else>
        <result column="${columnData.columnName}" jdbcType="${columnData.columnType}" property="${columnData.filedName}"/>
        </#if>
       </#list>
    </resultMap>

    <sql id="Base_Column_List">
     <#list allColumnData as columnData>${columnData.columnName}<#if columnData_has_next>,</#if></#list>
    </sql>

<#if !plus>
    <sql id="Builder_Where_Condition">
        <where>
            <if test="spwb != null and spwb.hasCondition">
                ${conditionSql}
            </if>
        </where>
    </sql>

    <sql id="Builder_Aggregation">
        <if test="spwb != null and spwb.hasAggregation">
            ${aggregationSql}
        </if>
    </sql>
</#if>

<#if !plus>
    <insert id="insert" useGeneratedKeys="true" keyProperty="${dbKey}" keyColumn="${dbKey}">
        INSERT INTO ${tableName} (${insertEles})
        VALUES (${insertVals})
    </insert>

    <select id="findBy${dbKey?cap_first}" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${tableName}
        WHERE ${keyParam}
    </select>

    <select id="findByBuilder" resultMap="BaseResultMap">
        SELECT
        <choose>
            <when test="spwb != null and spwb.hasColumns">
                ${columns}
            </when>
            <otherwise>
                <include refid="Base_Column_List"/>
            </otherwise>
        </choose>
        FROM ${tableName}
        <include refid="Builder_Where_Condition"/>
        <include refid="Builder_Aggregation"/>
    </select>

    <update id="updateByBuilder">
        UPDATE ${tableName}
        <trim prefix="SET" suffixOverrides=",">
            <if test="spwb != null and spwb.hasUpdate">
                ${updateSql}
            </if>
        </trim>
        <include refid="Builder_Where_Condition"/>
    </update>

<#if logicDelete??>
    <delete id="deleteBy${dbKey?cap_first}">
        UPDATE ${tableName}
        SET ${logicDelete.logicDeleteFieldName} = ${logicDelete.freeze}
        WHERE ${keyParam?replace("param.","")}
    </delete>
<#else>
    <delete id="deleteBy${dbKey?cap_first}">
        DELETE
        FROM ${tableName}
        WHERE ${keyParam?replace("param.","")}
    </delete>
</#if>
</#if>

</mapper>