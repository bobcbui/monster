<template>
  <table class="head-table" border="1" cellspacing="1">
    <tr align="center">
      <td width=100px>消息</td>
      <td width=100px>通讯录</td>
      <td width=100px>添加好友</td>
      <td width=100px>个人中心</td>
    </tr>
  </table>
  <router-view/>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
     
    };
  }
};
</script>

<style scoped>
.head-table{
  margin: auto;
  width: 100%;
  max-width: 800px;
}
</style>