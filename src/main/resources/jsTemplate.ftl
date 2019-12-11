/**
 *  ${tableRemark}Api集合
 */
define('${cabin.cabinPackage}/apis/${cabin.cabinPackage}Api', function (require, exports, module) {
    var SlimAjax = require("${cabin.cabinPackage}/common/js/SlimAjax");
    <#assign apiName= entityName?uncap_first+"Api"/>
    var ${apiName} = {};
    var URL_PREFIX='${requestMapping}';//前缀

    ${apiName}.page = function (vue) {
        return new SlimAjax(vue)
            .get(URL_PREFIX);
    };

    ${apiName}.detail = function (vue) {
        return new SlimAjax(vue)
            .get(URL_PREFIX);
    };

    ${apiName}.save = function (vue) {
        return new SlimAjax(vue)
          .post(URL_PREFIX);
    };

    ${apiName}.dele = function (vue) {
        return new SlimAjax(vue)
          .dele(URL_PREFIX);
    };

    module.exports = ${apiName};
});
