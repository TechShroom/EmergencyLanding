#version 150 core

uniform mat4 orthoMatrix;

in vec4 in_Position;
in vec4 in_Color;
in vec2 in_texCoord;

out vec4 pass_Color;
out vec2 pass_texCoord;

void main(void) {
	// Override gl_Position with our new calculated position
	gl_Position = orthoMatrix * in_Position;
	
	pass_Color = in_Color;
	pass_texCoord = in_texCoord;
}
