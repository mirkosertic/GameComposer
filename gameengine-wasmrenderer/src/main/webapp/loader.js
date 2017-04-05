var TeaVM = function() {

    function TeaVM() {
        this.instance = null;
        this.module = null;
        this.memory = null;
        this.memoryArray = null;
    }

    TeaVM.prototype.run = function(callback) {
        load(this, callback)
    };

    function load(teavm, callback) {
        var xhr = new XMLHttpRequest()
        xhr.responseType = "arraybuffer"
        xhr.open("GET", "teavm-wasm/classes.wasm")
        xhr.onload = function () {
            var response = xhr.response;
            if (!response) {
                return;
            }

            var importObj = {
                runtime: {
                    currentTimeMillis: function() {
                        return new Date().getTime();
                    },
                    towlower: function(c) {
                        return c;
                    }
                },
                logic: {
                    invokeMe : function() {
                        console.log(teavm.instance.exports.passThru(13));
                    }
                },
                log: {
                    log_int: function (int) {
                        console.log("Log int : " + int)
                    },
                    log_float: function (f) {
                        console.log("Log float : " + f)
                    },
                    log_double: function (du) {
                        console.log("Log double : " + du)
                    },
                    log_string: function (str) {

                        // 1364 Object Pointer
                        // 1388 Länge 4 Bytes
                        // 1392 UTF-8 Zeichen 2 Bytes
                        var len1 = teavm.memoryArray[str + 24];
                        var len2 = teavm.memoryArray[str + 25];
                        var len3 = teavm.memoryArray[str + 26];
                        var len4 = teavm.memoryArray[str + 27];

                        var totalLength = len1 + len2 * 256 + len3 * 65536 + len4 * 16777216;

                        var dataOffset = str + 28;
                        var data = '';
                        for (i=0;i<totalLength;i++) {
                            var firstCode = teavm.memoryArray[dataOffset++];
                            var secondCode = teavm.memoryArray[dataOffset++];
                            var theChar = String.fromCharCode(secondCode * 256 + firstCode);
                            data+=theChar;
                        }

                        console.log("log string : " + data);

                    }
                },
            }
            WebAssembly.instantiate(response, importObj).then(function(resultObject) {
                teavm.module = resultObject.module;
                teavm.instance = resultObject.instance;
                teavm.memory = teavm.instance.exports.memory;
                teavm.memoryArray = new Uint8Array(teavm.memory.buffer);
                console.log("Initialized")
                callback();
            }).catch(function(error) {
                console.log("Error : " + error);
            });
        }
        xhr.send()
    }

    return TeaVM;
}();
