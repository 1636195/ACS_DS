package baseNoStates;

/**
 * Interface for the Visitor design pattern applied to the area tree.
 * Each concrete visitor implements a different operation that traverses
 * the tree of partitions and spaces.
 *
 * @param <T> the type of result produced by the visitor
 */
public interface AreaVisitor<T> {

  /**
   * Visits a leaf space.
   *
   * @param space the space to visit
   * @return the result of the operation
   */
  T visit(Space space);

  /**
   * Visits a composite partition.
   *
   * @param partition the partition to visit
   * @return the result of the operation
   */
  T visit(Partition partition);
}