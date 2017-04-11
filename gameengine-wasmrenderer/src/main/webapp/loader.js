var TeaVM = function() {

    function TeaVM() {
        this.instance = null;
        this.module = null;
        this.memory = null;
        this.memoryArray = null;
    }

    TeaVM.prototype.run = function(callback) {
        load(this, callback)
    },

    TeaVM.prototype.loadIntoStringPool = function(str) {
        var stringPoolId = this.instance.exports.newStringPoolID()
        for (var i=0, len = str.length;i < len;i++) {
            this.instance.exports.addCharToStringPool(stringPoolId, str.charCodeAt(i))
        }
        return stringPoolId
    },

    TeaVM.prototype.pointerToString = function(str) {

        var len1 = this.memoryArray[str + 24]
        var len2 = this.memoryArray[str + 25]
        var len3 = this.memoryArray[str + 26]
        var len4 = this.memoryArray[str + 27]

        var totalLength = len1 + len2 * 256 + len3 * 65536 + len4 * 16777216

        var dataOffset = str + 28
        var data = ''
        for (i=0;i<totalLength;i++) {
            var firstCode = this.memoryArray[dataOffset++]
            var secondCode = this.memoryArray[dataOffset++]
            var theChar = String.fromCharCode(secondCode * 256 + firstCode)
            data+=theChar
        }

        return data
    };

    function load(teavm, callback) {
        var xhr = new XMLHttpRequest()
        xhr.responseType = "arraybuffer"
        xhr.open("GET", "teavm-wasm/classes.wasm")
        xhr.onload = function () {
            var response = xhr.response
            if (!response) {
                return;
            }

            var importObj = {
                runtime: {
                    currentTimeMillis: function() {
                        return new Date().getTime()
                    },
                    towlower: function(c) {
                        return c
                    },
                    getNaN: function() {
                        return NaN;
                    },
                    isInfinite: function(v) {
                        return !isFinite(v)
                    },
                    isFinite: function(v) {
                        return isFinite(v)
                    },
                    isNaN: function(v) {
                        return !isNaN(v)
                    },
                },
                math: Math,
                engine: {
                    bootstrap: function() {
                        var xhr = new XMLHttpRequest()
                        xhr.responseType = "text"
                        xhr.open("GET", "game.json")
                        xhr.onload = function () {

                            var gameJson = xhr.responseText
                            var stringPoolId = teavm.loadIntoStringPool(gameJson)

                            teavm.instance.exports.loadGameFromStringPool(stringPoolId)
                        }
                        xhr.send()
                    },
                    loadGameScene: function(str) {
                        var sceneId = teavm.pointerToString(str)

                        var xhr = new XMLHttpRequest()
                        xhr.responseType = "text"
                        xhr.open("GET", sceneId + "/scene.json")
                        xhr.onload = function () {

                            var sceneJson = xhr.responseText
                            var stringPoolId = teavm.loadIntoStringPool(sceneJson)

                            teavm.instance.exports.loadGameSceneFromStringPool(stringPoolId)
                        }
                        xhr.send()
                    }
                },
                log: {
                    log_string: function (str) {
                        console.log(teavm.pointerToString(str))

                    }
                },
            }
            WebAssembly.instantiate(response, importObj).then(function(resultObject) {
                teavm.module = resultObject.module
                teavm.instance = resultObject.instance
                teavm.memory = teavm.instance.exports.memory;
                teavm.memoryArray = new Uint8Array(teavm.memory.buffer)
                console.log("Initialized")
                callback()
            }).catch(function(error) {
                console.log("Error : " + error)
            });
        }
        xhr.send()
    }

    return TeaVM;
}();
