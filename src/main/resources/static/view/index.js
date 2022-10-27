let template = // html
`
<section>
	<aside id="aside" v-bind:class="[$store.state.indexItem == 'aside' ? 'z-index-top':'']">
		<div style="height:40px;background-color:azure;line-height:40px;padding:0 10px;border-bottom: 1px solid #a7a7a7;">
			<a style="padding-right:10px">消息</a>
			<a style="padding-right:10px">好友</a>
			<a style="padding-right:10px">群组</a>
			<router-link style="padding-right:10px" to="/add">添加</router-link>
			<router-link  style="float:right" to="/home">我的</router-link>
		</div>
		<div style="height:calc(100% - 70px); background-color:rgba(255, 190, 117, 0.3)">
			<ul>
				<li v-for="item in list" style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({name:'message',params: {username:item.username}})">
					💬{{item.username}}
				</li>
			</ul>
		</div>
		<div style="height:30px;line-height:30px;background-color:rgba(255, 190, 117, 0.11);padding: 0 10px;border-top: 1px solid #a7a7a7;">
			巴啦啦跨平台协议.关于
		</div>
	</aside>
		
	<main id="main" v-bind:class="[$store.state.indexItem == 'main' ? 'z-index-top' : '']">
		<div style="height:40px;background-color:azure;line-height:40px;padding:0 10px;border-bottom: 1px solid #a7a7a7;">
			<router-link to="/">返回</router-link>
		</div>
		<router-view></router-view>
	</main>
</section>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			type:"message",
			list:[{username:"123"},{username:"124"},{username:"125"}]
		}
	},
	methods: {

	},
	created() {
		request({
			url: "/api/other",
			method: "GET",
		}).then((response) => {

		});
	}
}
