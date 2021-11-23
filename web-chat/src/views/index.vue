<template>
  <nav>
    <a class="m-5">聊天软件</a>
    <a class="m-5" href="/login">登录</a>
  </nav>
  <div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
     
    };
  },
  computed: {
    
  },
  created() {

  },
  methods: {
    
  },
  mounted(){
   
  }
};
</script>

<style scoped>
.m-5{
  margin: 5px;
}
nav{
  border-bottom: 1px solid red;
}
</style>