
let template = // html
    `
<section>
        <aside id="aside" v-bind:class="[$store.state.indexItem == 'aside' ? 'z-index-top':'']">
            <div style="height:40px;background-color:azure;line-height:40px;padding:0 10px">
                <a style="padding-right:10px">消息</a>
                <a style="padding-right:10px">好友</a>
                <a style="padding-right:10px">群组</a>
                <a style="padding-right:10px">添加</a>
                <a style="float:right">我的</a>
            </div>
            <div style="height:calc(100% - 70px); background-color:rgba(255, 190, 117, 0.3)">
                <ul>
                    <li><router-link to="/message">asdf</router-link></li>
                    <li><router-link to="/message">asdf</router-link></li>
                </ul>
            </div>
            <div style="height:30px;line-height:30px;background-color:rgba(255, 190, 117, 0.11);padding: 0 10px">
                巴啦啦跨平台协议.关于
            </div>
        </aside>
        
        <main id="main" v-bind:class="[$store.state.indexItem == 'main' ? 'z-index-top' : '']">
            <div style="height:40px;background-color:azure;line-height:40px;padding:0 10px">
                <router-link to="/">返回</router-link>
            </div>
            <router-view></router-view>
        </main>
    </section>
`
export default {
    template: template,
    data: function () {
        return {

        }
    },
    methods: {

    },
    created(){
        request({
            url: "/api/other",
            method: "GET",
          }).then((response) => {
            alert(JSON.stringify(response))
          });
    }
}
