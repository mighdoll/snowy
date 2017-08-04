package snowy.util

import scala.collection.mutable.MutableList

/** Extend a mutable list with a removeElement method */
object RemoveList {
  implicit class RemoveListOps[A](val list: MutableList[A]) extends AnyVal {
    /** remove an element if present in the list
      * @return true if element was removed */
    def removeElement[A](elem: A): Boolean = {
      val origLength = list.length
      val newList    = list.filter(_ != elem)
      list.clear()
      list ++= newList
      newList.length != origLength
    }
  }
}
