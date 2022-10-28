#version 430 core

layout (location = 0) out vec4 color;

uniform vec4 shadeColor;

void main()
{
	color = shadeColor;
	gl_FragDepth = 1;
}