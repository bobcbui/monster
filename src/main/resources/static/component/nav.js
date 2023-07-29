let template = // html
`
<div style='width:100%;border-bottom: 1px solid black;padding:10px;background:#ffdefc;'>
    <div style='display: flex;'>
        <div style='width: 50%;'>
            <strong>🙂{{title}}</strong>
        </div>
        <div style='width: 50%;text-align: right;'>
            <slot></slot>
        </div>
    </div>
</div>
`
export default {
    props:{
        title: {
            type: String,
            default: '标题'
        }
    },
    template: template,
    methods:{
       
    }
}