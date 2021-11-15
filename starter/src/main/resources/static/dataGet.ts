class DateGet{
    constructor() {
        this.params={

        }
        this.listener = [];
    }
    flush = () => {
        this.listener.forEach(e=>{
            if(e.flush instanceof Function){
                try {
                    e.flush();
                }catch (error){
                    console.error(error);
                }
            }
        })
    }
    getData = () => {
        return axios.get(this.params.url,{params:this.params}).then(res=>{
            this.data = res.data;
            window[this.params.dataLocal] = res.data;
            return res.data;
        }).then(res=>{
            this.flush();
            return res;
        })
    }

    postData = () => {
        return axios.post(this.params.url,this.params.data).then(res=>{
            this.data = res.data;
            window[this.params.dataLocal] = res.data;
        }).then(res=>{
            this.flush();
            return res;
        })
    }
}