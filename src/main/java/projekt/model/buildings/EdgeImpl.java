package projekt.model.buildings;

import javafx.beans.property.Property;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.HexGrid;
import projekt.model.Intersection;
import projekt.model.Player;
import projekt.model.TilePosition;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Edge}.
 *
 * @param grid      the HexGrid instance this edge is placed in
 * @param position1 the first position
 * @param position2 the second position
 * @param roadOwner the road's owner, if a road has been built on this edge
 * @param port      a port this edge provides access to, if any
 */
public record EdgeImpl(
    HexGrid grid, TilePosition position1, TilePosition position2, Property<Player> roadOwner, Port port
) implements Edge {
    @Override
    public HexGrid getHexGrid() {
        return grid;
    }

    @Override
    public TilePosition getPosition1() {
        return position1;
    }

    @Override
    public TilePosition getPosition2() {
        return position2;
    }

    @Override
    public boolean hasPort() {
        return port != null;
    }

    @Override
    public Port getPort() {
        return port;
    }

    /**
     * Returns whether this edge connects to another edge.
     * @param other the other edge
     * @return true if and only if the two edges are connected via a common intersection
     */
    @Override
    @StudentImplementationRequired("H1.3")
    public boolean connectsTo(final Edge other) {
        // H1.3
        Set<Intersection> thisIntersections = getIntersections();
        // Get intersections connected by the other edge
        Set<Intersection> otherIntersections = other.getIntersections();
        // Check if there is any common intersection between the two sets
        return thisIntersections.stream().anyMatch(otherIntersections::contains);
    }

    /**
     * Returns the intersections connected by this edge.
     * @return the set of intersections connected by this edge
     */
    @Override
    @StudentImplementationRequired("H1.3")
    public Set<Intersection> getIntersections() {
        // H1.3
        return grid.getIntersections().values().stream()
            .filter(intersection -> intersection.getAdjacentTilePositions().containsAll(Set.of(position1, position2)))
            .collect(Collectors.toSet());
    }

    @Override
    public Property<Player> getRoadOwnerProperty() {
        return roadOwner;
    }

    /**
     * Returns the set of edges connected to this edge that are owned by the given player
     * @param player the player to check for
     * @return the set of edges connected to this edge that are owned by the given player
     */
    @Override
    @StudentImplementationRequired("H1.3")
    public Set<Edge> getConnectedRoads(final Player player) {
        // H1.3
        // Get intersections connected by this edge
        Set<Intersection> intersections = getIntersections();
        // Find all edges that are connected to the intersections of this edge and are owned by the player
        return grid.getEdges().values().stream()
            .filter(edge ->
                edge.getRoadOwnerProperty().getValue() == player &&
                    intersections.stream().anyMatch(intersection ->
                        intersection.getAdjacentTilePositions().contains(edge.getPosition1()) ||
                            intersection.getAdjacentIntersections().contains(edge.getPosition2())))
            .collect(Collectors.toSet());
    }
}
