# pokoban-expert

Maps:
30 x 30

Actions:

* MoveNorth, -South, -East, -West
* PushNorth, -South, -East, -West
* PullNorth, -South, -East, -West 

len(Actions) = 12

## Server API:

### Init
Url: /game/init/{fileName}

#### Reponse
```json
{
 state: State,
 map: <Content of file>,
 gameID: string
}
´´´

### Transistion
Url: /game/{gameID}/{action}

#### Response:
```json
{
    state: State, 
    action: string, 
    reward: 
    number, 
    done: bool,
    success: bool
  }
```

## Output to file:
```json
{
 initial: State
 transitions: [
  {
    state: State, 
    action: string, 
    reward: 
    number, 
    done: bool,
    success: bool
  }
 ]
}
```




State representation:
{
  boxes: [],
  walls: [],
  agents: [],
  goals: [],
  dimensions: number (assuming square)
}

Where:

* action: String-represention fra Pokoban-domænet
* reward: number
* done: boolean

Todo:

- Limit actions in expert
- Interface: 
- 0 eller 1 indexed?


