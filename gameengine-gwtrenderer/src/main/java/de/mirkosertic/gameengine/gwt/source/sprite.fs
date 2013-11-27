precision mediump float;

uniform sampler2D uMap;
uniform float uDrawBorder;

varying vec2 vUV;

void main() {
	if (uDrawBorder > 0.0) {
	    gl_FragColor = vec4(1.0,1.0,1.0,1.0);
	} else {
	    gl_FragColor = texture2D( uMap, vec2(vUV.x,1.0 - vUV.y));
    }
}