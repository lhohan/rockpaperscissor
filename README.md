# Rock Paper Scissors in Scala

A 'vanilla' Scala implementation using OO and FP concepts.

OO: Used for dependency management.

FP: Same input results in same output, also higher-order functions. Side-effects, notably the console, are abstracted but not managed using as effects. 

Worthwhile in this implementation

- The tests: 
  - `RockPaperScissorsCliSuite` was written from a behavior perspective simulating 'end to end' interaction with the console. A goal here is to have a test that is relatively stable when the implementation would change.

Note, not implemented:

- Advanced computer logarithm. The current one just implements generates a random 'hand'.
  - For more advanced logic the previous outcomes and 'hands' can be passed. 
    - The function `() => Option[Hand]` would turn into something like `List[Outcome] => Option[Hand]`, where `Outcome` also contains the moves played by both players.

## Running and development

This implementation uses `scala-cli`. A nix flake is setup to setup the required environment.

Run:

```bash
scala-cli run .
```

Test (watching file changes):

```bash
scala-cli test -w .
```

IDE:

Running either of above commands generates project file for IntelliJ IDEA (bsp) and Metals (e.g. in VSCode).