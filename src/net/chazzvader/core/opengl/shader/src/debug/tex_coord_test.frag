#version 430 core

layout (location = 0) out vec4 color;

uniform vec3 light_color;

in vec2 tex_coords;

void main()
{
	color = vec4(tex_coords, 0.0, 1.0);
}