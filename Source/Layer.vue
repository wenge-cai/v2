<template>
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
    <slot v-if="layerVisible" />
    <div v-if="source.footer != false" slot="footer" class="dialog-footer">
      <template v-for="(action, index) in source.action">
        <el-button v-if="handleExist(action.exist)" :key="index" :type="action.type || 'primary'" @click="action.click(source.data, layerData, source)">
          {{ $t(action.label) }}
        </el-button>
      </template>
      <el-button @click="handleClose">
        {{ $t('关闭') }}
      </el-button>
    </div>
  </el-dialog>
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
    this.source.data = this.source.data || {}
    if (!this.source.close) {
      this.source.close = () => {
        if (this.source.before) {
          this.source.before()
        }
        this.layerVisible = false
      }
    }
    if (!this.source.open) {
      this.source.open = (title, data = {}) => {
        this.source.data = data
        this.source.title = title
        this.layerData = data
        this.layerTitle = title
        this.layerVisible = true
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
    return {
      layerVisible: this.source.visible,
      layerFullscreen: this.source.fullscreen,
      layerTop: this.source.top,
      layerWidth: this.source.width,
      layerTitle: this.source.title,
      layerData: this.source.data
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
    }
  }
}
</script>
