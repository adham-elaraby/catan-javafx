package projekt.view.gameControls;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.Player;
import projekt.model.ResourceType;
import projekt.view.ResourceCardPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A dialog to prompt the user to select a number of resources.
 * The dialog shows the resources the player can choose from and lets the user
 * select a number of each resource.
 * If dropCards is true, the user is prompted to drop cards instead of selecting
 * them.
 * The result of the dialog is a map of the selected resources and their
 * amounts.
 */
public class SelectResourcesDialog extends Dialog<Map<ResourceType, Integer>> {

    /**
     * Creates a new SelectResourcesDialog for the given player and resources.
     *
     * @param amountToSelect        The amount of resources to select.
     * @param player                The player that is prompted to select resources.
     * @param resourcesToSelectFrom The resources the player can select from. If
     *                              null the player can select any resource.
     * @param dropCards             Whether the player should drop cards instead of
     *                              selecting them.
     */
    public SelectResourcesDialog(
        final int amountToSelect, final Player player,
        final Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        final DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().add(ButtonType.OK);
        dialogPane.setContent(init(amountToSelect, player, resourcesToSelectFrom, dropCards));
    }

    /**
     * Initializes the selection dialog with a grid of resource cards and spinners
     * to allow a player to select or drop a specified amount of resources.
     *
     * @param amountToSelect        The total amount of resources that the player needs to select or drop.
     * @param player                The player who is making the selection. This parameter can be
     *                              used for additional logic based on the player's current state.
     * @param resourcesToSelectFrom A map of resources that the player is allowed to select from.
     *                              If null, the player can select from all available resources.
     * @param dropCards             If true, the player is dropping resources instead of selecting.
     *                              This will adjust the instruction text accordingly.
     * @return A {@code Region} that contains the UI elements for the resource selection dialog.
     */

    @StudentImplementationRequired("H3.3")
    private Region init(
        final int amountToSelect, final Player player,
        Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        // TODO: H3.3
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Map<ResourceType, Integer> selectedResources = new HashMap<>();

        Label infoLabel = new Label();
        grid.add(infoLabel, 0, 0, 2, 1);

        // Initially set label with the amountToSelect
        infoLabel.setText("Resources to " + (dropCards ? "drop: " : "select: ") + amountToSelect);

        int row = 1;
        for (ResourceType resourceType : ResourceType.values()) {
            if (resourcesToSelectFrom == null || resourcesToSelectFrom.containsKey(resourceType)) {
                ResourceCardPane resourceCardPane = new ResourceCardPane(resourceType);
                grid.add(resourceCardPane, 0, row);

                int maxAmount = resourcesToSelectFrom != null ? resourcesToSelectFrom.get(resourceType) : amountToSelect;
                Spinner<Integer> spinner = new Spinner<>(0, maxAmount, 0);
                grid.add(spinner, 1, row);

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    selectedResources.put(resourceType, newValue);
                    int totalSelected = selectedResources.values().stream().mapToInt(Integer::intValue).sum();
                    infoLabel.setText("Resources to " + (dropCards ? "drop: " : "select: ") + (amountToSelect - totalSelected));

                    Node okButton = getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setDisable(totalSelected != amountToSelect);
                });

                row++;
            }
        }

        setResultConverter(button -> button == ButtonType.OK ? selectedResources : null);

        return grid;
    }
}
