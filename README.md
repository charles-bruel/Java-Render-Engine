# Java-Render-Engine

This is my oldest project that I believe still has merit. After my failure to use OpenGL to render my strategy game, I decided to make a game engine. I've always had a minimalist view when coding games; most of my early coding experience was Khan Academy then Minecraft mods, both of those heavily rely on code to create scenes, as opposed to the technique of most engines like Unity. I decided to make an engine deliberately without a graphical UI.

The project has a decent 3D rendered, with multiple lights, shadows, specular highlights, normal mapping, etc. It has a somewhat working obj file parser to import files. 

I was working on the UI part of the engine when I abandoned it. rendering a large amount of text caused stuttering. If I remember correctly, the stuttering was from the GC trying to clean up the tens of thousand of Matrix4f objects I was creating every frame to position all those letters. A feature like in C# where you can specify data types as structs which are stack allocated not heap allocated would have been great, but I chose java because that's what I knew.

![alt text](https://github.com/charles-bruel/Java-Render-Engine/blob/master/pictures/1.PNG?raw=true)
I used Kerbin from Kerbal Space Program as my primary lighting test object.

![alt text](https://github.com/charles-bruel/Java-Render-Engine/blob/master/pictures/2.PNG?raw=true)
I never got normal loading working correctly on the obj parser, I moved on before I finished it

![alt text](https://github.com/charles-bruel/Java-Render-Engine/blob/master/pictures/3.PNG?raw=true)
A program written in the engine
