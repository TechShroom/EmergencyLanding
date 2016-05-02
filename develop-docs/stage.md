Stages
=====

Stages are a solution to requiring strict transitions between different parts of a game, whether that be between initialization and game play or levels and main menus. This document aims to set forth a definition of what a Stage consists of, what can be done with one, and how the whole system should work. Get ready to read.

## Part 1: Setting the Stage
### What is a stage?
_~~A miserable pile of code.~~_

A stage is a way for a game to state what the next part of the game to execute should be, as well as being a unified way to handle error propagation through the stages. It may have other stages underneath it but it cannot dictate what those stages are. This allows for ease-of-use when dependents use the stage system themselves. A stage can and should dictate what stages come after it. This will allow the manager to throw an error across invalid state transitions.

A sample stage setup might be as follows:
![Sample Setup](ascii-images/basic-stage.png)
This flow is simple to understand. The application starts in the initialization stage, then either moves on to the main stage, if there are no unrecoverable errors, or moves to the error stage. The main stage may also encounter errors, but if it doesn't then it moves to the cleanup stage.

The built-in error stage is responsible for reporting the error and then moving to the cleanup stage. This unified error reporting system will be very useful, and can also be substituted for a no-op implementation at dev-time or be changed for a specific game.

### Sub-stages

Stages can have other stages run underneath them. It is up to the parent to handle errors from child stages. The parent may either (a) choose an appropriate action itself or (b) pass the error up to its parent to handle it. The parent should NOT assume that the error was originally from one of its sub-stages, as the same mechanism is used to handle (b) above. The original stage causing the error is available to each parent to keep the context.

Simple sub-stage system underneath the Main Stage from the sample above:
![Sample Sub-stage Setup](ascii-images/basic-sub-stage.png)

(Note: The `ExitSignalException` above would be implemented without stacktraces, making it much more efficient. **Do not** use stacktraces for signal exceptions!)

### Multi-threading Stages
Stages should not exist across threads. The concept of a thread would be hard to work in to such a system while keeping it simple and easy to use. Instead, each thread should have its own Stages that do not mingle with another thread.
