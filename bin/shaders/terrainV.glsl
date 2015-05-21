#version 400 core

#include "water.glvh"
#include "fog.glvh"

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec2 TexCoord;
smooth out vec3 Normal;
out vec3 ToLightVector[4];
out vec3 ToCameraVector;
out vec3 WorldPos;
out vec3 CameraPos;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

uniform vec3 lightPosition[4];



void main()
{
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	WorldPos = worldPosition.xyz;

	setClippingDistance(worldPosition);

	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	CalcFog(positionRelativeToCamera);
	CameraPos = positionRelativeToCamera.xyz;

	gl_Position = projectionMatrix * positionRelativeToCamera;
	TexCoord = texCoord;


	Normal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	
	for (int i = 0; i < 4; i++)
	{
		ToLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	
	ToCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	CalcFog(positionRelativeToCamera);
}