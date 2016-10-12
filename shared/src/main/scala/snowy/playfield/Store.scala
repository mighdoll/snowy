package snowy.playfield

/** collection for a set of playfield objects and grid to locate them efficiently on the playfield */
case class Store[A <: PlayfieldObject](items: Set[A] = Set[A](), grid: Grid[A] = Grid[A]()) {

  /** @return a copy of the store, with all items replaced */
  def replaceItems(fn: A => A): Store[A] = {
    val newItems = items.map(fn)
    val newGrid = Grid(grid.size, grid.spacing, newItems)
    Store(newItems, newGrid)
  }

  /** @return a copy of the store with all matching items removed */
  def removeMatchingItems(fn: A => Boolean): Store[A] = {
    val toRemove = items.filter(fn(_))
    val newGrid = toRemove.foldLeft(grid) { (grid, item) => grid.remove(item) }
    val newSet = toRemove.foldLeft(items) { (set, item) => set - item }
    Store[A](newSet, newGrid)
  }

  /** @return a copy of the store with one item added */
  def add(item: A): Store[A] = {
    Store(items + item, grid.add(item))
  }

  /** @return a copy of the store with one item removed */
  def remove(item: A): Store[A] = {
    Store(items - item, grid.remove(item))
  }

  /** @return a copy of the store with one item replaced with a new version */
  def replaceById(id: PlayId[A])(fn: A => A): Store[A] = {
    items.find { item => item.id == id } match {
      case Some(item) =>
        val newItem = fn(item)
        Store(items - item + newItem, grid.remove(item).add(newItem))
      case None       =>
        this
    }
  }

  def debugPrint(prefix:String):Unit = {
    println(s"$prefix size:${items.size}")
    items.foreach{item =>
      println(s"  $item")
    }
  }
}
