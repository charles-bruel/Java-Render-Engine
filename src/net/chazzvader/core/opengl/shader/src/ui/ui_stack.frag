#version 430 core

layout (location = 0) out vec4 color;

uniform sampler2D tex;
uniform vec3 background_color;

in vec2 tex_coords;
in vec2 pos;

void main()
{
	if(tex_coords.x > 1 || tex_coords.y > 1 || tex_coords.x < 0 || tex_coords.y < 0)
	{
		color = vec4(background_color, 1);
	}
	else
	{
		vec4 tex_color = texture(tex, tex_coords);
		if(tex_color.w < 1)
		{
			color = vec4(background_color, 1);
		}
		else
		{
			color = tex_color;
		}
	}
	gl_FragDepth = 0;
}