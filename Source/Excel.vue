<template>
  <div>
    <input ref="excel-upload-input" class="excel-upload-input" type="file" accept=".xlsx, .xls" @change="handleChange">
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
        <el-button type="primary" @click="handleUpload">
          {{ $t('上传') }}
        </el-button>
        <template v-for="(action, index) in source.action">
          <el-button v-if="handleExist(action.exist)" :key="index" :type="action.type || 'primary'" @click="action.click(excelData, excelHeader)">
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
      <el-table :data="excelData" border highlight-current-row style="width: 100%;">
        <el-table-column type="index" max-width="50" />
        <el-table-column
          v-for="(item, index) of excelHeader"
          :key="index"
          :prop="excelColumn && excelColumn[item] ? excelColumn[item].field : item"
          :label="item"
          show-overflow-tooltip
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import XLSX from 'xlsx'

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
      this.source.open = (title, header, column) => {
        this.source.title = title
        this.layerTitle = title
        this.excelHeader = this.excelHeader || header
        this.excelColumn = this.excelColumn || column
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
    if (!this.source.header) {
      if (this.source.column) {
        this.source.header = Object.keys(this.source.column)
      } else {
        this.source.header = ['A', 'B', 'C', 'D', 'E', 'F']
      }
    }
    if (!this.source.data) {
      this.source.data = []
    }
    console.log(this.source.header)
    return {
      layerVisible: this.source.visible,
      layerFullscreen: this.source.fullscreen,
      layerTop: this.source.top,
      layerWidth: this.source.width,
      layerTitle: this.source.title,
      excelHeader: this.source.header,
      excelColumn: this.source.column,
      excelData: this.source.data
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
    handleUpload() {
      this.$refs['excel-upload-input'].click()
    },
    handleChange(event) {
      if (!event) {
        this.excelHeader = this.source.header
        this.excelData = null
        return
      }
      const files = event.target.files
      const file = files[0]
      if (!file) {
        return
      }
      this.$refs['excel-upload-input'].value = null
      this.readerData(file)
    },
    readerData(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.onload = event => {
          const data = event.target.result
          const workbook = XLSX.read(data, { type: 'array' })
          const firstSheetName = workbook.SheetNames[0]
          const worksheet = workbook.Sheets[firstSheetName]
          this.source.header = this.excelHeader = this.getHeaderRow(worksheet)
          let options
          if (this.excelColumn) {
            options = {
              header: [],
              raw: true
            }
            for (let index = 0; index < this.excelHeader.length; index++) {
              const item = this.excelHeader[index]
              options.header.push(this.excelColumn[item].field)
            }
          }
          this.source.data = XLSX.utils.sheet_to_json(worksheet, options)
          if (options) {
            this.source.data.splice(0, 1)
          }
          this.excelData = this.source.data
          resolve()
        }
        reader.readAsArrayBuffer(file)
      })
    },
    getHeaderRow(sheet) {
      const headers = []
      const range = XLSX.utils.decode_range(sheet['!ref'])
      let C
      const R = range.s.r
      /* start in the first row */
      for (C = range.s.c; C <= range.e.c; ++C) { /* walk every column in the range */
        const cell = sheet[XLSX.utils.encode_cell({ c: C, r: R })]
        /* find the cell in the first row */
        let hdr = 'UNKNOWN ' + C // <-- replace with your desired default
        if (cell && cell.t) hdr = XLSX.utils.format_cell(cell)
        headers.push(hdr)
      }
      return headers
    }
  }
}
</script>

<style scoped>
.excel-upload-input{
  display: none;
  z-index: -9999;
}
</style>
