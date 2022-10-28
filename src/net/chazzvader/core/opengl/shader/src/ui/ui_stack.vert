#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texture_coordinates;

uniform mat4 world_matrix;
uniform float lower_tex_coord;
uniform float upper_tex_coord;

out vec2 tex_coords;
out vec2 pos;

void main()
{
	vec4 final = world_matrix * vec4(in_position, 1);
	
	final.x -= 0.5;
	final.x *= 2;
	
	final.y -= 0.5;
	final.y *= 2;
	
	gl_Position = final;
	tex_coords = vec2(in_texture_coordinates.x, ((in_texture_coordinates.y) * (1-upper_tex_coord)) + ((1-in_texture_coordinates.y) * (1-lower_tex_coord)));
	pos = in_position.xy;
}