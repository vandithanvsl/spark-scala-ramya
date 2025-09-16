object MergeSort {

  /**
   * Performs merge sort on a list of integers.
   * Recursively divides the list into halves, sorts each half,
   * and then merges the two sorted halves.
   *
   * @param list The input list of integers to sort
   * @return A new list containing the sorted elements
   */
  def mergeSort(list: List[Int]): List[Int] = {
    // Base case: lists with 0 or 1 element are already sorted
    if (list.length <= 1) list
    else {
      // Split the list approximately in half
      val (left, right) = list.splitAt(list.length / 2)

      // Recursively sort each half
      val sortedLeft = mergeSort(left)
      val sortedRight = mergeSort(right)

      // Merge the two sorted lists into one sorted list
      merge(sortedLeft, sortedRight)
    }
  }

  /**
   * Merges two sorted lists into one sorted list.
   *
   * @param left  A sorted list of integers
   * @param right A sorted list of integers
   * @return A new sorted list containing all elements of left and right
   */
  def merge(left: List[Int], right: List[Int]): List[Int] = (left, right) match {
    // If one list is empty, return the other
    case (Nil, _) => right
    case (_, Nil) => left

    // Compare heads of both lists and append the smaller one,
    // then recursively merge the remaining elements
    case (lHead :: lTail, rHead :: rTail) =>
      if (lHead <= rHead) lHead :: merge(lTail, right)
      else rHead :: merge(left, rTail)
  }
}



