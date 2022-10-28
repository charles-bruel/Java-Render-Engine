#version 430 core

layout (location = 0) out vec4 color;

uniform vec3 light_color;

in vec3 normal;

void main()
{
	color = vec4((normal + vec3(1, 1, 1))/2, 1.0);
}