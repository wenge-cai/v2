<template>
  <div class="list">
    <slot name="top" />
    <template v-if="source.filter">
      <div v-if="source.filter.items" class="filter-container" v-show="showSearch">
        <template v-for="(item, index) in source.filter.items">
          <template v-if="handleExist(item.exist)">
            <div v-if="item.slot" :key="index" class="filter-item">
              <slot :name="item.slot" :data="filterData" :item="item" />
            </div>
            <el-date-picker
              v-else-if="item.type === 'date' || item.type === 'datetime'"
              :key="index"
              v-model="filterData[item.field]"
              class="filter-item range"
              size="mini"
              type="daterange"
              range-separator="至"
              value-format="yyyy-MM-dd HH:mm:ss"
              :start-placeholder="words(item.label + '（开始）')"
              :end-placeholder="words(item.label + '（结束）')"
            />
            <el-select v-else-if="item.type === 'switch'" :key="index" v-model="filterData[item.field]" :placeholder="words(item.label)" class="filter-item" size="mini" clearable>
              <el-option :value="null" label="" />
              <el-option :value="true" :label="words('是')" />
              <el-option :value="false" :label="words('否')" />
            </el-select>
            <el-select v-else-if="item.options" :key="index" v-model="filterData[item.field]" :placeholder="words(item.label)" class="filter-item" size="mini" clearable>
              <el-option :value="null" label="" />
              <el-option v-for="(option, key) in item.options" :key="key" :value="option.value" :label="words(option.label)" />
            </el-select>
            <el-input v-else :key="index" v-model="filterData[item.field]" :placeholder="words(item.label)" class="filter-item" size="mini" />
          </template>
        </template>
        <el-button class="filter-item" type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ words('搜索') }}
        </el-button>
        <el-button class="filter-item" icon="el-icon-refresh" size="mini" @click="handleReset">
          {{ words('重置') }}
        </el-button>
      </div>
    </template>
    <slot name="filter" />
    <template v-if="source">
      <div v-if="!source.open && source.action" class="filter-container">
        <el-row :gutter="10" class="mb8">
          <el-col v-for="(action, index) in source.action" :key="index" :span="1.5">
            <template v-if="handleExist(action.exist)">
              <slot v-if="action.slot" v-permission="action.permission" :name="action.slot" />
              <el-button v-else v-permission="action.permission" :type="action.type" :icon="action.icon" size="mini" @click="action.click(listData)">
                {{ words(action.label) }}
              </el-button>
            </template>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="handleQuery" />
        </el-row>
      </div>
      <div v-if="source.note" class="filter-container note">{{ source.note }}</div>
      <slot name="header" />
      <el-table
        ref="list"
        v-loading="loading"
        :data="listData"
        fit
        highlight-current-row
        row-key="id"
        :max-height="source.height"
        :tree-props="source.tree"
        @sort-change="handleSort"
        @selection-change="handleSelection"
      >
        <el-table-column v-if="source.expand" type="expand">
          <template slot-scope="scope">
            <slot :name="source.expand" :data="scope.row" />
          </template>
        </el-table-column>
        <el-table-column v-if="source.selection" type="selection" width="50px" align="center" />
        <el-table-column v-if="source.number" :label="words('序号')" width="50px" align="center">
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <template v-for="(item, index) in source.items">
          <el-table-column v-if="handleExist(item.exist)" :key="index" :label="words(item.label)" :width="item.width || 'auto'" :align="item.align || 'center'" :show-overflow-tooltip="item.tooltip">
            <template slot-scope="scope">
              <slot v-if="item.slot" :name="item.slot" :data="scope.row" :index="scope.$index" />
              <template v-else-if="item.type === 'photo'">
                <el-image v-if="scope.row[item.field]" :src="FILE_SERVICE_STATIC_URL + scope.row[item.field]" :preview-src-list="[FILE_SERVICE_STATIC_URL + scope.row[item.field]]" fit="contain" style="height: 36px;" />
              </template>
              <el-switch
                v-else-if="item.type === 'switch'"
                v-model="scope.row[item.field]"
                :disabled="!item.change"
                :active-value="item.active"
                :inactive-value="item.inactive"
                @change="() => {
                  if (item.change) {
                    item.change(scope.row)
                  }
                }"
              />
              <el-button v-else-if="item.button" :type="item.button.type" size="mini" @click="item.button.click(scope.row, scope.$index)">
                {{ scope.row[item.field] }}
              </el-button>
              <template v-else-if="item.options">
                <template v-if="Array.isArray(item.options)">
                  <template v-for="(option, key) in item.options">
                    <template v-if="option.value == scope.row[item.field]">
                      <span :key="key">{{ option.label }}</span>
                    </template>
                  </template>
                </template>
                <span v-else>{{ item.options[scope.row[item.field]] }}</span>
              </template>
              <span v-else-if="item.range">{{ scope.row[item.field + 'Start'] ? (scope.row[item.field] = [scope.row[item.field + 'Start'], scope.row[item.field + 'End']]).join(' - ') : item.hold || '全部' }}</span>
              <span v-else-if="item.value">{{ item.value(scope.row) }}</span>
              <span v-else>{{ scope.row[item.field] }}</span>
            </template>
          </el-table-column>
        </template>
        <el-table-column v-if="source.operation" :label="words('操作')" align="center" min-width="150px" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <template v-for="(operation, index) in source.operation">
              <template v-if="operation.exist === undefined || operation.exist(scope.row, scope.$index)">
                <slot v-if="operation.slot" :name="operation.slot" :data="scope.row" :index="$index" />
                <el-button v-else :key="index" v-permission="operation.permission" :icon="operation.icon" type="text" size="mini" @click="operation.click(scope.row, scope.$index)">
                  {{ words(operation.label) }}
                </el-button>
              </template>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <slot name="footer" />
      <pagination v-if="source.page" v-show="source.total > 0" :total="source.total" :page.sync="source.current" :limit.sync="source.size" :page-sizes="source.sizes" @pagination="handleQuery" />
    </template>
    <slot name="bottom" />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  props: {
    source: {
      required: true,
      type: Object
    },
    data: {
      required: false,
      type: Array,
      default: null
    }
  },
  data() {
    let listData = this.source.data || this.data || []
    if (!Array.isArray(listData)) {
      listData = []
    }
    if (this.source.page) {
      this.source.current = this.source.current || 1
      this.source.size = this.source.size || 10
      this.source.sizes = this.source.sizes || [10, 20, 50, 100, 200]
      this.source.total = listData.length
    }
    if (this.source.tree === true) {
      this.source.tree = {
        children: 'children'
      }
    }
    this.source.param = this.source.param || {}
    this.source.action = this.source.action || {}
    return {
      showSearch: true,
      loading: false,
      filterData: this.clone(this.source.param),
      listData
    }
  },
  created() {
    this.source.reload = object => {
      if (object) {
        if (Array.isArray(object)) {
          this.listData = object
        } else if (typeof object === 'function') {
          object(this.filterData)
          this.handleQuery()
        } else if (typeof object === 'object') {
          this.filterData = object
          this.handleQuery()
        } else {
          this.handleReset()
        }
      } else {
        this.handleQuery()
      }
    }
    this.handleQuery()
    this.source.ref = () => this.$refs.list
  },
  methods: {
    handleExist(exist) {
      if (exist) {
        return exist(this.filterData, this.listData, this.source)
      }
      return true
    },
    handleSort(column, prop, order) {
      if (this.source.sort) {
        this.source.sort(column, prop, order)
      }
    },
    handleQuery() {
      if (this.source.query) {
        this.filterData.pageNum = this.source.current
        this.filterData.pageSize = this.source.size
        this.loading = true
        this.source.query(this.filterData).then(({ code, rows = [], total = 0 }) => {
          if (this.ok(code)) {
            this.source.total = total
            if (this.source.process) {
              this.source.process(data)
            }
            this.listData = this.source.data = rows
          }
          this.loading = false
        }).catch(e => {
          this.loading = false
        })
        // if (this.source.page) {
        //   this.source.query(this.source.current, this.source.size, this.filterData).then(({ code, data }) => {
        //     if (data && this.ok(code)) {
        //       this.source.total = data.total
        //       if (this.source.process) {
        //         this.source.process(data.rows)
        //       }
        //       this.listData = this.source.data = data.rows
        //     }
        //   })
        // } else {
        //   this.source.query(this.filterData).then(({ code, data }) => {
        //     if (data && this.ok(code)) {
        //       this.source.total = data.length
        //       if (this.source.process) {
        //         this.source.process(data)
        //       }
        //       this.listData = this.source.data = data
        //     }
        //   })
        // }
      } else {
        this.listData = this.source.data || this.data || []
      }
    },
    handleReset() {
      this.source.current = 1
      this.filterData = this.clone(this.source.param)
      this.handleQuery()
    },
    handleSelection(selection) {
      if (typeof this.source.selection === 'function') {
        this.source.selection(selection)
      } else {
        this.source.selection = selection
      }
    }
  }
}
</script>

<style>
  .el-table {
    overflow: auto;
  }
  .list .filter-item {
    max-width: 200px;
    margin-right: 10px;
    margin-left: 0px;
  }
  .list .filter-item.range {
    max-width: 320px;
  }
  .list .list-action {
    margin-right: 10px;
    margin-left: 0px;
  }
  .list .column-image {
    height: 36px;
  }
  .note {
    color: rgba(75, 86, 97, 0.5);
  }
</style>
