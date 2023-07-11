let template = // html
`
<cNav title='更多'>
	<cModal buttonName='添加'>
        sdfasdf
	</cModal>
</cNav>
<div class='p-10'>

</div>
`
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
import request from '../lib/request.js'
export default {
    template: template,
	data: function () {
        return {
			showCreateGroup: false,
            joinButtonText: "加为好友",
			createGroupForm: {
				name: "",
				nickname: "",
			},
			showJoinGroup: false,
			joinGroupForm: {

            },
            searchMember: null
        }
	},
    components: {
        cNav,cModal
    },
	destroyed() {
		
	},
	watch: {
		
	},
	computed: {
        memberMap(){
            return this.$store.state.memberMap;
        }
		
    },
    methods: {
		createGroup(){
			this.$store.state.socketLocal.send(JSON.stringify({ type: "createGroup", data: this.createGroupForm}))
		},
		searchGroup(){
			request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
				let account = this.joinGroupForm.ws
				let ws = decodeWsAccount(this.joinGroupForm.ws);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
				this.$store.state.socketGroup[account] = socket;

                let that = this;
                socket.onopen = function(e){
					that.groupSocket = socket;
                    socket.send(JSON.stringify({ type: "groupInfo"}))

                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "searchGroup"){
                       console.log("========================");
                    }
					if(data.type == "groupInfo"){
						that.joinGroupForm = data.data;
					}
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
                
            }).catch(function (error) {
                console.log(error);
            });
		
		},
		joinGroup(){
			this.joinGroupForm.memberAccount = this.$store.state.member.account;
			this.$store.state.socketGroup[this.joinGroupForm.account].send(JSON.stringify({ type: "joinGroup", data: this.joinGroupForm}))
			this.$store.state.socketLocal.send(JSON.stringify({ type: "joinGroup", data: this.joinGroupForm}))
		},
        searchMemberClick(){
            this.joinButtonText = "加为好友";
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let ws = decodeWsAccount(this.account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let that = this;
                socket.onopen = function(e){
                    that.memberSocket = socket;
                    socket.send(JSON.stringify({ type: "info"}))
                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "join"){
                        that.$store.state.socketLocal.send(JSON.stringify({ type: "addMember", data: data.data}))
                        that.$store.state.socketLocal.send(JSON.stringify({ type: "memberMap"}))
                    }
                    if(data.type == "info"){
                        that.searchMember = data.data;
                        // 如果 that.searchMember 在 memberMap 中
                        if(that.memberMap[that.searchMember.account]){
                            that.joinButtonText = "已是好友";
                        }
                    }
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
                
            }).catch(function (error) {
                console.log(error);
            });
        },
        joinMember(){
            this.memberSocket.send(JSON.stringify({ type: "join"}))
        }
		
	},
	created() {
		
	},
	mounted(){

    }
}
