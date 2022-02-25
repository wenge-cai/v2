<template>
  <div v-if="source.items" class="filter-container">
    <template v-for="(item, index) in source.items">
      <template v-if="handleExist(item.exist)">
        <el-date-picker
          v-if="item.type === 'date' || item.type === 'datetime'"
          :key="index"
          v-model="filterData[item.field]"
          class="filter-item range"
          type="daterange"
          range-separator="至"
          value-format="yyyy-MM-dd HH:mm:ss"
          :start-placeholder="$t(item.label + '（开始）')"
          :end-placeholder="$t(item.label + '（结束）')"
        />
        <el-select v-else-if="item.type === 'switch'" :key="index" v-model="filterData[item.field]" :placeholder="$t(item.label)" class="filter-item" clearable>
          <el-option :value="null" label="" />
          <el-option :value="true" :label="$t('是')" />
          <el-option :value="false" :label="$t('否')" />
        </el-select>
        <el-select v-else-if="item.options" :key="index" v-model="filterData[item.field]" :placeholder="$t(item.label)" class="filter-item" clearable>
          <el-option :value="null" label="" />
          <el-option v-for="(value, key) in item.options" :key="key" :value="key" :label="$t(value)" />
        </el-select>
        <el-input v-else :key="index" v-model="filterData[item.field]" :placeholder="$t(item.label)" class="filter-item" />
      </template>
    </template>
    <el-button v-if="source.query != false" class="filter-item" type="primary" icon="el-icon-search" @click="handleQuery">
      {{ $t('搜索') }}
    </el-button>
    <el-button v-if="source.reset != false" class="filter-item" icon="el-icon-refresh" @click="handleReset">
      {{ $t('重置') }}
    </el-button>
  </div>
</template>

<script>

export default {
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
    this.source.data = this.source.data || {}
    this.source.event = {
      query: () => {
        this.handleQuery()
      },
      reset: () => {
        this.handleReset()
      }
    }
    this.source.result = () => {
      return this.filterData
    }
    return {
      filterData: this.clone(this.source.data)
    }
  },
  methods: {
    handleExist(exist) {
      if (exist) {
        return exist(this.filterData, this.source)
      }
      return true
    },
    handleQuery() {
      if (this.source.query) {
        this.source.query(this.filterData)
      }
    },
    handleReset() {
      if (this.source.reset) {
        this.source.reset(this.filterData)
      }
      this.filterData = this.clone(this.source.data)
      this.handleQuery(this.filterData)
    }
  }
}
</script>

<style>
  .filter-item {
    max-width: 200px;
    margin-right: 10px;
    margin-left: 0px;
  }
  .filter-item.range {
    max-width: 320px;
  }
</style>
