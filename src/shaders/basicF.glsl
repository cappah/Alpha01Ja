#version 400 core

#include "fog.glfh"
#include "ambience.glfh"

const int MAX_NUM_LIGHTS = 4;

in vec4 V_Pos;
in vec2 TexCoord;
in vec3 Normal;
in vec3 ToLightVector[4];
in vec3 ToCameraVector;
in vec3 WorldPos;
in vec3 CameraPos;

out vec4 fragColor;

uniform vec3 lightColor[MAX_NUM_LIGHTS];
uniform vec3 attenuation[MAX_NUM_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

uniform sampler2D textureSampler;

#include "lightFuncs.glfh"

void main()
{
	vec3 unitNormal = normalize(Normal);
	vec3 unitVectorToCamera = normalize(ToCameraVector);
	
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for (int i = 0; i < MAX_NUM_LIGHTS; i++)
	{
		float distance = length(ToLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
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
	
	vec4 textureColor = texture(textureSampler, TexCoord);
	
	// Test alpha in texture and discard it
	if (textureColor.a < 0.5)
	{
		discard;
	}


	vec4 TotalPointLights = vec4(0, 0, 0, 0);

    for (int i = 0; i < MAX_POINT_LIGHTS; ++i)
    {
        TotalPointLights += CalcPointLight(gPointLight[i], CameraPos, unitNormal);
    }
    vec4 TotalLights = CalcDirectionalLight(gDirectionalLight, CameraPos, gDirectionalLight.direction, unitNormal) + TotalPointLights;
                        //CalcPointLight(gPointLight, CameraPos, unitNormal);

	fragColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0) +
	vec4(environmentTint(skyColor, 0.512), 0.7) + TotalLights + TotalPointLights;
	fragColor = mix(vec4(skyColor, 1.0), fragColor, Visibility);
}
