package projekt.model;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.buildings.Edge;
import projekt.model.buildings.Port;
import projekt.model.buildings.Settlement;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of {@link Intersection}.
 */
public class IntersectionImpl implements Intersection {
    private final TilePosition position0;
    private final TilePosition position1;
    private final TilePosition position2;
    private final HexGrid hexGrid;
    private Settlement settlement;

    /**
     * Creates a new intersection with the given positions.
     *
     * @param hexGrid   the hex grid
     * @param positions the positions
     */
    @DoNotTouch
    public IntersectionImpl(final HexGrid hexGrid, final List<TilePosition> positions) {
        this(positions.get(0), positions.get(1), positions.get(2), hexGrid);
    }

    /**
     * Creates a new intersection with the given positions.
     * Ensures that the positions are not null, not equal and next to each other.
     *
     * @param position0 the first position
     * @param position1 the second position
     * @param position2 the third position
     */
    @DoNotTouch
    public IntersectionImpl(final TilePosition position0, final TilePosition position1, final TilePosition position2, final HexGrid hexGrid) {
        if (position0 == null || position1 == null || position2 == null)
            throw new IllegalArgumentException("Positions must not be null");

        if (position0.equals(position1) || position0.equals(position2) || position1.equals(position2))
            throw new IllegalArgumentException("Positions must not be equal");

        if (!TilePosition.neighbours(position0).containsAll(Set.of(position1, position2))
            || !TilePosition.neighbours(position1).containsAll(Set.of(position0, position2)))
            throw new IllegalArgumentException(String.format("Positions must be neighbours: %s, %s, %s",
                                                             position0, position1, position2
            ));

        this.position0 = position0;
        this.position1 = position1;
        this.position2 = position2;
        this.hexGrid = hexGrid;
    }

    @Override
    public HexGrid getHexGrid() {
        return hexGrid;
    }

    @Override
    public Settlement getSettlement() {
        return settlement;
    }

    @Override
    public boolean hasSettlement() {
        return settlement != null;
    }

    @Override
    public boolean playerHasSettlement(final Player player) {
        return settlement != null && settlement.owner().equals(player);
    }

    /**
     * Places a village for the given player at this intersection if it is not already occupied.
     * The player must have a connected road if the ignoreRoadCheck parameter is false, otherwise the village cannot be placed.
     *
     * @param player          the player
     * @param ignoreRoadCheck if true, the connected road check is ignored
     * @return true if and only if the village was placed
     */
    @Override
    @StudentImplementationRequired("H1.4")
    public boolean placeVillage(final Player player, final boolean ignoreRoadCheck) {
        // H1.4
        // Check if the intersection is already occupied by a settlement.
        if (this.settlement != null) {
            return false;
        }
        // If we are not ignoring the road check, ensure that the player has a connected road.
        if (!ignoreRoadCheck && !playerHasConnectedRoad(player)) {
            return false;
        }
        // If the intersection is not occupied and (if required) there is a connected road,
        // place a village for the player.
        this.settlement = new Settlement(player, Settlement.Type.VILLAGE, this);
        return true;
    }

    /**
     * Upgrades the settlement at this intersection to a city if it is owned by the given player and is a village.
     *
     * @param player the player
     * @return true if and only if the settlement was upgraded
     */
    @Override
    @StudentImplementationRequired("H1.4")
    public boolean upgradeSettlement(final Player player) {
        // H1.4
        // Check if there is a settlement and if it's owned by the player.
        if (this.settlement != null && this.settlement.owner().equals(player)) {
            // Check if the settlement is already a city, which cannot be upgraded further.
            if (this.settlement.type() == Settlement.Type.CITY) {
                return false;
            }
            // Upgrade the village to a city.
            this.settlement = new Settlement(player, Settlement.Type.CITY, this);
            return true;
        }
        return false; // No settlement to upgrade or not owned by the player.
    }

    @Override
    public Port getPort() {
        return getConnectedEdges().stream()
            .filter(Edge::hasPort)
            .map(Edge::getPort)
            .findAny()
            .orElse(null);
    }

    @Override
    public Set<Edge> getConnectedEdges() {
        return Stream.of(
                Set.of(this.position1, this.position2),
                Set.of(this.position2, this.position0),
                Set.of(this.position0, this.position1)
            )
            .filter(this.hexGrid.getEdges()::containsKey)
            .map(this.hexGrid.getEdges()::get)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean playerHasConnectedRoad(final Player player) {
        return getConnectedEdges().stream()
            .anyMatch(edge -> edge.hasRoad() && edge.getRoadOwner().equals(player));
    }

    @Override
    public Set<Intersection> getAdjacentIntersections() {
        return hexGrid.getIntersections().entrySet().stream().filter(
                entry -> entry.getKey().containsAll(Set.of(position0, position1)) ||
                    entry.getKey().containsAll(Set.of(position1, position2)) ||
                    entry.getKey().containsAll(Set.of(position2, position0)))
            .map(Map.Entry::getValue)
            .filter(Predicate.not(this::equals))
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<TilePosition> getAdjacentTilePositions() {
        return Set.of(position0, position1, position2);
    }

    @Override
    public boolean isConnectedTo(final TilePosition... positions) {
        return Stream.of(positions)
            .allMatch(position -> this.position0.equals(position) || this.position1.equals(position) || this.position2.equals(position));
    }

    @Override
    public int hashCode() {
        return getAdjacentTilePositions().hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final IntersectionImpl intersection = (IntersectionImpl) o;
        return getAdjacentTilePositions().equals(intersection.getAdjacentTilePositions());
    }
}
