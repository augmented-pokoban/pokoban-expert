# pokoban-expert

Maps:
30 x 30

Actions:
MoveNorth, -South, -East, -West
PushNorth, -South, -East, -West
PullNorth, -South, -East, -West 

len(Actions) = 12

{
 initial: ...,
 transitions: [
  {state (after action), action, reward, done}
 ]
}

State representation:
{
  boxes: [],
  walls: [],
  agents: [],
  goals: [],
  dimensions: number (assuming square)
}

action: String-represention fra Pokoban-domænet

reward: number

done: boolean

Todo:
- Limit actions in expert
- Interface: 
- 0 eller 1 indexed?


