#version 150

//#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float Rotation;
uniform float Opacity;
uniform float Radius;
uniform vec2 Offset;
uniform vec4 Mask;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

vec2 rotate (vec2 uv, float theta){
    return mat2(cos(theta), sin(theta), -sin(theta), cos(theta)) * uv;
}

void main() {
    vec2 uv = floor(texCoord0.xy * 16) / 16;

    vec4 colour = vec4(0.0, 0.0, 0.0, 0.0);

    float size = Radius;

    if(uv.x >= Mask.x && uv.y >= Mask.y && uv.x <= Mask.z && uv.y <= Mask.w) {

        float x = uv.x - Offset.x;
        float y = uv.y - Offset.y;
        vec2 rotation = rotate(vec2(x, y), Rotation);

        float cube = max(abs(rotation.x), abs(rotation.y));

        if(cube - size < 0.0) {
            colour = vec4(0.0, 0.0, 0.0, (Opacity - .1));
        }
        if(cube - size < -0.03) {
            colour = vec4(0.0, 0.0, 0.0, Opacity);
        }
    }

    fragColor = colour;
}
