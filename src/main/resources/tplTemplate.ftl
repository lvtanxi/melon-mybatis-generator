<div class="${cabin.cabinPackage}-pages-${entityName?uncap_first} content-container slm-page" id="${cabin.cabinPackage}-pages-${entityName?uncap_first}">
    <div class="container-scroll">
        <!--search部分-->
        <section class="layout-pad-full layout-pad-top-nil layout-margin-bottom back-white">
            <d-form class="cabinvue-search-form">
                <d-row :gutter="24">
                    <d-col :xl="6" :lg="8" :md="8" :sm="12" :xs="24">
                        <div class="search-form-item">
                            <div class="item-label">
                                <span>姓名</span>
                            </div>
                            <div class="item-control">
                                <d-input placeholder="规则名称"></d-input>
                            </div>
                        </div>
                    </d-col>
                </d-row>
                <hr/>
                <div class="form-option-area">
                    <d-button type="primary" class="space-margin-right" @change="queryPageData">查询</d-button>
                    <d-button @click="resetSearchData">重置</d-button>
                </div>
            </d-form>
        </section>
        <!--search部分 end-->
        <!--表格部分-->
        <section class="layout-pad-full layout-margin-bottom back-white">
            <d-table :data-source="tableData"
                        :pagination="pagination"
                        :columns="columns"
                        :row-key="record => record.id"
                        @change="queryPageData">
                 <span slot="action" slot-scope="text, record">
                     <a href="javascript:;">Detail</a>
                     <d-divider type="vertical"/>
                     <a href="javascript:;">Delete</a>
                  </span>
            </d-table>
        </section>
        <!--表格部分 end-->
    </div>
</div>
