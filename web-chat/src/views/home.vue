<template>
  <div id="body">
    <div id="message">
      <div id="message-head">
          <!-- <div id="message-tx">头像</div> -->
          <div class="message-nc">昵称</div>
          <button class="head-button float-right">编辑</button>
      </div>
      <div id="message-body">
          <div class="b1-p1-mb-1 br-5 m-t-l-r-5" v-for="(item,index) in messageList" :key="index" @click="toGang(item[item.length-1].gangId)" style="height: 70px;overflow: hidden;" :class="{'active':gangId == item[item.length-1].gangId}">
              <strong>{{item[item.length-1].gangName}}</strong>
              <span style="float:right">{{timeFromNow(item[item.length-1].createTime)}}</span>
              <br>
              <span style="color: #8d8d8d;">{{item[item.length-1].fromName}}：{{item[item.length-1].text}}</span>
          </div>
      </div>
    </div>
    <div id="r-body">
      <div id="r-body-head">
          <div class="message-nc" v-if="gangId != undefined && $route.name == 'gang_message'" @click="toGangList()"><span id="syy">❌</span> {{gangName}} (10万+)</div>
          <div class="message-nc" v-if="$route.name != 'gang_message'" @click="toGangList()">首页</div>
          
          <div class="float-right message-nc" @click="grzx">个人中心</div>
      </div>
      <div id="r-body-body">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "demo",
  data() {
    return {
      gangName:"",
      gangId: this.$route.params.gangId,
      messageList:this.$store.state.message
    };
  },
  computed: {},
  created() {},
  watch:{
    $route(to, from){
      this.gangId = to.params.gangId
      try{
        this.gangName = JSON.parse(localStorage.getItem("gang"))[this.gangId]['gangName']
      }catch(e){

      }
      this.gangId = this.$route.params.gangId
      this.messageList = this.$store.state.message
    }
  },
  methods: {
    timeFromNow(oldTime){
      return oldTime;
    },
    toGang(id){
      //window.location.href = 
      this.$router.push('/gang/'+id)
    },
    toGangList(){
      this.$router.push('/')
    },
   
    grzx(){
      this.$router.push('/setting')
    }
  },
  mounted() {
    try{
      this.gangName = JSON.parse(localStorage.getItem("gang"))[this.gangId]['gangName']
    }catch(e){

    }
  },
};
</script>

<style scoped>

.active{
  background: rgb(228, 228, 228);
}


</style>