<template>
  <el-dialog
    width="40%"
    top="10vh"
    :center="true"
    :destroy-on-close="true"
    :modal-append-to-body="false"
    :close-on-click-modal="false"
    :title="$t(source.title)"
    :visible.sync="treeVisible"
    :before-close="source.close"
  >
    <el-tree
      ref="tree"
      :node-key="source.key"
      :props="{
        children: source.children,
        label: source.field
      }"
      show-checkbox
      default-expand-all
      :data="treeData"
      :default-checked-keys="source.keys"
    />
    <div slot="footer" class="dialog-footer">
      <el-button @click="source.close()">{{ $t('关闭') }}</el-button>
      <el-button @click="source.clear()">{{ $t('清空') }}</el-button>
      <el-button type="primary" @click="source.check()">{{ $t('全选') }}</el-button>
      <el-button type="primary" @click="source.handle.save()">{{ $t('提交') }}</el-button>
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
    this.source.key = this.source.key || 'id'
    this.source.children = this.source.children || 'children'
    this.source.handle = this.source.handle || {}

    if (!this.source.handle.list) {
      this.source.handle.list = data => {
        this.source.action.list(data).then(({ code, data }) => {
          if (this.ok(code)) {
            this.treeData = data
          }
        })
      }
    }
    if (!this.source.handle.check) {
      this.source.handle.check = id => {
        this.source.action.check(id).then(({ code, data }) => {
          if (this.ok(code)) {
            this.treeKeys = data
            if (this.$refs.tree) {
              this.$refs.tree.setCheckedKeys(this.treeKeys)
            }
          }
        })
      }
    }
    if (!this.source.handle.save) {
      this.source.handle.save = (id, keys) => {
        id = id || this.treeId
        keys = keys || this.$refs.tree.getCheckedKeys()
        this.source.action.save(id, keys).then(({ code, data }) => {
          if (this.ok(code)) {
            this.$message.success(this.$t('保存成功'))
            this.source.close()
          }
        })
      }
    }

    if (!this.source.close) {
      this.source.close = () => {
        this.treeKeys = []
        this.treeVisible = false
      }
    }
    if (!this.source.open) {
      this.source.open = (title, id) => {
        this.source.title = title
        this.treeId = id
        this.source.handle.check(id)
        this.treeVisible = true
      }
    }
    if (!this.source.data) {
      this.source.data = data => {
        if (data) {
          this.treeData = data
        } else {
          return this.treeData
        }
      }
    }
    this.source.clear = () => {
      this.$refs.tree.setCheckedKeys([])
    }
    this.source.check = (keys) => {
      if (keys) {
        if (this.$refs.tree) {
          this.$refs.tree.setCheckedKeys(keys)
        }
      } else {
        this.$refs.tree.setCheckedNodes(this.treeData)
      }
    }
    return {
      treeVisible: false,
      treeKeys: [],
      treeId: null,
      treeData: []
    }
  },
  created() {
    this.source.handle.list()
  }
}
</script>
