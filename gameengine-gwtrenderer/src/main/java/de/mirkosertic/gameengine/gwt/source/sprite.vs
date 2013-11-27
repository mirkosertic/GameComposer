attribute vec3 aVertexPosition;
attribute vec2 aUv;

uniform vec2 uScreenPosition;
uniform vec2 uScale;
uniform float uRotation;
uniform mat4 uPMatrix;

varying vec2 vUV;

void main(void) {

    vUV = aUv;

	vec2 rotatedAndScaledPosition;
	rotatedAndScaledPosition.x = ( cos( uRotation ) * aVertexPosition.x * uScale.x - sin( uRotation ) * aVertexPosition.y * uScale.y);
	rotatedAndScaledPosition.y = ( sin( uRotation ) * aVertexPosition.x * uScale.x + cos( uRotation ) * aVertexPosition.y * uScale.y);

    vec3 translatedVertex = vec3(rotatedAndScaledPosition.xy + uScreenPosition, aVertexPosition.z);

    gl_Position = uPMatrix * vec4(translatedVertex, 1.0 );
}