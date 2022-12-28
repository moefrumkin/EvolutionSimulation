# EvolutionSimulation
For the final project of my CS2 class, I designed an evolution simulation in the Java programming language. 
The simulation takes place on a grid of tiles, each with a certain amount of grass, indicated by the brightness of the tile. 
An organism has a head segment, followed by a tree of pink stomachs and red legs.
Each stomach increases food capacity and each leg increases movement speed.
In each cycle of the simulation, the grass grows following a logistic growth curve, then the organisms move.
An organism will move in the direction of the tile with the most grass within its field of view.
It will then eat as much grass as its stomachs can contain from the tile it ends up on.
After that it will consume the amount of food that is required by its appendages.
If an organisms runs out of food it will die and a black X will be drawn in its place.
If it has enough food left over, it will asexually reproduce and lay a white egg that will hatch in the next cycle.
Children have random mutations that differentiate them from their parents, either structurally, in visual range, or genetic stability.
