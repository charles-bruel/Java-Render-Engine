#version 430 core

layout (location = 0) out vec4 color;


//LIGHTS
struct point_light
{
	vec3 light_color;//4 8 12
    float linear;//16
	vec3 light_pos;//20 24 28
    float quadratic;//32
    float constant;//36
    int textureID;//40
    float far_plane;//44
    float padding;//48
};

layout (std430, binding = 0) buffer lights_buffer
{
	int num_lights;
	point_light point_lights[];
};

//AMIENT
uniform float ambient_strength = 0.1;
uniform vec3 ambient_color;

//SPECULAR
uniform int specular_power;
uniform sampler2D specular;
uniform float specular_strength;

//COLOR
uniform vec3 color_multiplier;
uniform sampler2D diffuse;

//NORMALS
uniform sampler2D normals_tex;

//SHADOW MAPS
uniform samplerCube depth_maps[4];

//MATH
in vec3 frag_pos;
in vec2 tex_coords;
in mat3 tbn;
uniform vec3 view_pos;

//DEBUG FLAGS
uniform int tc_debug_flag;//1 for true and false for everything else.
uniform int normal_debug_flag;//1 for true and false for everything else.

//FUNCTION PROTOTYPES
vec3 ambient();
vec3 point(int index, vec3 view_dir, vec3 normal);
vec3 gz(vec3 vector);
float shadow(vec3 frag_pos, int index, vec3 normal, vec3 light_dir);
vec4 regular();
vec4 tc_debug();
vec4 normal_debug();

//VARIABLES
vec3 combined_normals;

void main()
{
	vec4 result;
	if(normal_debug_flag == 1)
	{
		result = normal_debug();
	}
	else if(tc_debug_flag == 1)
	{
		result = tc_debug();
	}
	else
	{
		result = regular();
	}
	color = result;
}

vec4 normal_debug()
{
	combined_normals = texture(normals_tex, tex_coords).rgb;
    combined_normals = normalize(combined_normals * 2.0 - 1.0);
    combined_normals = normalize(tbn * combined_normals);
    
    return vec4((combined_normals+vec3(1, 1, 1))/2, 1);
}

vec4 tc_debug()
{
	return vec4(tex_coords, 0, 1);
}

vec4 regular()
{
	//COLOR
	vec4 buffer_color = texture(diffuse, tex_coords);
	if(buffer_color.w < 1)
	{
		//discard;
	}
	vec3 temp_color = buffer_color.xyz * color_multiplier;
	
	//CALCULATE NORMALS
	combined_normals = texture(normals_tex, tex_coords).rgb;
    combined_normals = normalize(combined_normals * 2.0 - 1.0);
    combined_normals = normalize(tbn * combined_normals);
	
	//LIGHTING
    vec3 ambient = ambient();
    vec3 view_dir = normalize(view_pos - frag_pos);
	vec3 temp = vec3(0, 0, 0);
	for(int i = 0;i < num_lights;i ++)
	{
		temp += point(i, view_dir, combined_normals);
	}
	
	//COMBINED
	vec3 result = (temp + ambient) * temp_color;
	return vec4(result, 1.0);
}

vec3 point(int index, vec3 view_dir, vec3 normal)
{
	vec3 light_color = point_lights[index].light_color;
	vec3 light_pos = point_lights[index].light_pos;
	
	//float constant = point_lights[index].constant;
	float constant = 1;
    float linear = point_lights[index].linear;
    float quadratic = point_lights[index].quadratic;

	float distance = length(light_pos - frag_pos);
	float attenuation = 1.0 / (constant + linear * distance + quadratic * (distance * distance));
	
	//DIFFUSE
	vec3 light_dir = normalize(light_pos - frag_pos);  
	float diff = max(dot(light_dir, combined_normals), 0.0);
	vec3 diffuse = diff * light_color;

	//SPECULAR
	vec3 halfway_dir = normalize(light_dir + view_dir);
	float spec = pow(max(dot(view_dir, halfway_dir), 0.0), specular_power);
	vec3 specular = vec3(texture(specular, tex_coords)) * spec * light_color * specular_strength;  

	return gz((diffuse + specular) * attenuation * (1.0 - shadow(frag_pos, index, normal, light_dir)));
}

vec3 ambient()
{
	return ambient_strength * ambient_color;
}

vec3 gz(vec3 vector)
{
	return vec3(max(vector.x, 0), max(vector.y, 0), max(vector.z, 0));
}

float shadow(vec3 frag_pos, int index, vec3 normal, vec3 light_dir)
{
	vec3 frag_to_light_dir = frag_pos - point_lights[index].light_pos; 
    float closest_depth = texture(depth_maps[point_lights[index].textureID], frag_to_light_dir).r;
    closest_depth *= point_lights[index].far_plane;
    float current_depth = length(frag_to_light_dir);
    //float shadow_bias = max(0.075 * (1.0 - dot(normal, light_dir)), 0.01);  
    float shadow_bias = 0.05;
    float shadow = current_depth -  shadow_bias > closest_depth ? 1.0 : 0.0;
    //color = vec4(vec3(texture(depth_maps[point_lights[index].textureID], frag_to_light_dir).r), 1.0);
    //color = vec4(vec3(shadow), 1);
    return shadow;
}