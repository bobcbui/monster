<template>
  <div style="height: calc(100% - 21px);  margin: auto;width: 100%;max-width: 800px;">
    <div style="height: calc(100% );width:100%; overflow-y: scroll;border:1px solid black;border-bottom:0px">
      <p v-for="(item,index) in $store.state.newMessage" :key="index" style="padding: 5px;" @click="toMessage(item.gangId)">{{item.gangName}} ：{{item.text}}</p>
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
      }
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

</style>