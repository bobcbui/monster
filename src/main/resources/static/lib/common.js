

let decodeWsAccount = (account) => {
    let s = account.split("@");
    let name = s[1].replaceAll(".","/");
    let ym = s[2]
    return "ws://"+ym+"/"+name;
}

let down = (id) => {
    try{
        var div = document.getElementById("show_words_"+id);
        div.scrollTop = div.scrollHeight;
    }catch(error){
        console.log("skldjfkladjsfkljasdkfjkdjfkdjfkd")
    }
}

let toDate = (time) => {
    // 时间戳转距离当前有多久，分钟，小时，天，月，年
    let now = new Date().getTime();
    let distance = now - time;
    if(distance < 60 * 1000){
        return "刚刚";
    }
    if(distance < 60 * 60 * 1000){
        return Math.floor(distance / (60 * 1000)) + "分钟前";
    }
    if(distance < 24 * 60 * 60 * 1000){
        return Math.floor(distance / (60 * 60 * 1000)) + "小时前";
    }
    if(distance < 30 * 24 * 60 * 60 * 1000){
        return Math.floor(distance / (24 * 60 * 60 * 1000)) + "天前";
    }
    if(distance < 12 * 30 * 24 * 60 * 60 * 1000){
        return Math.floor(distance / (30 * 24 * 60 * 60 * 1000)) + "月前";
    }
    return Math.floor(distance / (12 * 30 * 24 * 60 * 60 * 1000)) + "年前";
}