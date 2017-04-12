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

    TeaVM.prototype.int32FromMemory = function(pos) {
        var len1 = this.memoryArray[pos]
        var len2 = this.memoryArray[pos + 1]
        var len3 = this.memoryArray[pos + 2]
        var len4 = this.memoryArray[pos + 3]

        return len1 + len2 * 256 + len3 * 65536 + len4 * 16777216;
    },

    TeaVM.prototype.int16FromMemory = function(pos) {
        var len1 = this.memoryArray[pos]
        var len2 = this.memoryArray[pos + 1]

        return len2 * 256 + len1
    },

    TeaVM.prototype.pointerToString = function(str) {

        // Pointer to character array
        var dataOffset = this.int32FromMemory(str + 8)

        var totalLength = this.int32FromMemory(dataOffset + 8)

        var dataOffset = dataOffset + 12
        var data = ''
        for (i=0;i<totalLength;i++) {
            var theChar = String.fromCharCode(this.int16FromMemory(dataOffset))
            dataOffset+=2
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
