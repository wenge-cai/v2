<template>
  <div>
    <el-dialog
      :visible.sync="layerVisible"
      :width="layerWidth"
      :top="layerTop"
      :fullscreen="layerFullscreen"
      center
      append-to-body
      destroy-on-close
      :close-on-click-modal="false"
      :title="$t(layerTitle)"
      :before-close="handleClose"
    >
      <div class="filter-container">
        <el-button v-if="!source.auto" type="primary" @click="handleParse">
          {{ $t('解析') }}
        </el-button>
        <template v-for="(action, index) in source.action">
          <el-button v-if="handleExist(action.exist)" :key="index" :type="action.type || 'primary'" @click="action.click(jsonData, jsonHeader)">
            {{ $t(action.label) }}
          </el-button>
        </template>
        <el-button @click="handleChange()">
          {{ $t('清空') }}
        </el-button>
        <el-button @click="handleClose">
          {{ $t('关闭') }}
        </el-button>
      </div>
      <div class="filter-container">
        <el-input v-model="jsonString" type="textarea" :placeholder="$t('请输入数据')" @input="handleChange" />
      </div>
      <el-table :data="jsonData" border highlight-current-row style="width: 100%;">
        <el-table-column type="index" max-width="50" />
        <el-table-column
          v-for="(item, index) of jsonItems"
          :key="index"
          :prop="item.field"
          :label="item.label"
          show-overflow-tooltip
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>

export default {
  props: {
    source: {
      required: true,
      type: Object
    }
  },
  data() {
    if (!this.source.close) {
      this.source.close = () => {
        if (this.source.before) {
          this.source.before()
        }
        this.layerVisible = false
      }
    }
    if (!this.source.open) {
      this.source.open = (title) => {
        this.source.title = title
        this.layerTitle = title
        this.layerVisible = true
      }
    }
    if (!this.source.parse) {
      this.source.parse = data => {
        return data
      }
    }
    if (!this.source.visible) {
      this.source.visible = false
    }
    if (this.source.fullscreen !== false) {
      this.source.fullscreen = true
    }
    if (!this.source.top) {
      this.source.top = '2vh'
    }
    if (!this.source.width) {
      this.source.width = '60%'
    }
    if (!this.source.data) {
      this.source.data = []
    }
    return {
      layerVisible: this.source.visible,
      layerFullscreen: this.source.fullscreen,
      layerTop: this.source.top,
      layerWidth: this.source.width,
      layerTitle: this.source.title,
      jsonString: '',
      jsonItems: this.source.items,
      jsonData: this.source.data
    }
  },
  methods: {
    handleExist(exist) {
      if (exist) {
        return exist(this.source.data, this.source)
      }
      return true
    },
    handleClose() {
      this.source.close()
    },
    handleParse() {
      if (this.jsonString) {
        this.jsonData = this.source.parse(this.$json(this.jsonString))
      } else {
        this.jsonData = []
      }
    },
    handleChange() {
      if (this.source.auto) {
        this.handleParse()
      }
    }
  }
}
</script>
