package projekt.view.menus;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.PlayerImpl;
import projekt.model.PlayerImpl.Builder;

import java.util.function.Supplier;

/**
 * A Builder to create the create game view.
 * The create game view lets users add and remove players and start the game.
 * It is possible to give each player a name, a color and to select whether the
 * player is a bot or not.
 */
public class CreateGameBuilder extends MenuBuilder {
    private final ObservableList<PlayerImpl.Builder> observablePlayers;
    private final Supplier<Boolean> startGameHandler;

    /**
     * Creates a new CreateGameBuilder with the given players and handlers.
     *
     * @param players          The list of players to display and modify.
     * @param returnHandler    The handler to call when the user wants to return to
     *                         the main menu
     * @param startGameHandler The handler to call when the user wants to start the
     *                         game
     */
    @DoNotTouch
    public CreateGameBuilder(
        final ObservableList<PlayerImpl.Builder> players,
        final Runnable returnHandler,
        final Supplier<Boolean> startGameHandler
    ) {
        super("Start new Game", returnHandler);
        this.startGameHandler = startGameHandler;
        this.observablePlayers = players;
    }

    @Override
    protected Node initCenter() {
        final VBox mainBox = new VBox();
        mainBox.setStyle("-fx-font-size: 2em");
        // For icons see https://pictogrammers.com/library/mdi/
        final VBox playerListVBox = new VBox();
        this.observablePlayers.subscribe(() -> {
            playerListVBox.getChildren().clear();
            for (final PlayerImpl.Builder playerBuilder : this.observablePlayers) {
                final HBox playerListingHBox = new HBox();
                playerListingHBox.setAlignment(Pos.CENTER);
                final TextField playerNameTextField = new TextField(playerBuilder.nameOrDefault());
                playerNameTextField.setOnKeyPressed(e -> {
                    final String newName = playerNameTextField.getText();
                    if (newName.isBlank()) {
                        playerBuilder.name(null);
                        playerNameTextField.setText(playerBuilder.nameOrDefault());
                        playerNameTextField.selectAll();
                    } else {
                        playerBuilder.name(newName);
                    }
                });

                // Add the remove player button
                Button removePlayerButton = createRemovePlayerButton(playerBuilder.getId());
                playerListingHBox.getChildren().add(removePlayerButton);

                // Add the color picker
                Node colorPicker = createPlayerColorPicker(playerBuilder);
                playerListingHBox.getChildren().add(colorPicker);

                // Add the bot or player selector
                Node botOrPlayerSelector = createBotOrPlayerSelector(playerBuilder);
                playerListingHBox.getChildren().add(botOrPlayerSelector);

                playerListingHBox.getChildren().addAll(
                    playerNameTextField
                );
                playerListVBox.getChildren().add(playerListingHBox);
            }
        });

        // Add the add player button
        Node addPlayerButton = createAddPlayerButton();
        mainBox.getChildren().add(addPlayerButton);

        final Button startGameButton = new Button("Start Game");
        final Label startGameErrorLabel = new Label();
        startGameButton.setOnAction(e -> {
            if (!this.startGameHandler.get()) {
                startGameErrorLabel.setText("Cannot start game");
            }
        });

        mainBox.getChildren().addAll(
            playerListVBox,
            startGameButton,
            startGameErrorLabel
        );
        mainBox.alignmentProperty().set(Pos.TOP_CENTER);
        return mainBox;
    }

    /**
     * Creates a button to add a new player to the game.
     * The button adds a new player to the list of players when clicked.
     *
     * @return a button to add a new player to the game
     */
    @StudentImplementationRequired("H3.4")
    private Node createAddPlayerButton() {
        // H3.4
        Button addPlayerButton = new Button("Add Player");
        addPlayerButton.setOnAction(e -> {
            PlayerImpl.Builder newPlayerBuilder = nextPlayerBuilder();
            observablePlayers.add(newPlayerBuilder);
        });
        return addPlayerButton;
    }

    /**
     * Creates a color picker to select the color of the player.
     * Two players cannot have the same color.
     *
     * @param playerBuilder the builder for the player to create the color picker
     *                      for
     * @return a color picker to select the color of the player
     */
    @StudentImplementationRequired("H3.4")
    private Node createPlayerColorPicker(final Builder playerBuilder) {
        // H3.4
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> {
            Color newColor = colorPicker.getValue();
            boolean colorExists = observablePlayers.stream()
                .anyMatch(otherPlayerBuilder -> otherPlayerBuilder.getColor() == newColor);
            if (colorExists) {
                // Handle the case where the color is already in use
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This color is already taken by another player!");
                alert.show();
            } else {
                playerBuilder.color(newColor);
            }
        });
        return colorPicker;
    }

    /**
     * Creates a node to select whether the player is a bot or not.
     *
     * @param playerBuilder the builder for the player to create the selector for
     * @return a node to select whether the player is a bot or not
     */
    @StudentImplementationRequired("H3.4")
    private Node createBotOrPlayerSelector(final Builder playerBuilder) {
        // H3.4
        CheckBox botCheckBox = new CheckBox("Is AI?");
        botCheckBox.setSelected(playerBuilder.isAi());
        botCheckBox.setOnAction(e -> playerBuilder.ai(botCheckBox.isSelected()));
        return botCheckBox;
    }

    /**
     * Creates a button to remove the player with the given id.
     *
     * @param id the id of the player to remove
     * @return a button to remove the player with the given id
     */
    @StudentImplementationRequired("H3.4")
    private Button createRemovePlayerButton(final int id) {
        // H3.4
        Button removePlayerButton = new Button("Remove Player");
        removePlayerButton.setOnAction(e -> removePlayer(id));
        return removePlayerButton;
    }

    /**
     * Removes the player with the given id and updates the ids of the remaining
     * players.
     *
     * @param id the id of the player to remove
     */
    @StudentImplementationRequired("H3.4")
    private void removePlayer(final int id) {
        // H3.4
        observablePlayers.removeIf(playerBuilder -> playerBuilder.getId() == id);
        int newId = 1;
        for (PlayerImpl.Builder playerBuilder : observablePlayers) {
            playerBuilder.id(newId++);
        }
    }

    /**
     * Returns a new {@link PlayerImpl.Builder} for the player with the next id.
     *
     * @return a new {@link PlayerImpl.Builder} for the player with the next id
     */
    public PlayerImpl.Builder nextPlayerBuilder() {
        return new PlayerImpl.Builder(this.observablePlayers.size() + 1);
    }

}
