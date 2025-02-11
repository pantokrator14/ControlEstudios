public class MainMenuController {
    private boolean darkMode = false;

    @FXML
    private void toggleTheme() {
        darkMode = !darkMode;
        Scene scene = ((Node) event.getSource()).getScene();
        scene.getRoot().getStyleClass().removeAll("dark", "light");
        scene.getRoot().getStyleClass().add(darkMode ? "dark" : "light");
    }
}