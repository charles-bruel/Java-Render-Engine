#version 430 core

uniform vec3 light_pos;
uniform float far_plane;

in vec4 frag_pos;

void main()
{
    float light_distance = length(frag_pos.xyz - light_pos);
    
    light_distance = light_distance / far_plane;
    
    gl_FragDepth = light_distance;
}
