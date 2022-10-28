#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texture_coordinates;

uniform mat4 view_matrix;
uniform mat4 world_matrix;
uniform mat4 proj_matrix;

out vec2 tex_coords;

void main()
{
	vec4 final = proj_matrix * view_matrix * world_matrix * vec4(in_position, 1);
	gl_Position = final;
	tex_coords = in_texture_coordinates;
}