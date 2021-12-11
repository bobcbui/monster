<template>
  <div id="body">
    <div id="message">
      <div id="message-head">
          <div id="message-tx">头像</div>
          <div id="message-nc">昵称</div>
          <button class="head-button float-right">编辑</button>
      </div>
      <div id="message-body">
          <div class="b1-p1-mb-1 br-5" v-for="(item,index) in messageList" :key="index">
              <strong>asdf</strong>
              <span style="float:right">时间</span>
              <br>
              <span>一点：asdfasdfasdf</span>
          </div>
          
      </div>
    </div>
    <div id="r-body">
      <div id="r-body-head">
          <button class="head-button">群列表</button>
          <button class="head-button float-right">退出</button>
          <button class="head-button float-right">设置</button>
      </div>
      <div id="r-body-body">应用Body</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "demo",
  data() {
    return {
        messageList:[{},{},{},{}]
    };
  },
  computed: {},
  created() {},
  methods: {},
  mounted() {},
};
</script>

<style scoped>
html,body{
    margin: 0;
    padding: 0;
    height: 100%;
}
*{
    box-sizing: border-box;
}
#app{
    margin: auto;
    height: 100%;
    width: 100%;
    max-width: 1000px;
    max-height: 800px;           
    padding:70px
}
#body{
    border: 1px solid black;
    height: 100%;
}
#message{
    border-right: 1px solid black;
    width: 300px;
    height: 100%;
    float: left;
}
#message-tx{
    padding: 3px;
    border: 1px solid red;
    height: 40px;
    width: 40px;
    float: left;
}
#message-nc{
    padding-left: 5px;
    float: left;
    font-weight:bold;
}
#r-body{
    height: 100%;
    width:calc(100% - 300px);
    float: right;
}
#message-head,#r-body-head{
    height: 50px;
    border-bottom: 1px solid black;
    background-color: rgb(115, 168, 247);
    padding: 4px;
}
#message-body{
    height: calc(100% - 50px);
    background-color: blanchedalmond;
    padding: 4px;
}
#r-body-body{
    height: calc(100% - 50px);
    background-color: blanchedalmond;
    padding: 4px;
}


.b1-p1-mb-1{
    border: 1px solid black;
    padding: 3px;
    margin-bottom: 3px;
}

.br-5{
    border-radius: 5px;
}
.head-button{
    line-height: 34px;
    margin-left: 3px;
}
.float-right{
    float: right;
}

</style>