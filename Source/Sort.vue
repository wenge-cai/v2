<template>
  <el-dialog
    width="40%"
    top="10vh"
    center
    destroy-on-close
    append-to-body
    :close-on-click-modal="false"
    :title="sortTitle"
    :visible.sync="sortVisible"
    :before-close="handleClose"
  >
    <vuedraggable v-model="sortData" ghost-class="drag-sort-ghost">
      <transition-group>
        <div v-for="(data, index) in sortData" :key="index" class="drag-sort">
          <p>{{ data[source.field] }}</p>
        </div>
      </transition-group>
    </vuedraggable>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('取消') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('提交') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import vuedraggable from 'vuedraggable'

export default {
  components: { vuedraggable },
  props: {
    source: {
      required: true,
      type: Object
    }
  },
  data() {
    this.source.field = this.source.field || 'id'
    if (!this.source.close) {
      this.source.close = () => {
        this.sortVisible = false
      }
    }
    if (!this.source.open) {
      this.source.open = (title, data) => {
        this.sortTitle = this.$t(title)
        this.sortData = data
        this.sortVisible = true
      }
    }
    if (!this.source.data) {
      this.source.data = []
    }
    return {
      sortVisible: false,
      sortTitle: this.$t('排序'),
      sortData: this.source.data
    }
  },
  methods: {
    handleSubmit() {
      if (this.source.submit) {
        this.source.submit(this.sortData).then(({code}) => {
          if (this.ok(code)) {
            this.$message.success('提交成功')
            this.handleClose()
          }
        })
      } else {
        this.$log('sort:submit方法未设置')
      }
    },
    handleClose() {
      this.source.close(this.sortData)
    }
  }
}
</script>
