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
``` json
{
 state: State,
 map: <Content of file>,
 gameID: string
}
```

### Transistion
Url: /game/{gameID}/{action}

#### Response:
``` json
{
    state: State, 
    action: string, 
    reward: 
    number, 
    done: bool,
    success: bool
  }
```

### Terminate
Url: /game/{gameID}/terminate

#### Response:
None

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


## State object

```json
{
  boxes: [{col: number, row: number, letter: string}, ...],
  walls: [{col: number, row: number}],
  agents: [{col: number, row: number}],
  goals: [{col: number, row: number, letter: string}],
  dimensions: number (assuming square)
}
```
