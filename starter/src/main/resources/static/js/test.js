allTags = {
    "el-row":{
        tag:"el-row",
        props:[
            {
                name:"栅格间隔",
                key:":gutter"
            },
        ]
    },
    "el-col":{
        tag:"el-col",
        props:[
            {
                name:"栅格占据的列数",
                key:":span"
            },
            {
                name:"栅格左侧的间隔格数",
                key:":offset"
            },
        ]
    },
    "el-input":{
        tag:"el-input",
        props:[
            {
                name:"输入框占位文本",
                key:":placeholder"
            },
            {
                name:"是否可清空",
                key:":clearable"
            },
        ]
    },
    "el-button":{
        tag:"el-button",
        hasChild:true,
        props:[
            {
                name:"尺寸",
                key:"size"
            },
            {
                name:"类型",
                key:"type"
            },
            {
                name:"点击",
                key:"@click"
            },
        ]
    },
    "文本":{
        tag:"txt",
        hasChild:false,
        props:[

        ]
    }
}