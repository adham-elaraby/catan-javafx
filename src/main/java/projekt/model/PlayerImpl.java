package projekt.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.Config;
import projekt.model.buildings.Port;
import projekt.model.buildings.Settlement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static projekt.Config.MAX_CITIES;
import static projekt.Config.MAX_ROADS;
import static projekt.Config.MAX_VILLAGES;

/**
 * Default implementation of {@link Player}.
 */
public class PlayerImpl implements Player {
    private final HexGrid hexGrid;
    private final String name;
    private final int id;
    private final Color color;
    private final boolean ai;
    private final Map<ResourceType, Integer> resources = new HashMap<>();
    private final Map<DevelopmentCardType, Integer> developmentCards = new HashMap<>();
    private final Map<DevelopmentCardType, Integer> playedDevelopmentCards = new HashMap<>();

    @DoNotTouch("Please don't create a public Contructor, use the Builder instead.")
    private PlayerImpl(final HexGrid hexGrid, final Color color, final int id, final String name, final boolean ai) {
        this.hexGrid = hexGrid;
        this.color = color;
        this.id = id;
        this.name = name;
        this.ai = ai;
    }

    @Override
    public HexGrid getHexGrid() {
        return this.hexGrid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public boolean isAi() {
        return this.ai;
    }

    @Override
    public int getVictoryPoints() {
        final int buildingVictoryPoints = getSettlements().stream()
            .mapToInt(settlement -> settlement.type().resourceAmount)
            .sum();
        final int developmentCardsVictoryPoints = developmentCards.getOrDefault(DevelopmentCardType.VICTORY_POINTS, 0);

        return buildingVictoryPoints + developmentCardsVictoryPoints;
    }

    /**
     * Returns an unmodifiable collection of all the rescoources the player has.
     * @return unmodifiable map of resources
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public Map<ResourceType, Integer> getResources() {
        return Collections.unmodifiableMap(resources);
    }

    /**
     * Adds the given amount of the given resource to the player.
     * @param resourceType the type of resource to add
     * @param amount the amount of the resource to add
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public void addResource(final ResourceType resourceType, final int amount) {
        // put takes the key and the value
        // we use getOrDefault to get the current value of the resourceType and add the amount to it
        // if the resourceType is not in the map, we give it a default value of 0 and add the amount to it
        resources.put(resourceType, resources.getOrDefault(resourceType, 0) + amount);
    }

    /**
     * Adds multiple resources to the player.
     * @param resources a mapping of the to be added resources to their respective amounts
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public void addResources(final Map<ResourceType, Integer> resources) {
        // goes through the map and adds the resources to the player
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            addResource(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns true if the player has at least the given amount of the resource.
     * Returns false otherwise.
     * @param resources a mapping of resources to their amounts to check
     * @return true if the player has at least the given amount of the resource, false otherwise
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public boolean hasResources(final Map<ResourceType, Integer> resources) {
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            // get the value mapped to the key in the map, if the key is not in the map, we pass 0 forward
            // if the value is less than the entry value (from the parameter), we return false
            if (this.resources.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false; // if the player does not have enough resources, return false
            }
        }
        return true; // player has enough resources
    }

    /**
     * Removes the given amount of the given resource from the player.
     * @param resourceType the type of resource to remove
     * @param amount the amount of the resource to remove
     * @return true if the player had enough resources to remove, false otherwise
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public boolean removeResource(final ResourceType resourceType, final int amount) {
        // get the amount of the resourceType the player has, and check if he has enough
        int currentAmount = resources.getOrDefault(resourceType, 0);
        if (currentAmount < amount) {
            return false;
        }
        // overwrite the amount of the resourceType with the new amount
        resources.put(resourceType, currentAmount - amount);
        return true;
    }

    /**
     * Removes the given resources from the player.
     * @param resources a mapping of the to be removed resources to their respective amounts
     * @return true if the player had enough resources to remove, false otherwise
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public boolean removeResources(final Map<ResourceType, Integer> resources) {
        // compare the resources map passed as parameter with the player's resources
        if (!hasResources(resources)) {
            // does not have enough resources
            return false;
        }
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            removeResource(entry.getKey(), entry.getValue());
        }
        return true;
    }

    /**
     * This is a private helping method for getTradeRatio, that checks if the player has a special port for the given resourceType.
     * @param resourceType the resourceType to check for, as special port only works for one resourceType
     * @return true if the player has a special port for the given resourceType, false otherwise
     */
    private boolean hasSpecialPort(ResourceType resourceType) {
        // goes through settlements >> intersections >> ports and checks if the player has a special port for the given resourceType
        for (Settlement settlement : getSettlements()) {
            Port port = settlement.intersection().getPort();
            // check if the port resourceType is the same as the given resourceType and the ratio is 2 (special port)
            if (port != null && port.resourceType() == resourceType && port.ratio() == 2) {
                return true; // player has a special port for the given resourceType
            }
        }
        return false; // player does not have a special port for the given resourceType
    }

    /**
     * This is a private helping method for getTradeRatio, that checks if the player has a general port.
     * @return true if the player has a general port, false otherwise
     */
    private boolean hasGeneralPort() {
        // goes through settlements >> intersections >> ports and checks if the player has a general port
        for (Settlement settlement : getSettlements()) {
            Port port = settlement.intersection().getPort();
            // port.resourceType() == null means that the port is a general port and accepts any resourceType
            if (port != null && port.resourceType() == null && port.ratio() == 3) {
                return true; // player has a general port
            }
        }
        return false;
    }

    /**
     * Returns the ratio the player can trade the given resource for with the bank based on available Ports.
     * @param resourceType the resource to trade
     * @return the ratio the player can trade the given resource for with the bank, 4 if no port, 3 if general port, 2 if special port
     */
    @Override
    @StudentImplementationRequired("H1.1")
    public int getTradeRatio(final ResourceType resourceType) {
        if (hasSpecialPort(resourceType)) {
            return 2;
        } else if (hasGeneralPort()) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public int getRemainingRoads() {
        return MAX_ROADS - getRoads().size();
    }

    @Override
    public int getRemainingVillages() {
        return (int) (
            MAX_VILLAGES - getSettlements().stream()
                .filter(settlement -> settlement.type().equals(Settlement.Type.VILLAGE)).count()
        );
    }

    @Override
    public int getRemainingCities() {
        return (int) (
            MAX_CITIES - getSettlements().stream()
                .filter(settlement -> settlement.type().equals(Settlement.Type.CITY)).count()
        );
    }

    @Override
    @StudentImplementationRequired("H1.2")
    public Map<DevelopmentCardType, Integer> getDevelopmentCards() {
        // TODO: H1.2
        return org.tudalgo.algoutils.student.Student.crash("H1.2 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("H1.2")
    public void addDevelopmentCard(final DevelopmentCardType developmentCardType) {
        // TODO: H1.2
        org.tudalgo.algoutils.student.Student.crash("H1.2 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("H1.2")
    public boolean removeDevelopmentCard(final DevelopmentCardType developmentCardType) {
        // TODO: H1.2
        return org.tudalgo.algoutils.student.Student.crash("H1.2 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("H1.2")
    public int getTotalDevelopmentCards() {
        // TODO: H1.2
        return org.tudalgo.algoutils.student.Student.crash("H1.2 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("H1.2")
    public int getKnightsPlayed() {
        // TODO: H1.2
        return org.tudalgo.algoutils.student.Student.crash("H1.2 - Remove if implemented");
    }

    /**
     * Builder for {@link PlayerImpl}.
     * Allows to create a new player and modify its properties before building it.
     */
    @DoNotTouch
    public static class Builder {
        private int id;
        private Color color;
        private @Nullable String name;
        private final SimpleBooleanProperty ai = new SimpleBooleanProperty(false);

        /**
         * Creates a new builder for a player with the given id.
         *
         * @param id the id of the player to create
         */
        public Builder(final int id) {
            this.id = id;
            color(null);
        }

        /**
         * Returns the color of the player.
         *
         * @return the color of the player
         */
        public Color getColor() {
            return this.color;
        }

        /**
         * Sets the color of the player.
         *
         * @param playerColor the color of the player
         * @return this builder
         */
        public Builder color(final Color playerColor) {
            this.color = playerColor == null
                         ? new Color(
                Config.RANDOM.nextDouble(),
                Config.RANDOM.nextDouble(),
                Config.RANDOM.nextDouble(),
                1
            )
                         : playerColor;
            return this;
        }

        /**
         * Returns the name of the player.
         *
         * @return the name of the player
         */
        public @Nullable String getName() {
            return this.name;
        }

        /**
         * Sets the name of the player.
         *
         * @param playerName the name of the player
         * @return this builder
         */
        public Builder name(final @Nullable String playerName) {
            this.name = playerName;
            return this;
        }

        /**
         * Returns the name of the player or a default name if no name was set.
         * The default name is "Player" followed by the id of the player.
         *
         * @return the name of the player or a default name if no name was set
         */
        public String nameOrDefault() {
            return this.name == null ? String.format("Player%d", this.id) : this.name;
        }

        /**
         * Sets the id of the player.
         *
         * @param newId the id of the player
         * @return this builder
         */
        public Builder id(final int newId) {
            this.id = newId;
            return this;
        }

        /**
         * Returns the id of the player.
         *
         * @return the id of the player
         */
        public int getId() {
            return this.id;
        }

        /**
         * Returns whether the player is an AI.
         *
         * @return whether the player is an AI
         */
        public boolean isAi() {
            return this.ai.get();
        }

        /**
         * Returns the property indicating whether the player is an AI.
         *
         * @return the property indicating whether the player is an AI
         */
        public SimpleBooleanProperty aiProperty() {
            return this.ai;
        }

        /**
         * Sets whether the player is an AI.
         *
         * @param ai whether the player is an AI
         * @return this builder
         */
        public Builder ai(final boolean ai) {
            this.ai.set(ai);
            return this;
        }

        /**
         * Builds the player with the properties set in this builder.
         *
         * @param grid the grid the player is on
         * @return the player with the properties set in this builder
         */
        public Player build(final HexGrid grid) {
            return new PlayerImpl(grid, this.color, this.id, nameOrDefault(), this.ai.get());
        }
    }

    @Override
    public String toString() {
        return String.format("Player %d %s (%s)", getID(), getName(), getColor());
    }
}
