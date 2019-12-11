/**
 *  ${tableRemark}
 */
 <#assign temName= entityName?uncap_first/>
 <#assign apiName= entityName?uncap_first+"Api"/>
define('${cabin.cabinPackage}/pages/${temName}/${temName}', (require) => {
    let ${apiName} = require('${cabin.cabinPackage}/apis/${apiName}');
    const columns = [
    <#list allColumnData as columnData>
            {
                title: '${(columnData.columnRemark !="")?string(columnData.columnRemark,columnData.filedName)}',
                dataIndex: '${columnData.filedName}'
            }<#if columnData_has_next>,</#if>
          </#list>];

    return VuePage({
        source: ['${cabin.cabinPackage}/pages/${temName}/${temName}.tpl'],
        el: '${cabin.cabinPackage}-pages-${temName}',
        pageTitle: '${tableRemark}',
        data() {
            return {
                pagination: ${cabin.globalName}.pagination(),
                tableData: [],
                query:{}
                columns
            };
        },
        mounted() {
           this.queryPageData();
        },
        methods: {
            //分页加载
            queryPageData(paging) {
                ${apiName}.page(this)
                    .paging(this.query, paging)
                    .execute(res => console.log(res))
            },

            //清除搜索条件
            resetSearchData(paging) {
                Object.assign(this.$data.query, this.$options.data().query);
            }
        }
    });
});