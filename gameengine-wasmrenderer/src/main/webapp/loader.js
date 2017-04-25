var TeaVM = function() {

    function TeaVM(renderer) {
        this.instance = null;
        this.module = null;
        this.memory = null;
        this.memoryArray = null;
        this.cachedObjects = [];
        this.pixiRenderer = renderer
        this.stage = new PIXI.Container();
        this.sceneGraphInstances = [];
        this.touchedInLastFrame = []
    }

    TeaVM.prototype.run = function(callback) {
        load(this, callback)
    },

    TeaVM.prototype.putIntoCache = function(obj) {
        this.cachedObjects.push(obj)
        return this.cachedObjects.length - 1;
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

    TeaVM.prototype.putchar = function(charCode) {
        if (charCode == 10) {
            console.log(this.line);
            this.line = "";
        } else {
            this.line += String.fromCharCode(charCode);
        }
    },

    TeaVM.prototype.handleResize = function() {
        this.pixiRenderer.resize(window.innerWidth, window.innerHeight)
        this.instance.exports.handleResize()
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
                        return isNaN(v)
                    },
                    putchar: function(c) {
                        teavm.putChar(c)
                    }
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

                            teavm.instance.exports.loadGameSceneFromStringPool(stringPoolId, str)
                        }
                        xhr.send()
                    },
                    loadSpriteSheet: function(promiseId, resourceName) {
                        var loader = new PIXI.loaders.Loader()
                        var realResourceName = teavm.pointerToString(resourceName)

                        loader.add(realResourceName)
                        loader.pre(function(resource, chain) {
                            chain()
                        })
                        loader.load(function(loader, resources) {
                            var loadedJson = resources[realResourceName]

                            var frames = loadedJson.data.frames
                            Object.keys(frames).forEach(function(frame, index) {
                                var texture = new PIXI.Texture.fromFrame(frame)

                                var cacheId = teavm.putIntoCache(texture)

                                var stringPoolId = teavm.loadIntoStringPool(frame)
                                teavm.instance.exports.registerResourceToSpriteSheet(resourceName, stringPoolId, cacheId)
                            })

                            // We are done here
                            teavm.instance.exports.resolvePromiseWithID(promiseId, 42)
                        })
                    },
                    requestAnimationFrame: function() {
                        window.requestAnimationFrame(function(timestamp) {
                            teavm.instance.exports.runSingleStep()
                        })
                    },
                    screenWidth: function() {
                        return window.innerWidth
                    },
                    screenHeight: function() {
                        return window.innerHeight
                    },
                    beginFrame: function() {
                        console.log("beginFrame")
                        teavm.touchedInLastFrame = []
                    },
                    drawText: function(objectIdPtr,textPtr,fontFamilyPtr,fontSizePtr,
                        cssFillPtr,
                        cssStoktePtr,
                        x,
                        y,
                        centerX,
                        centerY,
                        rotationRad) {
                        var objectId = teavm.pointerToString(objectIdPtr)
                        var text = teavm.pointerToString(textPtr)
                        var fontFamily = teavm.pointerToString(fontFamilyPtr)
                        var fontSize = teavm.pointerToString(fontSizePtr)
                        var cssFill = teavm.pointerToString(cssFillPtr)
                        var stroke = teavm.pointerToString(cssStoktePtr)

                        var theInstance = teavm.sceneGraphInstances[objectId]
                        if (theInstance == null) {
                            theInstance = new PIXI.Text(text);
                            theInstance.scale.set(1,1)
                            theInstance.pivot.set(centerX, centerY)
                            teavm.sceneGraphInstances[objectId] = theInstance
                            teavm.stage.addChild(theInstance)
                        } else {
                            theInstance.text = text;
                        }

                        var theInstanceStyle = theInstance.style
                        theInstanceStyle.fontFamily = fontFamily
                        theInstanceStyle.fontSize = fontSize
                        theInstanceStyle.fill = cssFill
                        theInstanceStyle.stroke = stroke

                        theInstance.position.set(x, y)
                        theInstance.rotation = rotationRad

                        teavm.touchedInLastFrame[objectId] = theInstance

                    },
                    frameFinished: function() {
                        console.log("frame finished")

                        // Remove no longer needed instances
                        var theNewInstances = []
                        for (var key in teavm.sceneGraphInstances) {
                            var instance = teavm.sceneGraphInstances[key]
                            if (teavm.touchedInLastFrame[key] != null) {
                                theNewInstances[key] = instance
                            } else {
                                teavm.stage.removeChild(instance)
                            }
                        }
                        teavm,sceneGraphInstances = theNewInstances

                        teavm.pixiRenderer.render(teavm.stage)
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