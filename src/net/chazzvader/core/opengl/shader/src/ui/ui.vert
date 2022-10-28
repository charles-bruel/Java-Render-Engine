#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texture_coordinates;

uniform mat4 world_matrix;
uniform vec4 override_tex_coords;

out vec2 tex_coords;
out vec2 pos;

void main()
{
	vec4 final = world_matrix * vec4(in_position, 1);	
	gl_Position = final;
	vec2 tempTexCoords = in_texture_coordinates;
	tempTexCoords.x = (1-tempTexCoords.x)*override_tex_coords.x + tempTexCoords.x*override_tex_coords.z;
	tempTexCoords.y = (1-tempTexCoords.y)*override_tex_coords.y + tempTexCoords.y*override_tex_coords.w;
	tex_coords = tempTexCoords;
	pos = in_position.xy;
}