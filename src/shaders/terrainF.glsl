#version 400 core

#include "fog.glfh"
#include "ambience.glfh"
#include "multiTexture.glfh"
#include "lighting.glfh"

const int MAX_NUM_LIGHTS = 4;


in vec2 TexCoord;
in vec3 Normal;
in vec3 ToLightVector[MAX_NUM_LIGHTS];
uniform vec3 attenuation[MAX_NUM_LIGHTS];
in vec3 ToCameraVector;
in vec3 WorldPos;


out vec4 fragColor;

uniform vec3 lightColor[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;





void main()
{
	// MultiTexture terrain
	vec4 totalColor = calcMultiTexture(TexCoord);
	
	vec3 unitNormal = normalize(Normal);
	vec3 unitVectorToCamera = normalize(ToCameraVector);
	

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	for (int i = 0; i < MAX_NUM_LIGHTS; i++)
	{
		float lightDistance = length(ToLightVector[i]);

		// model how much light is available for this fragment
		float attFactor = attenuation[i].x + (attenuation[i].y * lightDistance) + (attenuation[i].z * lightDistance * lightDistance);
		vec3 unitLightVector = normalize(ToLightVector[i]);
	
		// Diffuse Lighting
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
	
		// Specular Lighting
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
	
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attFactor;
	}
	totalDiffuse = max(totalDiffuse, 0.2);

	fragColor = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 0.5) +
	environmentTint(skyColor, 0.2) +
	CalcDirectionalLight(gDirectionalLight, camPos, Normal, WorldPos);
	fragColor = mix(vec4(skyColor, 1.0), fragColor, Visibility);
}