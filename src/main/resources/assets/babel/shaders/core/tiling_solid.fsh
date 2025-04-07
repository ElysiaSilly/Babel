#version 150

//#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;


#define TILES_COUNT_X 4.0
#define TILES_COUNT_Y 3.0

vec3 BackgroundColor(vec2 resolution, sampler2D tilesTexture, vec2 fragCoord )
{
    vec2 p = (resolution - TILES_COUNT_X * fragCoord.xy) / resolution.x;
    //	vec2 p = (resolution - TILES_COUNT_Y * fragCoord.xy) / resolution.y;
    return texture (tilesTexture, p).xyz;
}

void main() {

    //vec3 col = BackgroundColor(texCoord0.xy * 2, Sampler0, texCoord0.xy * 2);

    vec2 uv = floor(texCoord0.xy * 2) / 2;

    vec3 col = texture(Sampler0, uv).xyz;

    fragColor = vec4(col, 0);


    //vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    //fragColor = linear_fog(vec4(col, 0), vertexDistance, FogStart, FogEnd, FogColor);
}