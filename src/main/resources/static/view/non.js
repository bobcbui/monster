let template = // html
`
<div>
    <div style='height:calc(100% - 100px)'>
        <div class='nav-head'>
            <router-link to="/">返回</router-link>
            <span style='float:right'>{{$route.query.url}}</span>
        </div>
        <div style='overflow-y: scroll;height:calc(100% - 40px)'>
            <div style='border: 1px solid black;word-break:break-all;margin: 5px;padding: 5px;border-radius: 5px;background: #efefef;' 
            v-if='$store.state.messageMap[$route.query.url] != undefined' v-for='(item,index) in $store.state.messageMap[$route.query.url].message' :key='index'>
            <span style='font-weight: bold;color: #5f5fba;'>{{item.nickname}}</span><span style='float:right'>{{item.createTime}}</span>
            <br>
            {{item.text}}
            </div>
        </div>
    </div>
non
</div>
`
export default {
    template: template,
    data: function(){
        return {

        }
    },
    methods: {

    }
}
