![Logo](https://cdn.modrinth.com/data/ElQgDJsn/images/abf9b0b63c169d0c356a063c072f4a60cebbcc2e.png)

[![modloader - Fabric](https://img.shields.io/badge/modloader-Fabric-orange?style=for-the-badge&logo=fabricmc)](https://fabricmc.net) ![Minecraft - 1.20.1 / 1.20.4](https://img.shields.io/badge/Minecraft-1.20.1_%2F_1.20.4-2ea44f?style=for-the-badge) ![environment - Client and Server](https://img.shields.io/badge/environment-Client_and_Server-blue?style=for-the-badge) [![License - GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-805fa0?style=for-the-badge)](https://) ![Modrinth Downloads](https://img.shields.io/modrinth/dt/ElQgDJsn?style=for-the-badge&color=00cf40)
<!-- SVG version -->
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/project/wild-temperatures) [![curseforge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)]()

A temperature mod expanding on vanilla mechanics to make the game a bit more difficult but doesn't require learning complex new mechanics!

# Features

- Temperature based on vanilla temperature values
- No new items or blocks
- Make the game a more challenging without being too annoying
- Expand on temperature / weather ideas already in the game
- New enchantment, Fire Protection counterpart called Frost Protection
- Temperature HUD and overlays
- Chainmail armor recipe
- Configurable
- Left handed mode
  
# Quick Explainer

- Extreme cold biomes cause cold damage and you will freeze to death, to avoid this wear at least 3 pieces of leather armor, enchant one piece of armor with Frost Protection.
- Extreme heat biomes cause heat damage and you will burn to death, to avoid this wear at least 3 pieces of chainmail armor, or enchant one piece of armor with Fire Protection, or drink a fire resistance
- The night in deserts are extremely cold.
- Frost protection gives you the resistance effect if it's below 0.2 biome degrees (you can look on the vanilla wiki to find this :) (this also means nights in deserts)
- Jumping in water protects you from heat (wild i know)
- Blocks, torches, lava etc. do not cause temperature to change in 1.0 (this might change in the future who knows)
- also important this is my first mod and first java project so the code is probably shit and is probably not fool proof, but it works :)

# Dependencies
Fabric loader 0.15.3
Fabric API 
owo-lib

# FAQ

### **What do you mean with vanilla mechanics and concepts**

#### best explanation is giving some examples: 

vanilla already has a mechanic for freezing with powdered snow, so i try to expand upon that in ways like this: wearing leather against the cold, freezing when you are in a extremely cold biome.

vanilla doesn't really have a counterpart mechanic for that (aside from being on fire) so i tried to create a natural counter part to it for example: chain mail protects you against extreme heat biomes (it doesn't make sense irl, but i think minecraft chainmail looks very airy and it gives it a new use haha)

### **New enchantment, what's that about? I thought this was vanilla themed**

#### yes, i'll explain my thought process for adding a new enchantment:

vanilla already has fire protection of course, which you can use to protect yourself against extreme heat in this mod, however there was no counterpart for the cold.

so i added frost protection. but when i added it i realised the enchantment had no functionality outside extreme cold protection. since there are no ice mobs or ice attacks in the game it felt weird to just make it an exact replica of fire protection but for frost. 

so instead ot make it a bit more useful i added the mechanic that if you are wearing a piece of armor with cold protection in an extremely cold biome (nights in the desert count too) you receive the resistance effect!

### **Mod compatibility?**

if there is (i haven't really tested) it will be extremely basic.

which means reading the current biome temperature at player location and using that for calculations.
there's no functionality at all to add items, biomes or blocks to the calculations.

this is my first mod so yeah, however i might consider adding better support in the future when i get more experience and if there is a want for it

again the basics should work, but stuff like cold nights in a certain biome etc is impossible
the enchantment should be fine though since it takes the current biome temperature into account

### License
The GNU GPLv3 lets people do almost anything they want with this project, except distributing closed source versions. If you do use this, make sure to disclose the source, have a copy of the license and copyright notice, use the same license, and changes made must be documented. For more information read the LICENSE file on the github.
