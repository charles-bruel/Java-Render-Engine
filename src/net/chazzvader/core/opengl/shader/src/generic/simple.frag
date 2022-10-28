#version 430 core

layout (location = 0) out vec4 color;

uniform vec3 color_multiplier;
uniform sampler2D tex;

in vec2 tex_coords;

void main()
{
	vec4 tex_color = texture(tex, tex_coords);
	if(tex_color.w < 1)
	{
		discard;
	}
	color = vec4(color_multiplier, 1) * tex_color;
}