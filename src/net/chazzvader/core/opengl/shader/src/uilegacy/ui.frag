#version 430 core

layout (location = 0) out vec4 color;

uniform vec3 color_multiplier;
uniform sampler2D tex;
uniform sampler2D bg;
uniform vec3 border_color;
uniform float border_size;
uniform float aspect_ratio;
uniform vec3 background_color_multiplier;

in vec2 tex_coords;
in vec2 pos;

void main()
{
	float ld = abs(pos.x + 1);
	float rd = abs(pos.x - 1);
	float td = abs(pos.y - 1);
	float bd = abs(pos.y + 1);
	
	vec2 new_tex_coords = tex_coords;//TODO: Calculate on vertex shader
	new_tex_coords.x *= (1 + border_size);
	new_tex_coords.x -= border_size/2;
	new_tex_coords.y *= (1 + border_size);
	new_tex_coords.y -= border_size/2;
	
	if(ld < border_size || rd < border_size || (td/aspect_ratio) < border_size || (bd/aspect_ratio) < border_size)
	{
		color = vec4(border_color, 1);
	}
	else
	{
		vec4 tex_color = texture(tex, new_tex_coords);
		if(tex_color.w < 1 || new_tex_coords.x > 1 || new_tex_coords.x < 0 || new_tex_coords.y > 1 || new_tex_coords.y < 0)
		{
			tex_color = texture(bg, tex_coords);
			if(tex_color.w < 1)
			{
				discard;
			}
			else
			{
				color = vec4(background_color_multiplier, 1) * tex_color;
			}
		}
		else
		{
			color = vec4(color_multiplier, 1) * tex_color;
		}
	}
	gl_FragDepth = 0;
}