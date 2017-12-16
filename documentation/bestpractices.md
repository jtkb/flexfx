#### Best Practices
1. Depend upon `UtilityService` or `Toolkit` in your bundle(s). Use `UtilityService` if inflation of FXML files is required. Use `Toolkit` if you a manually building your scene's node graph and need to be certain of the readiness of the Toolkit e.g. for calling `Platform.runLater()`.
2. If inflating FXML files use the `ControllerWrapper` class together with `UtilityService.populateWrapper()` as it handles the context switching for you and provides a level of type safety.
3. Normal rules still apply if altering the current scene graph, this must be done of the JavaFX thread. Applying (1) above will ensure it is safe to call `Platform.runLater()` anytime your bundle is ACTIVE.
4. __Look at the examples in the [examples](../examples/README.md) module.__