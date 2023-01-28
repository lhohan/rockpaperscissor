# Rock Paper Scissors in Scala

A 'vanilla' Scala implementation using OO and FP concepts.

OO: Used for dependency management.

FP: Same input results in same output, also higher-order functions. Side-effects, notably the console, are abstracted but not managed using as effects. 

## Running and development

This implementation uses `scala-cli`.

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