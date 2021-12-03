<template>
  <div style="height: calc(100% - 21px);  margin: auto;width: 100%;max-width: 800px;">
    <div style="height: 100%;width:100%; overflow-y: scroll;padding:5px; border:1px solid black;border-bottom:0px">
      <p>用户名：ask的JFK</p>
      <p>简介：askdsfasdfasdfasdfasdfasdf的JFK</p>
      <p>性别：女（暂时无法修改性别）</p>
      <p>是否可以私信：是 , 否</p>
	  <button @click="hi()">nihao </button>
    </div>
  </div>
</template>
<script>
export default {
  name: "home",
  data() {
    return {
      groupId:this.$route.params.groupId,
      formData:{
        to:this.$route.params.groupId,
        messageText:"",
        type:"group"
      },
    };
  },
  computed: {
    
  },
  created() {

  },
  methods: {
    toMessage(id){
      this.$router.push({
        path: '/group/'+ id
    })
    },
	hi(){
		alert(this.$store.state.login.id);
	}
  },
  mounted(){
   // alert(JSON.stringify(this.$store.state.message))
  }

};

</script>

<style scoped>
.send-but{
  position:fixed;
  bottom: 0px;
}
.message-body{
  height: 500px;
  width: 300px;
  background: rgb(167, 166, 166);
}
.message{
  padding-bottom: 10px;
}
p{
    padding: 10px;
}

</style>