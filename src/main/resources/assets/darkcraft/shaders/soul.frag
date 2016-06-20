 #version 110
	uniform sampler2D texture0;
	uniform sampler2D texture1;
	uniform sampler2D texture2;
	
	vec4 a;
	//------------------------
	//------------------------
	void main()
	{
		vec4 texel0 = texture2D(texture0, gl_TexCoord[1].st);
		vec4 texel1 = texture2D(texture1, gl_TexCoord[0].st);
		vec4 texel2 = texture2D(texture2, gl_TexCoord[2].st);
		gl_FragColor = mix(texel0,texel1,texel1.a);
	}