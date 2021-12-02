<template>
  <div class="head-table">
    <router-link style="padding:0 5px" to="/">消息</router-link>
    <router-link style="padding:0 5px" to="/gang">群列表</router-link>
    <router-link style="padding:0 5px" to="/">私信</router-link>
    <router-link style="padding:0 5px" to="/setting">个人中心</router-link>
  </div>
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
  border:1px solid black;
  border-bottom: 0px;
  margin: auto;
  width: 100%;
  max-width: 800px;
  text-align: center;
  border-top: 0;
}
</style>