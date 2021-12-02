<template>
  <div
    style="
      height: calc(100% - 21px);
      margin: auto;
      width: 100%;
      max-width: 800px;
    "
  >
    <div
      style="
        height: calc(100%);
        width: 100%;
        overflow-y: scroll;
        border: 1px solid black;
        border-bottom: 0px;
      "
    >
      <p
        v-for="(item, index) in gangList"
        :key="index"
        style="padding: 5px"
        
      >
      <router-link style="padding:0 5px" :to="'/gang/'+item.id">{{ item.name }}</router-link>
         <button style="float:right" @click="add(item.id)">添加</button>
      </p>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
export default {
  name: "plaza",
  data() {
    return {
      gangList: [],
    };
  },
  computed: {},
  created() {},
  methods: {
    loadGangList() {
      request({
        url: "/plaza/gang/list",
        method: "get",
      }).then((response) => {
        this.gangList = response.data;
      });
    },
    add(id,type) {
      request({
        url: "/plaza/gang/"+id,
        method: "post"
      }).then((response) => {
        alert(response.data)
      });
    },
  },
  mounted() {
    this.loadGangList();
  },
};
</script>

<style scoped>
.send-but {
  position: fixed;
  bottom: 0px;
}
.message-body {
  height: 500px;
  width: 300px;
  background: rgb(167, 166, 166);
}
.message {
  padding-bottom: 10px;
}
</style>