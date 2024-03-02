//对象方法序列化
function toGString(obj) {
    let str = "let $obj={};";
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            str += "$obj." + key + '=';
            const value = obj[key];
            if (typeof value === 'function') {
                let ts = Function.prototype.toString.call(value);
                if (ts.startsWith("function")) {
                    str += ts;
                } else str += " function " + ts;
            } else {
                str += JSON.stringify(value);
            }
            str += '\n;';
        }
    }
    return str += ";$obj;";
}