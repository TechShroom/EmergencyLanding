#version 150 core

uniform mat4 orthoMatrix;

in vec4 in_Position;
in vec4 in_Color;

out vec4 pass_Color;

void main(void) {
	gl_Position = in_Position;
	// Override gl_Position with our new calculated position
	gl_Position = orthoMatrix * in_Position;
	
	pass_Color = in_Color;
}