<template>
  <div>
    <el-button size="mini" @click="iconVisible = true">
      <i :class="source.data[source.field]" :style="{ fontSize: '2em' }">
        <template  v-if="!source.data[source.field]">...</template>
      </i>
    </el-button>
    <el-dialog :title="$t('选择图标')" top="10vh" width="80%" :append-to-body="true" :close-on-click-modal="false" :visible.sync="iconVisible">
      <ul class="icon-list">
        <li v-for="(icon, index) in iconData" :key="index" @click="handleSelect(icon)"><i :class="icon" /></li>
      </ul>
    </el-dialog>
  </div>
</template>

<script>
import iconList from './store/icon-list'

export default {
  props: {
    source: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      iconVisible: false,
      iconData: iconList
    }
  },
  methods: {
    handleSelect(value) {
      if (this.source.change) {
        this.source.change(value, this.source)
      } else {
        this.source.data[this.source.field] = value
      }
      this.iconVisible = false
    }
  }
}
</script>

<style>
  .icon-list {
    overflow: hidden;
    list-style: none;
    padding: 0!important;
    border-radius: 4px;
  }
  .icon-list li {
    float: left;
    width: 5%;
    text-align: center;
    height: 60px;
    line-height: 60px;
    font-size: 13px;
    color: #99a9bf;
    cursor: pointer;
  }
  .icon-list li:hover {
    color: #316fc7;
  }
  .icon-list li i {
    line-height: normal;
    font-size: 2em;
    font-family: Helvetica Neue,Helvetica,PingFang SC,Hiragino Sans GB,Microsoft YaHei,SimSun,sans-serif;
    transition: color .15s linear;
  }
</style>
