<template>
  <div style="height: calc(100% - 21px);  margin: auto;width: 100%;max-width: 800px;background: radial-gradient(200% 100% at bottom center,#f7f7b6,#e96f92,#1b2947);
		background: radial-gradient(220% 105% at top center,#1b2947 10%,#75517d 40%,#e96f92 65%,#f7f7b6);
		background-attachment: fixed;">
    <div style="height: calc(100% );width:100%; overflow-y: scroll;border:1px solid black;border-bottom:0px">
      <p v-for="(item,index) in $store.state.newMessage" :key="index" style="padding: 3px;border: 1px solid #625a5a;margin: 3px;border-radius: 10px;" @click="toMessage(item.gangId)"><strong>{{item.gangName}}</strong> <br>{{item.text}}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "home",
  data() {
    return {
      gangId:this.$route.params.gangId,
      formData:{
        to:this.$route.params.gangId,
        messageText:"",
        type:"gang"
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
        path: '/gang/'+ id
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