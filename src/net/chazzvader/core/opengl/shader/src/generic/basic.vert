#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texture_coordinates;
layout (location = 3) in vec3 in_normal;

uniform mat4 view_matrix;
uniform mat4 world_matrix;
uniform mat4 proj_matrix;
uniform mat4 normal_matrix;

out vec3 normal;
out vec3 frag_pos;
out vec2 tex_coords;

void main()
{
	vec4 final = proj_matrix * view_matrix * world_matrix * vec4(in_position, 1);
	frag_pos = vec3(world_matrix * vec4(in_position, 1));
	normal = normalize(mat3(normal_matrix) * in_normal);
	tex_coords = in_texture_coordinates;
	gl_Position = final;
}