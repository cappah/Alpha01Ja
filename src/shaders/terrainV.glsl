#version 400 core

#include "water.glvh"
#include "fog.glvh"

in vec3 position;
in vec2 texCoord;
in vec3 normal;

out vec2 TexCoord;
out vec3 SurfaceNormal;
out vec3 ToLightVector[4];
out vec3 ToCameraVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 lightPosition[4];


void main()
{
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

	setClippingDistance(worldPosition);

	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	TexCoord = texCoord;
	
	SurfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	
	for (int i = 0; i < 4; i++)
	{
		ToLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	
	ToCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	CalcFog(positionRelativeToCamera);
}