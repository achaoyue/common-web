methodConfig = {
    "最大值": {
        url: "/api/stock/bigThan",
        params: {
            "date": "this.name"
        },
        resp: [
            {"bigDate": "resp.data"}
        ],
        run: function () {
            NetUtil.send(this.url, this.params).then(resp => {

            })
        }
    }
}