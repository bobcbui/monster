let template = // html
`
<button @click='show = !show' class='h-100'>{{buttonName}}</button>
<div class='mode' v-if='show'>
	<div class='mode-body'>
		<div class='header p-10 border-b-1' style='background: var(--topColor);'>
            <button style='width:100%;' @click='close()'>关闭</button>
        </div>
        <div class='body p-10' style='background: var(--bottomColor);height: calc(100% - 44px);text-align: left'>
		    <slot></slot>
        </div>
	</div>
</div>
`
export default {
    props: {
        buttonName:{
            type: String,
            default: '按钮'
        }
    },
    data: () => {
        return {
            show: false
        }
    },
    methods: {
        close(){
            this.show = false;
        },
        open(){
            this.show = true;
        }
    },
    template: template
}