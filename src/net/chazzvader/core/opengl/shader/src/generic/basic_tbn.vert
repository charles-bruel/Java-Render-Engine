#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texture_coordinates;
layout (location = 3) in vec3 in_normal;
layout (location = 4) in vec3 in_tangent;
layout (location = 5) in vec3 in_bitangent;

uniform mat4 view_matrix;
uniform mat4 world_matrix;
uniform mat4 proj_matrix;

out vec3 normal;
out vec3 frag_pos;
out vec2 tex_coords;
out mat3 tbn;

void main()
{
	//PASS DATA
	tex_coords = in_texture_coordinates;
	
	//POSITION
	frag_pos = vec3(world_matrix * vec4(in_position, 1));
	vec4 final = proj_matrix * view_matrix * world_matrix * vec4(in_position, 1);
	gl_Position = final;
	
	//TBN
	vec3 T = normalize(vec3(world_matrix * vec4(in_tangent,   0.0)));
	vec3 B = normalize(vec3(world_matrix * vec4(in_bitangent, 0.0)));
	vec3 N = normalize(vec3(world_matrix * vec4(in_normal,    0.0)));
	tbn = mat3(T, B, N);
}