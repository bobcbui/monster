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
        v-for="(item, index) in groupList"
        :key="index"
        style="padding: 5px"
        @click="join(item.id)"
      >
        {{ item.name }} <button style="float:right">加入</button>
      </p>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
export default {
  name: "group_list",
  data() {
    return {
      groupList: [],
    };
  },
  computed: {},
  created() {},
  methods: {
    loadGangList() {
      request({
        url: "/gang/list",
        method: "get",
      }).then((response) => {
        this.groupList = response.data;
      });
    },
    join(id) {
      request({
        url: "/gang/insert",
        method: "post",
        data:{id:id}
      }).then((response) => {
        alert(jrcg)
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