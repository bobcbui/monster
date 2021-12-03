<template>
  <div class="head-table" style="background: radial-gradient(200% 100% at bottom center,#f7f7b6,#e96f92,#1b2947);
		background: radial-gradient(220% 105% at top center,#1b2947 10%,#75517d 40%,#e96f92 65%,#f7f7b6);
		background-attachment: fixed;">
    <router-link style="padding:0 5px" to="/"><font color="#FFFFFF">消息</font></router-link>
    <router-link style="padding:0 5px" to="/plaza"><font color="#FFFFFF">列表</font></router-link>
    <router-link style="padding:0 5px" to="/setting"><font color="#FFFFFF">我的</font></router-link>
	<button @click="tuichu()">退出</button>
  </div>
  <router-view/>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
     
    };
  },
  methods:{
	  tuichu(){
		  localStorage.clear();
		  this.$router.push("/login");
	  }
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