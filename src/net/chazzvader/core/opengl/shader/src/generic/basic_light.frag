#version 430 core

layout (location = 0) out vec4 color;

uniform vec3 light_color;

void main()
{
	color = vec4(light_color, 1.0);
}