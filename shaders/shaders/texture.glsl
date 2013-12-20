#version 150 core

uniform sampler2D texture_diffuse;
uniform float utexenabled;

in vec4 pass_Color;
in vec2 pass_texCoord;

out vec4 out_Color;

void main(void) {
	if(utexenabled > 0.5) {
	    out_Color = texture(texture_diffuse, pass_texCoord);
	} else {
		out_Color = pass_Color;
	}
}
